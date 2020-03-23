package com.viewcontroltest

import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import com.unity3d.player.UnityPlayerActivity

class TestUnityActivity : UnityPlayerActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_test_unity)
    }

    override fun onKeyDown(p0: Int, p1: KeyEvent?): Boolean {
        if (p0 == KEYCODE_BACK) {
            finish()
//            return true
        }
        return super.onKeyDown(p0, p1)
    }
}