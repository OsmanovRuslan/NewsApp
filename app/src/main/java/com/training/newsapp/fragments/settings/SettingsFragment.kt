package com.training.newsapp.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.training.newsapp.databinding.BottomSheetDialogBinding
import com.training.newsapp.databinding.FragmentSettingsBinding
import com.training.newsapp.fragments.ViewBindingFragment
import com.training.newsapp.fragments.news.NewsViewModel
import com.training.newsapp.preferences.IPrefs
import com.training.newsapp.preferences.Prefs
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>() {


    private val vm by lazy {
        ViewModelProvider(this, SettingsViewModelFactory(requireContext()))[SettingsViewModel::class.java]
    }
    private val bottomSheetBinding by lazy { BottomSheetDialogBinding.inflate(layoutInflater) }


    override fun makeBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm.initializeSettings(requireContext())

        vm.themeChosen
            .onEach { binding.textTheme.text = it }
            .launchIn(lifecycleScope)

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(bottomSheetBinding.root)

        binding.textTheme.setOnClickListener {
            bottomSheetDialog.show()
        }
        bottomSheetBinding.btnLightTheme.setOnClickListener {
            vm.themeSetListener(requireContext(), "1")
            bottomSheetDialog.dismiss()
        }
        bottomSheetBinding.btnDarkTheme.setOnClickListener {
            vm.themeSetListener(requireContext(),"2")
            bottomSheetDialog.dismiss()
        }
        bottomSheetBinding.btnSystemTheme.setOnClickListener {
            vm.themeSetListener(requireContext(),"3")
            bottomSheetDialog.dismiss()
        }
    }

}