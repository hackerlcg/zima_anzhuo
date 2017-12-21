package com.beihui.market.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

public class ImageUtils {

    public static Bitmap getFixedBitmap(String path) {
        if (path != null) {
            Bitmap source = BitmapFactory.decodeFile(path);
            return rotateBitmapByDegree(source, getBitmapDegree(path));
        }
        return null;
    }

    public static Bitmap getFixedBitmap(String path, int size) {
        if (path != null) {
            Bitmap source = readBitmapInSampleSize(path, size);
            return rotateBitmapByDegree(source, getBitmapDegree(path));
        }
        return null;
    }

    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    public static Bitmap rotateBitmapByDegree(Bitmap source, int degree) {
        if (source != null) {
            // 根据旋转角度，生成旋转矩阵
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
            if (source != bitmap && !source.isRecycled()) {
                source.recycle();
            }
            return bitmap;
        }
        return null;
    }

    public static Bitmap readBitmapInSampleSize(String path, int size) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        Bitmap avatar;
        int max = Math.max(options.outHeight, options.outWidth);
        options.inJustDecodeBounds = false;
        if (max > size) {
            options.inSampleSize = max / size;
            avatar = BitmapFactory.decodeFile(path, options);
        } else {
            avatar = BitmapFactory.decodeFile(path, options);
        }
        return avatar;
    }

}
