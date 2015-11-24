package com.example.user.myapplication;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;

/**
 * Created by user on 2015/11/24.
 */
public class CustomCamera extends Activity {
    SurfaceView surfaceView;
    SurfaceHolder surfaceholder;
    Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_camera);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceholder = surfaceView.getHolder();
        surfaceholder.addCallback(callback);
    }

    /*************
     * 相机的声明周期与activity的 声明周期绑定
     ************/
    @Override
    protected void onResume() {
        super.onResume();
        if (camera == null) {
            camera = getCamera();
            if(surfaceholder!=null){
                setStartProview(camera,surfaceholder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
       releaseCamera();
    }

    /**********************************************/


    public  void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.button5:
                setStartProview(camera,surfaceholder);
                break;
        }
    }

    private Camera getCamera() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            camera = null;
            e.printStackTrace();
        }
        return camera;
    }

    //设置相机的预览
    private void setStartProview(Camera camera,SurfaceHolder  holder) {
        try {
            camera.setPreviewDisplay(holder);
            //旋转的 camera  的角度
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //释放相机占用的系统资源
    private   void releaseCamera(){
        if(camera!=null){
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera=null;
        }
    }
    private  SurfaceHolder.Callback callback =new  SurfaceHolder.Callback(){

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
          setStartProview(camera,surfaceholder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            camera.stopPreview();
            setStartProview(camera,surfaceholder);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            releaseCamera();
        }
    };
}
