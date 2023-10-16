package ru.deltadelete.lab9.ui.first_fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.stream.Collectors;

import ru.deltadelete.lab9.GlobalViewModel;
import ru.deltadelete.lab9.R;
import ru.deltadelete.lab9.databinding.FragmentFirstBinding;
import ru.deltadelete.lab9.ui.edit_text_fragment.EditTextFragment;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private GlobalViewModel globalViewModel;

//    private final ActivityResultLauncher<String> fileSave =
//            registerForActivityResult(new SaveFile(), o -> {
//                globalViewModel.setFile(o);
//                writeFile(binding.textInput.getText().toString(), o);
//            });

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        globalViewModel = new ViewModelProvider(requireActivity()).get(GlobalViewModel.class);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.newFileButton.setOnClickListener(view1 -> {
            Context context = requireContext();
            TextInputLayout field = new TextInputLayout(context);
            field.setPadding(16, 16, 16, 16);
            field.setHint("Название файла");

            EditText edit = new EditText(context);
            field.addView(edit);

            androidx.appcompat.app.AlertDialog dialog = new MaterialAlertDialogBuilder(context,
                    com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered)
                    .setTitle("Введите имя нового файла")
                    .setPositiveButton("Создать", (dialogInterface, i) -> {
                        requireActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.nav_host_fragment_content_main,
                                        EditTextFragment.newInstance(), "edit_text_fragment")
                                .addToBackStack("edit_text_fragment")
                                .commit();
                    })
                    .setView(field)
                    .create();

            dialog.show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}