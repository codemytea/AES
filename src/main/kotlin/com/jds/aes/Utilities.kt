package com.jds.aes

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer
import se.akerfeldt.okhttp.signpost.SigningInterceptor
import java.util.*

class Utilities {
    companion object {
        fun timeToUnixTimeStamp(time: Date): Int {
            println(time)
            val instant = time.toInstant()
            return instant.epochSecond.toInt()
        }

        fun sendMsg(
            sender: String = "Jabche",
            message: String = "Example Text",
            sendtime: Int? = null,
            userref: String? = null,
            callback_url: String? = null,
            msisdn: Long = 447565533834L,
        ): Boolean {
            //initial set up
            val key = "OCBBaD_nZPhOhLEPhg9mfBal"
            val secret = "EVr0u&HQTU!TpFb4ds3gI35iHH%28Nye!SojJe.T"
            val consumer = OkHttpOAuthConsumer(key, secret)
            val client = OkHttpClient.Builder()
                .addInterceptor(SigningInterceptor(consumer))
                .build()
            val json = JSONObject()

            //if param is null, don't put into json
            json.put("sender", sender)
            json.put("message", message)
            if (sendtime != null) json.put("sendtime", sendtime)
            //userref ?: json.put("userref", userref)
            //callback_url ?: json.put("callback_url", callback_url)
            json.put(
                "recipients", JSONArray().put(
                    JSONObject().put("msisdn", msisdn)
                )
            )

            //if all params are null, message can't be sent
            //if (json.isEmpty) return false

            val body = json.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val signedRequest = consumer.sign(
                Request.Builder()
                    .url("https://gatewayapi.com/rest/mtsms")
                    .post(body)
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
}