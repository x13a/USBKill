package me.lucky.usbkill

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager

class UsbReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != UsbManager.ACTION_USB_DEVICE_ATTACHED &&
            intent?.action != UsbManager.ACTION_USB_ACCESSORY_ATTACHED) return
        val prefs = Preferences.new(context ?: return)
        if (!prefs.isEnabled ||
            !context.getSystemService(KeyguardManager::class.java).isDeviceLocked) return
        Utils(context).sendBroadcast(prefs)
    }
}