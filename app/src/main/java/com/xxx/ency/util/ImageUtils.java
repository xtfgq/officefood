package com.xxx.ency.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.graphics.Bitmap.CompressFormat.JPEG;

/**
 * Created by guoqiang on 2018/1/8.
 */

public class ImageUtils {
    public static String getImagePath(Uri uri, String selection,Context context) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor =context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    public static String getUriString(Uri uri, ContentResolver cr) {
        String imgPath = null;
        if (uri != null) {
            String uriString = uri.toString();
            //小米手机的适配问题，小米手机的uri以file开头，其他的手机都以content开头
            //以content开头的uri表明图片插入数据库中了，而以file开头表示没有插入数据库
            //所以就不能通过query来查询，否则获取的cursor会为null。
            if (uriString.startsWith("file")) {
                //uri的格式为file:///mnt....,将前七个过滤掉获取路径
                imgPath = uriString.substring(7, uriString.length());
                return imgPath;
            }
            Cursor cursor = cr.query(uri, null, null, null, null);
            cursor.moveToFirst();
            imgPath = cursor.getString(1); // 图片文件路径

        }
        return imgPath;
    }
    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param activity
     * @param imageUri
     * @author guoqaing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    public static long getAutoFileOrFilesSize(String filePath) {

        File file = new File(filePath);

        long blockSize = 0;

        try {

            if (file.isDirectory()) {

                blockSize = getFileSizes(file);

            } else {

                blockSize = getFileSize(file);

            }

        } catch (Exception e) {

            e.printStackTrace();


        }

        return blockSize;

    }
    private static long getFileSizes(File f) throws Exception

    {

        long size = 0;

        File flist[] = f.listFiles();

        for (int i = 0; i < flist.length; i++) {

            if (flist[i].isDirectory()) {

                size = size + getFileSizes(flist[i]);

            } else {

                size = size + getFileSize(flist[i]);

            }

        }

        return size;

    }

    private static long getFileSize(File file) throws Exception

    {

        long size = 0;

        if (file.exists()) {

            FileInputStream fis = null;

            fis = new FileInputStream(file);

            size = fis.available();
        } else {
            file.createNewFile();

        }
        return size;

    }
}
