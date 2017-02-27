package com.example.sinyakkirill.lab_1_files_aes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;

import static android.provider.ContactsContract.Intents.Insert.NOTES;

public class MainActivity extends AppCompatActivity {

    private final static String NOTES = "notes.txt";

    EditText textEditText;
    EditText transferEditText;
    Button translateButton;
    Button addWordButton;
    Button closeAppButton;
    Button nextPageButtom;
    ListView dictionaryListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textEditText = (EditText) findViewById(R.id.textEditText);
        transferEditText = (EditText) findViewById(R.id.transferEditText);

        translateButton = (Button) findViewById(R.id.btnTranslate);
        addWordButton = (Button) findViewById(R.id.btnAddWord);
        closeAppButton = (Button) findViewById(R.id.btnCloseApp);
        nextPageButtom = (Button) findViewById(R.id.btnNextPage);

        dictionaryListView = (ListView) findViewById(R.id.dictionaryListView);

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTransfer(textEditText, transferEditText, readToFile(NOTES));
            }
        });



        nextPageButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FoldersFilesActivity.class);
                startActivity(intent);
            }
        });

        addWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!textEditText.getText().toString().equals("") && !transferEditText.getText().toString().equals("")){
                    writeToFile(NOTES, textEditText.getText().toString() +
                    ":" + transferEditText.getText().toString());
                    Toast.makeText(getApplicationContext(), "Word added!", Toast.LENGTH_SHORT).show();
                    textEditText.setText("");
                    transferEditText.setText("");
                }
                else
                    Toast.makeText(getApplicationContext(), "Not enter word or transfer!", Toast.LENGTH_SHORT).show();
            }
        });

        closeAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void writeToFile(String path, String data){
        try {
            FileOutputStream mOutput = openFileOutput(NOTES, Activity.MODE_PRIVATE);
            mOutput.write(data.getBytes());
            mOutput.close();
        } catch (FileNotFoundException ea) {
            ea.printStackTrace();
        } catch (IOException ee) {
            ee.printStackTrace();
        }

    }

    private  HashMap<String, String> readToFile(String path){
        HashMap<String, String> dictionary = new HashMap<>();
        try {
            InputStream in = openFileInput(NOTES);

            if (in != null) {
                InputStreamReader tmp = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(tmp);
                String str = "";

                while ((str = reader.readLine()) != null) {
                    dictionary.put(str.split(":")[0], str.split(":")[1]);       //разбиваем строку "date:date" на два слова
                }

                in.close();
            }
        }
        catch (Exception e) {
            // that's OK, we probably haven't created it yet

            try {
                FileOutputStream mOutput = openFileOutput(NOTES, Activity.MODE_PRIVATE);
                String data = "THIS DATA WRITTEN TO A FILE FIRST TIME!!!";
                mOutput.write(data.getBytes());
                mOutput.close();
            } catch (FileNotFoundException ea) {
                ea.printStackTrace();
            } catch (IOException ee) {
                ee.printStackTrace();
            }

        }
        return dictionary;
    }

    private void showTransfer(EditText inputEditText, EditText outputEditText, HashMap<String, String> myDictionary){
        if(inputEditText.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(), "Enter Word, please!", Toast.LENGTH_SHORT).show();
        else if(myDictionary.get(inputEditText.getText().toString()) != null){
            outputEditText.setText(myDictionary.get(inputEditText.getText().toString()));
        }
        else {
            outputEditText.setText("Not found.");
        }
    }
}
