package com.example.projectn12.adapter;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectn12.R;
import com.example.projectn12.fragment.product.AddProduct;
import com.example.projectn12.fragment.shopping.ProductDetailsFragment;
import com.example.projectn12.models.Product;
import com.example.projectn12.models.serProduct;

import java.text.DecimalFormat;
import java.util.List;

public class ArrProduct  extends RecyclerView.Adapter<ArrProduct.ArrProductViewHolder> {

    Context context;
    private List<Product> list;
    private FragmentManager fragmentManager;

    public ArrProduct(Context context, List<Product> list , FragmentManager fragmentManager) {
        this.context = context;
        this.list = list;
        this.fragmentManager = fragmentManager;

    }

    @NonNull
    @Override
    public ArrProduct.ArrProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArrProduct.ArrProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.billing_products_arr_item1,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArrProduct.ArrProductViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(list.get(position).getImages().get(0)).into(holder.newImg);
        holder.newName.setText (list.get(position).getName());
        holder.newPrice.setText (String.valueOf(list.get(position).getPrice()));
        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProduct fragment = new AddProduct();
                AddProduct.check = true ;
                AddProduct.updateProduct = list.get(position);
                fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.shoppingHostFragment, fragment);
                ft.addToBackStack(null).commit();

            }
        });

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serProduct a = new serProduct();
                a.setNewProduct(list.get(position));
                a.delProduct(context);
                list.remove(position);
                ArrProduct.this.notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ArrProductViewHolder extends RecyclerView.ViewHolder {
        ImageView newImg;
        TextView newName, newPrice;
        Button btnDel ,  btnUpdate;
        public ArrProductViewHolder(@NonNull View itemView) {
            super(itemView);
            newImg= itemView.findViewById(R.id.imageCartProduct);
            newName=itemView.findViewById(R.id.tvProductCartName);
            newPrice=itemView.findViewById(R.id.tvProductCartPrice);
            btnDel=itemView.findViewById(R.id.delete);
            btnUpdate=itemView.findViewById(R.id.update);
        }
    }
}
