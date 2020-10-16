package com.dhimandasgupta.composeaccount.ui.contracts

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.dhimandasgupta.composeaccount.ext.getTargetDirectory
import com.yalantis.ucrop.UCrop
import java.io.File

class CropActivityContract(private val sourceActivity: Activity) : ActivityResultContract<Uri, Uri?>() {
    override fun createIntent(context: Context, uri: Uri): Intent {
        val destinationDirectory = sourceActivity.getTargetDirectory()
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdir()
        }

        val lastPath = uri.lastPathSegment ?: ""

        val destinationFile = File(destinationDirectory, "${lastPath}Cropped.jpg")
        return UCrop.of(uri, Uri.fromFile(destinationFile))
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(800, 800)
            .getIntent(sourceActivity)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        if (resultCode.isResultOk() && intent != null) {
            return UCrop.getOutput(intent)
        }

        return null
    }
}

private fun Int.isResultOk() = this == RESULT_OK
