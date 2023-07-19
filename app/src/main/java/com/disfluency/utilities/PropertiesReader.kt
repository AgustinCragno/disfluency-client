package com.disfluency.utilities

import android.content.Context
import java.util.*

private const val CONFIG_FILE = "application.properties"

object PropertiesReader {
    private var properties: Properties? = null

    fun initialize(context: Context){
        properties = Properties()
        properties?.load(context.assets.open(CONFIG_FILE))
    }
    fun getProperty(key: String): String = properties!!.getProperty(key)
}