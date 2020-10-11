package com.dhimandasgupta.composeaccount.ui.data

import com.dhimandasgupta.composeaccount.data.defaultAccountPreferences

sealed class AccountItem(open val id: Int)

data class AccountHeading(override val id: Int, val label: String) : AccountItem(id)

data class AccountProfileImage(override val id: Int, val profileImage: String) : AccountItem(id)

data class AccountProfileText(override val id: Int, val label: String, val value: String) : AccountItem(id)

data class AccountProfileSwitch(override val id: Int, val label: String, val detailsLabel: String, val switchValue: Boolean) : AccountItem(id)

data class AccountProfileLink(override val id: Int, val label: String, val url: String) : AccountItem(id)

data class AllAccountItems(val accountItems: List<AccountItem>)

fun defaultAllAccountItems(): AllAccountItems = defaultAccountPreferences().toFullAccountProfile(false)
