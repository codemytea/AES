package com.aes.servicerecievesms.Model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class RecievedSMSDTO(
    var id: Int = 0,                //The ID of the MO SMS
    var msisdn: Long = 0,            // The MSISDN of the mobile device who sent the SMS.
    var receiver: Long = 0,          //The short code on which the SMS was received.
    var message: String = "",        //The body of the SMS, incl. keyword.
    var senttime: Int = 0,          //The UNIX Timestamp when the SMS was sent.
    var webhook_label: String? = null, //Label of the webhook who matched the SMS.
    var sender: String? = null,        //If the SMS was sent with a text based sender, then this field is set. Optional.
    var mcc: Int? = null,              //MCC, mobile country code. Optional.
    var mnc: Int? = null,              //MNC, mobile network code. Optional.
    var varidity_period: Int? = null,  //How long the SMS is varid. Optional.
    var encoding: String? = null,      //Encoding of the received SMS. Optional.
    var udh: String? = null,           //User data header of the received SMS. Optional.
    var payload: String? = null,       //Binary payload of the received SMS. Optional.
    var country_code: String? = null,  //Country code of the msisdn. Optional
    var country_prefix: Int? = null    //Country prefix of the msisdn. Optional
) {

}