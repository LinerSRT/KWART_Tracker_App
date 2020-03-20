package com.liner.familytracker.utils;

import android.content.Context;
import android.os.Build;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.liner.familytracker.R;

public class InfoUtil {

    public static void showSnack(View view, Context context, String messageText, String actionText, View.OnClickListener listener){
        Snackbar snackbar = Snackbar.make(view, messageText, Snackbar.LENGTH_SHORT)
                .setAction(actionText, listener);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackbar.setActionTextColor(context.getColor(R.color.accent_color));
        } else {
            snackbar.setActionTextColor(context.getResources().getColor(R.color.accent_color));
        }
        snackbar.show();
    }

}
