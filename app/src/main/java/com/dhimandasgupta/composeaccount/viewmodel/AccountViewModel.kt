package com.dhimandasgupta.composeaccount.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dhimandasgupta.composeaccount.data.AccountPreferencesRepository
import com.dhimandasgupta.composeaccount.ui.data.toFullAccountProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AccountViewModel @ViewModelInject constructor(
    private val accountPreferencesRepository: AccountPreferencesRepository
) : ViewModel() {
    val allAccountItems = accountPreferencesRepository
        .getAccountPreferences()
        .map { accountPreferences ->
            accountPreferences.toFullAccountProfile()
        }
        .asLiveData(viewModelScope.coroutineContext)

    init {
        viewModelScope.launch {
            delay(5_000)
            setName("Dhiman Dasgupta")
        }
    }

    fun setProfileImagePath(filePath: String) = viewModelScope.launch {
        accountPreferencesRepository.setProfileImagePath(imagePath = filePath)
    }

    fun deleteProfileImage() = viewModelScope.launch {
        accountPreferencesRepository.setProfileImagePath("")
    }

    fun setName(name: String) = viewModelScope.launch {
        accountPreferencesRepository.setName(name = name)
    }

    fun setAsyncToggle(checked: Boolean) = viewModelScope.launch {
        delay(1_000)
        accountPreferencesRepository.setAsyncToggle(checked = checked)
    }

    fun setSyncToggle(checked: Boolean) = viewModelScope.launch {
        accountPreferencesRepository.setSyncToggle(checked = checked)
    }
}
