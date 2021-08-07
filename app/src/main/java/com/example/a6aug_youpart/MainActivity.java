package com.example.a6aug_youpart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.PrecomputedText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;

import javax.xml.transform.Result;

public class MainActivity extends AppCompatActivity {
    private Button mBtnSaveData ;
    private EditText mEtData;
    private TextView mTvSavedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initviews();
    }

    private void initviews() {
        mBtnSaveData = findViewById(R.id.btnSaveData);
        mTvSavedData = findViewById(R.id.tvSavedData);
        mEtData = findViewById(R.id.etData);

        mBtnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = mEtData.getText().toString();
                AsyncTaskSaveFile saveFile = new AsyncTaskSaveFile(data,getApplicationContext());
                saveFile.execute();
            }
        });
    }
    private class AsyncTaskSaveFile extends AsyncTask<Void,Void,Void>{

        private String data;
        private Context context;

        public AsyncTaskSaveFile(String data, Context context) {
            this.data = data;
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter
                        (context.openFileOutput("data.txt",Context.MODE_PRIVATE));
                outputStreamWriter.write(data);
                outputStreamWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            String savedData = "";
            Toast.makeText(context,"Your File Saved",Toast.LENGTH_SHORT).show();

            try {
                InputStream inputStream = context.openFileInput("data.txt");

                if (inputStream!= null){
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String dataReceived = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((dataReceived = bufferedReader.readLine()) != null){
                        stringBuilder.append("\n").append(dataReceived);
                    }

                    inputStream.close();
                    data  = stringBuilder.toString();
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(context,"File not found"+e.toString(),Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mTvSavedData.setText(data);
        }
    }
}