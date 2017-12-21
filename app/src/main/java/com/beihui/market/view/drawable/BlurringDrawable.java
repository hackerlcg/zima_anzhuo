package com.beihui.market.view.drawable;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.ViewTreeObserver;

public class BlurringDrawable extends Drawable {

    private int downSampleFactor;
    private int overlayColor;
    private View blurredView;
    private int blurredViewWidth;
    private int blurredViewHeight;
    private boolean downSampleFactorChanged;
    private Bitmap bitmapToBlur;
    private Bitmap blurredBitmap;
    private Canvas blurringCanvas;
    private RenderScript renderScript;
    private ScriptIntrinsicBlur blurScript;
    private Allocation blurInput;
    private Allocation blurOutput;

    private Paint paint = new Paint();
    private Paint colorPaint = new Paint();

    private ViewTreeObserver.OnDrawListener onDrawListener = new ViewTreeObserver.OnDrawListener() {
        @Override
        public void onDraw() {
            invalidateSelf();
        }
    };

    public BlurringDrawable(Context context) {
        this(context, 6, 10, Color.parseColor("#88000000"));
    }

    public BlurringDrawable(Context context, int downSampleFactor, int blurRadius, int overlayColor) {
        initializeRenderScript(context);
        setDownSampleFactor(downSampleFactor);
        setBlurRadius(blurRadius);
        setOverlayColor(overlayColor);
    }


    @Override
    public void draw(@NonNull Canvas canvas) {
        if (blurredView != null) {
            if (this.prepare()) {
                if (blurredView.getBackground() != null && blurredView.getBackground() instanceof ColorDrawable) {
                    bitmapToBlur.eraseColor(((ColorDrawable) blurredView.getBackground()).getColor());
                } else {
                    bitmapToBlur.eraseColor(0);
                }

                blurredView.draw(blurringCanvas);
                this.blur();
                canvas.save();
                canvas.translate(blurredView.getX() - getBounds().left, blurredView.getY() - getBounds().top);
                canvas.scale((float) downSampleFactor, (float) downSampleFactor);
                canvas.drawBitmap(blurredBitmap, 0.0F, 0.0F, paint);
                canvas.restore();
            }
            int alpha = Math.round(Color.alpha(overlayColor) * (paint.getAlpha() / (float) 255.0));
            canvas.drawColor(Color.argb(alpha, Color.red(overlayColor), Color.green(overlayColor), Color.blue(overlayColor)));
        }
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {
        paint.setAlpha(alpha);
        colorPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    public void setBlurRadius(int radius) {
        this.blurScript.setRadius((float) radius);
    }

    public void setDownSampleFactor(int factor) {
        if (factor <= 0) {
            throw new IllegalArgumentException("DownSampleFactor must be greater than 0.");
        } else {
            if (downSampleFactor != factor) {
                downSampleFactor = factor;
                downSampleFactorChanged = true;
            }

        }
    }

    public void setOverlayColor(int color) {
        colorPaint.setColor(color);
        this.overlayColor = color;
    }

    public void bindBlurredView(View blurredView) {
        this.blurredView = blurredView;
        if (this.blurredView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                this.blurredView.getViewTreeObserver().addOnDrawListener(onDrawListener);
            }
        }
    }

    public void unbindBlurredView() {
        if (this.blurredView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                this.blurredView.getViewTreeObserver().removeOnDrawListener(onDrawListener);
            }
        }
    }

    private void initializeRenderScript(Context context) {
        renderScript = RenderScript.create(context);
        blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(this.renderScript));
    }

    private boolean prepare() {
        int width = blurredView.getWidth();
        int height = blurredView.getHeight();
        if (blurringCanvas == null || downSampleFactorChanged || blurredViewWidth != width || blurredViewHeight != height) {
            downSampleFactorChanged = false;
            blurredViewWidth = width;
            blurredViewHeight = height;
            int scaledWidth = width / downSampleFactor;
            int scaledHeight = height / downSampleFactor;
            scaledWidth = scaledWidth - scaledWidth % 4 + 4;
            scaledHeight = scaledHeight - scaledHeight % 4 + 4;
            if (blurredBitmap == null || blurredBitmap.getWidth() != scaledWidth || blurredBitmap.getHeight() != scaledHeight) {
                bitmapToBlur = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
                if (bitmapToBlur == null) {
                    return false;
                }

                blurredBitmap = Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888);
                if (blurredBitmap == null) {
                    return false;
                }
            }

            blurringCanvas = new Canvas(this.bitmapToBlur);
            blurringCanvas.scale(1.0F / (float) downSampleFactor, 1.0F / (float) this.downSampleFactor);
            blurInput = Allocation.createFromBitmap(renderScript, bitmapToBlur, Allocation.MipmapControl.MIPMAP_NONE, 1);
            blurOutput = Allocation.createTyped(renderScript, blurInput.getType());
        }

        return true;
    }

    private void blur() {
        blurInput.copyFrom(bitmapToBlur);
        blurScript.setInput(blurInput);
        blurScript.forEach(blurOutput);
        blurOutput.copyTo(blurredBitmap);
    }

}
