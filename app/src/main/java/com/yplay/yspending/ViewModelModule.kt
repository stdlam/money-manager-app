package com.yplay.yspending

import androidx.lifecycle.ViewModel
import com.yplay.yspending.ui.home.HomeViewModel
import com.yplay.yspending.ui.overview.OverviewViewModel
import com.yplay.yspending.ui.summarydetail.SummaryDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OverviewViewModel::class)
    abstract fun bindOverviewViewModel(overviewViewModel: OverviewViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SummaryDetailViewModel::class)
    abstract fun bindSummaryDetailViewModel(summaryDetailViewModel: SummaryDetailViewModel): ViewModel
}