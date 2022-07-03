# USBKill

USB connection trigger.

[comment]: <> ([<img )

[comment]: <> (     src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png")

[comment]: <> (     alt="Get it on F-Droid")

[comment]: <> (     height="80">]&#40;https://f-droid.org/packages/me.lucky.usbkill/&#41;)

[comment]: <> ([<img )

[comment]: <> (      src="https://play.google.com/intl/en_us/badges/images/generic/en-play-badge.png" )

[comment]: <> (      alt="Get it on Google Play" )

[comment]: <> (      height="80">]&#40;https://play.google.com/store/apps/details?id=me.lucky.usbkill&#41;)

<img 
     src="https://raw.githubusercontent.com/x13a/USBKill/main/fastlane/metadata/android/en-US/images/phoneScreenshots/1.png" 
     width="30%" 
     height="30%">

Tiny app to listen for USB connection events **while the device is locked**.  
When found, it will send a broadcast message.

## Wasted

* action: `me.lucky.wasted.action.TRIGGER`
* receiver: `me.lucky.wasted/.TriggerReceiver`
* secret: the code from Wasted

Do not forget to activate the `Broadcast` trigger in Wasted.

## Permissions

* FOREGROUND_SERVICE - receive usb state events
* RECEIVE_BOOT_COMPLETED - persist service across reboots

## License
[![GNU GPLv3 Image](https://www.gnu.org/graphics/gplv3-127x51.png)](https://www.gnu.org/licenses/gpl-3.0.en.html)  

This application is Free Software: You can use, study share and improve it at your will. 
Specifically you can redistribute and/or modify it under the terms of the
[GNU General Public License v3](https://www.gnu.org/licenses/gpl.html) as published by the Free 
Software Foundation.
