package com.training.newsapp.fragments.settings

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModel
import com.training.newsapp.R
import com.training.newsapp.preferences.IPrefs
import kotlinx.coroutines.flow.MutableStateFlow

class SettingsViewModel(private val prefs: IPrefs) : ViewModel() {

    val themeChosen = MutableStateFlow("")

    fun initializeSettings(context: Context) {
        when (prefs.theme){
            "1" -> themeChosen.value = context.getString(R.string.light)
            "2" -> themeChosen.value = context.getString(R.string.night)
            else -> themeChosen.value = context.getString(R.string.system)
        }
    }

    fun themeSetListener(context: Context, themeCode: String) {
        val themes = mapOf(
            "1" to Triple(AppCompatDelegate.MODE_NIGHT_NO, R.string.light, "1"),
            "2" to Triple(AppCompatDelegate.MODE_NIGHT_YES, R.string.night, "2"),
            "3" to Triple(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, R.string.system, "3")
        )

        themes[themeCode]?.let { (mode, stringRes, modeTheme) ->
            AppCompatDelegate.setDefaultNightMode(mode)
            themeChosen.value = context.getString(stringRes)
            prefs.theme = modeTheme
        }
    }

}