package com.example.videoplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView myRecyclerView;
    MyAdapter obj_adapter;
    public static int REQUEST_PERMISSION = 1;
    File directory;
    boolean boolean_permission;
    public static ArrayList<File> fileArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myRecyclerView = (RecyclerView) findViewById(R.id.listVideoRecycler);

        //Phone Memory & SD Card
        directory = new File("/mnt/");

        //Phone Memory & SD Card
        //directory = new File("/storage/");
        GridLayoutManager manager = new GridLayoutManager(MainActivity.this, 2);
        myRecyclerView.setLayoutManager(manager);

        permissionForVideo();

    }

    private void permissionForVideo() {
        if ((ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))) {
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);

            }
        } else {
            boolean_permission = true;
            getFile(directory);
            obj_adapter = new MyAdapter(getApplicationContext(), fileArrayList);
            myRecyclerView.setAdapter(obj_adapter);


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                boolean_permission = true;
                getFile(directory);
                obj_adapter = new MyAdapter(getApplicationContext(), fileArrayList);
                myRecyclerView.setAdapter(obj_adapter);
            } else {
                Toast.makeText(this, "Please Allow The Permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ArrayList<File> getFile(File directory) {
        File listFile[] = directory.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    getFile(listFile[i]);
                } else {
                    boolean_permission = false;
                    if (listFile[i].getName().endsWith(".mp4")) {
                        for (int j = 0; j < fileArrayList.size(); j++) {
                            if (fileArrayList.get(j).getName().equals(listFile[i].getName())) {
                                boolean_permission = true;

                            } else {
                                fileArrayList.add(listFile[i]);
                            }
                        }
                    }
                }
            }

        }
        return fileArrayList;
    }
}




