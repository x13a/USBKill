package me.lucky.usbkill

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.UserManager
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class Preferences(ctx: Context, encrypted: Boolean = true) {
    companion object {
        private const val ENABLED = "enabled"
        private const val ACTION = "action"
        private const val RECEIVER = "receiver"
        private const val SECRET = "secret"

        private const val FILE_NAME = "sec_shared_prefs"

        fun new(ctx: Context) = Preferences(
            ctx,
            encrypted = Build.VERSION.SDK_INT < Build.VERSION_CODES.N ||
                ctx.getSystemService(UserManager::class.java).isUserUnlocked,
        )
    }

    private val prefs: SharedPreferences = if (encrypted) {
        val mk = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        EncryptedSharedPreferences.create(
            FILE_NAME,
            mk,
            ctx,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    } else {
        val context = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            ctx.createDeviceProtectedStorageContext() else ctx
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    var isEnabled: Boolean
        get() = prefs.getBoolean(ENABLED, false)
        set(value) = prefs.edit { putBoolean(ENABLED, value) }

    var action: String
        get() = prefs.getString(ACTION, "") ?: ""
        set(value) = prefs.edit { putString(ACTION, value) }

    var receiver: String
        get() = prefs.getString(RECEIVER, "") ?: ""
        set(value) = prefs.edit { putString(RECEIVER, value) }

    var secret: String
        get() = prefs.getString(SECRET, "") ?: ""
        set(value) = prefs.edit { putString(SECRET, value) }

    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) =
        prefs.registerOnSharedPreferenceChangeListener(listener)

    fun unregisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) =
        prefs.unregisterOnSharedPreferenceChangeListener(listener)

    fun copyTo(dst: Preferences, key: String? = null) = dst.prefs.edit {
        for (entry in prefs.all.entries) {
            val k = entry.key
            if (key != null && k != key) continue
            val v = entry.value ?: continue
            when (v) {
                is Boolean -> putBoolean(k, v)
                is Int -> putInt(k, v)
                is String -> putString(k, v)
            }
        }
    }
}