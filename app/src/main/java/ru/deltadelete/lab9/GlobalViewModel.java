package ru.deltadelete.lab9;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GlobalViewModel extends AndroidViewModel {
    private FileChangedListener fileChangedListener;
    private Boolean writePermission;
    private String text;
    private Set<String> files;
    private String file;

    public GlobalViewModel(@NonNull Application application) {
        super(application);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
        this.fileChangedListener.call(file);
        Log.d(GlobalViewModel.class.getName(), String.format("File changed to %s", file));
    }

    public void setOnFileChangedListener(FileChangedListener fileChangedListener) {
        this.fileChangedListener = fileChangedListener;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        if (text == null) {
            text = readFile(getApplication().getApplicationContext(), getFile());
        }
        return text;
    }

    public Set<String> getFiles() {
        if (this.files == null) {
            SharedPreferences prefs = getApplication().getSharedPreferences(
                    "Lab9",
                    Context.MODE_PRIVATE
            );
            this.files = prefs.getStringSet("files", new HashSet<String>());
        }
        return this.files;
    }

    public void setFiles(Set<String> files) {
        this.files = files;
        SharedPreferences prefs = getApplication().getSharedPreferences(
                "Lab9",
                Context.MODE_PRIVATE
        );
        SharedPreferences.Editor edit = prefs.edit();
        edit.putStringSet("files", new HashSet<String>(files));
        edit.apply();
    }

    public void addFile(String file) {
        Set<String> files = getFiles();
        files.add(file);
        setFiles(files);
    }

    public interface FileChangedListener {
        public void call(String file);
    }

    public static String readFile(Context context, String filename) {
        if (filename.length() == 0) {
            return "";
        }

        File file = new File(context.getFilesDir(), filename);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String text = reader.lines().collect(Collectors.joining());
            reader.close();
            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void writeFile(Context context, String text, String fileName) {
        if (fileName == null || fileName.length() == 0) {
            return;
        }
        File file = new File(context.getFilesDir(), fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream stream = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(text);
            writer.flush();
            writer.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
