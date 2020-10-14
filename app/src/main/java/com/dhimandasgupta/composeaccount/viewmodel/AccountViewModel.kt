package com.dhimandasgupta.composeaccount.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dhimandasgupta.composeaccount.data.AccountPreferencesRepository
import com.dhimandasgupta.composeaccount.ui.data.AllAccountItems
import com.dhimandasgupta.composeaccount.ui.data.defaultAllAccountItems
import com.dhimandasgupta.composeaccount.ui.data.toFullAccountProfile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class AccountViewModel @ViewModelInject constructor(
    private val accountPreferencesRepository: AccountPreferencesRepository
) : ViewModel() {
    // Source One
    private val allAccountItemsFromRepository = accountPreferencesRepository
        .getAccountPreferences()
        .onStart { delay(2_000) }
        .asLiveData(viewModelScope.coroutineContext)

    // Source Two
    private val locationLiveData = MutableLiveData(false)

    // Merged private mutable source
    private val allAccountItemsMediatorLiveData = MediatorLiveData<AllAccountItems>().also {
        it.value = defaultAllAccountItems()
    }

    // Merged public immutable source
    val allAccountItems: LiveData<AllAccountItems> = allAccountItemsMediatorLiveData

    init {
        allAccountItemsMediatorLiveData.addSource(allAccountItemsFromRepository) {
            mergeLiveDatumToGenerateFinalUIModel()
        }
        allAccountItemsMediatorLiveData.addSource(locationLiveData) {
            mergeLiveDatumToGenerateFinalUIModel()
        }
    }

    override fun onCleared() {
        super.onCleared()
        allAccountItemsMediatorLiveData.removeSource(allAccountItemsFromRepository)
        allAccountItemsMediatorLiveData.removeSource(locationLiveData)
    }

    private fun mergeLiveDatumToGenerateFinalUIModel() {
        val finaleUIModel = allAccountItemsFromRepository.value?.toFullAccountProfile(locationLiveData.value ?: false)
        allAccountItemsMediatorLiveData.postValue(finaleUIModel)
    }

    fun setLocationGranted(granted: Boolean) {
        locationLiveData.postValue(granted)
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
