package com.training.newsapp.fragments

import android.app.LocaleManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.training.newsapp.R
import com.training.newsapp.databinding.BottomSheetDialogLanguageBinding
import com.training.newsapp.databinding.BottomSheetDialogThemeBinding
import com.training.newsapp.databinding.FragmentSettingsBinding
import java.util.Locale

class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>() {

    private val bottomSheetBindingTheme by lazy { BottomSheetDialogThemeBinding.inflate(layoutInflater) }
    private val bottomSheetBindingLanguage by lazy { BottomSheetDialogLanguageBinding.inflate(layoutInflater) }
    private lateinit var prefs: SharedPreferences

    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = requireContext().getSharedPreferences("MODE", MODE_PRIVATE)


        when (prefs.getString("theme", "1")) {
            "1" -> binding.textTheme.text = requireContext().getString(R.string.light)
            "2" -> binding.textTheme.text = requireContext().getString(R.string.night)
            "3" -> binding.textTheme.text = requireContext().getString(R.string.system)
            else ->  binding.textTheme.text = requireContext().getString(R.string.light)
        }
        when (prefs.getString("language", "ru")) {
            "ru" -> binding.textLanguage.text = requireContext().getString(R.string.russian)
            "en" -> binding.textLanguage.text = requireContext().getString(R.string.english)
            else ->  binding.textTheme.text = requireContext().getString(R.string.russian)
        }

        val bottomSheetDialogTheme = BottomSheetDialog(requireContext())
        bottomSheetDialogTheme.setContentView(bottomSheetBindingTheme.root)

        binding.textTheme.setOnClickListener {
            bottomSheetDialogTheme.show()
        }
        bottomSheetBindingTheme.btnLightTheme.setOnClickListener {
            themeSetListener("1")
            bottomSheetDialogTheme.dismiss()
        }
        bottomSheetBindingTheme.btnDarkTheme.setOnClickListener {
            themeSetListener("2")
            bottomSheetDialogTheme.dismiss()
        }
        bottomSheetBindingTheme.btnSystemTheme.setOnClickListener {
            themeSetListener("3")
            bottomSheetDialogTheme.dismiss()
        }

        val bottomSheetDialogLanguage = BottomSheetDialog(requireContext())
        bottomSheetDialogLanguage.setContentView(bottomSheetBindingLanguage.root)

        binding.textLanguage.setOnClickListener {
            bottomSheetDialogLanguage.show()
        }
        bottomSheetBindingLanguage.btnRussian .setOnClickListener {
            languageSetListener("ru")
            bottomSheetDialogLanguage.dismiss()
        }
        bottomSheetBindingLanguage.btnEnglish.setOnClickListener {
            languageSetListener("en")
            bottomSheetDialogLanguage.dismiss()
        }
    }

    private fun themeSetListener(themeCode: String) {
        val themes = mapOf(
            "1" to Triple(AppCompatDelegate.MODE_NIGHT_NO, R.string.light, "1"),
            "2" to Triple(AppCompatDelegate.MODE_NIGHT_YES, R.string.night, "2"),
            "3" to Triple(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, R.string.system, "3")
        )

        themes[themeCode]?.let { (mode, stringRes, modeTheme) ->
            AppCompatDelegate.setDefaultNightMode(mode)
            binding.textTheme.text = requireContext().getString(stringRes)
            prefs.edit().putString("theme", modeTheme).apply()
        }
    }

    private fun languageSetListener(languageCode: String) {
        val language = mapOf(
            "ru" to Pair("ru", R.string.russian),
            "en" to Pair("en", R.string.english),
        )

        language[languageCode]?.let { (language, stringRes) ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                requireContext().getSystemService(LocaleManager::class.java).applicationLocales = LocaleList.forLanguageTags(language)
            } else {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
            }
            binding.textLanguage.text = requireContext().getString(stringRes)
            prefs.edit().putString("language", language).apply()
        }
    }

}