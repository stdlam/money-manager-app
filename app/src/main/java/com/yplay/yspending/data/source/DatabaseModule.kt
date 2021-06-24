package com.yplay.yspending.data.source

import androidx.room.Room
import com.yplay.yspending.SpendingApp
import com.yplay.yspending.data.source.local.spend.SpendDao
import com.yplay.yspending.data.source.local.spend.SpendDatabase
import com.yplay.yspending.data.source.local.suggest.SuggestionDao
import com.yplay.yspending.data.source.local.suggest.SuggestionDatabase
import com.yplay.yspending.enum.Constant
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideSpendDatabase(app: SpendingApp): SpendDatabase {
        return Room.databaseBuilder(app.applicationContext, SpendDatabase::class.java, Constant.SPEND_DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideSpendDao(db: SpendDatabase): SpendDao {
        return db.spendDao()
    }

    @Singleton
    @Provides
    fun provideSuggestionDatabase(app: SpendingApp): SuggestionDatabase {
        return Room.databaseBuilder(app.applicationContext, SuggestionDatabase::class.java, Constant.SUGGESTION_DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideSuggestionDao(db: SuggestionDatabase): SuggestionDao {
        return db.suggestionDao()
    }

}