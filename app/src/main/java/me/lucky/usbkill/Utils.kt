package me.lucky.usbkill

import android.content.Context
import android.content.Intent

class Utils(private val ctx: Context) {
    companion object {
        private const val KEY = "CODE"
    }

    fun sendBroadcast(prefs: Preferences) {
        val action = prefs.action
        if (action.isEmpty()) return
        ctx.sendBroadcast(Intent(action).apply {
            val cls = prefs.receiver.split('/')
            val packageName = cls.firstOrNull() ?: ""
            if (packageName.isNotEmpty()) {
                setPackage(packageName)
                if (cls.size == 2)
                    setClassName(
                        packageName,
                        "$packageName.${cls[1].trimStart('.')}",
                    )
            }
            addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
            val code = prefs.authenticationCode
            if (code.isNotEmpty()) putExtra(KEY, code)
        })
    }
}