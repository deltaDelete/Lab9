package ru.deltadelete.lab9;

import android.text.Editable;
import android.text.TextWatcher;

public class TextChangedListener implements TextWatcher {

    private AfterTextChangedListener afterTextChangedListener;
    private BeforeTextChangedListener beforeTextChangedListener;
    private OnTextChangedListener onTextChangedListener;

    public TextChangedListener(AfterTextChangedListener afterTextChangedListener) {
        this.afterTextChangedListener = afterTextChangedListener;
    }

    public TextChangedListener(BeforeTextChangedListener beforeTextChangedListener) {
        this.beforeTextChangedListener = beforeTextChangedListener;
    }

    public TextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
    }

    public TextChangedListener(
            AfterTextChangedListener afterTextChangedListener,
            BeforeTextChangedListener beforeTextChangedListener,
            OnTextChangedListener onTextChangedListener
    ) {
        this.beforeTextChangedListener = beforeTextChangedListener;
        this.afterTextChangedListener = afterTextChangedListener;
        this.onTextChangedListener = onTextChangedListener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (this.beforeTextChangedListener == null) {
            return;
        }
        this.beforeTextChangedListener.beforeTextChanged(charSequence, i, i1, i2);
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (this.onTextChangedListener == null) {
            return;
        }
        this.onTextChangedListener.onTextChanged(charSequence, i, i1, i2);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (this.afterTextChangedListener == null) {
            return;
        }
        this.afterTextChangedListener.afterTextChanged(editable);
    }

    public interface AfterTextChangedListener {
        public void afterTextChanged(Editable editable);
    }
    public interface BeforeTextChangedListener {
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2);
    }
    public interface OnTextChangedListener {
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2);
    }
}
