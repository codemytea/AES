package com.aes.smsservices.Enums

import com.aes.smsservices.Models.RecievedMessageDTO
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Page(
    val messages: List<RecievedMessageDTO> = emptyList(),
    val page: Int = 0,
    val pages: Int = 1,

    @JsonAlias("per_page")
    val perPage: Int = 0,
    val results: Int = 0

)