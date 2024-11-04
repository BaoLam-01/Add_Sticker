package vn.tapbi.sample2021kotlin.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {
    static public Bitmap cropBitmapToBoundingBox(Bitmap picToCrop, int unusedSpaceColor) {
        /**
         * 지정한 색깔 (unusedSpaceColor) 을 제외시킨 이미지를 리턴함.
         */

        int[] pixels = new int[picToCrop.getHeight() * picToCrop.getWidth()];
        int marginTop = 0, marginBottom = 0, marginLeft = 0, marginRight = 0, i;
        picToCrop.getPixels(pixels, 0, picToCrop.getWidth(), 0, 0,
                picToCrop.getWidth(), picToCrop.getHeight());

        for (i = 0; i < pixels.length; i++) {
            if (pixels[i] != unusedSpaceColor) {
                marginTop = i / picToCrop.getWidth();
                break;
            }
        }

        outerLoop1:
        for (i = 0; i < picToCrop.getWidth(); i++) {
            for (int j = i; j < pixels.length; j += picToCrop.getWidth()) {
                if (pixels[j] != unusedSpaceColor) {
                    marginLeft = j % picToCrop.getWidth();
                    break outerLoop1;
                }
            }
        }

        for (i = pixels.length - 1; i >= 0; i--) {
            if (pixels[i] != unusedSpaceColor) {
                marginBottom = (pixels.length - i) / picToCrop.getWidth();
                break;
            }
        }

        outerLoop2:
        for (i = pixels.length - 1; i >= 0; i--) {
            for (int j = i; j >= 0; j -= picToCrop.getWidth()) {
                if (pixels[j] != unusedSpaceColor) {
                    marginRight = picToCrop.getWidth()
                            - (j % picToCrop.getWidth());
                    break outerLoop2;
                }
            }
        }

        return Bitmap.createBitmap(picToCrop, marginLeft, marginTop,
                picToCrop.getWidth() - marginLeft - marginRight,
                picToCrop.getHeight() - marginTop - marginBottom);
    }

    public static Bitmap getCroppedBitmap(Bitmap src, Path path, float left, float top) {
        Bitmap output = Bitmap.createBitmap(src.getWidth(),
                src.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0XFF000000);

        canvas.drawPath(path, paint);

        // Keeps the source pixels that cover the destination pixels,
        // discards the remaining source and destination pixels.
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(src, left, top, paint);

        return output;
    }


    public static Uri saveBitmapToFile(Bitmap bitmap, Context context){
        File file = new File(context.getCacheDir(), "cutOutImage.png");
        FileOutputStream fos;
        try {
            fos =  new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Uri.fromFile(file);
    }

    public static Bitmap getBitmapFromUri(Uri uri, Context context){
        Bitmap bitmap;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(inputStream);

            assert inputStream != null;
            inputStream.close();

            return bitmap;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Bitmap scaleBitmapAndKeepRation(Bitmap targetBmp, int reqHeightInPixels,int reqWidthInPixels) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0f, 0f, (float)targetBmp.getWidth(), (float) targetBmp.getHeight()),
                new RectF(0f, 0f, (float)reqWidthInPixels, (float) reqHeightInPixels),
                Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(targetBmp, 0, 0, targetBmp.getWidth(), targetBmp.getHeight(),m, true);
    }
}
