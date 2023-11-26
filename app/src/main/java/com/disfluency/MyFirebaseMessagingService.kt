package com.disfluency

import com.google.firebase.messaging.FirebaseMessagingService
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MyFirebaseMessagingService: FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        //TODO hacer lo que querramos con lo que llega en la notif
        Log.v("CloudMessage",": From ${message.from}")
        if(message.data.isNotEmpty()){
            Log.v("CloudMessage","Data ${message.data}")
        }

        message.data.let {
            Log.v("CloudMessage", "Message data body: ${it["body"]}")
            Log.v("CloudMessage", "Message data title: ${it["title"]}")
            showNotificationOnStatusBar(it)
        }
    }

    private fun showNotificationOnStatusBar(data: Map<String, String>) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        intent.putExtra("title", data["title"])
        intent.putExtra("body", data["body"])

        var requestCode = System.currentTimeMillis().toInt()
        var pendingIntent: PendingIntent
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_MUTABLE)
        }else{
            pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        }
        val builder = NotificationCompat.Builder(this, "Global").setAutoCancel(true)
            .setContentTitle(data["title"])
            .setContentText(data["body"])
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.BigTextStyle().bigText((data["body"])))
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
        with(NotificationManagerCompat.from(this)){
            notify(requestCode, builder.build())
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        GlobalScope.launch {
            saveGCMToken(token)
        }
    }

    // Save GCM token DataStore Preference
    private suspend fun saveGCMToken(token: String) {
        val gckTokenKey = stringPreferencesKey("gcm_token")
        baseContext.dataStore.edit {pref ->
            pref[gckTokenKey] = token
        }
    }
}