package com.disfluency.utilities.avatar

import android.content.Context
import com.disfluency.R

object AvatarManager {

    private var avatars = LinkedHashMap<Int, Int>()

    private const val DEFAULT_AVATAR = R.drawable.avatar_default

    fun initialize(context: Context){
        val avatarList = context.resources.obtainTypedArray(R.array.avatar_list)

        for (i in 0 until avatarList.length()){
            val id = avatarList.getResourceId(i, -1)
            val file = avatarList.getString(i)!!.substringAfterLast("/")
            avatars[getIndexNumber(file)] = id
        }

        avatarList.recycle()
    }

    fun getAvatarId(index: Int): Int{
        return avatars[index] ?: DEFAULT_AVATAR
    }

    fun getAvatars(): List<Int> {
        return avatars.values.toList()
    }

    fun getIndices(): List<Int> {
        return avatars.keys.toList()
    }

    fun getAvatarIndex(id: Int): Int{
        return avatars.filterValues { id == it }.keys.first()
    }

    private fun getIndexNumber(fileName: String): Int {
        return fileName.substringAfter("_").substringBefore(".").toInt()
    }
}