package com.ppal007.cameracaptureimage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.ppal007.cameracaptureimage.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private static final int CAMERA_REQUEST_CODE = 111;
    private static final int GALLERY_REQUEST_CODE = 222;

    private AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);

//        request for camera permission
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }


//        button camera click listener
        binding.buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                initialize dialog
                dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setIcon(R.drawable.ic_photo_camera);
                dialogBuilder.setTitle("Take Photo");
                dialogBuilder.setMessage("Take Photo or Open Gallery");
                dialogBuilder.setCancelable(true);
                dialogBuilder.setNeutralButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        open camera
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA_REQUEST_CODE);
                        dialog.dismiss();
                    }
                });

                dialogBuilder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @SuppressLint("IntentReset")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        open gallery
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Select File"), GALLERY_REQUEST_CODE);
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == CAMERA_REQUEST_CODE) {
//                get capture image
                assert data != null;
                Bitmap captureImage = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
//                set capture image to imageView
                binding.imageView.setImageBitmap(captureImage);

            } else if (requestCode == GALLERY_REQUEST_CODE) {

//                get image from gallery
                assert data != null;
                Uri selectImageUri = data.getData();
//                set image to imageView
                binding.imageView.setImageURI(selectImageUri);

            }

        }

    }
}
