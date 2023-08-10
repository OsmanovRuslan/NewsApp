package com.training.newsapp.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class Prefs(context: Context) : IPrefs {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("NEWS_APP", Context.MODE_PRIVATE)

    companion object{
        private const val KEY_MODE_THEME = "theme"

        fun getInstance(context: Context):IPrefs{
            return Prefs(context)
        }
    }

    override var theme: String?
        get() = sharedPreferences.getString(KEY_MODE_THEME, "1")
        set(value) = sharedPreferences.edit {putString(KEY_MODE_THEME, value!!)}

    override fun clearPrefs() {
        sharedPreferences.edit().clear().apply()
    }

}