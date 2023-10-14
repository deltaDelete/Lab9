package ru.deltadelete.lab9;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.stream.Collectors;

import ru.deltadelete.lab9.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private GlobalViewModel globalViewModel;

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

        globalViewModel.setOnFileChangedListener(this::readFile);
    }

    private void readFile(Uri fileUri) {
        if (fileUri == null) {
            return;
        }
        File file = new File(fileUri.getPath());
//        if (!file.canRead()) {
//            return;
//        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String text = reader.lines().collect(Collectors.joining());
            binding.textInput.setText(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}