package com.dhimandasgupta.composeaccount.ext

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.CATEGORY_BROWSABLE
import android.content.Intent.CATEGORY_DEFAULT
import android.content.Intent.FLAG_ACTIVITY_MULTIPLE_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_REQUIRE_NON_BROWSER
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.PermissionChecker

fun Context.openSettings() = startActivity(
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:$packageName")
    ).also {
        it.addCategory(CATEGORY_DEFAULT)
        it.flags = FLAG_ACTIVITY_NEW_TASK
    }
)

fun Context.openAppOrBrowser(url: String) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) openApp(url) else openBrowser(url)

@RequiresApi(Build.VERSION_CODES.R)
private fun Context.openApp(url: String) {
    try {
        val i = Intent(ACTION_VIEW, Uri.parse(url)).apply {
            addCategory(CATEGORY_BROWSABLE)
            flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_REQUIRE_NON_BROWSER
        }
        startActivity(i)
    } catch (e: ActivityNotFoundException) {
        openBrowser(url)
    }
}

private fun Context.openBrowser(url: String) {
    val i = Intent(ACTION_VIEW, Uri.parse(url)).also { intent ->
        intent.addFlags(FLAG_ACTIVITY_MULTIPLE_TASK or FLAG_ACTIVITY_NEW_TASK)
    }
    i.resolveActivity(packageManager)?.let {
        startActivity(i)
    } ?: Toast.makeText(this, "Looks like you don't have browser", Toast.LENGTH_SHORT).show()
}

fun Context.isLocationPermissionGranted() =
    PermissionChecker.checkCallingOrSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED &&
        PermissionChecker.checkCallingOrSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED
