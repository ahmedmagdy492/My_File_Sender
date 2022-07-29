package com.magdyradwan.my_file_sender;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.magdyradwan.my_file_sender.adapters.FileAdapter;
import com.magdyradwan.my_file_sender.dto.UploadFileDTO;
import com.magdyradwan.my_file_sender.helpers.FileReaderHelper;
import com.magdyradwan.my_file_sender.helpers.HttpClient;
import com.magdyradwan.my_file_sender.helpers.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private GridView files;
    private FloatingActionButton btnSend;
    private FloatingActionButton btnAdd;
    private FloatingActionButton btnMenu;
    private boolean clicked = false;
    private ArrayList<String> allFiles = new ArrayList<>();

    private ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            result ->{
                if(result != null) {
                    allFiles.add(result.toString());
                    files.setAdapter(new FileAdapter(this, R.layout.file_item, allFiles));
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        files = findViewById(R.id.files);
        btnMenu = findViewById(R.id.menu);
        btnSend = findViewById(R.id.btnSend);
        btnAdd = findViewById(R.id.upload_file);

        btnMenu.setOnClickListener(v -> {
            setVisiablity(clicked);
            clicked = !clicked;
        });

        btnAdd.setOnClickListener(v -> {
            mGetContent.launch("*/*");
        });

        btnSend.setOnClickListener(v -> {
            if(allFiles.size() > 3) {
                Toast.makeText(this, "Maximum number of files to send is 3", Toast.LENGTH_SHORT).show();
            }
            else if(allFiles.size() > 0) {
                scanQR();
            }
            else {
                Toast.makeText(this, "Please Select Files First", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);
        if(result.getContents() != null) {
            String url = result.getContents();
            HttpClient httpClient = new HttpClient();
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            // TODO: send http request with the files in the files array
            for(String path: allFiles) {
                executorService.execute(() -> {
                    try {
                        byte[] bytes = FileReaderHelper.readUri(this, Uri.parse(path));
                        UploadFileDTO uploadFileDTO = new UploadFileDTO("",
                                Utility.convertFromByteArrToBase64(bytes), getContentResolver().getType(Uri.parse(path)));

                        String response = httpClient.postRequest(url, uploadFileDTO.convertToJson());
                        if(response.contains("true")) {
                            runOnUiThread(() -> {
                                Toast.makeText(MainActivity.this, "Files has been uploaded successfully", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } 
                    catch (IOException e) {
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            }
        }
    }

    private void setVisiablity(boolean value) {
        if(!value) {
            btnSend.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.VISIBLE);
            btnMenu.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_baseline_close_24));
        }
        else {
            btnSend.setVisibility(View.INVISIBLE);
            btnAdd.setVisibility(View.INVISIBLE);
            btnMenu.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.ic_baseline_menu_24));
        }
    }

    private void scanQR() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setPrompt("Scan QR Code to Send");
        intentIntegrator.setCameraId(0);

        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();
    }
}