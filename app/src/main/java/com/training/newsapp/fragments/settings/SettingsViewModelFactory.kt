package com.training.newsapp.fragments.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.training.newsapp.preferences.IPrefs
import com.training.newsapp.preferences.Prefs

class SettingsViewModelFactory(var context: Context): ViewModelProvider.Factory {

    private lateinit var prefs: IPrefs

    private fun createPrefs(): IPrefs{
        prefs = Prefs(context = context)
        return prefs
    }


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingsViewModel(createPrefs()) as T
    }

}