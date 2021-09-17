package com.gyenno.zero.medical.network

import com.gyenno.zero.medical.entity.Audience
import com.gyenno.zero.medical.entity.JiGuangPushBody
import com.gyenno.zero.medical.entity.Notification
import java.util.*
import kotlin.random.Random.Default.nextInt

/**
 * Created by Chon Den.
 *
 * @date 2021/09/17
 */

fun generatePushAuthorization(): String {
    val base64AuthString = "38609f157e9c19f916d11317:9039475beb945c56cdb4193b"
    return Base64.getEncoder().encodeToString(base64AuthString.toByteArray())
}

fun main() {
//    println(generatePushAuthorization())
    val nextValues = List(10) { nextInt(4000, 6000) }
    println(nextValues)
}

fun getPushContentBody(registrationId: String, content: String): JiGuangPushBody {
    val audience = Audience(listOf(registrationId))
    val notification = Notification(content)
    return JiGuangPushBody(audience, notification)
}

