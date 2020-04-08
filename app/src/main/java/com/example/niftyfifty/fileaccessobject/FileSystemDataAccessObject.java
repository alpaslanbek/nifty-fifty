package com.example.niftyfifty.fileaccessobject;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileSystemDataAccessObject {

    private Context context;

    public FileSystemDataAccessObject(Context context) {
        this.context = context;
    }

    public File getDirectory(String fileName) {

        File directory = null;
        if (fileName.isEmpty()) {
            directory = context.getFilesDir();
        }
        else {
            directory = context.getDir(fileName, context.MODE_PRIVATE);
        }
        return directory;
    }

    public File[] getInternalFileData(String fileName) {
        File[] files = null;
        File directory = null;
        if (fileName.isEmpty()) {
            directory = context.getFilesDir();
        }
        else {
            directory = context.getDir(fileName, context.MODE_PRIVATE);
        }
        files = directory.listFiles();
        return files;
    }

    public void writeDataToFile(String fileName, String userInfo) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(userInfo.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "FileNotFoundException: " + e.toString(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "IOException: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
