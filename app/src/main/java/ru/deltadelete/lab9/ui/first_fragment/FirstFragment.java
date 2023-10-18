package ru.deltadelete.lab9.ui.first_fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
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

        binding.newFileButton.setOnClickListener(this::newFileClick);
        binding.loadFilesButton.setOnClickListener(this::loadFilesClick);
    }

    private void loadFilesClick(View view) {
        Context context = requireContext();

        File[] files = globalViewModel.getFiles();
        String[] fileNames = Arrays.stream(files)
                .filter(File::isFile)
                .map(File::getName)
                .toArray(String[]::new);

        AtomicReference<String> fileToOpen = new AtomicReference<>("");

//        ContextThemeWrapper themeContext = new ContextThemeWrapper(context,
//                com.google.android.material.R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox_ExposedDropdownMenu);
//
//        TextInputLayout field = new TextInputLayout(themeContext);
//        field.setPadding(16, 16, 16, 16);
//        field.setHint("Название файла");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(themeContext,
//                android.R.layout.simple_dropdown_item_1line, fileNames);
//        MaterialAutoCompleteTextView edit = new MaterialAutoCompleteTextView(themeContext);
//        edit.setAdapter(adapter);
//        edit.setInputType(0);
//        edit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                fileToOpen.set(fileNames[i]);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                fileToOpen.set("");
//            }
//        });
//
//        field.addView(edit);


        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(
                context,
                com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        )
                .setTitle("Открыть предыдущий файл");

        if (fileNames.length == 0) {
            dialogBuilder = dialogBuilder
                    .setPositiveButton("Закрыть", (dialog, which) -> dialog.dismiss())
                    .setMessage("Здесь ничего нет");
        } else {
            dialogBuilder = dialogBuilder
                .setPositiveButton("Создать", (dialogInterface, i) -> {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.nav_host_fragment_content_main,
                                EditTextFragment.newInstance(), "edit_text_fragment"
                        )
                        .addToBackStack("edit_text_fragment")
                        .commit();
            })
                    .setSingleChoiceItems(fileNames, 0, (dialogInterface, which) -> {
                        fileToOpen.set(fileNames[which]);
                        globalViewModel.setFile(fileToOpen.get());
                    });
        }

        dialogBuilder.create().show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void newFileClick(View view) {
        Context context = requireContext();
        TextInputLayout field = new TextInputLayout(context);
        field.setPadding(16, 16, 16, 16);
        field.setHint("Название файла");

        EditText edit = new EditText(context);
        field.addView(edit);

        AlertDialog dialog = new MaterialAlertDialogBuilder(
                context,
                com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        )
                .setTitle("Введите имя нового файла")
                .setPositiveButton(R.string.open, (dialogInterface, i) -> {
                    globalViewModel.setText("");
                    globalViewModel.setFile(edit.getText().toString());
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.nav_host_fragment_content_main,
                                    EditTextFragment.newInstance(), "edit_text_fragment"
                            )
                            .addToBackStack("edit_text_fragment")
                            .commit();
                })
                .setView(field)
                .create();

        dialog.show();
    }
}