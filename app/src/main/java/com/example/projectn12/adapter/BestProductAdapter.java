package com.example.projectn12.adapter;



import static androidx.navigation.fragment.NavHostFragment.findNavController;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectn12.R;
import com.example.projectn12.fragment.shopping.ProductDetailsFragment;
import com.example.projectn12.models.Product;

import java.util.List;

public class BestProductAdapter extends RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder> {
     Context context;
    private List<Product> list;
    private FragmentManager fragmentManager;

    public BestProductAdapter(Context context, List<Product> list, FragmentManager fragmentManager) {
        this.context = context;
        this.list = list;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public BestProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BestProductAdapter.BestProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_rv_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BestProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (!list.get(position).getImages().isEmpty()) {
            Glide.with(context).load(list.get(position).getImages().get(0)).into(holder.newImg);
        }
        else{
            Glide.with(context).load("https://firebasestorage.googleapis.com/v0/b/projectn12.appspot.com/o/images%2F0ed6e2cf-a7d6-4ee6-8b43-209022fc164e?alt=media&token=a8d84580-08b1-48e3-80fa-2e9acb42bf06").into(holder.newImg);
        }
        //Glide.with(context).load(list.get(position).getImages().get(0)).into(holder.newImg);
        holder.newName.setText (list.get(position).getName());
        holder.newPrice.setText (String.valueOf(list.get(position).getPrice()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("detailed", product);
                ProductDetailsFragment fragment = new ProductDetailsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft= fragmentManager.beginTransaction();
                ft.replace(R.id.shoppingHostFragment, fragment);
                ft.addToBackStack(null).commit();;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BestProductViewHolder extends RecyclerView.ViewHolder {
        ImageView newImg;
        TextView newName, newPrice;
        public BestProductViewHolder(@NonNull View itemView) {
            super(itemView);
            newImg= itemView.findViewById(R.id.img_product);
            newName=itemView.findViewById(R.id.tv_name);
            newPrice=itemView.findViewById(R.id.tv_price);
        }
    }
}
