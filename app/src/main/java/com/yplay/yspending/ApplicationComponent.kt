package com.yplay.yspending

import com.yplay.yspending.data.source.DatabaseModule
import com.yplay.yspending.ui.home.HomeComponent
import com.yplay.yspending.ui.home.HomeModule
import com.yplay.yspending.ui.overview.OverviewComponent
import com.yplay.yspending.ui.overview.OverviewModule
import com.yplay.yspending.ui.splashscreen.SplashComponent
import com.yplay.yspending.ui.splashscreen.SplashModule
import com.yplay.yspending.ui.summarydetail.SummaryDetailComponent
import com.yplay.yspending.ui.summarydetail.SummaryDetailModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class, DatabaseModule::class])
interface ApplicationComponent {
    fun inject(spendingApp: SpendingApp)

    fun plus(module: HomeModule): HomeComponent

    fun plus(module: OverviewModule): OverviewComponent

    fun plus(module: SummaryDetailModule): SummaryDetailComponent

    fun plus(module: SplashModule): SplashComponent
}