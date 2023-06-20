package com.example.hkbptarutung.utils

import android.content.Context

class PreferenceUtils {
    companion object {

        private fun getPref(context: Context) =
            context.getSharedPreferences("app", Context.MODE_PRIVATE)

        fun getUid(context: Context) = getPref(context).getString("uid", "")

        fun setUid(context: Context, uid: String) = getPref(context)
            .edit().putString("uid", uid)

        fun setAdmin(context: Context, value: Boolean = true) {
            getPref(context).edit().putBoolean("isAdmin", value).apply()
        }

        fun isAdmin(context: Context) = getPref(context).getBoolean("isAdmin", false)
    }
}