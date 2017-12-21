package com.beihui.market.util.viewutils;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;

import java.lang.ref.WeakReference;

public class ToastUtils {

    private static WeakReference<Toast> toast;

    public static void showShort(Context context, String msg, @DrawableRes int drawableRes) {
        Drawable drawable = context.getResources().getDrawable(drawableRes);
        showShort(context, msg, drawable);
    }

    public static void showShort(Context context, String msg, Drawable drawable) {
        cancel();
        toast = new WeakReference<>(createToast(context, msg, drawable));
        if (toast.get() != null) {
            toast.get().show();
        }
    }

    public static void cancel() {
        if (toast != null && toast.get() != null) {
            toast.get().cancel();
        }
    }

    private static Toast createToast(Context context, String msg, Drawable drawable) {
        if (context == null)
            return null;
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_toast_as_alert, null);
        toast.setView(view);

        if (msg != null && drawable != null) {
            view.findViewById(R.id.both).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.text)).setText(msg);
            ((ImageView) view.findViewById(R.id.image)).setImageDrawable(drawable);
        } else if (msg != null) {
            view.findViewById(R.id.single_text).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.text_single)).setText(msg);
        } else if (drawable != null) {
            view.findViewById(R.id.single_image).setVisibility(View.VISIBLE);
            ((ImageView) view.findViewById(R.id.image_single)).setImageDrawable(drawable);
        }

        return toast;
    }
}
