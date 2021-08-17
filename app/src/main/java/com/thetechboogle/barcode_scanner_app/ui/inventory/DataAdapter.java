package com.thetechboogle.barcode_scanner_app.ui.inventory;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thetechboogle.barcode_scanner_app.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.Inflater;

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ProductItemView> {

    private ArrayList<ProductData> productDataList;
    private Context context;

    public DataAdapter(ArrayList<ProductData> productDataList ,Context context) {
        this.productDataList = productDataList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ProductItemView onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inventory_viewholder, parent, false);
        return new ProductItemView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductItemView holder, int position) {
        ProductData item =  productDataList.get(position);
        holder.setupData(item);
    }

    @Override
    public int getItemCount() {
        return productDataList.size();
    }

    static class ProductItemView extends RecyclerView.ViewHolder{

        private TextView itemName, itemQty, itemExpiry;

        public ProductItemView(@NonNull @NotNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.ItemName);
            itemQty = itemView.findViewById(R.id.ItemQty);
            itemExpiry = itemView.findViewById(R.id.ItemExpiry);
        }

        void setupData(ProductData data){
            itemName.setText(String.format(Locale.ENGLISH, "Name: %s", data.getItemName()));
            itemQty.setText(String.format(Locale.ENGLISH,"Qty: %s",data.getItemQty()));
            itemExpiry.setText(String.format(Locale.ENGLISH, "Expiry: %s", data.getExpiryDate().toString()));
        }
    }
}
