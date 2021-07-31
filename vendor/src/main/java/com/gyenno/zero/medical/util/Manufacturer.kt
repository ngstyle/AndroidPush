package com.gyenno.zero.medical.util

enum class Manufacturer {
    HUAWEI, XIAOMI, OPPO, VIVO, MEIZU, OTHER;

    companion object {
        val currentBrand: Manufacturer by lazy {
            when (android.os.Build.MANUFACTURER.lowercase()) {
                "xiaomi" -> XIAOMI
                "huawei" -> HUAWEI
                else -> OTHER
            }
        }
    }
}