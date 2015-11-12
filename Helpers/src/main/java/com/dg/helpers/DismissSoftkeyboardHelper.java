package com.dg.helpers;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by Daniel Cohen Gindi (danielgindi@gmail.com)
 */
public class DismissSoftkeyboardHelper
{

    public static void setupUI(final Context context, View view)
    {
        if (view == null) return;

        if (!(view instanceof EditText))
        {
            view.setOnTouchListener(new View.OnTouchListener()
            {
                public boolean onTouch(View v, MotionEvent event)
                {
                    hideSoftKeyboard(context);
                    return false;
                }
            });
        }

        if (view instanceof ViewGroup)
        {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
            {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(context, innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Context context)
    {
        try
        {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception ex)
        {

        }
    }
}