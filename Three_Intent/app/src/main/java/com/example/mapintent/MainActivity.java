package com.example.mapintent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private Button btnWeb ;
    private EditText textWeb ;
    private EditText tittle;
    private EditText location;
    private EditText des;
    private CheckBox checkallday;
    private Button btnAddE;
    private ImageView iMage;
    private Button btnCapture;
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnWeb=findViewById(R.id.btnWeb);
        textWeb=findViewById(R.id.WebUri);
        tittle =findViewById(R.id.tittle);
        location =findViewById(R.id.location);
        des =findViewById(R.id.Descrip);
        checkallday =findViewById(R.id.CAllday);
        btnAddE=findViewById(R.id.btnAddEvent);
        iMage=findViewById(R.id.displayCapture);
        btnCapture=findViewById(R.id.btnCapture);

        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToWebsite();
            }
        });

        btnAddE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEvent();
            }
        });

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermission();
            }
        });
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},CAMERA_PERM_CODE);
        }
        else
            CapturePicture(); 
    }

    private void CapturePicture() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera,CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== CAMERA_REQUEST_CODE){
            Bitmap image =(Bitmap) data.getExtras().get("data");
            iMage.setImageBitmap(image);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CapturePicture();
            } else {
                Toast.makeText(this, "Camera Permission is required to use camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void AddEvent() {
        boolean C_allday;
        if (checkallday.isChecked()) C_allday=true;
        else C_allday=false;
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, tittle.getText().toString())
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText().toString())
                .putExtra(CalendarContract.Events.DESCRIPTION,des.getText().toString())
                .putExtra(CalendarContract.Events.ALL_DAY, C_allday);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void GoToWebsite() {
        String uriText = textWeb.getText().toString();
        Uri uri = Uri.parse(uriText);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        if (browserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(browserIntent);
        }else {
            Toast.makeText(MainActivity.this,"No Website",Toast.LENGTH_SHORT).show();
        }
    }



}