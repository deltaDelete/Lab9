package ru.deltadelete.lab9;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GlobalViewModel extends AndroidViewModel {
    private FileChangedListener fileChangedListener;
    private String text;
    private String file;
    private ArrayAdapter<String> adapter;

    public GlobalViewModel(@NonNull Application application) {
        super(application);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
        if (this.fileChangedListener != null) {
            this.fileChangedListener.call(file);
        }
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

    private final File directory = new File(
            getApplication().getApplicationContext().getFilesDir() + "/texts/"
    );
    public File[] getFiles() {
        if (!directory.exists()) {
            directory.mkdir();
        }
        return directory.listFiles();
    }

    public ArrayAdapter<String> getAdapter(Context context) {
        if (adapter == null) {
            File[] files = getFiles();
            List<String> filenames = Arrays.stream(files).map(File::getName).collect(Collectors.toList());
            adapter = new ArrayAdapter<>(context, R.layout.file_item, R.id.text1, filenames);
        }
        return adapter;
    }

    public interface FileChangedListener {
        public void call(String file);
    }

    public static String readFile(Context context, String filename) {
        if (filename == null || filename.length() == 0) {
            return "";
        }

        File file = new File(context.getFilesDir().getPath()+File.separator+"texts", filename);
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

    public void writeFile(Context context, String text, String fileName) {
        if (fileName == null || fileName.length() == 0) {
            return;
        }
        File file = new File(context.getFilesDir().getPath()+File.separator+"texts", fileName);

        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    onFileCreated(file);
                }
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

    private void onFileCreated(File file) {
        if (adapter == null) {
            return;
        }
        adapter.add(file.getName());
    }
}
