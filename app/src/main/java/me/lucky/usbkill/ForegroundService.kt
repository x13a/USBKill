package me.lucky.usbkill

import android.app.KeyguardManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ForegroundService : Service() {
    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val ACTION_USB_STATE = "android.hardware.usb.action.USB_STATE"
    }

    private val usbStateReceiver = UsbStateReceiver()

    override fun onCreate() {
        super.onCreate()
        init()
    }

    override fun onDestroy() {
        super.onDestroy()
        deinit()
    }

    private fun init() {
        registerReceiver(usbStateReceiver, IntentFilter(ACTION_USB_STATE))
    }

    private fun deinit() {
        unregisterReceiver(usbStateReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        startForeground(
            NOTIFICATION_ID,
            NotificationCompat.Builder(this, NotificationManager.CHANNEL_DEFAULT_ID)
                .setContentTitle(getString(R.string.service_notification_title))
                .setSmallIcon(android.R.drawable.ic_delete)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()
        )
        return START_STICKY
    }

    private class UsbStateReceiver : BroadcastReceiver() {
        companion object {
            private const val KEY_1 = "connected"
            private const val KEY_2 = "host_connected"
        }

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action != ACTION_USB_STATE) return
            val prefs = Preferences(context ?: return)
            if (!prefs.isEnabled ||
                !context.getSystemService(KeyguardManager::class.java).isDeviceLocked) return
            val extras = intent.extras ?: return
            if (!extras.getBoolean(KEY_1) && !extras.getBoolean(KEY_2)) return
            Utils(context).sendBroadcast(prefs)
        }
    }
}