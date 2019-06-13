package com.mrlonewolfer.basicexternalmemorystorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int My_PERMISSION_REQUEST_FOR_WRITE_EXTERNAL =1 ;
    private static final String My_FOLDER_NAME = "StorageFolder";
    private static final String MY_FILE_Name ="SaveMeFIle" ;
    Button btnWrite,btnRead;
    EditText edtMsg;
    TextView txtResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRead=findViewById(R.id.btnRead);
        btnWrite=findViewById(R.id.btnWrite);
        edtMsg=findViewById(R.id.edtMsg);
        txtResult=findViewById(R.id.txtResult);

        btnRead.setOnClickListener(this);
        btnWrite.setOnClickListener(this);

        checkStoragePermision();
    }

    private void checkStoragePermision() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]
                                {
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE

                                }, My_PERMISSION_REQUEST_FOR_WRITE_EXTERNAL);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==My_PERMISSION_REQUEST_FOR_WRITE_EXTERNAL){
            if(grantResults.length>0
                && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this, "Thank You", Toast.LENGTH_SHORT).show();
            }
            else{

                btnWrite.setVisibility(View.GONE);
                btnRead.setVisibility(View.GONE);
            }


        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.btnWrite){
            
           WriteDataToPublicMemory();
        }
        if(v.getId()==R.id.btnRead){
            readDataFromPublicMemory();
        }
    }

    private void readDataFromPublicMemory() {
        File myFolder = Environment.getExternalStorageDirectory();
        myFolder= new File(myFolder,My_FOLDER_NAME);
        if(!myFolder.exists()){

            Toast.makeText(this, "Directory is Not Availabele", Toast.LENGTH_SHORT).show();
            return;
        }
        //After Checking Folder it will visit FIle
        myFolder=new File(myFolder,MY_FILE_Name);


        if(!myFolder.exists()){

            Toast.makeText(this, "Their is nothing To Display", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            FileInputStream fileInputStream= new FileInputStream(myFolder);
            byte b[]= new byte[fileInputStream.available()];
            fileInputStream.read(b);
            String msg=new String(b);
            txtResult.setText(msg);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void WriteDataToPublicMemory() {
        File myFolder= Environment.getExternalStorageDirectory();
        //Create folder first
        myFolder=new File(myFolder,My_FOLDER_NAME);
        if(!myFolder.exists()){

            if(myFolder.mkdir()){
                Toast.makeText(this, "Folder Created Succesfully", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Folder is Not Created Due to Some Reason", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        //After Creating  Folder Create File
        myFolder=new File(myFolder,MY_FILE_Name);
        String msg=edtMsg.getText().toString();

        try {
            FileOutputStream fileOutputStream= new FileOutputStream(myFolder);
            fileOutputStream.write(msg.getBytes());
            fileOutputStream.close();
            Toast.makeText(this, "Your Message Save Succesfully", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        edtMsg.setText("");
    }
}
