package com.dhimandasgupta.composeaccount.ui.data

import com.dhimandasgupta.composeaccount.AccountPreferences

private const val ACCOUNT_PROFILE_IMAGE = "ACCOUNT_PROFILE_IMAGE"
private const val ACCOUNT_HEADING_CONTACTS = "ACCOUNT_HEADING_CONTACTS"
private const val ACCOUNT_TEXT_NAME = "ACCOUNT_TEXT_NAME"
private const val ACCOUNT_TEXT_EMAIL = "ACCOUNT_TEXT_EMAIL"
private const val ACCOUNT_TEXT_NUMBER = "ACCOUNT_TEXT_NUMBER"
private const val ACCOUNT_HEADING_TOGGLES = "ACCOUNT_HEADING_TOGGLES"
const val ACCOUNT_SWITCH_PRIVACY = "ACCOUNT_SWITCH_PRIVACY"
const val ACCOUNT_SWITCH_LOCATION = "ACCOUNT_SWITCH_LOCATION"
const val ACCOUNT_SWITCH_DATA_USAGE = "ACCOUNT_SWITCH_DATA_USAGE"
private const val ACCOUNT_HEADING_LINKS = "ACCOUNT_HEADING_LINKS"
private const val ACCOUNT_LINK_GITHUB = "ACCOUNT_LINK_GITHUB"
private const val ACCOUNT_LINK_FB = "ACCOUNT_LINK_FB"
private const val ACCOUNT_LINK_TWITTER = "ACCOUNT_LINK_TWITTER"
private const val ACCOUNT_LINK_INSTAGRAM = "ACCOUNT_LINK_INSTAGRAM"
private const val ACCOUNT_LINK_YOUTUBE = "ACCOUNT_LINK_YOUTUBE"

fun AccountPreferences?.toFullAccountProfile(locationGranted: Boolean = false) = AllAccountItems(
    accountItems = listOf(
        AccountProfileImage(
            id = ACCOUNT_PROFILE_IMAGE.hashCode(),
            profileImage = this?.profileImageLocalFilePath ?: "",
        ),
        AccountHeading(
            id = ACCOUNT_HEADING_CONTACTS.hashCode(),
            label = "Contacts",
        ),
        AccountProfileText(
            id = ACCOUNT_TEXT_NAME.hashCode(),
            label = "Profile Name",
            value = this?.name ?: "----",
        ),
        AccountProfileText(
            id = ACCOUNT_TEXT_EMAIL.hashCode(),
            label = "Email",
            value = this?.email ?: "----",
        ),
        AccountProfileText(
            id = ACCOUNT_TEXT_NUMBER.hashCode(),
            label = "Profile Number",
            value = this?.phoneNumber ?: "----",
        ),
        AccountHeading(
            id = ACCOUNT_HEADING_TOGGLES.hashCode(),
            label = "Privacy",
        ),
        AccountProfileSwitch(
            id = ACCOUNT_SWITCH_PRIVACY.hashCode(),
            label = "Some Async Operation",
            detailsLabel = "On changing the switch it dose some async operation so the change will not be immediate",
            switchValue = this?.asyncToggle ?: false
        ),
        AccountProfileSwitch(
            id = ACCOUNT_SWITCH_LOCATION.hashCode(),
            label = "Indicates some toggle",
            detailsLabel = "This switch's value depends on some external conditions and this is not toggleable",
            switchValue = locationGranted
        ),
        AccountProfileSwitch(
            id = ACCOUNT_SWITCH_DATA_USAGE.hashCode(),
            label = "Some sync Operation",
            detailsLabel = "On changing the switch it dose some sync operation so the change will be immediate",
            switchValue = this?.syncToggle ?: false
        ),
        AccountHeading(
            id = ACCOUNT_HEADING_LINKS.hashCode(),
            label = "Links",
        ),
        AccountProfileLink(
            id = ACCOUNT_LINK_GITHUB.hashCode(),
            label = "Github",
            url = this?.githubLink ?: ""
        ),
        AccountProfileLink(
            id = ACCOUNT_LINK_FB.hashCode(),
            label = "Facebook",
            url = "https://www.facebook.com/dhiman.dasgupta.71"
        ),
        AccountProfileLink(
            id = ACCOUNT_LINK_TWITTER.hashCode(),
            label = "Twitter",
            url = "https://twitter.com/dhiman4android"
        ),
        AccountProfileLink(
            id = ACCOUNT_LINK_INSTAGRAM.hashCode(),
            label = "Instagram",
            url = "https://www.instagram.com/dhimandasgupta/"
        ),
        AccountProfileLink(
            id = ACCOUNT_LINK_YOUTUBE.hashCode(),
            label = "Youtube",
            url = "https://www.youtube.com/c/dhimandasgupta"
        ),
    )
)
