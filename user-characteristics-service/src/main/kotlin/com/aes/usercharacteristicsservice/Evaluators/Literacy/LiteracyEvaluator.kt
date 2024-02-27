package com.aes.usercharacteristicsservice.Evaluators.Literacy

import com.aes.common.Repositories.MessageRepository
import com.aes.common.Repositories.UserRepository
import com.aes.common.logging.Logging
import com.aes.common.logging.logger
import com.aes.usercharacteristicsservice.Utilities.Utils.scaleProbability
import jakarta.transaction.Transactional
import org.languagetool.JLanguageTool
import org.languagetool.Language
import org.languagetool.Languages
import org.languagetool.rules.RuleMatch
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.*


@Service
@Configuration
@EnableScheduling
class LiteracyEvaluator(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
) : Logging {

    @Scheduled(cron = "0 0 1 * * ?")
    //@Scheduled(cron = "0/10 * * ? * *")
    @Transactional
    fun calculateLiteracyLevel(){

        userRepository.findAll().forEach { user ->
            val messages = messageRepository.getMessageByUserIdAndType(user.id).also {
                if (it != null) {
                    if (it.isEmpty()) return@forEach
                }
            }

            // Calculate average word count per message
            val averageWordCount = calculateWordCountScore(messages?.map { it.message.length }.average())

            // Calculate average number of errors per message - weighted
            val averageErrorsPerMessage = messages?.map { errorsInMessage(it.message) }.average() * 6

            // Calculate average message readability - weighted
            val averageReadability = messages?.map { messageReadability(it.message) }.average() * 2

            // Calculate average vocabulary complexity (Type-Token Ratio)
            val averageVocabularyComplexity = messages?.map { calculateTypeTokenRatio(it.message) }.average()

            // Combine individual metrics to calculate overall user literacy level
            val literacy =  ((averageWordCount + averageErrorsPerMessage + averageReadability + averageVocabularyComplexity) / 10.0).toFloat()

            user.literacy = literacy
            userRepository.save(user)

            logger().info("User ${user.id} estimated literacy level is $literacy")
        }
    }

    private fun calculateWordCountScore(averageWordCount: Double): Double {
        return scaleProbability(averageWordCount, 17.5) * 100.0
    }

    /**
     * Number of errors in a message, suh as typos, syntactical errors etc
     * 100 represents no mistakes and 0 represents mistakes everywhere
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
     * Number of syllables in a word
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
     * 100.0 is most easy to read, 0.0 is extremely difficult to read.
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
     * 100 represents the greatest vocabulary complexity and 0 represents the least
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