package ru.deltadelete.lab9.ui.edit_text_fragment;

import android.content.Context;
import android.database.Observable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.stream.Collectors;

import ru.deltadelete.lab9.GlobalViewModel;
import ru.deltadelete.lab9.MainActivity;
import ru.deltadelete.lab9.R;
import ru.deltadelete.lab9.TextChangedListener;
import ru.deltadelete.lab9.databinding.FragmentEditTextBinding;

public class EditTextFragment extends Fragment {

    private FragmentEditTextBinding binding;
    private GlobalViewModel globalViewModel;
    private ActionBar actionBar;

    public EditTextFragment() {
        // Required empty public constructor
    }

    public static EditTextFragment newInstance() {
        EditTextFragment fragment = new EditTextFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditTextBinding.inflate(inflater, container, false);
        globalViewModel = new ViewModelProvider(requireActivity()).get(GlobalViewModel.class);

        actionBar = ((MainActivity)requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.text.setText(globalViewModel.getText());
        binding.text.addTextChangedListener(new TextChangedListener(editable -> {
            globalViewModel.setText(editable.toString());
        }));
        binding.saveButton.setOnClickListener(view1 -> {
            globalViewModel.writeFile(
                    requireContext(),
                    globalViewModel.getText(),
                    globalViewModel.getFile());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        actionBar.setDisplayHomeAsUpEnabled(false);
    }
}