package com.example.user.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
                captureView.setImageBitmap(bitmap);
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
