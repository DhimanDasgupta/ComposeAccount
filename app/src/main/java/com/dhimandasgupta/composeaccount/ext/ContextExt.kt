package com.dhimandasgupta.composeaccount.ext

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.content.PermissionChecker

fun Context.openSettings() = startActivity(
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:$packageName")
    ).also {
        it.addCategory(Intent.CATEGORY_DEFAULT)
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
)

fun Context.openLinkOnExternalApplication(url: String) = try {
    val i = Intent(Intent.ACTION_VIEW, Uri.parse(url)).also { intent ->
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(i)
} catch (e: Exception) {
    Toast.makeText(this, "Looks you don't have supporting apps to open this link", Toast.LENGTH_SHORT).show()
}

fun Context.isLocationPermissionGranted() =
    PermissionChecker.checkCallingOrSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED &&
    PermissionChecker.checkCallingOrSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED
