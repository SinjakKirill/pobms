package com.example.sinyakkirill.lab_1_files_aes;

import android.app.ListActivity;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class FoldersFilesActivity extends ListActivity {

    private List<String> item = null;
    private List<String> path = null;
    private String root;
    private TextView myPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders_files);
        /////
        FileOutputStream fOut = null;
        //Since you are creating a subdirectory, you need to make sure it's there first
        File directory = new File(Environment.getExternalStorageDirectory(), "AutoWriter");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            //Create the stream pointing at the file location
            fOut = new FileOutputStream(new File(directory, "samplefile.txt"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String nochartOutput = Cryptography.AES("See my collection!");

        OutputStreamWriter osw = new OutputStreamWriter(fOut);
        try {
            osw.write(nochartOutput);

            osw.flush();
            osw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        ////
        myPath = (TextView)findViewById(R.id.path);

        root = Environment.getExternalStorageDirectory().getPath();

        getDir(root);
    }

    private void getDir(String dirPath)
    {
        myPath.setText("Location: " + dirPath);
        item = new ArrayList<String>();
        path = new ArrayList<String>();
        File f = new File(dirPath);
        File[] files = f.listFiles();

        if(!dirPath.equals(root))
        {
            item.add(root);
            path.add(root);
            item.add("../");
            path.add(f.getParent());
        }

        for(int i=0; i < files.length; i++)
        {
            File file = files[i];

            if(!file.isHidden() && file.canRead()){
                path.add(file.getPath());
                if(file.isDirectory()){
                    item.add(file.getName() + "/");
                }else{
                    item.add(file.getName());
                }
            }
        }

        ArrayAdapter<String> fileList =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, item);
        setListAdapter(fileList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        File file = new File(path.get(position));

        if (file.isDirectory())
        {
            if(file.canRead()){
                getDir(path.get(position));
            }else{
                new AlertDialog.Builder(this)
                        .setTitle("[" + file.getName() + "] folder can't be read!")
                        .setPositiveButton("OK", null).show();
            }
        }else {
            File _file = new File(Environment.getExternalStorageDirectory(),"AutoWriter/samplefile.txt");
            byte[] arrByte = new byte[(int)_file.length()];
            try {

                new FileInputStream(_file).read(arrByte);
                Toast.makeText(getApplicationContext(), new String(arrByte), Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            new AlertDialog.Builder(this)
                    .setTitle("[" + new String(arrByte) + "]")
                    .setPositiveButton("OK", null).show();

        }

    }
}
