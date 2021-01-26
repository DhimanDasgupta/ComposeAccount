package com.dhimandasgupta.composeaccount.ui.screens

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.outlined.Face
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.AmbientConfiguration
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.dhimandasgupta.composeaccount.ui.common.ComposeAccountTheme
import com.dhimandasgupta.composeaccount.ui.data.ACCOUNT_SWITCH_DATA_USAGE
import com.dhimandasgupta.composeaccount.ui.data.ACCOUNT_SWITCH_LOCATION
import com.dhimandasgupta.composeaccount.ui.data.ACCOUNT_SWITCH_PRIVACY
import com.dhimandasgupta.composeaccount.ui.data.ACCOUNT_TEXT_NAME
import com.dhimandasgupta.composeaccount.ui.data.AccountHeading
import com.dhimandasgupta.composeaccount.ui.data.AccountProfileImage
import com.dhimandasgupta.composeaccount.ui.data.AccountProfileLink
import com.dhimandasgupta.composeaccount.ui.data.AccountProfileSwitch
import com.dhimandasgupta.composeaccount.ui.data.AccountProfileText
import com.dhimandasgupta.composeaccount.ui.data.AllAccountItems
import com.dhimandasgupta.composeaccount.viewmodel.AccountViewModel
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.io.File

@ExperimentalCoroutinesApi
@Composable
fun AccountRoot(
    accountViewModel: AccountViewModel,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeletePhoto: () -> Unit,
    onLocationRequested: () -> Unit,
    onRequestToOpenBrowser: (String) -> Unit
) {
    val accountState = accountViewModel.allAccountItems.observeAsState(null)

    ComposeAccountTheme {
        Column(
            modifier = Modifier
                .background(color = colors.surface)
                .fillMaxSize()
        ) {
            CreateToolbar()

            accountState.value?.let { allAccountItems ->
                CreateAccountList(
                    allAccountItems = allAccountItems,
                    accountViewModel = accountViewModel,
                    onCameraClicked = onCameraClicked,
                    onGalleryClicked = onGalleryClicked,
                    onDeletePhoto = onDeletePhoto,
                    onLocationRequested = onLocationRequested,
                    onRequestToOpenBrowser = onRequestToOpenBrowser,
                )
            } ?: CreateLoading()
        }
    }
}

@Composable
fun CreateToolbar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = "Account Settings",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            color = colors.onSurface,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = typography.h5,
        )
    }
}

@Composable
fun CreateLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = colors.onSurface
        )
    }
}

@ExperimentalCoroutinesApi
@Composable
fun CreateAccountList(
    allAccountItems: AllAccountItems,
    accountViewModel: AccountViewModel,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeletePhoto: () -> Unit,
    onLocationRequested: () -> Unit,
    onRequestToOpenBrowser: (String) -> Unit,
) {
    when (AmbientConfiguration.current.orientation) {
        ORIENTATION_LANDSCAPE -> CreateAccountListForLandscape(
            allAccountItems = allAccountItems,
            accountViewModel = accountViewModel,
            onCameraClicked = onCameraClicked,
            onGalleryClicked = onGalleryClicked,
            onDeletePhoto = onDeletePhoto,
            onLocationRequested = onLocationRequested,
            onRequestToOpenBrowser = onRequestToOpenBrowser,
        )
        else -> CreateAccountListForPortrait(
            allAccountItems = allAccountItems,
            accountViewModel = accountViewModel,
            onCameraClicked = onCameraClicked,
            onGalleryClicked = onGalleryClicked,
            onDeletePhoto = onDeletePhoto,
            onLocationRequested = onLocationRequested,
            onRequestToOpenBrowser = onRequestToOpenBrowser,
        )
    }
}

@ExperimentalCoroutinesApi
@Composable
fun CreateAccountListForPortrait(
    allAccountItems: AllAccountItems,
    accountViewModel: AccountViewModel,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeletePhoto: () -> Unit,
    onLocationRequested: () -> Unit,
    onRequestToOpenBrowser: (String) -> Unit,
) {
    LazyColumn {
        items(
            items = allAccountItems.accountItems,
            itemContent = { accountItem ->
                when (accountItem) {
                    is AccountProfileImage -> CreateAccountProfileImage(
                        accountProfileImage = accountItem,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        onCameraClicked = onCameraClicked,
                        onGalleryClicked = onGalleryClicked,
                        onDeletePhoto = onDeletePhoto,
                    )
                    is AccountHeading -> CreateAccountProfileHeading(
                        accountProfileHeading = accountItem,
                    )
                    is AccountProfileText -> CreateAccountProfileText(
                        accountViewModel = accountViewModel,
                        accountProfileText = accountItem,
                    )
                    is AccountProfileSwitch -> CreateAccountProfileSwitch(
                        accountProfileSwitch = accountItem,
                        accountViewModel = accountViewModel,
                        onLocationRequested = onLocationRequested,
                    )
                    is AccountProfileLink -> CreateAccountProfileLink(
                        accountProfileLink = accountItem,
                        onRequestToOpenBrowser = onRequestToOpenBrowser
                    )
                }
            }
        )
    }
}

