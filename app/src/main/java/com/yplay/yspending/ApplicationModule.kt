package com.yplay.yspending

import com.yplay.yspending.data.source.DatabaseModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [DatabaseModule::class])
class ApplicationModule internal constructor(private val mApplication: SpendingApp) {
    @Singleton
    @Provides
    fun provideApplication() : SpendingApp {
        return mApplication
    }

}