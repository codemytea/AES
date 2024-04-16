package com.aes.usercharacteristicsservice.Evaluators.Literacy

import com.aes.common.Repositories.MessageRepository
import com.aes.common.Repositories.UserRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.usercharacteristicsservice.Utilities.Utils.scaleProbability
import jakarta.transaction.Transactional
import org.languagetool.JLanguageTool
import org.languagetool.Languages
import org.languagetool.rules.RuleMatch
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


@Service
@Configuration
@EnableScheduling
class LiteracyEvaluator(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
) : Logging {


    /**
     * Calculates a users literacy level based on the complexity of their messages (average word count and token tye ratio)
     * as well as their messages readability and average number of errors.
     *
     * Produces a result between 0.0 (meaning very illiterate) and 100.0 (meaning very literate) and saves this to the DB
     * */
    @Scheduled(cron = "0 0 1 * * ?")
    //@Scheduled(cron = "0/10 * * ? * *")
    @Transactional
    fun calculateLiteracyLevel() {

        userRepository.findAll().forEach { user ->
            val messages = messageRepository.getMessageByUserIdAndType(user.id).also {
                if (it != null) {
                    if (it.isEmpty()) return@forEach
                }
            }

            // Calculate average word count per message
            val averageWordCount = messages?.map { it.message.length }?.let { calculateWordCountScore(it.average()) }

            // Calculate average number of errors per message - weighted
            val averageErrorsPerMessage = (messages?.map { errorsInMessage(it.message) }?.average() ?: 0.0) * 6

            // Calculate average message readability - weighted
            val averageReadability = (messages?.map { messageReadability(it.message) }?.average() ?: 0.0) * 2

            // Calculate average vocabulary complexity (Type-Token Ratio)
            val averageVocabularyComplexity = messages?.map { calculateTypeTokenRatio(it.message) }?.average()

            // Combine individual metrics to calculate overall user literacy level
            val literacy = ((averageWordCount ?: 0.0) +
                    (averageErrorsPerMessage) +
                    (averageReadability) +
                    (averageVocabularyComplexity ?: 0.0) / 10.0).toFloat() / 4 //scale back to 0-100

            user.literacy = literacy
            userRepository.save(user)

            logger().info("User ${user.id} estimated literacy level is $literacy")
        }
    }

    /**
     * Taking the average number of words in the users all time messages, the number is scaled between 0.0 and 100.0
     * where 100.0 is most verbose. The scaling uses 17.5 to provide an average score of 50.0 as the average number of
     * words in a message is 17.5
     *
     * @param averageWordCount - the average number of words in all the users messages
     * @return a double between 0 and 100 where 0 is least verbose and 100 is most
     * */
    private fun calculateWordCountScore(averageWordCount: Double): Double {
        return scaleProbability(averageWordCount, 17.5) * 100.0
    }

    /**
     * Calculates the number of errors in a message, such as typos, syntactical errors etc.
     *
     * @param message - a users message
     * @return a double between 0 and 100 where 0 is the most mistakes and 100 is none
     * */
    private fun errorsInMessage(message: String): Double {

        val langTool = JLanguageTool(Languages.getLanguageForShortCode("en-GB"))
        val matches: List<RuleMatch> = langTool.check(message)

        // Calculate the percentage of mistakes in the message
        val mistakePercentage = (matches.size.toDouble() / message.split("\\s+".toRegex()).size) * 100.0

        // Return the inverted percentage to scale it from 0 to 100
        return 100.0 - mistakePercentage
    }

    /**
     * Calculates the number of syllables in a word
     *
     * @param word - the word to perform the calculation on
     * @return the number of syllables in teh word
     * */
    private fun countSyllables(word: String): Int {
        var syllableCount = 0
        var prevCharWasVowel = false

        for (ch in word) {
            val isVowel = ch.lowercaseChar() in "aeiouy"
            if (isVowel && !prevCharWasVowel) {
                syllableCount++
            }
            prevCharWasVowel = isVowel
        }

        if (syllableCount == 0 && word.isNotEmpty()) {
            syllableCount = 1
        }

        return syllableCount
    }


    /**
     * Calculates a messages' readability based on Flesch reading-ease score (FRES) test
     *
     * @param message - teh users message to perform the calculation on
     * @return a double between 0 and 100 where 0 is very difficult to read and 100 is very easy to read
     * */
    private fun messageReadability(message: String): Double {
        val words = message.split("\\s+".toRegex()).size
        val sentences = message.split("[.!?]+".toRegex()).size

        val syllableCount = message.split("\\s+".toRegex()).sumOf { countSyllables(it) }

        return 206.835 - 1.015 * (words.toDouble() / sentences) - 84.6 * (syllableCount.toDouble() / words)
    }

    /**
     * Calculates a messages' vocabulary complexity in a rudimentary way
     * by getting number of unique words in a message. It is assumed
     * that the greater number of unique words, the greater the messages
     * vocabulary complexity.
     *
     * @param message - the users message to perform the calculation on
     * @return a double between 0 and 100 where 0 is very low vocabulary complexity and 100 is very high complexity.
     * */
    fun calculateTypeTokenRatio(message: String): Double {
        val words = message.split("\\s+".toRegex())
        val totalWords = words.size

        // Count the number of unique words (types)
        val uniqueWords = words.toSet().size

        // Calculate the Type-Token Ratio (TTR)
        val typeTokenRatio = uniqueWords.toDouble() / totalWords

        // Scale the result to a range between 0 and 100
        return typeTokenRatio * 100.0
    }
}