@ExperimentalCoroutinesApi
@Composable
fun CreateAccountListForLandscape(
    allAccountItems: AllAccountItems,
    accountViewModel: AccountViewModel,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeletePhoto: () -> Unit,
    onLocationRequested: () -> Unit,
    onRequestToOpenBrowser: (String) -> Unit,
) {
    val firstItemIsProfileImage = allAccountItems.accountItems.isNotEmpty() && allAccountItems.accountItems[0] is AccountProfileImage

    if (firstItemIsProfileImage) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
        ) {
            CreateAccountProfileImage(
                accountProfileImage = allAccountItems.accountItems[0] as AccountProfileImage,
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .fillMaxHeight(),
                onCameraClicked = onCameraClicked,
                onGalleryClicked = onGalleryClicked,
                onDeletePhoto = onDeletePhoto,
            )
            LazyColumn {
                items(
                    items = allAccountItems.accountItems.subList(1, allAccountItems.accountItems.size),
                    itemContent = { accountItem ->
                        when (accountItem) {
                            is AccountHeading -> CreateAccountProfileHeading(
                                accountProfileHeading = accountItem,
                            )
                            is AccountProfileText -> CreateAccountProfileText(
                                accountViewModel = accountViewModel,
                                accountProfileText = accountItem,
                            )
                            is AccountProfileSwitch -> CreateAccountProfileSwitch(
                                accountProfileSwitch = accountItem,
                                accountViewModel = accountViewModel,
                                onLocationRequested = onLocationRequested,
                            )
                            is AccountProfileLink -> CreateAccountProfileLink(
                                accountProfileLink = accountItem,
                                onRequestToOpenBrowser = onRequestToOpenBrowser,
                            )
                            else -> {}
                        }
                    }
                )
            }
        }
    } else {
        CreateAccountListForLandscape(
            allAccountItems = allAccountItems,
            accountViewModel = accountViewModel,
            onCameraClicked = onCameraClicked,
            onGalleryClicked = onGalleryClicked,
            onDeletePhoto = onDeletePhoto,
            onLocationRequested = onLocationRequested,
            onRequestToOpenBrowser = onRequestToOpenBrowser,
        )
    }
}

@Composable
fun CreateAccountProfileImage(
    accountProfileImage: AccountProfileImage,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeletePhoto: () -> Unit,
    modifier: Modifier
) {
    val directory = File(AmbientContext.current.cacheDir, "Camera")

    val openDialog = remember { mutableStateOf(false) }

    Spacer(
        modifier = Modifier
            .size(8.dp)
    )
    Box(
        modifier = Modifier
            .then(modifier)
            .size(156.dp),
        contentAlignment = Alignment.Center,
    ) {
        // Need to check How to show Placeholder in Coil in compose
        if (accountProfileImage.profileImage.isNotBlank()) {
            CoilImage(
                data = File(
                    directory,
                    accountProfileImage.profileImage
                ),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(156.dp)
                    .clip(CircleShape),
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.Face.copy(
                    defaultWidth = 156.dp,
                    defaultHeight = 156.dp
                ),
                tint = colors.onSurface,
                modifier = Modifier
                    .size(156.dp)
                    .clip(CircleShape),
            )
        }

        Icon(
            imageVector = Icons.Filled.Face.copy(
                defaultWidth = 48.dp,
                defaultHeight = 48.dp
            ),
            tint = colors.onSurface,
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .preferredSize(48.dp)
                .clickable(
                    onClick = { openDialog.value = true }
                )
        )
    }

    if (openDialog.value) {
        ShowImageUpdateSelectionDialog(
            onCameraClicked = onCameraClicked,
            onGalleryClicked = onGalleryClicked,
            onDeletePhoto = if (accountProfileImage.profileImage.isNotBlank()) onDeletePhoto else null,
        ) { openDialog.value = false }
    }
}

@Composable
fun CreateAccountProfileHeading(
    accountProfileHeading: AccountHeading
) {
    Text(
        text = accountProfileHeading.label,
        style = typography.overline,
        textAlign = TextAlign.Start,
        color = colors.onSurface,
        modifier = Modifier
            .wrapContentWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
    )
}

