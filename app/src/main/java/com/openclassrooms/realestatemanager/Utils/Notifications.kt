package com.openclassrooms.realestatemanager.Utils

import android.app.NotificationManager
import android.os.Bundle
import com.openclassrooms.realestatemanager.R.mipmap.ic_launcher
import android.app.PendingIntent
import android.app.NotificationChannel
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.content.Intent
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.openclassrooms.realestatemanager.Controller.Activities.MainActivity
import com.openclassrooms.realestatemanager.R


/**
 * Created by Adrien Deguffroy on 12/11/2018.
 */
class Notifications {

    private val notificationID = 105

    private lateinit var mContext:Context
    private lateinit var mNotificationManager:NotificationManager
    private lateinit var mBuilder:NotificationCompat.Builder

    private val channelID = "com.openclassrooms.realestatemanager"
    private val channelName = "RealEstateManager"

    /**
     * Create and push the notification
     */
    fun sendNotification(context: Context) {
        this.mContext = context
        Log.e("TAG", "sendNotification: ")
        // Creates an explicit intent for an Activity in your app
        val resultIntent = Intent(mContext, MainActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        //Build notification
        val notification = buildLocalNotification(mContext, pendingIntent).build()

        mNotificationManager = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(channelID, channelName, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            mBuilder.setChannelId(channelID)
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
        mNotificationManager.notify(notificationID, notification)
    }

    private fun buildLocalNotification(mContext: Context, pendingIntent: PendingIntent): NotificationCompat.Builder {
        Log.e("TAG", "buildLocalNotification: ")
        mBuilder = NotificationCompat.Builder(mContext, channelID)
        mBuilder.setSmallIcon(R.drawable.baseline_location_city_black_24)
        mBuilder.setContentTitle(mContext.resources.getString(R.string.notification_title))
                .setContentText(mContext.resources.getString(R.string.notification_message))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        return mBuilder
    }
}