package com.hanmajid.android.predictivebackgesture

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.window.OnBackInvokedCallback
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

/**
 * 主要看AndroidX的情况,注意是[onBackPressedDispatcher]
 *
 * 记得xml添加[android:enableOnBackInvokedCallback="true"]属性
 */
class MainActivity : AppCompatActivity() {

    private lateinit var onBackInvokedCallback: OnBackInvokedCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // For Non-AndroidX:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedCallback = OnBackInvokedCallback {
                // Your app's onBackPressed logic goes here...
                Log.i("TAG", "Non-AndroidX: onBackInvoked")
            }
            // onBackInvokedDispatcher.registerOnBackInvokedCallback(
            //     OnBackInvokedDispatcher.PRIORITY_DEFAULT,
            //     onBackInvokedCallback,
            // )
        }

        // For AndroidX:
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Your app's onBackPressed logic goes here...
                Log.i("TAG", "AndroidX: handleOnBackPressed")
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        // You can enable/disable onBackPressedCallback by using `isEnabled` method:
        onBackPressedCallback.isEnabled = false
    }

    override fun onDestroy() {
        super.onDestroy()

        // For Non-AndroidX:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.unregisterOnBackInvokedCallback(onBackInvokedCallback)
        }
    }
}