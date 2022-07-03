package me.lucky.usbkill

import android.content.Context
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class Preferences(ctx: Context) {
    companion object {
        private const val ENABLED = "enabled"
        private const val ACTION = "action"
        private const val RECEIVER = "receiver"
        private const val SECRET = "secret"

        private const val FILE_NAME = "sec_shared_prefs"
    }

    private val mk = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val prefs = EncryptedSharedPreferences.create(
        FILE_NAME,
        mk,
        ctx,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

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
}