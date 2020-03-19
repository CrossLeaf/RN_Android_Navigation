package com.viewcontroltest.ui.notifications

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactPackage
import com.facebook.react.ReactRootView
import com.facebook.react.common.LifecycleState
import com.facebook.react.shell.MainReactPackage
import com.viewcontroltest.FileSaveUtil
import com.viewcontroltest.ui.rn.RNPackage
import java.util.*
import java.util.regex.Pattern

class NotificationsFragment : Fragment() {

    private val TAG = javaClass.simpleName
    private lateinit var notificationsViewModel: NotificationsViewModel

    private var reactRootView: ReactRootView? = null
    private var reactInstanceManager: ReactInstanceManager? = null
    private val reactPackageList: List<ReactPackage> = Arrays.asList(
            MainReactPackage(),
            RNPackage()
    )

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
//        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
//        val textView: TextView = root.findViewById(R.id.text_notifications)
//        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        val path = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path}/index.android.bundle"
        Log.d(TAG, "onCreateView: path = $path")
        val moduleName = getModuleName(FileSaveUtil.readSDFile(path))
//        val moduleName = "newHello"

        reactRootView = ReactRootView(context)
        reactInstanceManager = ReactInstanceManager.builder()
                .setApplication(activity?.application)
                .setCurrentActivity(activity)
                // 主要從這去設定要讀取的 jsbundle
                .setJSBundleFile(path)
                .setJSMainModulePath("index")
                .addPackages(reactPackageList)
                .setUseDeveloperSupport(true)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build()

        reactRootView!!.startReactApplication(reactInstanceManager,
                moduleName, null)
        return reactRootView
    }

    /**
     * 取得 module 名稱
     *
     * @param content bundle 內容
     * @return module name
     */
    private fun getModuleName(content: String): String? {
        var moduleName = ""
        val regexp = ".exports\\s*=\\s*\\{\\s*\"*name\"*:\\s*\"([^\"]+)\"\\s*,\\s*\"*displayName\"*"
        val pattern = Pattern.compile(regexp)
        val matcher = pattern.matcher(content)
        while (matcher.find()) {
            if (matcher.groupCount() >= 1) {
                moduleName = matcher.group(1)
            }
        }
        Log.d(TAG, "getModuleName: $moduleName")
        print("getModuleName1: $moduleName")
        return moduleName
    }
}