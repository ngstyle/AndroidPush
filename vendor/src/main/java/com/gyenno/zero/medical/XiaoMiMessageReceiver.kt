package com.gyenno.zero.medical

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.xiaomi.mipush.sdk.*
import timber.log.Timber


class XiaoMiMessageReceiver: PushMessageReceiver() {

    private var mRegId: String? = null
    private val mResultCode: Long = -1
    private val mReason: String? = null
    private val mCommand: String? = null
    private var mMessage: String? = null
    private var mTopic: String? = null
    private var mAlias: String? = null
    private var mUserAccount: String? = null
    private var mStartTime: String? = null
    private var mEndTime: String? = null

    override fun onReceivePassThroughMessage(context: Context?, message: MiPushMessage) {
        Timber.e("【onReceivePassThroughMessage】%s", message.toString())
        mMessage = message.content
        if (!TextUtils.isEmpty(message.topic)) {
            mTopic = message.topic
        } else if (!TextUtils.isEmpty(message.alias)) {
            mAlias = message.alias
        } else if (!TextUtils.isEmpty(message.userAccount)) {
            mUserAccount = message.userAccount
        }

        MiPushMessageLiveData.postValue(message)
    }

    override fun onNotificationMessageClicked(context: Context?, message: MiPushMessage) {
        Timber.e("【onNotificationMessageClicked】%s", message.toString())
        mMessage = message.content
        if (!TextUtils.isEmpty(message.topic)) {
            mTopic = message.topic
        } else if (!TextUtils.isEmpty(message.alias)) {
            mAlias = message.alias
        } else if (!TextUtils.isEmpty(message.userAccount)) {
            mUserAccount = message.userAccount
        }
        MiPushMessageLiveData.postValue(message)
    }

    override fun onNotificationMessageArrived(context: Context?, message: MiPushMessage) {
        Timber.e("【onNotificationMessageArrived】%s", message.toString())
        mMessage = message.content
        if (!TextUtils.isEmpty(message.topic)) {
            mTopic = message.topic
        } else if (!TextUtils.isEmpty(message.alias)) {
            mAlias = message.alias
        } else if (!TextUtils.isEmpty(message.userAccount)) {
            mUserAccount = message.userAccount
        }
        MiPushMessageLiveData.postValue(message)
    }

    override fun onCommandResult(context: Context?, message: MiPushCommandMessage) {
        Timber.e("【onCommandResult】%s", message.toString())
        val command: String = message.command
        val arguments: List<String> = message.commandArguments
        val cmdArg1 = if (arguments.isNotEmpty()) arguments[0] else null
        val cmdArg2 = if (arguments.size > 1) arguments[1] else null
        if (MiPushClient.COMMAND_REGISTER == command) {
            if (message.resultCode.equals(ErrorCode.SUCCESS)) {
                mRegId = cmdArg1
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS == command) {
            if (message.resultCode.equals(ErrorCode.SUCCESS)) {
                mAlias = cmdArg1
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS == command) {
            if (message.resultCode.equals(ErrorCode.SUCCESS)) {
                mAlias = cmdArg1
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC == command) {
            if (message.resultCode.equals(ErrorCode.SUCCESS)) {
                mTopic = cmdArg1
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC == command) {
            if (message.resultCode.equals(ErrorCode.SUCCESS)) {
                mTopic = cmdArg1
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME == command) {
            if (message.resultCode.equals(ErrorCode.SUCCESS)) {
                mStartTime = cmdArg1
                mEndTime = cmdArg2
            }
        }
        MiPushCommandMessageLiveData.postValue(message)
    }

    override fun onReceiveRegisterResult(context: Context?, message: MiPushCommandMessage) {
        Timber.e("【onReceiveRegisterResult】%s", message.toString())
        val command: String = message.command
        val arguments: List<String> = message.commandArguments
        val cmdArg1 = if (arguments.isNotEmpty()) arguments[0] else null
        val cmdArg2 = if (arguments.size > 1) arguments[1] else null
        if (MiPushClient.COMMAND_REGISTER == command) {
            if (message.resultCode.equals(ErrorCode.SUCCESS)) {
                mRegId = cmdArg1
            }
        }
        MiPushCommandMessageLiveData.postValue(message)
    }
}


object MiPushMessageLiveData: MutableLiveData<MiPushMessage>() {

}

object MiPushCommandMessageLiveData: MutableLiveData<MiPushCommandMessage>() {

}