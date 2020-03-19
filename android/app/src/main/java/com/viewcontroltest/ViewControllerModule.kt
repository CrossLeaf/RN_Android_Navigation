package com.viewcontroltest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.facebook.react.bridge.Promise
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
        val intent = Intent(currentActivity, HomeActivity::class.java)
        currentActivity?.startActivity(intent)
    }

    @ReactMethod
    fun showNativeActivity(param: String, promise: Promise) {
        Log.d("ViewController", "show activity.")
        val intent = Intent(currentActivity, HomeActivity::class.java)
        intent.putExtras(Bundle().apply {
            putString("param1", param)
        })
        currentActivity?.startActivity(intent)
        promise.reject("")
    }
}