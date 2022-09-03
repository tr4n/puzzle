package com.tr4n.puzzle.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tr4n.puzzle.di.App.context

/**
 * example request permissions
 */
/*
class Activity/Fragment {
    // multiple permissions
    private val multiplePermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA
    )
    private val multiplePermissionsCode = 1111
    /**
     * request multiple permissions with listener
     */
    private fun requestMultiplePermissionWithListener() {
        requestPermissions(
            multiplePermissions,
            multiplePermissionsCode,
            object : RequestPermissionListener {
                override fun onPermissionRationaleShouldBeShown(requestPermission: () -> Unit) {
                    context?.showDialog(
                        message = "Please allow permissions to use this feature",
                        textPositive = "OK",
                        positiveListener = {
                            requestPermission.invoke()
                        },
                        textNegative = "Cancel"
                    )
                }
                override fun onPermissionPermanentlyDenied(openAppSetting: () -> Unit) {
                    context?.showDialog(
                        message = "Permission Disabled, Please allow permissions to use this feature",
                        textPositive = "OK",
                        positiveListener = {
                            openAppSetting.invoke()
                        },
                        textNegative = "Cancel"
                    )
                }
                override fun onPermissionGranted() {
                    showToast("Granted, do work")
                }
            })
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // multiple permission
        handleOnRequestPermissionResult(
            multiplePermissionsCode,
            requestCode,
            permissions,
            grantResults,
            object : PermissionResultListener {
                override fun onPermissionRationaleShouldBeShown() {
                    showToast("permission denied")
                }
                override fun onPermissionPermanentlyDenied() {
                    showToast("permission permanently disabled")
                }
                override fun onPermissionGranted() {
                    showToast("permission granted")
                }
            }
        )
    }
}
*/

/**
 * open app details setting
 */
@SuppressLint("QueryPermissionsNeeded")
fun Context.openAppDetailSettings() {
    val intent = Intent()
    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
    val uri = Uri.fromParts("package", packageName, null)
    intent.data = uri
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    }
}

/**
 * set first time asking multiple permissions
 */
fun Context.firstTimeAskingPermissions(permissions: Array<String>, isFirstTime: Boolean) {
    val sharedPreference: SharedPreferences? = getSharedPreferences(packageName, MODE_PRIVATE)
    for (permission in permissions) {
        sharedPreference?.edit()?.putBoolean(permission, isFirstTime)?.apply()
    }
}

/**
 * check if first time asking multiple permissions
 */
fun Context.isFirstTimeAskingPermissions(permissions: Array<String>): Boolean {
    val sharedPreference: SharedPreferences? = getSharedPreferences(packageName, MODE_PRIVATE)
    for (permission in permissions) {
        if (sharedPreference?.getBoolean(permission, true) == true) {
            return true
        }
    }
    return false
}

/**
 * check grandResults are granted
 */
