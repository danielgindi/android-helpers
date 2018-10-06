package com.dg.helpers

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.Checkable
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton

@Suppress("unused")
object DismissSoftkeyboardHelper
{

    fun setupUI(context: Context, view: View?)
    {
        if (view == null) return

        val viewPackageName = view.javaClass.getPackage()?.name
        val viewClassName = view.javaClass.simpleName

        if (view is EditText ||
                view is Button ||
                view is ImageButton ||
                view is Checkable ||
                view is DatePicker ||
                view is AdapterView<*> ||
                view is android.widget.NumberPicker ||
                view is android.widget.RadioGroup ||
                view is android.widget.TimePicker ||
                Build.VERSION.SDK_INT >= 21 && view is android.widget.Toolbar ||
                viewPackageName == "android.widget" && viewClassName == "Toolbar" ||
                viewClassName == "TextInputLayout")
        {
            return
        }

        view.setOnTouchListener { _, _ ->
            hideSoftKeyboard(context)
            false
        }

        if (view is ViewGroup)
        {
            for (i in 0 until view.childCount)
            {
                val innerView = view.getChildAt(i)
                setupUI(context, innerView)
            }
        }
    }

    fun hideSoftKeyboard(context: Context)
    {
        try
        {
            val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow((context as Activity).currentFocus!!.windowToken, 0)
        }
        catch (ex: Exception)
        {

        }

    }
}