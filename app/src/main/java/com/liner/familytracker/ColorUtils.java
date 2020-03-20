package com.liner.familytracker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;

public class ColorUtils {
    public static int getThemeColor(Resources.Theme theme, int attrColor) {
        TypedValue typedValue = new TypedValue();
        int parsedColor;

        if (theme.resolveAttribute(attrColor,
                typedValue, true)) {
            switch (typedValue.type) {
                case TypedValue.TYPE_INT_COLOR_ARGB4:
                    parsedColor = Color.argb(
                            (typedValue.data & 0xf000) >> 8,
                            (typedValue.data & 0xf00) >> 4,
                            typedValue.data & 0xf0,
                            (typedValue.data & 0xf) << 4);
                    break;

                case TypedValue.TYPE_INT_COLOR_RGB4:
                    parsedColor = Color.rgb(
                            (typedValue.data & 0xf00) >> 4,
                            typedValue.data & 0xf0,
                            (typedValue.data & 0xf) << 4);
                    break;

                case TypedValue.TYPE_INT_COLOR_ARGB8:
                    parsedColor = typedValue.data;
                    break;

                case TypedValue.TYPE_INT_COLOR_RGB8:
                    parsedColor = Color.rgb(
                            (typedValue.data & 0xff0000) >> 16,
                            (typedValue.data & 0xff00) >> 8,
                            typedValue.data & 0xff);
                    break;

                default:
                    throw new RuntimeException("ClassName: couldn't parse theme " +
                            "background color attribute " + typedValue.toString());
            }
        } else {
            throw new RuntimeException("ClassName: couldn't find background color in " +
                    "theme");
        }
        return parsedColor;
    }
    public static int getThemeColor(Context context, int attrColor) {
        TypedValue typedValue = new TypedValue();
        int parsedColor;
        if (context.getTheme().resolveAttribute(attrColor,
                typedValue, true)) {
            switch (typedValue.type) {
                case TypedValue.TYPE_INT_COLOR_ARGB4:
                    parsedColor = Color.argb(
                            (typedValue.data & 0xf000) >> 8,
                            (typedValue.data & 0xf00) >> 4,
                            typedValue.data & 0xf0,
                            (typedValue.data & 0xf) << 4);
                    break;

                case TypedValue.TYPE_INT_COLOR_RGB4:
                    parsedColor = Color.rgb(
                            (typedValue.data & 0xf00) >> 4,
                            typedValue.data & 0xf0,
                            (typedValue.data & 0xf) << 4);
                    break;

                case TypedValue.TYPE_INT_COLOR_ARGB8:
                    parsedColor = typedValue.data;
                    break;

                case TypedValue.TYPE_INT_COLOR_RGB8:
                    parsedColor = Color.rgb(
                            (typedValue.data & 0xff0000) >> 16,
                            (typedValue.data & 0xff00) >> 8,
                            typedValue.data & 0xff);
                    break;

                default:
                    throw new RuntimeException("ClassName: couldn't parse theme " +
                            "background color attribute " + typedValue.toString());
            }
        } else {
            throw new RuntimeException("ClassName: couldn't find background color in " +
                    "theme");
        }
        return parsedColor;
    }
}
