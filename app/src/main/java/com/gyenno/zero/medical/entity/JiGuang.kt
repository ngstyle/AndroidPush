package com.gyenno.zero.medical.entity

/**
 * Created by Chon Den.
 *
 * @date 2021/09/17
 */
data class JiGuangPushBody(
    val audience: Audience,
    val notification: Notification,
    val platform: List<String> = listOf("android")
)

data class Audience(
    val registration_id: List<String>
)

data class Notification(
    val alert: String,
    val android: Android = Android()
)

data class Android(
    val badge_add_num: Int = 1,
    val badge_class: String = "com.gyenno.zero.medical.MainActivity",
    val intent: Intent = Intent(),
    val style: Int = 1,
    val uri_activity: String = "com.gyenno.zero.medical.MainActivity"
)

data class Intent(
    val url: String = "intent:#Intent;component=com.gyenno.zero.medical/com.gyenno.zero.medical.MainActivity;end"
)

data class JiGuangPushResponse(
    val msg_id: String,
    val sendno: String,
    var uuid: String? = null
)

