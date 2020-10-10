package com.dhimandasgupta.composeaccount.ui.screens

import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.widget.Toast
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
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
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
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
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.dhimandasgupta.composeaccount.ui.common.ComposeAccountTheme
import com.dhimandasgupta.composeaccount.ui.data.ACCOUNT_SWITCH_DATA_USAGE
import com.dhimandasgupta.composeaccount.ui.data.ACCOUNT_SWITCH_LOCATION
import com.dhimandasgupta.composeaccount.ui.data.ACCOUNT_SWITCH_PRIVACY
import com.dhimandasgupta.composeaccount.ui.data.AccountHeading
import com.dhimandasgupta.composeaccount.ui.data.AccountProfileImage
import com.dhimandasgupta.composeaccount.ui.data.AccountProfileLink
import com.dhimandasgupta.composeaccount.ui.data.AccountProfileSwitch
import com.dhimandasgupta.composeaccount.ui.data.AccountProfileText
import com.dhimandasgupta.composeaccount.ui.data.AllAccountItems
import com.dhimandasgupta.composeaccount.ui.data.defaultAllAccountItems
import com.dhimandasgupta.composeaccount.viewmodel.AccountViewModel
import dev.chrisbanes.accompanist.coil.CoilImage
import java.io.File

@Composable
fun AccountRoot(
    accountViewModel: AccountViewModel,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeletePhoto: () -> Unit,
    onRequestToOpenBrowser: (String) -> Unit
) {
    val accountState = accountViewModel.allAccountItems.observeAsState(defaultAllAccountItems())

    ComposeAccountTheme {
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colors.surface)
                .fillMaxSize()
        ) {
            CreateToolbar()
            CreateAccountList(
                allAccountItems = accountState.value,
                accountViewModel = accountViewModel,
                onCameraClicked = onCameraClicked,
                onGalleryClicked = onGalleryClicked,
                onDeletePhoto = onDeletePhoto,
                onRequestToOpenBrowser = onRequestToOpenBrowser,
            )
        }
    }
}

@Composable
fun CreateToolbar() {
    Box(
        modifier = Modifier.fillMaxWidth().height(36.dp),
        alignment = Alignment.Center,
    ) {
        Text(
            text = "Account Settings",
            style = MaterialTheme.typography.button,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        )
    }
}

@Composable
fun CreateAccountList(
    allAccountItems: AllAccountItems,
    accountViewModel: AccountViewModel,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeletePhoto: () -> Unit,
    onRequestToOpenBrowser: (String) -> Unit,
) {
    when (ConfigurationAmbient.current.orientation) {
        ORIENTATION_LANDSCAPE -> CreateAccountListForLandscape(
            allAccountItems = allAccountItems,
            accountViewModel = accountViewModel,
            onCameraClicked = onCameraClicked,
            onGalleryClicked = onGalleryClicked,
            onDeletePhoto = onDeletePhoto,
            onRequestToOpenBrowser = onRequestToOpenBrowser,
        )
        else -> CreateAccountListForPortrait(
            allAccountItems = allAccountItems,
            accountViewModel = accountViewModel,
            onCameraClicked = onCameraClicked,
            onGalleryClicked = onGalleryClicked,
            onDeletePhoto = onDeletePhoto,
            onRequestToOpenBrowser = onRequestToOpenBrowser,
        )
    }
}

@Composable
fun CreateAccountListForPortrait(
    allAccountItems: AllAccountItems,
    accountViewModel: AccountViewModel,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeletePhoto: () -> Unit,
    onRequestToOpenBrowser: (String) -> Unit,
) {
    LazyColumnFor(items = allAccountItems.accountItems) {
        when (it) {
            is AccountProfileImage -> CreateAccountProfileImage(
                accountProfileImage = it,
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                onCameraClicked = onCameraClicked,
                onGalleryClicked = onGalleryClicked,
                onDeletePhoto = onDeletePhoto,
            )
            is AccountHeading -> CreateAccountProfileHeading(accountProfileHeading = it)
            is AccountProfileText -> CreateAccountProfileText(accountProfileText = it)
            is AccountProfileSwitch -> CreateAccountProfileSwitch(
                accountProfileSwitch = it,
                accountViewModel = accountViewModel
            )
            is AccountProfileLink -> CreateAccountProfileLink(
                accountProfileLink = it,
                onRequestToOpenBrowser = onRequestToOpenBrowser
            )
        }
    }
}