@ExperimentalCoroutinesApi
@Composable
fun CreateAccountProfileText(
    accountViewModel: AccountViewModel,
    accountProfileText: AccountProfileText,
) {
    val openNameChangeDialog = remember { mutableStateOf(false) }

    if (openNameChangeDialog.value) {
        ShowNameUpdateDialog(
            initialName = accountProfileText.value,
            onSaveClicked = { name -> accountViewModel.setName(name) },
            onDismissRequest = { openNameChangeDialog.value = false },
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = accountProfileText.label,
            style = typography.body2,
            textAlign = TextAlign.Start,
            maxLines = 1,
            color = colors.onSurface,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(
            modifier =
            Modifier.size(4.dp)
        )
        Text(
            text = accountProfileText.value,
            style = typography.subtitle2,
            textAlign = TextAlign.End,
            maxLines = 1,
            color = colors.onSurface,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .clickable(
                    onClick = {
                        when (accountProfileText.id) {
                            ACCOUNT_TEXT_NAME.hashCode() -> openNameChangeDialog.value = true
                            else -> { }
                        }
                    }
                )
        )
    }
}

@ExperimentalCoroutinesApi
@Composable
fun CreateAccountProfileSwitch(
    accountProfileSwitch: AccountProfileSwitch,
    accountViewModel: AccountViewModel,
    onLocationRequested: () -> Unit,
) {
    val openPermissionOffRequestDialog = remember { mutableStateOf(false) }

    if (openPermissionOffRequestDialog.value) {
        OnLocationSwitchOffRequest(onDismissRequest = { openPermissionOffRequestDialog.value = false })
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.75f)
        ) {
            Text(
                text = accountProfileSwitch.label,
                style = typography.body2,
                textAlign = TextAlign.Start,
                maxLines = 1,
                color = colors.onSurface,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(
                modifier = Modifier.size(4.dp)
            )
            Text(
                text = accountProfileSwitch.detailsLabel,
                style = typography.caption,
                textAlign = TextAlign.Start,
                color = colors.onSurface,
            )
        }
        Switch(
            checked = accountProfileSwitch.switchValue,
            onCheckedChange = { checked ->
                when (accountProfileSwitch.id) {
                    ACCOUNT_SWITCH_PRIVACY.hashCode() -> {
                        accountViewModel.setAsyncToggle(checked = checked)
                    }
                    ACCOUNT_SWITCH_LOCATION.hashCode() -> {
                        if (checked) onLocationRequested.invoke() else openPermissionOffRequestDialog.value = true
                    }
                    ACCOUNT_SWITCH_DATA_USAGE.hashCode() -> {
                        accountViewModel.setSyncToggle(checked = checked)
                    }
                }
            },
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth(0.25f),
        )
    }
}

@Composable
fun CreateAccountProfileLink(
    accountProfileLink: AccountProfileLink,
    onRequestToOpenBrowser: (String) -> Unit,
) {
    Text(
        text = accountProfileLink.label,
        style = typography.subtitle2,
        textAlign = TextAlign.Start,
        maxLines = 1,
        color = colors.onSurface,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .wrapContentWidth()
            .clickable(onClick = { onRequestToOpenBrowser.invoke(accountProfileLink.url) })
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
fun OnLocationSwitchOffRequest(
    onDismissRequest: () -> Unit,
) {
    ComposeAccountTheme {
        Dialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = colors.surface,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Permission can not be denied from application, please go to app settings and turn off you location permission manually.",
                    color = colors.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun ShowImageUpdateSelectionDialog(
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeletePhoto: (() -> Unit)? = null,
    onDismissRequest: () -> Unit,
) {
    ComposeAccountTheme {
        Dialog(
            onDismissRequest = onDismissRequest,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = colors.background,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp),
            ) {
                Text(
                    text = "Select from Camera",
                    maxLines = 1,
                    color = colors.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                onDismissRequest.invoke()
                                onCameraClicked.invoke()
                            }
                        )
                        .padding(4.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Select from Gallery",
                    maxLines = 1,
                    color = colors.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .clickable(
                            onClick = {
                                onDismissRequest.invoke()
                                onGalleryClicked.invoke()
                            }
                        )
                        .padding(4.dp)
                )

                if (onDeletePhoto != null) {
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = "Delete Image",
                        maxLines = 1,
                        color = colors.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    onDismissRequest.invoke()
                                    onDeletePhoto.invoke()
                                }
                            )
                            .padding(4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ShowNameUpdateDialog(
    initialName: String,
    onSaveClicked: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val textState = remember { mutableStateOf(TextFieldValue(initialName)) }

    ComposeAccountTheme {
        Dialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = colors.background,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = "Change your profile name",
                    maxLines = 1,
                    color = colors.onSurface,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(4.dp),
                )
                Spacer(
                    modifier = Modifier
                        .size(8.dp),
                )
                TextField(
                    value = textState.value,
                    textStyle = TextStyle(
                        color = colors.onSurface,
                    ),
                    onValueChange = { textState.value = it },
                )
                Spacer(
                    modifier = Modifier
                        .size(8.dp),
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = "Cancel",
                        maxLines = 1,
                        color = colors.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .clickable(onClick = onDismissRequest)
                            .padding(top = 16.dp),
                    )

                    Text(
                        text = "Save",
                        maxLines = 1,
                        color = colors.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    onSaveClicked.invoke(textState.value.text)
                                    onDismissRequest.invoke()
                                }
                            )
                            .padding(top = 16.dp),
                    )
                }
            }
        }
    }
}
