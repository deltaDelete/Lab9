package ru.deltadelete.lab9;

import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ru.deltadelete.lab9.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 228123;
    private static final int READ_PERMISSION_REQUEST_CODE = 2;
    private static final int WRITE_PERMISSION_REQUEST_CODE = 1;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private GlobalViewModel globalViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setTitle(R.string.app_name);
        setSupportActionBar(binding.toolbar);

        NavHostFragment navHostFragment =
                (NavHostFragment) (getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main));
        NavController navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        globalViewModel = new ViewModelProvider(this).get(GlobalViewModel.class);

        binding.fab.setOnClickListener((view) -> {
            EditText edit = new EditText(this);
            new MaterialAlertDialogBuilder(this, com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered)
                    .setTitle("Введите имя файла")
                    .setView(edit)
                    .setPositiveButton("Выбрать", (dialogInterface, i) -> {
                        globalViewModel.setFile(edit.getText().toString());
                    })
                    .create()
                    .show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setFile(String file) {
        globalViewModel.setFile(file);
    }
}