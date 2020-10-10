package com.dhimandasgupta.composeaccount.ui.activities

import android.Manifest
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.ViewModelProvider
import com.dhimandasgupta.composeaccount.ext.copyToCacheDirectory
import com.dhimandasgupta.composeaccount.ext.deleteCacheDirectory
import com.dhimandasgupta.composeaccount.ext.getCameraFileUri
import com.dhimandasgupta.composeaccount.ext.openLinkOnExternalApplication
import com.dhimandasgupta.composeaccount.ext.openSettings
import com.dhimandasgupta.composeaccount.ui.screens.AccountRoot
import com.dhimandasgupta.composeaccount.viewmodel.AccountViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val accountViewModel: AccountViewModel by lazy { ViewModelProvider(this).get(AccountViewModel::class.java) }

    private val cameraUri: Uri by lazy { getCameraFileUri() }

    private val askCameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            capturePhoto.launch(cameraUri)
        } else {
            showSnackBar(
                text = "Need your permission to take picture",
                actionText = "Open Settings",
                actionClick = { openSettings() }
            )
        }
    }

    private val capturePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) { capturedSuccessfully ->
        if (capturedSuccessfully) {
            cameraUri.lastPathSegment?.let { storagePath ->
                accountViewModel.setProfileImagePath(storagePath)
            }
        } else {
            showSnackBar(
                text = "Looks like you did not clicked any photo",
                actionText = "Open Camera",
                actionClick = { launchCamera() }
            )
        }
    }

    private val selectPicture: ActivityResultLauncher<String> by lazy {
        activityResultRegistry.register("select_picture", ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val copiedLocation = copyToCacheDirectory(uri)
                copiedLocation?.lastPathSegment?.let { storagePath ->
                    accountViewModel.setProfileImagePath(storagePath)
                }
            } ?: showSnackBar(
                text = "Looks like you did not selected anything",
                actionText = "Open Gallery",
                actionClick = { launchGallery() }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccountRoot(
                accountViewModel = accountViewModel,
                onCameraClicked = {
                    launchCamera()
                },
                onGalleryClicked = {
                    launchGallery()
                },
                onDeletePhoto = {
                    deletePhoto()
                },
                onRequestToOpenBrowser = {
                    openExternalBrowser(it)
                }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        askCameraPermission.unregister()
        capturePhoto.unregister()
        selectPicture.unregister()
    }

    private fun launchCamera() {
        askCameraPermission.launch(Manifest.permission.CAMERA)
    }

    private fun launchGallery() {
        selectPicture.launch("image/*")
    }

    private fun deletePhoto() {
        accountViewModel.deleteProfileImage()
        deleteCacheDirectory()
    }

    private fun openExternalBrowser(url: String) {
        openLinkOnExternalApplication(url = url)
    }

    private fun showSnackBar(
        text: String,
        actionText: String? = null,
        actionClick: (() -> Unit)? = null
    ) {
        Snackbar.make(
            window.decorView,
            text,
            Snackbar.LENGTH_SHORT
        ).setAction(actionText ?: "") { actionClick?.invoke() }.show()
    }
}
