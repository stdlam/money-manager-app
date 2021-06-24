package com.yplay.yspending.ui.home

import dagger.Subcomponent

@Subcomponent(modules = [HomeModule::class])
interface HomeComponent {
    fun inject(fragment: HomeFragment)
}