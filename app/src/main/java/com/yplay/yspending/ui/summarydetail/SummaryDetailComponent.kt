package com.yplay.yspending.ui.summarydetail

import dagger.Subcomponent

@Subcomponent(modules = [SummaryDetailModule::class])
interface SummaryDetailComponent {
    fun inject(fragment: SummaryDetailFragment)
    fun inject(fragment: SummaryDetailDialogFragment)
}