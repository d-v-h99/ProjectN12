package com.example.projectn12.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectn12.R;
import com.example.projectn12.models.Product;

import java.util.List;

public class SpecialProductsAdapter extends RecyclerView.Adapter<SpecialProductsAdapter.SpecialProductViewHolder> {
    private Context context;
    private List<Product> list;

    public SpecialProductsAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SpecialProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SpecialProductsAdapter.SpecialProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.special_rv_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialProductViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).getImages().get(0)).into(holder.newImg);
        holder.newName.setText (list.get(position).getName());
        holder.newPrice.setText (String.valueOf(list.get(position).getPrice()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SpecialProductViewHolder extends RecyclerView.ViewHolder {
        ImageView newImg;
        TextView newName, newPrice;
        Button btncart;
        public SpecialProductViewHolder(@NonNull View itemView) {
            super(itemView);
            newImg= itemView.findViewById(R.id.imageSpecialRvItem);
            newName=itemView.findViewById(R.id.tvSpecialProductName);
            newPrice=itemView.findViewById(R.id.tvSpecialPrdouctPrice);
            btncart=itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}
