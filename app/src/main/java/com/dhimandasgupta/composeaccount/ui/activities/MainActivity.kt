package com.dhimandasgupta.composeaccount.ui.activities

import android.Manifest
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.dhimandasgupta.composeaccount.ext.copyToCacheDirectory
import com.dhimandasgupta.composeaccount.ext.deleteCacheDirectory
import com.dhimandasgupta.composeaccount.ext.getCameraFileUri
import com.dhimandasgupta.composeaccount.ext.isLocationPermissionGranted
import com.dhimandasgupta.composeaccount.ext.openLinkOnExternalApplication
import com.dhimandasgupta.composeaccount.ext.openSettings
import com.dhimandasgupta.composeaccount.ui.contracts.CropActivityContract
import com.dhimandasgupta.composeaccount.ui.screens.AccountRoot
import com.dhimandasgupta.composeaccount.viewmodel.AccountViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val accountViewModel: AccountViewModel by viewModels()

    private val cameraUri: Uri by lazy { getCameraFileUri() }

    private val askLocationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        accountViewModel.setLocationGranted(granted = granted)
    }

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
            cropImage.launch(cameraUri)
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
                copiedLocation?.let { cropImage.launch(copiedLocation) }
            } ?: showSnackBar(
                text = "Looks like you did not selected anything",
                actionText = "Open Gallery",
                actionClick = { launchGallery() }
            )
        }
    }

    private val cropImage = registerForActivityResult(CropActivityContract(this)) { uri ->
        uri?.lastPathSegment?.let { storagePath -> accountViewModel.setProfileImagePath(storagePath) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccountRoot(
                accountViewModel = accountViewModel,
                onCameraClicked = { launchCamera() },
                onGalleryClicked = { launchGallery() },
                onDeletePhoto = { deletePhoto() },
                onLocationRequested = { launchLocationPermission() },
                onRequestToOpenBrowser = { openExternalBrowser(it) }
            )
        }
    }

    override fun onResume() {
        super.onResume()

        accountViewModel.setLocationGranted(isLocationPermissionGranted())
    }

    override fun onDestroy() {
        super.onDestroy()

        askLocationPermission.unregister()
        askCameraPermission.unregister()
        capturePhoto.unregister()
        selectPicture.unregister()
        cropImage.unregister()
    }

    private fun launchCamera(): Unit = askCameraPermission.launch(Manifest.permission.CAMERA)

    private fun launchGallery(): Unit = selectPicture.launch("image/*")

    private fun deletePhoto() {
        accountViewModel.deleteProfileImage()
        deleteCacheDirectory()
    }

    private fun launchLocationPermission(): Unit = askLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)

    private fun openExternalBrowser(url: String) = openLinkOnExternalApplication(url = url)

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
