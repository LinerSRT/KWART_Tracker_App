package com.liner.familytracker.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapMarkerUtils {
    public static void createUserMarker(Context context, Bitmap photo, IListener listener){
        Bitmap result;
        float scale;
        result = Bitmap.createBitmap(dp(context,56), dp(context,70), Bitmap.Config.ARGB_8888);
        result.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(result);
        Drawable drawable = context.getResources().getDrawable(R.drawable.marker_bg);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        RectF bitmapRect = new RectF();
        canvas.save();
        if (photo != null) {
            BitmapShader shader = new BitmapShader(photo, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Matrix matrix = new Matrix();
            scale = dp(context,56) / (float) photo.getWidth();
            matrix.postTranslate(dp(context,5), dp(context,5));
            matrix.postScale(scale, scale);
            roundPaint.setShader(shader);
            shader.setLocalMatrix(matrix);
            bitmapRect.set(dp(context,5), dp(context,5), dp(context,46 + 5), dp(context,46 + 5));
            canvas.drawRoundRect(bitmapRect, dp(context,23), dp(context,23), roundPaint);
        }
        canvas.restore();
        listener.onMarkerReady(result);
    }
    public static void createUserMarker(final Context context, String photoUrl, final IListener listener){
        Bitmap result;
        result = Bitmap.createBitmap(dp(context,56), dp(context,70), Bitmap.Config.ARGB_8888);
        result.eraseColor(Color.TRANSPARENT);
        final Canvas canvas = new Canvas(result);
        Drawable drawable = context.getResources().getDrawable(R.drawable.marker_bg);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        final Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final RectF bitmapRect = new RectF();
        canvas.save();
        final Bitmap finalResult = result;
        Picasso.with(context).load(photoUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                matrix.postTranslate(dp(context,5), dp(context,5));
                matrix.postScale(dp(context,56) / (float) bitmap.getWidth(), dp(context,56) / (float) bitmap.getWidth());
                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(dp(context,5), dp(context,5), dp(context,46 + 5), dp(context,46 + 5));
                canvas.drawRoundRect(bitmapRect, dp(context,23), dp(context,23), roundPaint);
                canvas.restore();
                listener.onMarkerReady(finalResult);
            }
            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
    }
    private static int dp(Context context, float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(context.getResources().getDisplayMetrics().density * value);
    }
    public interface IListener{
        void onMarkerReady(Bitmap bitmap);
    }
}
