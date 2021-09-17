package com.gyenno.zero.medical

import android.content.Context
import androidx.lifecycle.MutableLiveData
import cn.jpush.android.api.CmdMessage
import cn.jpush.android.api.JPushMessage
import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.service.JPushMessageReceiver
import timber.log.Timber

/**
 * Created by Chon Den.
 *
 * @date 2021/07/29
 */
class JpushReceiver: JPushMessageReceiver() {


    override fun onRegister(context: Context?, p1: String?) {
        super.onRegister(context, p1)
        Timber.e("RegistrationId: $p1")
    }

    override fun onCommandResult(context: Context, cmdMessage: CmdMessage) {
        Timber.e("[onCommandResult] $cmdMessage")

        if (cmdMessage.cmd == 10000 && cmdMessage.extra != null) {
            val token = cmdMessage.extra.getString("token")
            val platform = cmdMessage.extra.getInt("platform")
            var deviceName = "unkown"
            when (platform) {
                1 -> deviceName = "小米"
                2 -> deviceName = "华为"
                3 -> deviceName = "魅族"
                4 -> deviceName = "OPPO"
                5 -> deviceName = "VIVO"
                6 -> deviceName = "ASUS"
                8 -> deviceName = "FCM"
            }
            Timber.e("获取到 $deviceName 的token:$token")
        }
    }

    override fun onNotifyMessageArrived(context: Context?, notificationMessage: NotificationMessage?) {
        super.onNotifyMessageArrived(context, notificationMessage)
        notificationMessage?.let {
            NotificationMessageLiveData.postValue(it)
        }
    }

    override fun onAliasOperatorResult(context: Context?, message: JPushMessage) {
        super.onAliasOperatorResult(context, message)
//        val sequence = message.sequence
//        val alias = message.alias
        AliasLiveData.postValue(message)
        Timber.i("action - onAliasOperatorResult, jPushMessage = $message, thread = ${Thread.currentThread().name}")
//        when (message.errorCode) {
//            0 -> {
//                when (sequence) {
//                    SEQUENCE_GET_ALIAS,
//                    SEQUENCE_SET_ALIAS -> {
//                        AliasLiveData.postValue(message)
//                    }
//                }
//            }
//            else -> {
//                // onAliasOperator error
//            }
//        }
    }

    companion object {
        const val SEQUENCE_GET_ALIAS = 1001
        const val SEQUENCE_SET_ALIAS = 1002
    }
}

object AliasLiveData: MutableLiveData<JPushMessage>()
object NotificationMessageLiveData: MutableLiveData<NotificationMessage>()