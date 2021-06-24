package com.yplay.yspending.ui.overview

import dagger.Subcomponent

@Subcomponent(modules = [OverviewModule::class])
interface OverviewComponent {
    fun inject(overviewFragment: OverviewFragment)
}