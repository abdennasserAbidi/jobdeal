package com.example.myjob.common
import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.updatePadding

object EdgeToEdgeUtil {
    private const val STATUS_BAR_TAG = "status_bar"

    @JvmStatic
    fun applyEdgeToEdgeInsets(activity: Activity, statusBarDrawable: Drawable?) {

        val view = activity.findViewById<View>(android.R.id.content) ?: return

        ViewCompat.setOnApplyWindowInsetsListener(view) { view, windowInsets ->
            val bars = windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout()
            )

            val statusBarHeight = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            applyStatusBarColor(activity.window, statusBarDrawable, true, statusBarHeight)

            val controller = WindowCompat.getInsetsController(activity.window, activity.window.decorView)
            controller.isAppearanceLightStatusBars = true
            controller.isAppearanceLightNavigationBars = true

            view.updatePadding (
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = 0
            )

            windowInsets
        }
    }

    private fun applyStatusBarColor(window: Window, statusBarBackground: Drawable?, isDecor: Boolean, height: Int): View {
        val parent = if (isDecor) window.decorView as ViewGroup
        else window.findViewById<ViewGroup>(android.R.id.content)

        var fakeStatusBarView = parent.findViewWithTag<View>(STATUS_BAR_TAG)
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.isGone) fakeStatusBarView.visibility = View.VISIBLE
            fakeStatusBarView.background = statusBarBackground
        } else {
            fakeStatusBarView = createStatusBarView(window.context, statusBarBackground, height)
            parent.addView(fakeStatusBarView)
        }
        return fakeStatusBarView
    }

    private fun createStatusBarView(context: Context, statusBarBackground: Drawable?, height: Int): View {
        return View(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
            background = statusBarBackground
            tag = STATUS_BAR_TAG
        }
    }

    // this for landscape .
    @JvmStatic
    fun handleInsetsByOrientation(activity: Activity, statusBarDrawable: Drawable?) {
        val view = activity.findViewById<View>(android.R.id.content) ?: return

        ViewCompat.setOnApplyWindowInsetsListener(view) { view, windowInsets ->
            val bars = windowInsets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout()
            )

            val statusBarHeight = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            applyStatusBarColor(activity.window, statusBarDrawable, true, statusBarHeight)

            windowInsets
        }
    }
}
