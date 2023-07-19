package com.disfluency.utilities

import android.content.Context
import java.util.*

private const val CONFIG_FILE = "application.properties"

object PropertiesReader {
    private var manager: PropertiesManager? = null

    fun initialize(context: Context){
        manager = PropertiesManager(context)
    }
    fun getProperty(key: String): String = manager!!.get(key)
}

private class PropertiesManager(context: Context){
    var properties: Properties
        private set

    init {
        properties = Properties()
        properties.load(context.assets.open(CONFIG_FILE))
    }

    fun get(key: String): String = properties.getProperty(key)
}