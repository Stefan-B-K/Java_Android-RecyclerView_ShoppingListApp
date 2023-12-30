package com.istef.shoppinglist;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.istef.shoppinglist.data.DBManager;
import com.istef.shoppinglist.databinding.ActivityMainBinding;
import com.istef.shoppinglist.util.Config;

import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> activityResultLauncher = Config.resultFromDialogAddEdit(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.istef.shoppinglist.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.toolbar
                .getOverflowIcon()
                .setColorFilter(getResources().getColor(R.color.white),
                        PorterDuff.Mode.SRC_ATOP);

        binding.fab.setOnClickListener(view -> Config.showDialogAddEdit(this, 0, null, null, activityResultLauncher));

        try (DBManager dbManager = new DBManager(this)) {
            if (dbManager.itemCount() > 0) {
                startActivity(new Intent(this, ListActivity.class));
                finish();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}