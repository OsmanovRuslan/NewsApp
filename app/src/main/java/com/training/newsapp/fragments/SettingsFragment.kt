package com.training.newsapp.fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.training.newsapp.R
import com.training.newsapp.databinding.BottomSheetDialogBinding
import com.training.newsapp.databinding.FragmentSettingsBinding

class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>() {

    private val bottomSheetBinding by lazy { BottomSheetDialogBinding.inflate(layoutInflater) }
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

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        binding.textTheme.setOnClickListener {
            bottomSheetDialog.show()
        }
        bottomSheetBinding.btnLightTheme.setOnClickListener {
            themeSetListener("1")
            bottomSheetDialog.dismiss()
        }
        bottomSheetBinding.btnDarkTheme.setOnClickListener {
            themeSetListener("2")
            bottomSheetDialog.dismiss()
        }
        bottomSheetBinding.btnSystemTheme.setOnClickListener {
            themeSetListener("3")
            bottomSheetDialog.dismiss()
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
}