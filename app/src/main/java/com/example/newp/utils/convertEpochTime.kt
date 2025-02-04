package com.example.newp.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone


fun convertMillisToTimeInIST(millis: Long): String {
        // Create a Calendar instance
        val calendar = Calendar.getInstance()

        // Set the time to the provided milliseconds
        calendar.timeInMillis = millis

        // Set the timezone to IST (Indian Standard Time)
        calendar.timeZone = TimeZone.getTimeZone("Asia/Kolkata")

        // Create a SimpleDateFormat with the desired format
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        // Format the calendar time into a string
        return dateFormat.format(calendar.time)
    }
