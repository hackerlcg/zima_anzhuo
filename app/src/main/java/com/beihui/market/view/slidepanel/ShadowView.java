package com.beihui.market.view.slidepanel;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;

public class ShadowView extends View {
    private Paint paint = new Paint();
    private RectF rectF = new RectF();

    private float[] factors = {0, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1};

    public ShadowView(Context context) {
        super(context);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rectF.width() != getWidth() || rectF.width() == 0) {
            LinearGradient gradient = new LinearGradient(0, 0, getWidth() * 1.1f, 0,
                    generateColors(factors), factors, Shader.TileMode.REPEAT);
            paint.setShader(gradient);
            rectF.set(0, 0, getWidth(), getHeight());
        }
        canvas.drawRect(rectF, paint);
    }

    int[] generateColors(float[] factors) {
        int[] color = new int[factors.length];
        for (int i = 0; i < factors.length; ++i) {
            color[i] = Color.argb((int) (easeInCubic(factors[i]) * 255), 0, 0, 0);
        }
        return color;
    }

    double easeInCubic(float input) {
        return input * input * input;
    }

}
