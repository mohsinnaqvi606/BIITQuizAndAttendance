package com.naqvi.biitquizandattendance.Camera;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.naqvi.biitquizandattendance.R;
import com.naqvi.biitquizandattendance.SharedReference;

import es.dmoral.toasty.Toasty;

public class Take_Picture_Activity extends AppCompatActivity {

    Bitmap myimg1, myimg2;
    Camera camera1, camera2;
    FrameLayout frameLayout1, frameLayout2;
    Show_Back_Camera showBackCamera;
    Show_Front_Camera showFrontCamera;
    ImageView img1, img2, main_img, img_take_picture;
    RelativeLayout layout_confirmation;
    boolean isFirst = true;
    SharedReference SharedRef;
    TextView tv_pin, tv_selfie;
    String base64 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);


        getSupportActionBar().hide();
        SharedRef = new SharedReference(Take_Picture_Activity.this);
        frameLayout1 = findViewById(R.id.frameLayout1);
        frameLayout2 = findViewById(R.id.frameLayout2);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        tv_pin = findViewById(R.id.tv_pin);
        tv_selfie = findViewById(R.id.tv_selfie);
        main_img = findViewById(R.id.main_img);
        img_take_picture = findViewById(R.id.img_take_picture);
        layout_confirmation = findViewById(R.id.layout_confirmation);
        layout_confirmation.setVisibility(View.GONE);
        tv_selfie.setVisibility(View.GONE);

        img1.setVisibility(View.GONE);
        img2.setVisibility(View.GONE);
        main_img.setVisibility(View.GONE);
        runCamer1.run();

    }


    Runnable runCamer1 = new Runnable() {
        @Override
        public void run() {
            frameLayout1.removeAllViews();
            try {
                camera1 = Camera.open(0);
                showBackCamera = new Show_Back_Camera(Take_Picture_Activity.this, camera1);
                frameLayout1.addView(showBackCamera);
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };


    Runnable runCamer2 = new Runnable() {
        @Override
        public void run() {
            frameLayout1.removeAllViews();
            try {
                camera2 = Camera.open(1);
                showFrontCamera = new Show_Front_Camera(Take_Picture_Activity.this, camera2);
                frameLayout2.addView(showFrontCamera);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        camera2.takePicture(null, null, mPictureCallback);
                    }
                }, 2500);
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    };

    Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            if (isFirst) {
                myimg1 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                frameLayout1.removeAllViews();
                frameLayout1.setVisibility(View.GONE);

                Matrix matrix = new Matrix();
                matrix.setRotate(90);
                myimg1 = Bitmap.createBitmap(myimg1, 0, 0, myimg1.getWidth(), myimg1.getHeight(), matrix, false);

            //    img1.setImageBitmap(myimg1);
                img1.setImageBitmap(Bitmap.createScaledBitmap(myimg1, 750, 650, false));

                img1.setVisibility(View.VISIBLE);
                isFirst = false;
                tv_pin.setVisibility(View.GONE);
                tv_selfie.setVisibility(View.VISIBLE);
                runCamer2.run();
            } else {

                try {
                    tv_selfie.setVisibility(View.GONE);
                    myimg2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    frameLayout2.removeAllViews();
                    frameLayout2.setVisibility(View.GONE);

                    Matrix matrix = new Matrix();
                    matrix.setRotate(270);
                    myimg2 = Bitmap.createBitmap(myimg2, 0, 0, myimg2.getWidth(), myimg2.getHeight(), matrix, true);

                 //   img2.setImageBitmap(myimg2);
                    img2.setImageBitmap(Bitmap.createScaledBitmap(myimg2, 750, 650, false));

                    img2.setVisibility(View.VISIBLE);

                    img1.setVisibility(View.GONE);
                    img2.setVisibility(View.GONE);
                    main_img.setVisibility(View.VISIBLE);

                    int h = myimg1.getHeight() + myimg2.getHeight();
                    int w;
                    if (myimg1.getWidth() >= myimg2.getHeight()) {
                        w = myimg1.getWidth();
                    } else {
                        w = myimg2.getWidth();
                    }

                    Bitmap.Config config = myimg1.getConfig();
                    if (config == null) {
                        config = Bitmap.Config.ARGB_8888;
                    }

                    Bitmap newBitmap = Bitmap.createBitmap(w, h, config);
                    Canvas newCanvas = new Canvas(newBitmap);


                    newCanvas.drawBitmap(myimg1, 0, 0, null);
                    newCanvas.drawBitmap(myimg2, 0, myimg1.getHeight(), null);
                    base64 = ImageUtil.convertToBase64(newBitmap);
                    SharedRef.saveImage(base64);
                    img_take_picture.setVisibility(View.GONE);
                 //   main_img.setImageBitmap(newBitmap);
                    main_img.setImageBitmap(Bitmap.createScaledBitmap(newBitmap, 250, 400, false));

                    layout_confirmation.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    Toasty.normal(Take_Picture_Activity.this, e.getMessage(), Toasty.LENGTH_LONG).show();
                }
            }
        }
    };


    public void capture_Pic_Click(View view) {
        if (isFirst) {
            img_take_picture.setVisibility(View.GONE);
            camera1.takePicture(null, null, mPictureCallback);
        }
    }

    public void take_picture_again(View view) {
        isFirst = true;
        main_img.setVisibility(View.GONE);
        layout_confirmation.setVisibility(View.GONE);
        img_take_picture.setVisibility(View.VISIBLE);
        frameLayout1.setVisibility(View.VISIBLE);
        frameLayout2.setVisibility(View.VISIBLE);
        tv_pin.setVisibility(View.VISIBLE);

        runCamer1.run();
    }

    public void img_confirm(View view) {
        finish();
    }

    @Override
    public void onBackPressed() {
    }
}
