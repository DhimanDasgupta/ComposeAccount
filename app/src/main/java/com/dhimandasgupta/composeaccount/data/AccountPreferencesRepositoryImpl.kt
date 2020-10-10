package com.dhimandasgupta.composeaccount.data

import androidx.datastore.DataStore
import com.dhimandasgupta.composeaccount.AccountPreferences
import kotlinx.coroutines.flow.Flow

class AccountPreferencesRepositoryImpl(private val dataStore: DataStore<AccountPreferences>) : AccountPreferencesRepository {
    override fun getAccountPreferences(): Flow<AccountPreferences> = dataStore.data

    override suspend fun setProfileImagePath(imagePath: String) {
        dataStore.updateData { accountPreferences ->
            accountPreferences.toBuilder().setProfileImageLocalFilePath(imagePath).build()
        }
    }

    override suspend fun setName(name: String) {
        dataStore.updateData { accountPreferences ->
            accountPreferences.toBuilder().setName(name).build()
        }
    }

    override suspend fun setAsyncToggle(checked: Boolean) {
        dataStore.updateData { accountPreferences ->
            accountPreferences.toBuilder().setAsyncToggle(checked).build()
        }
    }

    override suspend fun setSyncToggle(checked: Boolean) {
        dataStore.updateData { accountPreferences ->
            accountPreferences.toBuilder().setSyncToggle(checked).build()
        }
    }
}
