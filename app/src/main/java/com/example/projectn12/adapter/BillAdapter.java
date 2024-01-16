package com.example.projectn12.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectn12.R;
import com.example.projectn12.models.CartProduct;

import java.text.DecimalFormat;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillHolder> {
    Context context;
    private List<CartProduct> list;
    int totalAmount = 0;

    public BillAdapter(Context context, List<CartProduct> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillAdapter.BillHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.billing_products_rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BillHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImages()).into(holder.newImg);
        holder.newName.setText(list.get(position).getProductName());
        holder.newPrice.setText(chuyenDoiTien(String.valueOf((int) (Float.valueOf(list.get(position).getProductPrice()) * 1))));
        holder.quantity.setText(String.valueOf(list.get(position).getTotalQuantity()));
        float quantity = Float.parseFloat(list.get(position).getTotalQuantity().toString());
        Integer price = list.get(position).getProductPrice();
        totalAmount += (int) (quantity * price);
        Intent intent = new Intent("MyTotalAmountBill");
        intent.putExtra("totalAmountBill", chuyenDoiTien(String.valueOf(totalAmount)));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BillHolder extends RecyclerView.ViewHolder {
        ImageView newImg;
        TextView newName, newPrice, quantity;
        public BillHolder(@NonNull View itemView) {
            super(itemView);
            newImg = itemView.findViewById(R.id.imageCartProduct);
            newName = itemView.findViewById(R.id.tvProductCartName);
            newPrice = itemView.findViewById(R.id.tvProductCartPrice);
            quantity = itemView.findViewById(R.id.tvBillingProductQuantity);
        }
    }
    String chuyenDoiTien(String tien) {
        float amount = Float.parseFloat(tien);
        DecimalFormat decimalFormat = new DecimalFormat("#,### VND");
        String formattedAmount = decimalFormat.format(amount);
        return formattedAmount;
    }
}
