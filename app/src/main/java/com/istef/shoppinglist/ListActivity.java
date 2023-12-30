package com.istef.shoppinglist;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.istef.shoppinglist.adapter.ListRecViewAdapter;
import com.istef.shoppinglist.data.DBManager;
import com.istef.shoppinglist.model.Item;
import com.istef.shoppinglist.util.Config;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    private final ActivityResultLauncher<Intent> addEditResultLauncher = Config.resultFromDialogAddEdit(this);
    private final ActivityResultLauncher<Intent> confirmResultLauncher = Config.resultFromDialogAConfirm(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try (DBManager dbManager = new DBManager(this)) {

            List<Item> itemList = dbManager.getAllItems();
            ListRecViewAdapter listRecViewAdapter = new ListRecViewAdapter(this, itemList, addEditResultLauncher, confirmResultLauncher);
            listRecViewAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(listRecViewAdapter);

        }

        FloatingActionButton fab = findViewById(R.id.fab_list);
        fab.setOnClickListener(v -> Config.showDialogAddEdit(this, 0, null, null, addEditResultLauncher));
    }

}