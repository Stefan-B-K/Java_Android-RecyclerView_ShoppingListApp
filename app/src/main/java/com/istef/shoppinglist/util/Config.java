package com.istef.shoppinglist.util;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.istef.shoppinglist.ListActivity;
import com.istef.shoppinglist.R;
import com.istef.shoppinglist.adapter.ListRecViewAdapter;
import com.istef.shoppinglist.data.DBManager;
import com.istef.shoppinglist.model.Item;


public class Config {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "shopping_list";
    public static final String DB_TABLE_NAME = "shopping_items";

    public static final String KEY_ID = "id";
    public static final String KEY_ITEM = "item";
    public static final String KEY_SIZE = "size";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_DATE_ADDED = "date_added";

    public static final int SHOW_LIST = 200;


    public static <T extends AppCompatActivity> void showDialogAddEdit(T context,
                                                                       int itemIndex,
                                                                       @Nullable RecyclerView.Adapter<ListRecViewAdapter.ViewHolder> adapter,
                                                                       @Nullable final Item itemToEdit,
                                                                       ActivityResultLauncher<Intent> activityResultLauncher) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_add_edit, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        EditText inputItem = view.findViewById(R.id.inputNewItem);
        EditText inputSize = view.findViewById(R.id.inputSize);
        EditText inputQuantity = view.findViewById(R.id.inputQuantity);
        Button btnSave = view.findViewById(R.id.buttonSave);

        Item item = itemToEdit == null ? new Item() : itemToEdit;

        if (itemToEdit != null) {
            btnSave.setText(R.string.update);
            inputItem.setText(itemToEdit.getItem());
            inputSize.setText(String.valueOf(itemToEdit.getSize()));
            inputQuantity.setText(String.valueOf(itemToEdit.getQuantity()));
        }

        alertDialog.show();

        btnSave.setOnClickListener(v -> {

            if (inputItem.getText().toString().trim().isEmpty()
                    || inputSize.getText().toString().trim().isEmpty()
                    || inputQuantity.getText().toString().trim().isEmpty()) {
                Snackbar.make(view, "Empty fields not allowed", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED).show();
                return;
            }

            try (DBManager dbManager = new DBManager(context)) {
                item.setItem(inputItem.getText().toString().trim());
                item.setSize(Integer.parseInt(inputSize.getText().toString().trim()));
                item.setQuantity(Integer.parseInt(inputQuantity.getText().toString().trim()));

                if (itemToEdit != null) {
                    dbManager.updateItem(item);
                    if (adapter != null) adapter.notifyItemChanged(itemIndex, item);
                    alertDialog.dismiss();
                } else {
                    dbManager.addItem(item);
                    new Handler().postDelayed(() -> {
                        alertDialog.dismiss();
                        activityResultLauncher.launch(new Intent(context, ListActivity.class));
                        context.finish();
                    }, 200);
                }
            }
        });

    }

    public static <T extends AppCompatActivity> ActivityResultLauncher<Intent> resultFromDialogAddEdit(T context) {
        return context.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
//                Intent data = result.getData();
//                String message = data.getStringExtra("message");
//                if (result.getResultCode() == SHOW_LIST) {
//                }
                });
    }

    public static <T extends AppCompatActivity> ActivityResultLauncher<Intent> resultFromDialogAConfirm(T context) {
        return context.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
//                Intent data = result.getData();
//                String message = data.getStringExtra("message");
//                if (result.getResultCode() == SHOW_LIST) {
//                }
                });
    }


}