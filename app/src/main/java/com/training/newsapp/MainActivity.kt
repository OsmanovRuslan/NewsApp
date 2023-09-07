package com.training.newsapp

import android.app.LocaleManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.training.newsapp.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: SharedPreferences

    private val languageChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_LOCALE_CHANGED) {
                val newLanguageCode = Locale.getDefault().language
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    context.getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.forLanguageTags(newLanguageCode)
                } else {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(newLanguageCode))
                }
                prefs.edit().putString("language", newLanguageCode).apply()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = getSharedPreferences("MODE", MODE_PRIVATE)

        val languagePhone = Locale.getDefault().language
        languageSetter(languagePhone)
        prefs.edit().putString("language", languagePhone).apply()
        

        val filter = IntentFilter(Intent.ACTION_LOCALE_CHANGED)
        registerReceiver(languageChangeReceiver, filter)

        themeSetter(prefs.getString("theme", "1").toString())
        languageSetter(prefs.getString("language", "ru").toString())

        val bottomNavigationView = binding.bottomNavigationView
        val navController = binding.fragmentContainerView.getFragment<NavHostFragment>().navController
        bottomNavigationView.setupWithNavController(navController)
    }
    private fun themeSetter(themeCode: String) {
        val themes = mapOf(
            "1" to Pair(AppCompatDelegate.MODE_NIGHT_NO, "1"),
            "2" to Pair(AppCompatDelegate.MODE_NIGHT_YES, "2"),
            "3" to Pair(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, "3")
        )

        themes[themeCode]?.let { (mode, modeTheme) ->
            AppCompatDelegate.setDefaultNightMode(mode)
            prefs.edit().putString("theme", modeTheme).apply()
        }
    }
    private fun languageSetter(languageCode: String) {
        val languages = mapOf(
            "ru" to "ru",
            "en" to "en"
        )

        languages[languageCode]?.let { language ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                applicationContext.getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.forLanguageTags(language)
            } else {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
            }
            prefs.edit().putString("language", language).apply()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(languageChangeReceiver)
    }
}