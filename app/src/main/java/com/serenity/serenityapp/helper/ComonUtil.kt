package com.serenity.serenityapp.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import java.lang.reflect.TypeVariable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Context.moveToActivity(activity: Class<out Activity>) {
    val intent = Intent(this, activity)
    startActivity(intent)
    (this as Activity).finish()
}

fun Context.moveToActivity(activity: Class<out Activity>, apply: Intent.() -> Unit) {
    val intent = Intent(this, activity).apply(apply)
    startActivity(intent)
    (this as Activity).finish()
}

fun Context.openNotificationAccessSettings() {
    val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
    startActivity(intent)
}

fun Context.isNotificationListenerServiceEnabled(): Boolean {
    val enabledNotificationListeners =
        Settings.Secure.getString(this.contentResolver, "enabled_notification_listeners")
    val packageName = this.packageName
    return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName)
}

fun getCurrentDate(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return currentDate.format(formatter)
}
