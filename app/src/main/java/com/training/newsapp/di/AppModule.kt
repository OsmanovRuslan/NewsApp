package com.training.newsapp.di

import com.training.newsapp.fragments.favorites.FavoritesViewModel
import com.training.newsapp.fragments.news.NewsViewModel
import com.training.newsapp.fragments.settings.SettingsViewModel
import com.training.newsapp.preferences.Prefs
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { Prefs.getInstance(get()) }
    viewModel{NewsViewModel()}
    viewModel{FavoritesViewModel()}
    viewModelOf(::SettingsViewModel)
}