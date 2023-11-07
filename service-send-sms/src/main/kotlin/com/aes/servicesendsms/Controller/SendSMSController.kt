package com.aes.servicesendsms.Controller

import com.aes.servicesendsms.Model.ResponseDTO
import com.aes.servicesendsms.Model.SendMessageDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.util.logging.Logger
import okhttp3.OkHttpClient
import okhttp3.Request
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor

@RestController("/rest")
class SendSMSController() {

    //.url("https://gatewayapi.com/rest/mtsms")

    @PostMapping("/mtsms")
    fun sendSMS(@RequestBody resource: SendMessageDTO) {
        Logger.getAnonymousLogger().info("Sending message with ID ${resource.userref}")
        val key = "OCBBaD_nZPhOhLEPhg9mfBal"
        val secret = "EVr0u&HQTU!TpFb4ds3gI35iHH%28Nye!SojJe.T"
        val consumer = OkHttpOAuthConsumer(key, secret)
        val client = OkHttpClient.Builder()
            .addInterceptor(SigningInterceptor(consumer))
            .build()

        RestTemplate().postForEntity(URI.create("https://gatewayapi.com/rest/mtsms"), resource, ResponseDTO::class.java)


        val signedRequest = consumer.sign(
            Request.Builder()
                .url("https://gatewayapi.com/rest/mtsms")
                .post(.....)
                .build()
        ).unwrap() as Request

        //try sending sms, either throws IOException/IllegalStateException or returns true
        client.newCall(signedRequest).execute().use { response ->
            println(response.body?.string())
        }

        //If no exception was thrown, but the response wasn't successful, return false
        return true
    }


}