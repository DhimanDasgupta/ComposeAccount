package com.dhimandasgupta.composeaccount.data

import androidx.datastore.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.dhimandasgupta.composeaccount.AccountPreferences
import java.io.InputStream
import java.io.OutputStream

class AccountPreferenceSerializer : Serializer<AccountPreferences> {
    companion object {
        val accountPreferenceSerializer = AccountPreferenceSerializer()
        const val prefFileName: String = "AccountPreference.pb"

        const val DEFAULT_NAME = "----"
        const val PHONE_NUMBER = "+91 9916107201"
        const val GIT_LINK = "https://github.com/DhimanDasgupta"
        const val EMAIL = "dhiman4android@gmail.com"
        val ID: String = EMAIL.hashCode().toString()
    }

    override fun readFrom(input: InputStream): AccountPreferences = try {
        AccountPreferences.parseFrom(input).toFull()
    } catch (exception: InvalidProtocolBufferException) {
        defaultAccountPreferences()
    }

    override fun writeTo(t: AccountPreferences, output: OutputStream) = t.writeTo(output)
}

/**
 * Some properties are static and some are dynamic
 * This will return the AccountPreferences with all properties filled
 * */
private fun AccountPreferences.toFull() = AccountPreferences
    .newBuilder()
    .setName(if (this.name.isBlank()) AccountPreferenceSerializer.DEFAULT_NAME else this.name)
    .setPhoneNumber(AccountPreferenceSerializer.PHONE_NUMBER)
    .setProfileImageLocalFilePath(this.profileImageLocalFilePath)
    .setId(AccountPreferenceSerializer.ID)
    .setEmail(AccountPreferenceSerializer.EMAIL)
    .setGithubLink(AccountPreferenceSerializer.GIT_LINK)
    .setAsyncToggle(this.asyncToggle)
    .setSyncToggle(this.syncToggle)
    .build()

/**
 * Some properties are static and some are dynamic
 * This will return the AccountPreferences with all properties filled with default values
 * */
fun defaultAccountPreferences(): AccountPreferences = AccountPreferences
    .newBuilder()
    .setName(AccountPreferenceSerializer.DEFAULT_NAME)
    .setPhoneNumber(AccountPreferenceSerializer.PHONE_NUMBER)
    .setId(AccountPreferenceSerializer.ID)
    .setEmail(AccountPreferenceSerializer.EMAIL)
    .setGithubLink(AccountPreferenceSerializer.GIT_LINK)
    .setAsyncToggle(false)
    .setSyncToggle(false)
    .build()
