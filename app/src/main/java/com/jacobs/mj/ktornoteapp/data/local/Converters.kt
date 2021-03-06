package com.jacobs.mj.ktornoteapp.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by mj on 2021/03/06 at 8:10 AM
 *
 * Telling room how to save a list of owners and how to display it when we need the string for the UI
 *
 * 2nd class to be created
 */
class Converters {

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toList(string: String): List<String> {
        return Gson().fromJson(string, object : TypeToken<List<String>>() {}.type)
    }
}