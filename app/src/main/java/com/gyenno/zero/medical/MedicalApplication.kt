package com.gyenno.zero.medical

import android.app.Application
import android.util.Log
import cn.jpush.android.api.JPushInterface
import timber.log.Timber

/**
 * Created by Chon Den.
 *
 * @date 2021/07/29
 */
class MedicalApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        JPushInterface.init(this)
        Timber.i("Application onCreate")
    }
}