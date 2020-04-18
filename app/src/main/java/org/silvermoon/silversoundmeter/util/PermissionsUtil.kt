package org.silvermoon.silversoundmeter.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity


class PermissionsUtil() {


    companion object{

        fun hasPermissions(context: Context): Boolean  {
            return ContextCompat.checkSelfPermission(context,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }

        fun requestPermissions(activity: FragmentActivity) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.RECORD_AUDIO
                )
            ) {
                ActivityCompat.requestPermissions(
                    activity, arrayOf(Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE

                    ),
                    PERMISSIONS_REQ_CODE
                )
            }
        }
    }
}