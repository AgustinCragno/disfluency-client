package com.disfluency.utilities.format

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun formatMillisecondsAsMinuteAndSeconds(milliseconds: Long): String {
    return SimpleDateFormat("mm:ss").format(Date(milliseconds))
}