fun isGrantedGrantResults(grantResults: IntArray): Boolean {
    if (grantResults.isEmpty()) return false
    for (grantResult in grantResults) {
        if (grantResult != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

/**
 * check if multiple permissions are granted or not
 */
fun Context.shouldAskPermissions(permissions: Array<String>): Boolean {
    for (permission in permissions) {
        if (ContextCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
    }
    return false
}

fun Context.allPermissionsGranted(permissions: Array<String>) = permissions.all {
    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}

/**
 * check if should show request permissions rationale in activity
 */
fun <T : Activity> T.shouldShowRequestPermissionsRationale(permissions: Array<out String>): Boolean {
    for (permission in permissions) {
        if (shouldShowRequestPermissionRationale(permission)) {
            return true
        }
    }
    return false
}

/**
 * check if should show request permissions rationale in fragment
 */
fun <T : Fragment> T.shouldShowRequestPermissionsRationale(permissions: Array<out String>): Boolean {
    for (permission in permissions) {
        if (shouldShowRequestPermissionRationale(permission)) {
            return true
        }
    }
    return false
}

/**
 * request permissions in activity
 */
fun <T : Activity> T.requestPermissions(
    permissions: Array<String>,
    permissionRequestCode: Int,
    requestPermissionListener: RequestPermissionListener
) {
    val shouldAskPermissions = shouldAskPermissions(permissions)
    val shouldShowRequestPermissionRationale = shouldShowRequestPermissionsRationale(permissions)
    val isFirstTimeAskingPermissions = isFirstTimeAskingPermissions(permissions)

    when {
        // permissions denied previously
        shouldAskPermissions && shouldShowRequestPermissionRationale -> {
            requestPermissionListener.onPermissionRationaleShouldBeShown {
                requestPermissions(permissions, permissionRequestCode)
            }
        }
        // Permission denied or first time requested
        shouldAskPermissions && isFirstTimeAskingPermissions -> {
            firstTimeAskingPermissions(permissions, false)
            requestPermissions(permissions, permissionRequestCode)
        }
        // permission disabled
        // Handle the feature without permission or ask user to manually allow permission
        shouldAskPermissions -> {
            requestPermissionListener.onPermissionPermanentlyDenied {
                openAppDetailSettings()
            }
        }
        // permission granted
        else -> {
            requestPermissionListener.onPermissionGranted()
        }
    }
}

/**
 * request permissions in fragment
 */
fun <T : Fragment> T.requestPermissions(
    permissions: Array<String>,
    permissionRequestCode: Int,
    requestPermissionListener: RequestPermissionListener
) {
    val context = context ?: return
    val shouldAskPermissions = context.shouldAskPermissions(permissions)
    val shouldShowRequestPermissionRationale = shouldShowRequestPermissionsRationale(permissions)
    val isFirstTimeAskingPermissions = context.isFirstTimeAskingPermissions(permissions)

    when {
        // permissions denied previously
        shouldAskPermissions && shouldShowRequestPermissionRationale -> {
            requestPermissionListener.onPermissionRationaleShouldBeShown {
                requestPermissions(permissions, permissionRequestCode)
            }
        }
        // Permission denied or first time requested
        shouldAskPermissions && isFirstTimeAskingPermissions -> {
            context.firstTimeAskingPermissions(permissions, false)
            requestPermissions(permissions, permissionRequestCode)
        }
        // permission disabled
        // Handle the feature without permission or ask user to manually allow permission
        shouldAskPermissions -> {
            requestPermissionListener.onPermissionPermanentlyDenied {
                context.openAppDetailSettings()
            }
        }
        // permission granted
        else -> {
            requestPermissionListener.onPermissionGranted()
        }
    }
}

/**
 * Callback on various cases on checking permission
 *
 * 1.  Below M, runtime permission not needed. In that case onPermissionGranted() would be called.
 * If permission is already granted, onPermissionGranted() would be called.
 *
 * 2.  Equal and Above M, if the permission is being asked first time onNeedPermission() would be called.
 *
 * 3.  Equal and Above M, if the permission is previously asked but not granted, onPermissionPreviouslyDenied()
 * would be called.
 *
 * 4.  Equal and Above M, if the permission is disabled by device policy or the user checked "Never ask again"
 * check box on previous request permission, onPermissionDisabled() would be called.
 */
interface RequestPermissionListener {
    /**
     * Callback on permission previously denied
     * should show permission rationale and continue permission request
     */
    fun onPermissionRationaleShouldBeShown(requestPermission: () -> Unit) {}

    /**
     * Callback on permission "Never show again" checked and denied
     * should show message and open app setting
     */
    fun onPermissionPermanentlyDenied(openAppSetting: () -> Unit) {}

    /**
     * Callback on permission granted
     */
    fun onPermissionGranted() {}
}

/**
 * handle request permission result with listener in activity
 */
fun <T : Activity> T.handleOnRequestPermissionResult(
    requestPermissionCode: Int,
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray,
    permissionResultListener: PermissionResultListener
) {
    if (requestPermissionCode == requestCode) {
        if (isGrantedGrantResults(grantResults)) {
            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            permissionResultListener.onPermissionGranted()
        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            if (shouldShowRequestPermissionsRationale(permissions)) {
                // permission denied
                permissionResultListener.onPermissionRationaleShouldBeShown()
            } else {
                // permission disabled or never ask again
                permissionResultListener.onPermissionPermanentlyDenied()
            }
        }
    }
}

/**
 * handle request permission result with listener in fragment
 */
fun <T : Fragment> T.handleOnRequestPermissionResult(
    requestPermissionCode: Int,
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray,
    permissionResultListener: PermissionResultListener
) {
    if (requestPermissionCode == requestCode) {
        if (isGrantedGrantResults(grantResults)) {
            // permission was granted, yay! Do the
            // contacts-related task you need to do.
            permissionResultListener.onPermissionGranted()
        } else {
            // permission denied, boo! Disable the
            // functionality that depends on this permission.
            if (shouldShowRequestPermissionsRationale(permissions)) {
                // permission denied
                permissionResultListener.onPermissionRationaleShouldBeShown()
            } else {
                // permission disabled or never ask again
                permissionResultListener.onPermissionPermanentlyDenied()
            }
        }
    }
}

/**
 * request permission result listener
 */
interface PermissionResultListener {
    /**
     * Callback on permission denied
     */
    fun onPermissionRationaleShouldBeShown() {}

    /**
     * Callback on permission "Never show again" checked and denied
     */
    fun onPermissionPermanentlyDenied() {}

    /**
     * Callback on permission granted
     */
    fun onPermissionGranted() {}
}

