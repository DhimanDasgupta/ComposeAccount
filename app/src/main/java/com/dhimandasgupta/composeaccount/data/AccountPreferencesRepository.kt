package com.dhimandasgupta.composeaccount.data

import com.dhimandasgupta.composeaccount.AccountPreferences
import kotlinx.coroutines.flow.Flow

interface AccountPreferencesRepository {
    fun getAccountPreferences(): Flow<AccountPreferences>

    suspend fun setProfileImagePath(imagePath: String)

    suspend fun setName(name: String)

    suspend fun setAsyncToggle(checked: Boolean)

    suspend fun setSyncToggle(checked: Boolean)
}
