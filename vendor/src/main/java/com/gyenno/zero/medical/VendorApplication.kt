package com.gyenno.zero.medical

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import com.gyenno.zero.medical.util.Manufacturer
import com.huawei.hms.push.HmsMessaging
import com.huawei.hms.support.common.ActivityMgr
import com.xiaomi.channel.commonutils.logger.LoggerInterface
import com.xiaomi.mipush.sdk.Logger
import com.xiaomi.mipush.sdk.MiPushClient
import timber.log.Timber


/**
 * Created by Chon Den.
 *
 * @date 2021/07/29
 */
class VendorApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())


        if (shouldInit()) {
            initVendorPush()
        }
    }

    private fun initVendorPush() {
        Timber.e("Brand: %s", android.os.Build.MANUFACTURER)

        when (Manufacturer.currentBrand) {
            Manufacturer.HUAWEI -> {
                // 华为推送
                HmsMessaging.getInstance(this).isAutoInitEnabled = true
                ActivityMgr.INST.init(this)
            }
            Manufacturer.XIAOMI -> {
                // 小米推送
                MiPushClient.registerPush(this, "2882303761519999494", "5251999912494")
                //打开Log
                val newLogger: LoggerInterface = object : LoggerInterface {
                    override fun setTag(tag: String?) {
                        // ignore
                    }

                    override fun log(content: String?, t: Throwable?) {
                        Timber.e(t)
                    }

                    override fun log(content: String?) {
                        Timber.d(content)
                    }
                }
                Logger.setLogger(this, newLogger)
            }
        }
    }

    private fun shouldInit(): Boolean {
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processInfos = am.runningAppProcesses
        val mainProcessName = applicationInfo.processName
        val myPid: Int = Process.myPid()
        for (info in processInfos) {
            if (info.pid == myPid && mainProcessName == info.processName) {
                return true
            }
        }
        return false
    }
}