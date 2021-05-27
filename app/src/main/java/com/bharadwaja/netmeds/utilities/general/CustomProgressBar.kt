package com.bharadwaja.netmeds.utilities.general

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog


class CustomProgressBar(mContext: Context) {
    lateinit var dialog: AlertDialog
    val context = mContext

    init {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam
        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam
        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = "Loading ..."
        tvText.setTextColor(Color.parseColor("#000000"))
        tvText.textSize = 20f
        tvText.layoutParams = llParam
        ll.addView(progressBar)
        ll.addView(tvText)
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)
        dialog = builder.create()

        val window: Window? = dialog.getWindow()
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog.getWindow()?.getAttributes())
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog.getWindow()?.setAttributes(layoutParams)
        }
    }

    fun setProgressDialog() {
      //  dialog.show()
    }

    fun HideProgressDialog() {
       // dialog.hide()
    }
}