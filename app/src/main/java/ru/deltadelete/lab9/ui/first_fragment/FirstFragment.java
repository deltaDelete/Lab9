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
import java.util.List;
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
        initGrid();
    }

    private void initGrid() {
        ArrayAdapter<String> adapter = globalViewModel.getAdapter(requireContext());
        binding.gridview.setAdapter(adapter);
        binding.gridview.setOnItemClickListener((parent, view, position, id) -> {
            String file = adapter.getItem(position);
            globalViewModel.setFile(file);
            globalViewModel.setText(null);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.nav_host_fragment_content_main,
                            EditTextFragment.newInstance(), "edit_text_fragment"
                    )
                    .addToBackStack("edit_text_fragment")
                    .commit();
        });
    }

    private void loadFilesClick(View view) {
        Context context = requireContext();

        File[] files = globalViewModel.getFiles();
        String[] fileNames = Arrays.stream(files)
                .filter(File::isFile)
                .map(File::getName)
                .toArray(String[]::new);

        AtomicReference<String> fileToOpen = new AtomicReference<>("");

        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(
                context,
                com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        )
                .setTitle(R.string.open_previous);

        if (fileNames.length == 0) {
            dialogBuilder = dialogBuilder
                    .setPositiveButton(R.string.close, (dialog, which) -> dialog.dismiss())
                    .setMessage(R.string.nothing);
        } else {
            dialogBuilder = dialogBuilder
                    .setPositiveButton(R.string.open, (dialogInterface, i) -> {
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
                        globalViewModel.setText(
                                GlobalViewModel.readFile(requireContext(), fileToOpen.get())
                        );
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
        field.setHint(R.string.filename);

        EditText edit = new EditText(context);
        field.addView(edit);

        AlertDialog dialog = new MaterialAlertDialogBuilder(
                context,
                com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        )
                .setTitle(R.string.new_filename)
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