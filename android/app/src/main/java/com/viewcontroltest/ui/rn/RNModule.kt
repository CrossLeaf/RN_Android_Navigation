package com.viewcontroltest.ui.rn

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import java.util.*

class RNModule(context: ReactApplicationContext?) : ReactContextBaseJavaModule(context!!) {
    override fun getName(): String { // Tell React the name of the module
// https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
        return REACT_CLASS
    }

    override fun getConstants(): Map<String, Any>? { // Export any constants to be used in your native module
// https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
        val constants: MutableMap<String, Any> = HashMap()
        constants["EXAMPLE_CONSTANT"] = "example"
        return constants
    }

    @ReactMethod
    fun close() {
        if (currentActivity != null) {
            currentActivity!!.finish()
        }
    }

    companion object {
        const val REACT_CLASS = "backToNative"
        private var reactContext: ReactApplicationContext? = null
        private fun emitDeviceEvent(eventName: String, eventData: WritableMap?) { // A method for emitting from the native side to JS
// https://facebook.github.io/react-native/docs/native-modules-android.html#sending-events-to-javascript
            reactContext!!.getJSModule(RCTDeviceEventEmitter::class.java).emit(eventName, eventData)
        }
    }

    init { // Pass in the context to the constructor and save it so you can emit events
// https://facebook.github.io/react-native/docs/native-modules-android.html#the-toast-module
        reactContext = context
    }
}