package me.lucky.usbkill

import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged

import me.lucky.usbkill.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: Preferences
    private lateinit var prefsdb: Preferences

    private val prefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        prefs.copyTo(prefsdb, key)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setup()
    }

    override fun onStart() {
        super.onStart()
        prefs.registerListener(prefsListener)
    }

    override fun onStop() {
        super.onStop()
        prefs.unregisterListener(prefsListener)
    }

    private fun init() {
        prefs = Preferences(this)
        prefsdb = Preferences(this, encrypted = false)
        prefs.copyTo(prefsdb)
        NotificationManager(this).createNotificationChannels()
        binding.apply {
            action.editText?.setText(prefs.action)
            receiver.editText?.setText(prefs.receiver)
            secret.editText?.setText(prefs.secret)
            toggle.isChecked = prefs.isEnabled
        }
    }

    private fun setup() {
        binding.apply {
            action.editText?.doAfterTextChanged {
                prefs.action = it?.toString() ?: ""
            }
            receiver.editText?.doAfterTextChanged {
                prefs.receiver = it?.toString() ?: ""
            }
            secret.editText?.doAfterTextChanged {
                prefs.secret = it?.toString() ?: ""
            }
            toggle.setOnCheckedChangeListener { _, isChecked ->
                prefs.isEnabled = isChecked
                setComponentState(UsbReceiver::class.java, isChecked)
                setComponentState(RestartReceiver::class.java, isChecked)
                setForegroundServiceState(isChecked)
            }
        }
    }

    private fun setForegroundServiceState(value: Boolean) =
        Intent(this.applicationContext, ForegroundService::class.java).also {
            if (value) ContextCompat.startForegroundService(this.applicationContext, it)
            else stopService(it)
        }

    private fun setComponentState(cls: Class<*>, enabled: Boolean) =
        packageManager.setComponentEnabledSetting(
            ComponentName(this, cls),
            if (enabled) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP,
        )
}