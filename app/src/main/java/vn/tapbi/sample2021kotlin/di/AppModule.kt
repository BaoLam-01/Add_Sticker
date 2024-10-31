package vn.tapbi.sample2021kotlin.di

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.tapbi.sample2021kotlin.common.Constant
import vn.tapbi.sample2021kotlin.data.local.SharedPreferenceHelper
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideShared(context: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideSharedPreference(sharedPreferences: SharedPreferences): SharedPreferenceHelper {
        return SharedPreferenceHelper(sharedPreferences)
    }


}
