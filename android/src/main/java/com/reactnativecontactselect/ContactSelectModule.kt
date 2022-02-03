package com.reactnativecontactselect

import android.Manifest
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ActivityEventListener;
import android.app.Activity;


import android.content.Intent
import android.net.Uri

import com.facebook.react.bridge.Arguments


import android.content.ContentResolver
import android.content.pm.PackageManager
import android.provider.ContactsContract.CommonDataKinds.Phone

import androidx.core.content.ContextCompat
import com.facebook.react.modules.core.PermissionListener
import com.facebook.react.modules.core.PermissionAwareActivity


class ContactSelectModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ActivityEventListener, PermissionListener {
  private var contentResolver: ContentResolver? = null;

  init {
    reactApplicationContext.addActivityEventListener(this)
    contentResolver = getReactApplicationContext().getContentResolver();
  }


  private var reactPromise: Promise? = null

  val ERROR_CONTACT_CANCELLED = "SELECTION_CANCELLED"
  val ERROR_CONTACT_NO_DATA = "NO_SELECTION_DATA"
  val ERROR_CONTACT_EXCEPTION = "CONTACT_EXCEPTION"
  val ERROR_CONTACT_PERMISSION = "CONTACT_NO_PERMISSION"

  private val REQUEST_CONTACT_SELECTION_CODE = 2
  private val REQUEST_CONTACT_PERMISSION_CODE = 112

  override fun getName(): String {
    return "ContactSelect"
  }

  @ReactMethod
  fun selectContact(promise: Promise) {
    reactPromise = promise;
    if (ContextCompat.checkSelfPermission(reactApplicationContext, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
      val PERMISSIONS = arrayOf(Manifest.permission.READ_CONTACTS)
      if (currentActivity != null) {
        val activity = currentActivity as PermissionAwareActivity;
        activity?.requestPermissions(PERMISSIONS, REQUEST_CONTACT_PERMISSION_CODE, this);
      }
    } else {
      pickContact()
    }
  }

  private fun pickContact() {
    val intent = Intent(Intent.ACTION_PICK, Uri.parse("content://contacts")).setType(Phone.CONTENT_TYPE)
    currentActivity!!.startActivityForResult(intent, REQUEST_CONTACT_SELECTION_CODE)
  }

  override fun onActivityResult(activity: Activity, requestCode: Int, resultCode: Int, intent: Intent?) {
    try {
      if (requestCode == REQUEST_CONTACT_SELECTION_CODE) {

        if (resultCode == Activity.RESULT_OK) {

          val cursor = contentResolver?.query(intent?.data!!, null, null, null, null)

          if (!cursor?.moveToFirst()!!) {
            reactPromise?.reject(ERROR_CONTACT_NO_DATA, "No contact found")
            return
          }

          val contactId = cursor?.getString(cursor.getColumnIndex(Phone.CONTACT_ID))

          val contactData = Arguments.createMap()

          val phoneNumber = cursor?.getString(cursor.getColumnIndex(Phone.NUMBER))
          val name = cursor?.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME))

          contactData.putString("name", name)
          contactData.putString("id", contactId)
          contactData.putString("phone", phoneNumber)

          reactPromise?.resolve(contactData);
        } else {
          reactPromise?.reject(ERROR_CONTACT_CANCELLED, "User cancelled")
        }
      }
    } catch (exception: Exception) {
      reactPromise?.reject(ERROR_CONTACT_EXCEPTION, exception.message)
    }
  }

  override fun onNewIntent(intent: Intent) {

  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?): Boolean {
    when (requestCode) {
      REQUEST_CONTACT_PERMISSION_CODE -> {
        if (grantResults!!.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          pickContact()
          return true;
        } else {
          return false;
          reactPromise?.reject(ERROR_CONTACT_PERMISSION, "Permission not permitted")
        }
      }
    }
    reactPromise?.reject(ERROR_CONTACT_CANCELLED, "User canceled")
    return true;
  }
}
