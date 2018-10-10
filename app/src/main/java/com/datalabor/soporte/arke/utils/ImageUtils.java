package com.datalabor.soporte.arke.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageUtils {

    private static final int MAX_IMAGE_DIMENSION = 1024;

    public static Bitmap getCorrectlyOrientedImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        return srcBitmap;
    }

    public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    public static Bitmap createScaledBitmap(Bitmap originalBitmap, int desiredSide) {
        Bitmap resizedBitmap = null;
        if (originalBitmap.getWidth() != originalBitmap.getHeight()) {
            int newHeight = 0;
            int newWidth = 0;
            int width = originalBitmap.getWidth();
            int height = originalBitmap.getHeight();
            if (width > height) {
                newHeight = desiredSide;
                newWidth = (width * newHeight) / height;
            } else {
                newWidth = desiredSide;
                newHeight = (newWidth * height) / width;
            }
            resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);
        } else {
            resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, desiredSide, desiredSide, true);
        }
        return resizedBitmap;
    }

    /** Used to decode the selected used picture into a fixed size bitmap */
    public static Bitmap decodeUriIntoBitmap(Context context, Uri selectedImageUri) throws FileNotFoundException {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImageUri), null, options);

        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = 1;

        final int REQUIRED_SIZE = 200;

        int width_tmp = options.outWidth, height_tmp = options.outHeight;
        int scale = 1;

        Bitmap decodedBitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImageUri), null, options2);

        Bitmap scaledBitmap = createScaledBitmap(decodedBitmap, MAX_IMAGE_DIMENSION);

        return scaledBitmap;
    }

    public static File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //
        // File storageDir = Environment.getExternalStorageDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        boolean creado = storageDir.mkdirs();

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }


    public static String getImage64(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);

        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }


}
