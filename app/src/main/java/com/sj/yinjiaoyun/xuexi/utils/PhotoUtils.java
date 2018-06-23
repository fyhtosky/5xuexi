package com.sj.yinjiaoyun.xuexi.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import com.sj.yinjiaoyun.xuexi.entry.Photo;
import com.sj.yinjiaoyun.xuexi.entry.PhotoFolder;
import com.sj.yinjiaoyun.xuexi.http.CallBack;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by wanzhiying on 2017/3/16.
 */
public class PhotoUtils {
    public static Handler handler=new Handler(Looper.getMainLooper());
    /** 首先默认个文件保存路径 */
   //保存到SD卡
    private static final String SAVE_PIC_PATH=Environment.getExternalStorageState().equals(
            Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory().getAbsolutePath() : "/mnt/sdcard;";//保存到SD卡
    private static final String SAVE_REAL_PATH = SAVE_PIC_PATH + "/5xuexi/savePic";//保存的确切位置

    public static Map<String, PhotoFolder> getPhotos(Context context) {
        Map<String, PhotoFolder> folderMap = new HashMap<>();

        String allPhotosKey = "所有图片";
        PhotoFolder allFolder = new PhotoFolder();
        allFolder.setName(allPhotosKey);
        allFolder.setDirPath(allPhotosKey);
        allFolder.setPhotoList(new ArrayList<Photo>());
        folderMap.put(allPhotosKey, allFolder);

        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();

        // 只查询jpeg和png的图片
        Cursor mCursor = mContentResolver.query(imageUri, null,
                MediaStore.Images.Media.MIME_TYPE + " in(?, ?)",
                new String[] { "image/jpeg", "image/png" },
                MediaStore.Images.Media.DATE_MODIFIED + " desc");

        if (mCursor != null && mCursor.moveToFirst()) {
            int pathIndex = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                // 获取图片的路径
                String path = mCursor.getString(pathIndex);

                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                if (parentFile == null) {
                    continue;
                }
                String dirPath = parentFile.getAbsolutePath();

                if (folderMap.containsKey(dirPath)) {
                    Photo photo = new Photo(path);
                    PhotoFolder photoFolder = folderMap.get(dirPath);
                    photoFolder.getPhotoList().add(photo);
                    folderMap.get(allPhotosKey).getPhotoList().add(photo);

                } else {
                    // 初始化imageFolder
                    PhotoFolder photoFolder = new PhotoFolder();
                    List<Photo> photoList = new ArrayList<>();
                    Photo photo = new Photo(path);
                    photoList.add(photo);
                    photoFolder.setPhotoList(photoList);
                    photoFolder.setDirPath(dirPath);
                    photoFolder.setName(dirPath.substring(dirPath.lastIndexOf(File.separator) + 1, dirPath.length()));
                    folderMap.put(dirPath, photoFolder);
                    folderMap.get(allPhotosKey).getPhotoList().add(photo);
                }
            } while (mCursor.moveToNext());
        }
        mCursor.close();
        return folderMap;
    }
    public static void saveFile(Context context,Bitmap bm) throws IOException {
        Calendar now = new GregorianCalendar();
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        String fileName = simpleDate.format(now.getTime());
        File foder = new File(SAVE_REAL_PATH);
        if (!foder.exists()) {
            foder.mkdirs();
        }
        File myCaptureFile = new File(SAVE_REAL_PATH, fileName);
        if (!myCaptureFile.exists()) {
            myCaptureFile.createNewFile();

        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    myCaptureFile.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //这个广播的目的就是更新图库，发了这个广播进入相册就可以找到你保存的图片了！，记得要传你更新的file哦
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(SAVE_REAL_PATH));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    /**
     * 网络url转换为Bitmap
     * @param url
     * @return
     */
    public static void decodeUriAsBitmapFromNet(final String url, final CallBack  callBack) {
        new Thread(){
            @Override
            public void run() {
                URL fileUrl = null;
                Bitmap bitmap;
                try {
                    fileUrl = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                try {
                    if(fileUrl!=null){
                        HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                        final Bitmap finalBitmap = bitmap;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(finalBitmap);
                            }
                        });
                        is.close();
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailure(e.getMessage());
                        }
                    });
                }

            }
        }.start();

    }
}
