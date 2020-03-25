package com.liner.familytracker.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.liner.familytracker.R;

public class StatusIconView extends LinearLayout {


    private ImageView icon, status;
    private int iconResource = R.drawable.internet;
    private int iconEnabledResource = R.drawable.check_success;
    private int iconDisabledResource = R.drawable.check_fail;
    private boolean enabled = false;

    public StatusIconView(Context context) {
        super(context);
        View view = inflate(getContext(), R.layout.status_icon_layout, null);
        icon = view.findViewById(R.id.status_icon);
        status = view.findViewById(R.id.status_statusIcon);
        addView(view);
        status.setImageResource(this.enabled ? iconEnabledResource:iconDisabledResource);
    }

    public StatusIconView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StatusIconView, 0, 0);
        try {
            iconResource = typedArray.getResourceId(R.styleable.StatusIconView_si_icon, R.drawable.internet);
            iconEnabledResource = typedArray.getResourceId(R.styleable.StatusIconView_si_check_icon, R.drawable.check_success);
            iconDisabledResource = typedArray.getResourceId(R.styleable.StatusIconView_si_uncheck_icon, R.drawable.check_fail);
            enabled = typedArray.getBoolean(R.styleable.StatusIconView_si_enabled, false);
        } finally {
            typedArray.recycle();
        }
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        View view = inflate(getContext(), R.layout.status_icon_layout, null);
        icon = view.findViewById(R.id.status_icon);
        status = view.findViewById(R.id.status_statusIcon);
        addView(view);
        status.setImageResource(this.enabled ? iconEnabledResource:iconDisabledResource);
        icon.setImageResource(iconResource);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        status.setImageResource(this.enabled ? iconEnabledResource:iconDisabledResource);
        status.setColorFilter(this.enabled ? getContext().getResources().getColor(R.color.accent_color):getContext().getResources().getColor(R.color.text_color));
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
        icon.setImageResource(iconResource);
    }

    public boolean isIconEnabled() {
        return this.enabled;
    }
}