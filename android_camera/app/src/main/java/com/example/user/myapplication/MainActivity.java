package com.example.user.myapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    String originImagePath;//原始文件的地址
    String cutImagePath;// 剪贴文件的地址；
   public  String environmentPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(R.id.imageView);
        environmentPath = Environment.getExternalStorageDirectory().getPath();
        originImagePath = environmentPath + "/camera.jpg";
        cutImagePath = environmentPath + "/1.jpg";
    }

    //button 的 onclick事件
    public void viewClick(View view) {
        switch (view.getId()) {
            //调用intent的压缩图片
            case R.id.button:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1000);
                break;
            //调用相机拍照后的原图
            case R.id.button2:
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 下面这句指定调用相机拍照后的照片存储的路径
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(originImagePath)));
                startActivityForResult(intent2, 2000);
                break;
            case R.id.button3:
                scalePhotoZoom(Uri.fromFile(new File(originImagePath)), cutImagePath);
                break;
            case R.id.button4:
                   /* 使用Intent.ACTION_GET_CONTENT这个Action */
                Intent openSysAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                /* 开启Pictures画面Type设定为image */
                openSysAlbum.setType("image/*");
                startActivityForResult(openSysAlbum, 4000);
            break;
            case R.id.button6:
                Intent customActivity=new Intent(this,CustomCamera.class);
                startActivity(customActivity);
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Camera", "resultCode:" + resultCode);
        if (resultCode == RESULT_OK && requestCode == 1000) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            imageView.setImageBitmap(bitmap);


        } else if (resultCode == RESULT_OK && requestCode == 2000) {
            Bitmap bitmap = readImageFile(originImagePath);
            imageView.setImageBitmap(bitmap);


        } else if (resultCode == RESULT_OK && requestCode == 3000) {
            Bitmap bitmap = readImageFile(cutImagePath);
            imageView.setImageBitmap(bitmap);
        }else if (resultCode == RESULT_OK && requestCode == 4000) {

            Uri  uri=data.getData();
            Bitmap bitmap = openImageFile(uri);
            imageView.setImageBitmap(bitmap);
        }
    }
    //通过 URI 读取文件
    private Bitmap openImageFile(Uri   uri){
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap = null;
        try {
            bitmap =BitmapFactory.decodeStream(cr.openInputStream(uri));
            cr.openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return   bitmap;
    }
    //通过 filePath  读取文件
    private Bitmap readImageFile(String filePath) {
        FileInputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            inputStream = new FileInputStream(filePath);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * @param uri          图片的地址
     * @param cutImagePath 剪切图片的地址
     */
    public void scalePhotoZoom(Uri uri, String cutImagePath) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 5);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 4);// x:y=1:2
        intent.putExtra("output", Uri.fromFile(new File(cutImagePath)));
        intent.putExtra("outputFormat", "JPEG");//返回格式
        startActivityForResult(intent, 3000);
    }

    public void browsePhotoAlbum() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
