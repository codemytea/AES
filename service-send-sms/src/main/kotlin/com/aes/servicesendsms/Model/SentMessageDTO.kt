package com.aes.servicesendsms.Model

class SendMessageDTO(
    var `class`: String = "standard", //can be premium or secret, not using for purposes of this project
    var message: String, //sms content UTF-8, will be transcoded dep on 'encoding' field. Required unless payload is specified
    var sender: String, //up to 11 alphanum or 15 digits
    var sendtime: Int, //seconds since epoch to schedule message sending at specific time
    var tags: List<String>, //A list of string tags, which will be replaced with the tag values for each recipient.
    var userref: String, //returned when delivery status notification recieved
    var priority: String, //BULK, NORMAL, URGENT or VERY_URGENT
    var validity_period: Int, //in seconds. If message not delivered in this period, it will expire. Min= 60 sec, max = 5 days
    var encoding: String, //Use UCS2 to send a unicode message
    var destaddr: String, //DISPLAY (flash unsaved sms), MOBILE (the default), SIMCARD, EXTUNIT
    var payload: String, //If you are sending a binary SMS, ie. a SMS you have encoded yourself or with special content for feature phones (non-smartphones). You may specify a payload, encoded as Base64. If specified, message must not be set and tags are unavailable.
    var udh: String, //enables additional func for binary sms eg base64
    var callback_url: String, //send status notifications to this URL instead of standard webhook
    var label: String, //can be used to uniquely identify a customer/organisation
    var max_parts: Int, //1-255 can't be used w tags or binary sms'. Limits number of sms messages a single message will send. Useful if sent by machine
    var extra_details: String, //To get more details about the number of parts sent to each recipient, set this to “recipients_usage”.
    var recipients: List<RecipientDTO> //List of recipients
) {
}