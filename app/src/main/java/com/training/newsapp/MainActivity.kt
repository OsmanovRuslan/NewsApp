package com.training.newsapp

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.training.newsapp.databinding.ActivityMainBinding
import com.training.newsapp.preferences.IPrefs
import com.training.newsapp.preferences.Prefs

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val bottomNavigationView = binding.bottomNavigationView
        val navController = binding.fragmentContainerView.getFragment<NavHostFragment>().navController
        bottomNavigationView.setupWithNavController(navController)
    }
}