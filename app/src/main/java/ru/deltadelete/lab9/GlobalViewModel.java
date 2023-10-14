package ru.deltadelete.lab9;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class GlobalViewModel extends AndroidViewModel {
    private FileChangedListener fileChangedListener;

    public GlobalViewModel(@NonNull Application application) {
        super(application);
    }

    private Uri file;

    public Uri getFile() {
        return file;
    }

    public void setFile(Uri file) {
        this.file = file;
        this.fileChangedListener.call(file);
        Log.d(GlobalViewModel.class.getName(), String.format("File changed to %s", file.toString()));
    }

    public void setOnFileChangedListener(FileChangedListener fileChangedListener) {
        this.fileChangedListener = fileChangedListener;
    }

    public interface FileChangedListener {
        public void call(Uri file);
    }
}
