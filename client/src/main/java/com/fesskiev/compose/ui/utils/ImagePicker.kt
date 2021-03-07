package com.fesskiev.compose.ui.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.MediaStore
import java.io.File
import kotlin.collections.ArrayList

fun Context.getBitmapFromIntent(intent: Intent): Pair<Bitmap, File>? {
    val uri = getPickImageResultUri(intent) ?: return null
    val bitmap =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
    } else {
        MediaStore.Images.Media.getBitmap(contentResolver, uri)
    }
    return Pair(bitmap, File(uri.path))
}

fun Context.pickImageChooserIntent(title: CharSequence): Intent? {
    val intents = mutableListOf<Intent>()
        intents.addAll(getCameraIntents())
    val galleryIntents: List<Intent> = getGalleryIntents()
    intents.addAll(galleryIntents)
    val target: Intent
    if (intents.isEmpty()) {
        target = Intent()
    } else {
        target = intents[intents.size - 1]
        intents.removeAt(intents.size - 1)
    }
    val chooserIntent = Intent.createChooser(target, title)
    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toTypedArray<Parcelable>())
    return chooserIntent
}

fun Context.getCameraIntents(): List<Intent> {
    val intents = mutableListOf<Intent>()
    val outputFileUri: Uri? = getCaptureImageOutputUri()
    val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    val listCam = packageManager.queryIntentActivities(captureIntent, 0)
    for (res in listCam) {
        val intent = Intent(captureIntent)
        intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
        intent.setPackage(res.activityInfo.packageName)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
        intents.add(intent)
    }
    return intents
}

fun Context.getGalleryIntents(): List<Intent> {
    val intents: MutableList<Intent> = ArrayList()
    val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
    galleryIntent.type = "image/*"
    val listGallery = packageManager.queryIntentActivities(galleryIntent, 0)
    for (res in listGallery) {
        val intent = Intent(galleryIntent)
        intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
        intent.setPackage(res.activityInfo.packageName)
        intents.add(intent)
    }
    return intents
}

fun Context.getCaptureImageOutputUri(): Uri? {
    var outputFileUri: Uri? = null
    val getImage: File? = externalCacheDir
    if (getImage != null) {
        outputFileUri = Uri.fromFile(File(getImage.path, "image.jpeg"))
    }
    return outputFileUri
}

fun Context.getPickImageResultUri(data: Intent?): Uri? {
    var isCamera = true
    if (data != null && data.data != null) {
        val action = data.action
        isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
    }
    return if (isCamera || data?.data == null) getCaptureImageOutputUri() else data.data
}
