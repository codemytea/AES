package com.aes.messagecompiler

import com.aes.common.Entities.User
import com.aes.common.Entities.UserKnowledge
import com.aes.common.Enums.Age
import com.aes.common.Enums.Gender
import com.aes.common.Enums.HandlableMessageType
import com.aes.messagecompiler.Controller.CompilerPipeline
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class MessageCompilerApplicationTests {

    @Autowired
    lateinit var cP: CompilerPipeline

    @Test
    fun segmentationTest() {
        val temp = cP.finalSplit(
            mapOf(
                HandlableMessageType.GENERAL to listOf("Hello. Lovely weather I like your sweater."),
                HandlableMessageType.AGRICULTURAL_QUESTION to listOf(
                    "The answer to your agricultural question is to plant your corn now. The answer to your agricultural question is the best time to sell is tomorrow. There are lots of sweetcorn varieties to choose from, offering different levels of sweetness. The size and quantity of cobs can differ between varieties, as can the height and vigour of plants. There are early, mid-season and late-ripening varieties, allowing you to harvest over a long season if you grow several types. In cold locations, choose an early ripening variety. F1 hybrids produce reliably uniform plants with good vigour. Most modern varieties are ‘supersweet’ types – the flavour is much sweeter than older varieties and cobs retain their sugar content for longer after picking, but plants are less vigorous and kernels may be chewier. Just take care not to grow supersweet varieties close to other varieties, as cross-pollination can mean you may not get the super sweetness you expect. You can also buy ‘tendersweet’ varieties, which are almost as sweet and less chewy. More unusual options include baby corn varieties, for harvesting finger-sized immature cobs for eating whole, and varieties for using as popcorn. Choose a warm, sheltered, sunny growing site, protected from strong winds, and with fertile soil. Cobs are unlikely to ripen if they’re not in full sun, and sweetcorn is less successful in dry or heavy soil. Prepare the site by removing any weeds, then add two bucketfuls of garden compost or well-rotted manure per square metre/yard. You can also rake in a high potassium general fertiliser, such as Vitax Q4, at a rate of three handfuls per square metre/yard. Seeds need a soil temperature above 10°C (50°F) to germinate, so wait until late spring. Warm the soil with cloches or a plastic-free crop cover before sowing, and keep in place for as long as possible to protect the young plants. Sow seeds 2cm (1in) deep, in a grid not a row, spacing them 34–45cm (14–18in) apart in each direction. Sow two or three seeds at each point, to allow for losses or failures, then thin out any extra seedlings to leave just the strongest one at each point. With baby corn varieties, sow 20cm (8in) apart in rows not grids, as The transfer of pollen grains from a male anther to a female stigma, either within the same flower or between two different flowers. It’s usually followed by fertilisation and seed production. Some flowers are pollinated by insects or other small creatures, others are wind pollinated. It’s also possible to pollinate flowers by hand, if you want to breed new plants or ensure successful pollination when there are few natural pollinators. pollination is not required."
                ),
                HandlableMessageType.INFORMATION to listOf("can you please give me more information on your location? I will stop asking for more information from now on")
            )
        )
        assert(temp.isNotEmpty())
    }

    @Test
    fun improveSuggestabilityTest() {
        val temp = cP.improveSuggestability(
            mapOf(
                HandlableMessageType.GENERAL to listOf("Hello.", "Lovely weather.", "I like your sweater."),
                HandlableMessageType.AGRICULTURAL_QUESTION to listOf(
                    "The answer to your agricultural question is to plant your corn now",
                    "The answer to your agricultural question is the best time to sell is tomorrow. There are lots of sweetcorn varieties to choose from, offering different levels of sweetness. The size and quantity of cobs can differ between varieties, as can the height and vigour of plants. There are early, mid-season and late-ripening varieties, allowing you to harvest over a long season if you grow several types. In cold locations, choose an early ripening variety. F1 hybrids produce reliably uniform plants with good vigour. Most modern varieties are ‘supersweet’ types – the flavour is much sweeter than older varieties and cobs retain their sugar content for longer after picking, but plants are less vigorous and kernels may be chewier. Just take care not to grow supersweet varieties close to other varieties, as cross-pollination can mean you may not get the super sweetness you expect. You can also buy ‘tendersweet’ varieties, which are almost as sweet and less chewy. More unusual options include baby corn varieties, for harvesting finger-sized immature cobs for eating whole, and varieties for using as popcorn. Choose a warm, sheltered, sunny growing site, protected from strong winds, and with fertile soil. Cobs are unlikely to ripen if they’re not in full sun, and sweetcorn is less successful in dry or heavy soil. Prepare the site by removing any weeds, then add two bucketfuls of garden compost or well-rotted manure per square metre/yard. You can also rake in a high potassium general fertiliser, such as Vitax Q4, at a rate of three handfuls per square metre/yard. Seeds need a soil temperature above 10°C (50°F) to germinate, so wait until late spring. Warm the soil with cloches or a plastic-free crop cover before sowing, and keep in place for as long as possible to protect the young plants. Sow seeds 2cm (1in) deep, in a grid not a row, spacing them 34–45cm (14–18in) apart in each direction. Sow two or three seeds at each point, to allow for losses or failures, then thin out any extra seedlings to leave just the strongest one at each point. With baby corn varieties, sow 20cm (8in) apart in rows not grids, as The transfer of pollen grains from a male anther to a female stigma, either within the same flower or between two different flowers. It’s usually followed by fertilisation and seed production. Some flowers are pollinated by insects or other small creatures, others are wind pollinated. It’s also possible to pollinate flowers by hand, if you want to breed new plants or ensure successful pollination when there are few natural pollinators. pollination is not required."
                ),
                HandlableMessageType.INFORMATION to listOf(
                    "can you please give me more information on your location?",
                    "I will stop asking for more information from now on"
                )
            ),
            User(
                UUID.randomUUID(),
                phoneNumber = listOf(447500000000),
                age = Age.AGED,
                gender = Gender.FEMALE,
                literacy = 20f,
                knowledgeAreas = mutableListOf(
                    UserKnowledge(
                        knowledgeLevel = 0.7,
                    )
                )
            )
        )
        assert(temp.isNotEmpty())
    }

}
