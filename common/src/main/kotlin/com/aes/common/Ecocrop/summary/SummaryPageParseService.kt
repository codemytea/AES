package com.aes.common.Ecocrop.summary

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springframework.stereotype.Service

@Service
class SummaryPageParseService {

    private fun link(letter: String) = "https://ecocrop.review.fao.org/ecocrop/srv/en/cropList?name=$letter&relation=beginsWith"


    private fun parseTableRow(tr: Element): String?{
        return tr.getElementsByTag("td").getOrNull(1)?.text()
    }

    private fun parsePage(document: Document): List<String>{
        return document.getElementById("content")
            .getElementsByTag("table")
            .first()!!
            .getElementsByTag("tr")
            .mapNotNull {
                parseTableRow(it)
            }
    }

    private fun getPage(letter: String): Document{
        return try{
            Jsoup.connect(link(letter)).execute().parse()
        } catch(e: Throwable){
            throw e
        }
    }


    fun getIdsForLetter(letter: String): List<String>{
        val doc = getPage(letter)
        return parsePage(doc)
    }

}