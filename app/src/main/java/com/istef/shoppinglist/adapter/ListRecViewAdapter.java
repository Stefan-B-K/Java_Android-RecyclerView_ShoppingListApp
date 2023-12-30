package com.istef.shoppinglist.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.istef.shoppinglist.MainActivity;
import com.istef.shoppinglist.R;
import com.istef.shoppinglist.data.DBManager;
import com.istef.shoppinglist.model.Item;
import com.istef.shoppinglist.util.Config;

import java.util.List;

public class ListRecViewAdapter extends RecyclerView.Adapter<ListRecViewAdapter.ViewHolder> {

    private final Context context;
    private final List<Item> itemList;
    private final ActivityResultLauncher<Intent> confirmResultLauncher;
    private final ActivityResultLauncher<Intent> editResultLauncher;

    public ListRecViewAdapter(Context context,
                              List<Item> itemList,
                              ActivityResultLauncher<Intent> editResultLauncher,
                              ActivityResultLauncher<Intent> confirmResultLauncher) {
        this.context = context;
        this.itemList = itemList;
        this.editResultLauncher = editResultLauncher;
        this.confirmResultLauncher = confirmResultLauncher;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.item.setText(item.getItem());
        holder.size.setText(String.valueOf(item.getSize()));
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        holder.dateAdded.setText(item.getDateAdded());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item;
        public TextView size;
        public TextView quantity;
        public TextView dateAdded;
        public long id;
        public ImageButton btnEdit;
        public ImageButton btnDelete;

        private AlertDialog alertDialog;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.txtItem);
            size = itemView.findViewById(R.id.txtSize);
            quantity = itemView.findViewById(R.id.txtQty);
            dateAdded = itemView.findViewById(R.id.txtDateAdded);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnEdit.setOnClickListener(v -> {
                        int itemIndex = getAdapterPosition();
                        Config.showDialogAddEdit(
                                (AppCompatActivity) context,
                                itemIndex,
                                ListRecViewAdapter.this,
                                itemList.get(itemIndex),
                                editResultLauncher);
                    });

            btnDelete.setOnClickListener(v -> showDialogConfirm());

        }

        private void showDialogConfirm() {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
            builder.setView(view);
            alertDialog = builder.create();

            Button btnYes = view.findViewById(R.id.btnYes);
            Button btnNo = view.findViewById(R.id.btnNo);

            btnYes.setOnClickListener(v -> deleteItem());
            btnNo.setOnClickListener(v -> alertDialog.dismiss());

            alertDialog.show();
        }

        private void deleteItem() {
            {
                try (DBManager dbManager = new DBManager(context)) {
                    int itemIndex = getAdapterPosition();
                    dbManager.deleteItem(itemList.get(itemIndex));
                    itemList.remove(itemIndex);
                    notifyItemRemoved(itemIndex);

                    if (dbManager.itemCount() > 0) {
                        alertDialog.dismiss();
                    } else {
                        new Handler().postDelayed(() -> {
                            alertDialog.dismiss();
                            confirmResultLauncher.launch(new Intent(context, MainActivity.class));
                            ((Activity) context).finish();
                        }, 200);
                    }

                }

            }
        }

    }


}