package com.viewcontroltest

import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class ViewControllerModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "ViewControllerModule"
    }

    @ReactMethod
    fun showActivity() {
        Log.d("ViewController", "show activity.")
    }
}