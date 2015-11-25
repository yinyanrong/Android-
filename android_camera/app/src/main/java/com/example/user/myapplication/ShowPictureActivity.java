package com.example.user.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by user on 2015/11/24.
 */
public class ShowPictureActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_picture_activity);
        ImageView  captureView= (ImageView) findViewById(R.id.imageView2);
        Intent intent=getIntent();
        String  filepath=intent.getStringExtra("filepath");
        if(!TextUtils.isEmpty(filepath)){
            try {
                FileInputStream  fileInputStream=new FileInputStream(filepath);
                Bitmap bitmap=BitmapFactory.decodeStream(fileInputStream);
                // 部分手机 可能会 出现  报错 显示不出来Bitmap too large to be uploaded into a texture (3120x4160, max=4096x4096)
                Matrix  matrix=new Matrix();
                matrix.setScale(0.5f,0.5f);
                matrix.setRotate(90);
                Bitmap bitmap1=  Bitmap.createBitmap(bitmap, 0, 0,800,400 , matrix, true);
                captureView.setImageBitmap(bitmap1);
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }
}
