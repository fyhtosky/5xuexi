package com.sj.yinjiaoyun.xuexi.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.sj.yinjiaoyun.xuexi.R;
import com.sj.yinjiaoyun.xuexi.adapter.FolderAdapter;
import com.sj.yinjiaoyun.xuexi.adapter.SinglePhotoAdapter;
import com.sj.yinjiaoyun.xuexi.bean.PictureReturnBean;
import com.sj.yinjiaoyun.xuexi.bean.ReturnBean;
import com.sj.yinjiaoyun.xuexi.entry.MyBaseActivity;
import com.sj.yinjiaoyun.xuexi.entry.Photo;
import com.sj.yinjiaoyun.xuexi.entry.PhotoFolder;
import com.sj.yinjiaoyun.xuexi.http.Api;
import com.sj.yinjiaoyun.xuexi.http.CallBack;
import com.sj.yinjiaoyun.xuexi.http.HttpClient;
import com.sj.yinjiaoyun.xuexi.utils.OtherUtils;
import com.sj.yinjiaoyun.xuexi.utils.PhotoUtils;
import com.sj.yinjiaoyun.xuexi.utils.PhotoZoomUtil;
import com.sj.yinjiaoyun.xuexi.utils.PreferencesUtils;
import com.sj.yinjiaoyun.xuexi.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChangeHeadPictureActivity extends MyBaseActivity  {


    public final static String TAG = "ChangeHeadPictureActivity";
    //启动相册的标示
    public final static int REQUEST_CAMERA = 1;

    private final static String ALL_PHOTO = "所有图片";
    //启动相册剪裁的标示
    private static final int RESULT_CAMERA_CROP_PATH_RESULT =0 ;


    private GridView mGridView;
    private Map<String, PhotoFolder> mFolderMap;
    private List<Photo> mPhotoLists = new ArrayList<>();
    private SinglePhotoAdapter mPhotoAdapter;
    private ProgressDialog mProgressDialog;
    private ListView mFolderListView;

    private TextView mPhotoNumTV;
    private TextView mPhotoNameTV;
    /** 文件夹列表是否处于显示状态 */
    boolean mIsFolderViewShow = false;
    /** 文件夹列表是否被初始化，确保只被初始化一次 */
    boolean mIsFolderViewInit = false;

    /** 拍照时存储拍照结果的临时文件 */
    private File mTmpFile;
    private File  fileCropUri;


    private String mUserName;
    //获取读取内存的权限
    private static final int READ_EXTERNAL_STORAGE=1;
    //获取打开相机的权限
    private static final int OPEN_CAMERA=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_head_picture);
        initView();
        if (!OtherUtils.isExternalStorageAvailable()) {
            Toast.makeText(this, "No SD card!", Toast.LENGTH_SHORT).show();
            return;
        }

        //发起请求获得用户许可,可以在此请求多个权限
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[] { android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE);
        }else {
            getPhotosTask.execute();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
          if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
              if(requestCode==READ_EXTERNAL_STORAGE){
                  getPhotosTask.execute();
              }else if(requestCode==OPEN_CAMERA){
                  showCamera();
              }
          }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mUserName = PreferencesUtils.getSharePreStr(this, "username");
        ToastUtil.getNetworkSatte(ChangeHeadPictureActivity.this);
    }

    private void initView() {
         Button  mCommitBtn = (Button) findViewById(R.id.commit);
        mCommitBtn.setVisibility(View.GONE);
        mGridView = (GridView) findViewById(R.id.photo_gridview);
        mPhotoNumTV = (TextView) findViewById(R.id.photo_num);
        mPhotoNameTV = (TextView) findViewById(R.id.floder_name);
        findViewById(R.id.bottom_tab_bar).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //消费触摸事件，防止触摸底部tab栏也会选中图片
                return true;
            }
        });
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 获取照片的异步任务
     */

    public AsyncTask getPhotosTask = new AsyncTask() {
        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(ChangeHeadPictureActivity.this, null, "loading...");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            mFolderMap = PhotoUtils.getPhotos(
                    ChangeHeadPictureActivity.this.getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            getPhotosSuccess();
        }
    };
    private void getPhotosSuccess() {
        mProgressDialog.dismiss();
        mPhotoLists.addAll(mFolderMap.get(ALL_PHOTO).getPhotoList());

        mPhotoNumTV.setText(OtherUtils.formatResourceString(getApplicationContext(),
                R.string.photos_num, mPhotoLists.size()));

        mPhotoAdapter = new SinglePhotoAdapter(this.getApplicationContext(), mPhotoLists);
        mPhotoAdapter.setIsShowCamera(true);
        mGridView.setAdapter(mPhotoAdapter);
        Set<String> keys = mFolderMap.keySet();
        final List<PhotoFolder> folders = new ArrayList<>();
        for (String key : keys) {
            if (ALL_PHOTO.equals(key)) {
                PhotoFolder folder = mFolderMap.get(key);
                folder.setIsSelected(true);
                folders.add(0, folder);
            } else {
                folders.add(mFolderMap.get(key));
            }
        }
        mPhotoNameTV.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                toggleFolderList(folders);
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ( position == 0) {
                    //发起请求获得用户许可,可以在此请求多个权限
                    if (ContextCompat.checkSelfPermission(ChangeHeadPictureActivity.this, android.Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ChangeHeadPictureActivity.this,new String[] {android.Manifest.permission.CAMERA},
                                OPEN_CAMERA  );
                    }else {
                        //同意该权限则直接启动相机
                        showCamera();
                    }
                }else{
                    /**
                     * 跳转剪切界面
                     */
                  Photo p=mPhotoLists.get(position);
                  Logger.d("选择图片的路劲："+p.getPath());
//                    cropImg(p.getPath());
//                    fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
                    mTmpFile=new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
                    File file = new File(p.getPath());
                    Uri inputUri;
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                        inputUri=FileProvider.getUriForFile(ChangeHeadPictureActivity.this, "com.sj.yinjiaoyun.xuexi.fileProvider", file);
                    }else {
                        inputUri = Uri.fromFile(file);
                    }
                    startPhotoZoom(inputUri,mTmpFile);//设置输入类型
//
                }

            }
        });
    }



    /**
     * 跳转相册剪切界面
   　＊picPath
     */
    public void cropImg(String picPath ) {
        File file = new File(picPath);
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            String url = PhotoZoomUtil.getPath(this, uri);
            if(url!=null){
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            }
        } else {
            intent.setDataAndType(uri, "image/*");
        }

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_CAMERA_CROP_PATH_RESULT);
    }
    /**
     * 选择相机
     */
    private void showCamera() {
        // 创建临时文件存储照片
        mTmpFile=  new File(getExternalFilesDir("img"), "temp.jpg");
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager()) != null){
            // 设置系统相机拍照后的输出路径
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                Uri uriForFile = FileProvider.getUriForFile(ChangeHeadPictureActivity.this, "com.sj.yinjiaoyun.xuexi.fileProvider", mTmpFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }else {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
            }
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        }else{
            Toast.makeText(getApplicationContext(),
                    R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 相机拍照完成后，返回图片路径
        if(requestCode == REQUEST_CAMERA){
            if(resultCode == Activity.RESULT_OK) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                    Uri inputUri=FileProvider.getUriForFile(ChangeHeadPictureActivity.this, "com.sj.yinjiaoyun.xuexi.fileProvider", mTmpFile);
                    startPhotoZoom(inputUri,mTmpFile);//设置输入类型
                }else {
                    Uri inputUri = Uri.fromFile(mTmpFile);
                    startPhotoZoom(inputUri,mTmpFile);
                }
//                if (mTmpFile != null) {
//                    //剪切
//                    cropImg(mTmpFile.getAbsolutePath());
//                }
//            }else{
//                if(mTmpFile != null && mTmpFile.exists()){
//                    mTmpFile.delete();
//                }
            }
            //相机拍完后或者相册选择后剪裁返回的数据
        }else  if(requestCode==RESULT_CAMERA_CROP_PATH_RESULT){
            if(resultCode == Activity.RESULT_OK) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(
                            getContentResolver().openInputStream(Uri.fromFile(mTmpFile)));
                    UploadPhoto(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

//                //获得剪裁之后的数据
//                if (data != null) {
//                   Uri uri= data.getData();
//                   Logger.d("裁剪之后的照片："+uri);
//                    Bundle extras = data.getExtras();
//
//                    if (extras != null) {
//                         Bitmap   bitmap = extras.getParcelable("data");
//                        UploadPhoto(bitmap);
//                    }
//                }
            }
        }
    }

    /**
     * 裁剪图片方法实现
     * @param inputUri
     */
    private void startPhotoZoom(Uri inputUri,File file) {
        if (inputUri == null) {
            Logger.d("The uri is not exist.");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri outPutUri = Uri.fromFile(file);
            intent.setDataAndType(inputUri, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }else {
            Uri outPutUri = Uri.fromFile(file);
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                //这个方法是处理4.4以上图片返回的Uri对象不同的处理方法
                String url = PhotoZoomUtil.getPath(this, inputUri);
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            }else {
                intent.setDataAndType(inputUri, "image/*");
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        }
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式
        startActivityForResult(intent, RESULT_CAMERA_CROP_PATH_RESULT);//这里就将裁剪后的图片的Uri返回了

        }

    private void UploadPhoto(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //将bitmap一字节流输出 Bitmap.CompressFormat.PNG 压缩格式，100：压缩率，baos：字节流
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] buffer = baos.toByteArray();
        System.out.println("图片的大小："+buffer.length);
        //将图片的字节流数据加密成base64字符输出
        final String photo = Base64.encodeToString(buffer, 0, buffer.length,Base64.DEFAULT).replace("+", "%2B");
        HttpClient.postArray1(Api.COMMON_UPLOAD_PHOTO,
                "image=" + photo + "&fileName=" + new Date().getTime() + ".png" + "&mimeType=png&groupType="+String.valueOf(1), new CallBack<StringBuffer>() {
                    @Override
                    public void onSuccess(StringBuffer result) {
                        Logger.d("上传图片返回的URL:"+result.toString());
                        Gson gson=new Gson();
                        PictureReturnBean bean=gson.fromJson(result.toString(),PictureReturnBean.class);
                        if(bean.isSuccess()){
                            Logger.d("上传图片返回的URL："+bean.getData().getUrl());
//                            ToastUtil.showShortToast(ChangeHeadPictureActivity.this,"图片上传成功");
                            PreferencesUtils.putSharePre(getApplicationContext(),"userImg",bean.getData().getUrl());
                            //更新头像
                            initData(1,bean.getData().getUrl());
                            ChangeHeadPictureActivity.this.finish();
                        }else{
                            ToastUtil.showShortToast(ChangeHeadPictureActivity.this,bean.getMessage());
                        }
                    }

                });
    }
    /**
     * 显示或者隐藏文件夹列表
     * @param folders
     */
    private void toggleFolderList(final List<PhotoFolder> folders) {
        //初始化文件夹列表
        if(!mIsFolderViewInit) {
            ViewStub folderStub = (ViewStub) findViewById(R.id.floder_stub);
            folderStub.inflate();
            View dimLayout = findViewById(R.id.dim_layout);
            mFolderListView = (ListView) findViewById(R.id.listview_floder);
            final FolderAdapter adapter = new FolderAdapter(this, folders);
            mFolderListView.setAdapter(adapter);
            mFolderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (PhotoFolder folder : folders) {
                        folder.setIsSelected(false);
                    }
                    PhotoFolder folder = folders.get(position);
                    folder.setIsSelected(true);
                    adapter.notifyDataSetChanged();

                    mPhotoLists.clear();
                    mPhotoLists.addAll(folder.getPhotoList());
                    //这里重新设置adapter而不是直接notifyDataSetChanged，是让GridView返回顶部
                    mGridView.setAdapter(mPhotoAdapter);
                    mPhotoNumTV.setText(OtherUtils.formatResourceString(getApplicationContext(),
                            R.string.photos_num, mPhotoLists.size()));
                    mPhotoNameTV.setText(folder.getName());
                    toggle();
                }
            });
            dimLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mIsFolderViewShow) {
                        toggle();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            initAnimation(dimLayout);
            mIsFolderViewInit = true;
        }
        toggle();
    }
    /**
     * 弹出或者收起文件夹列表
     */
    private void toggle() {
        if(mIsFolderViewShow) {
            outAnimatorSet.start();
            mIsFolderViewShow = false;
        } else {
            inAnimatorSet.start();
            mIsFolderViewShow = true;
        }
    }
    /**
     * 初始化文件夹列表的显示隐藏动画
     */
    AnimatorSet inAnimatorSet = new AnimatorSet();
    AnimatorSet outAnimatorSet = new AnimatorSet();
    private void initAnimation(View dimLayout) {
        ObjectAnimator alphaInAnimator, alphaOutAnimator, transInAnimator, transOutAnimator;
        //获取actionBar的高
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        /**
         * 这里的高度是，屏幕高度减去上、下tab栏，并且上面留有一个tab栏的高度
         * 所以这里减去3个actionBarHeight的高度
         */
        int height = OtherUtils.getHeightInPx(this) - 3*actionBarHeight;
        alphaInAnimator = ObjectAnimator.ofFloat(dimLayout, "alpha", 0f, 0.7f);
        alphaOutAnimator = ObjectAnimator.ofFloat(dimLayout, "alpha", 0.7f, 0f);
        transInAnimator = ObjectAnimator.ofFloat(mFolderListView, "translationY", height , 0);
        transOutAnimator = ObjectAnimator.ofFloat(mFolderListView, "translationY", 0, height);

        LinearInterpolator linearInterpolator = new LinearInterpolator();

        inAnimatorSet.play(transInAnimator).with(alphaInAnimator);
        inAnimatorSet.setDuration(300);
        inAnimatorSet.setInterpolator(linearInterpolator);
        outAnimatorSet.play(transOutAnimator).with(alphaOutAnimator);
        outAnimatorSet.setDuration(300);
        outAnimatorSet.setInterpolator(linearInterpolator);
    }
    /**
     * 发送请求修改家庭地址
     */
    private void initData(final int araType, final String content) {
        /**
         * type
         * 1. 头像
         * 2. 姓名：(有学籍)不可编辑
         * 3. 性别：(有学籍)不可编辑性别 1:男       2:女        3:保密
         * 4. 身份证号：(有学籍)不可编辑
         * 5. 用户名
         * 6. 民族
         * 7. 政治面貌
         * 8. 固话
         * 9. 邮政编码
         * 10. 家庭地址

         */
        HashMap<String, String> map = new HashMap<>();
        map.put("id", mUserName);
        map.put("type", String.valueOf(araType));
        map.put("content", content);
        HttpClient.post(this, Api.MODIFY_USER_INFO, map, new CallBack<ReturnBean>() {
            @Override
            public void onSuccess(ReturnBean result) {
                if (result == null) {
                    return;
                }
                if (result.isSuccess()) {
                     Logger.d("图片更新成功");
                }else {
                    ToastUtil.showShortToast(ChangeHeadPictureActivity.this, result.getMessage());
                }

                }


        });
    }

}
