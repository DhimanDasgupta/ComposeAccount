package com.dhimandasgupta.composeaccount.di

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.createDataStore
import com.dhimandasgupta.composeaccount.AccountPreferences
import com.dhimandasgupta.composeaccount.data.AccountPreferenceSerializer
import com.dhimandasgupta.composeaccount.data.AccountPreferencesRepository
import com.dhimandasgupta.composeaccount.data.AccountPreferencesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object DataStoreModule {
    @Provides
    fun providesDataStore(@ApplicationContext context: Context): DataStore<AccountPreferences> =
        context.createDataStore(
            fileName = AccountPreferenceSerializer.prefFileName,
            serializer = AccountPreferenceSerializer.accountPreferenceSerializer
        )
}

@Module
@InstallIn(ActivityRetainedComponent::class)
class ActivityModule {
    @Provides
    fun providesAccountPreferencesRepository(dataStore: DataStore<AccountPreferences>): AccountPreferencesRepository =
        AccountPreferencesRepositoryImpl(
            dataStore = dataStore
        )
}
