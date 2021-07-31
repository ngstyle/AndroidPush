package com.gyenno.zero.medical

import android.os.Bundle
import com.huawei.hms.push.HmsMessageService
import timber.log.Timber

class HuaWeiPushService: HmsMessageService() {

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Timber.e("receive token: $token")
    }

    override fun onNewToken(token: String?, bun: Bundle?) {
        super.onNewToken(token, bun)
        Timber.e("receive token with bundle: $token")
    }
}