@Composable
fun CreateAccountListForLandscape(
    allAccountItems: AllAccountItems,
    accountViewModel: AccountViewModel,
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeletePhoto: () -> Unit,
    onRequestToOpenBrowser: (String) -> Unit,
) {
    val firstItemIsProfileImage = allAccountItems.accountItems.isNotEmpty() && allAccountItems.accountItems[0] is AccountProfileImage

    if (firstItemIsProfileImage) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            CreateAccountProfileImage(
                accountProfileImage = allAccountItems.accountItems[0] as AccountProfileImage,
                modifier = Modifier.fillMaxWidth(0.4f).fillMaxHeight(),
                onCameraClicked = onCameraClicked,
                onGalleryClicked = onGalleryClicked,
                onDeletePhoto = onDeletePhoto,
            )
            LazyColumnFor(items = allAccountItems.accountItems.subList(1, allAccountItems.accountItems.size)) {
                when (it) {
                    is AccountHeading -> CreateAccountProfileHeading(accountProfileHeading = it)
                    is AccountProfileText -> CreateAccountProfileText(accountProfileText = it)
                    is AccountProfileSwitch -> CreateAccountProfileSwitch(
                        accountProfileSwitch = it,
                        accountViewModel = accountViewModel
                    )
                    is AccountProfileLink -> CreateAccountProfileLink(
                        accountProfileLink = it,
                        onRequestToOpenBrowser = onRequestToOpenBrowser
                    )
                    else -> {}
                }
            }
        }
    } else {
        CreateAccountListForLandscape(
            allAccountItems = allAccountItems,
            accountViewModel = accountViewModel,
            onCameraClicked = onCameraClicked,
            onGalleryClicked = onGalleryClicked,
            onDeletePhoto = onDeletePhoto,
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
    val directory = File(ContextAmbient.current.cacheDir, "Camera")

    val openDialog = remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.size(8.dp))
    Box(
        modifier = Modifier.then(modifier).size(156.dp),
        alignment = Alignment.Center,
    ) {
        // Need to check How to show Placeholder in Coil in compose
        if (accountProfileImage.profileImage.isNotBlank()) {
            CoilImage(
                data = File(directory, accountProfileImage.profileImage),
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(156.dp).clip(CircleShape),
            )
        } else {
            Icon(
                asset = Icons.Outlined.Face.copy(defaultWidth = 156.dp, defaultHeight = 156.dp),
                modifier = Modifier.size(156.dp).clip(CircleShape),
                tint = MaterialTheme.colors.onSurface,
            )
        }

        Icon(
            asset = Icons.Filled.Face.copy(defaultWidth = 48.dp, defaultHeight = 48.dp),
            tint = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .preferredSize(48.dp)
                .clickable(
                    onClick = { openDialog.value = true }
                )
        )
    }

    if (openDialog.value) {
        ImageUpdateDialog(
            onCameraClicked = onCameraClicked,
            onGalleryClicked = onGalleryClicked,
            onDeletePhoto = if (accountProfileImage.profileImage.isNotBlank()) onDeletePhoto else null,
        ) { openDialog.value = false }
    }
}

@Composable
fun CreateAccountProfileHeading(accountProfileHeading: AccountHeading) {
    Text(
        text = accountProfileHeading.label,
        style = MaterialTheme.typography.overline,
        textAlign = TextAlign.Start,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
fun CreateAccountProfileText(accountProfileText: AccountProfileText) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = accountProfileText.label,
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Start,
            maxLines = 1,
            color = MaterialTheme.colors.onSurface,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = accountProfileText.value,
            style = MaterialTheme.typography.subtitle2,
            textAlign = TextAlign.End,
            maxLines = 1,
            color = MaterialTheme.colors.onSurface,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun CreateAccountProfileSwitch(accountProfileSwitch: AccountProfileSwitch, accountViewModel: AccountViewModel) {
    val context = ContextAmbient.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.75f)
        ) {
            Text(
                text = accountProfileSwitch.label,
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Start,
                maxLines = 1,
                color = MaterialTheme.colors.onSurface,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = accountProfileSwitch.detailsLabel,
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colors.onSurface,
            )
        }
        Switch(
            checked = accountProfileSwitch.switchValue,
            onCheckedChange = { checked ->
                when (accountProfileSwitch.id) {
                    ACCOUNT_SWITCH_PRIVACY.hashCode() -> { accountViewModel.setAsyncToggle(checked = checked) }
                    ACCOUNT_SWITCH_LOCATION.hashCode() -> { Toast.makeText(context, "This is not toggleable", Toast.LENGTH_SHORT).show() }
                    ACCOUNT_SWITCH_DATA_USAGE.hashCode() -> { accountViewModel.setSyncToggle(checked = checked) }
                }
            },
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(4.dp).fillMaxWidth(0.25f)
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
        style = MaterialTheme.typography.subtitle2,
        textAlign = TextAlign.Start,
        maxLines = 1,
        color = MaterialTheme.colors.onSurface,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { onRequestToOpenBrowser.invoke(accountProfileLink.url) })
            .padding(horizontal = 16.dp, vertical = 8.dp),
    )
}

@Composable
fun ImageUpdateDialog(
    onCameraClicked: () -> Unit,
    onGalleryClicked: () -> Unit,
    onDeletePhoto: (() -> Unit)? = null,
    onDismissRequest: () -> Unit,
) {
    ComposeAccountTheme {
        // Check AlertDialog
        Popup(
            isFocusable = true,
            onDismissRequest = onDismissRequest,
            offset = IntOffset(200, 200),
            alignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(color = MaterialTheme.colors.surface, shape = MaterialTheme.shapes.medium)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Select from Camera",
                    maxLines = 1,
                    color = MaterialTheme.colors.onSurface,
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
                Text(
                    text = "Select from Gallery",
                    maxLines = 1,
                    color = MaterialTheme.colors.onSurface,
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
                    Text(
                        text = "Delete Image",
                        maxLines = 1,
                        color = MaterialTheme.colors.onSurface,
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
