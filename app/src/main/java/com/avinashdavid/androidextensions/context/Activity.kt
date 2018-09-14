package com.avinashdavid.androidextensions.context

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.util.Log
import com.avinashdavid.androidextensions.getLogTagForFile
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

//REGION: image and media
/**
 * @param requestCode: Request code for the library intent. Listen for standard activity results with this request code
 * @param chooserTitleString (optional): Title for the chooser dialog
 */
fun Activity.goToDeviceLibraryForPickingImage(requestCode: Int, chooserTitleString: String = "Select Picture") {
    val libraryIntent = Intent().apply {
        type = "image/*"
        action = Intent.ACTION_GET_CONTENT
    }
    this.startActivityForResult(Intent.createChooser(libraryIntent, chooserTitleString),
            requestCode)
}

/**
 * @param requestCode: Request code for the library intent. Listen for standard activity results with this request code
 * @param chooserTitleString (optional): Title for the chooser dialog
 */
fun Activity.goToDeviceLibraryForPickingVideo(requestCode: Int, chooserTitleString: String = "Select Video") {
    val libraryIntent = Intent().apply {
        type = "video/*"
        action = Intent.ACTION_GET_CONTENT
    }
    this.startActivityForResult(Intent.createChooser(libraryIntent, chooserTitleString),
            requestCode)
}

/**
 * @param requestCode: Request code for the library intent. Listen for standard activity results with this request code
 * @param fileNameForCapturedImage (optional): File name for the saved image. Defaults to a time stamp
 * @see If the user has not previously granted your application Camera permissions, this method will ask for permission, but return null
 * @return Uri of the created file, if obtained
 */
fun Activity.goToCameraForCapturingImage(requestCode: Int, fileNameForCapturedImage: String? = null) : Uri? {
    //ensure that the app has camera permissions
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.CAMERA))
        {
            //popup to allow camera permission
            //Console.WriteLine("Request Camera Permission Here");
        }
        else
        {
            ActivityCompat.requestPermissions(this, listOf(Manifest.permission.CAMERA).toTypedArray(),
                    requestCode)
        }
    }
    else
    {
        //if permission is granted, start making the intent
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(this.applicationContext.packageManager) != null)
        {
            //create the file where the full captured image will be stored
            var photoFile: File? = null;
            try
            {
                photoFile = createImageFile(this, fileNameForCapturedImage)
            }
            catch (er: IOException)
            {
                Log.e(getLogTagForFile("Activity"), "Error creating file to store captured image", er)
            }

            if (photoFile != null)
            {
                //get a reference to the file and pass it to the intent for use to save the picture
                val cameraPictureUri = FileProvider.getUriForFile(this, this.applicationContext.packageName, photoFile);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPictureUri);

                this.startActivityForResult(intent, requestCode)

                //return the URI for use by the UI
                return cameraPictureUri
            }
        }
    }
    return null
}

private fun createImageFile(context: Context, fileName: String?) : File {
    // Create an image file name
    val imageFileName = fileName ?: "JPEG_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}_"
    val storageDir = context.applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
    )

    // Save a file: path for use with ACTION_VIEW intents
    val imagePath = image.absolutePath
    // if needed, make the directory for this file
    image.parentFile.mkdir()
    return image
}
///REGION