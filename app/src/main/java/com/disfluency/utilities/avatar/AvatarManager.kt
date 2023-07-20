package com.disfluency.utilities.avatar

import android.content.Context
import com.disfluency.R

object AvatarManager {
    private const val AVATAR_NAMING_FORMAT = "avatar_%d.png"

    private var avatars = LinkedHashMap<String, Int>()

    fun initialize(context: Context){
        val avatarList = context.resources.obtainTypedArray(R.array.avatar_list)

        for (i in 0 until avatarList.length()){
            val id = avatarList.getResourceId(i, -1)
            val file = avatarList.getString(i)!!.substringAfterLast("/")
            avatars[file] = id
        }

        avatarList.recycle()
    }

    fun getAvatarId(index: Int): Int{
        return avatars[AVATAR_NAMING_FORMAT.format(index)] ?: throw AvatarNotFoundException(index)
    }

    fun getAvatarIndex(id: Int): Int{
        val file = avatars.filterValues { id == it }.keys.first()
        return file.substringAfter("_").substringBefore(".").toInt()
    }
}