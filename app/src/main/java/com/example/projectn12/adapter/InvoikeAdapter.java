package com.example.projectn12.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectn12.R;
import com.example.projectn12.models.CartProduct;
import com.example.projectn12.models.Item;


import java.util.List;

public class InvoikeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Item> items;

    public InvoikeAdapter(List<Item> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==0){
            return new DateInvoike(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.date_item,
                            parent,
                            false
                    )
            );
        } else if (viewType==1) {
            return new BillInvoike(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.total_bill,
                            parent,
                            false
                    )
            );

        } else{
            return new ProductHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.billing_products_rv_item1,
                            parent,
                            false
                    ),
                    parent.getContext()
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            String trip = (String) items.get(position).getObject();
            ((DateInvoike) holder).setDate(trip);
        } else if (getItemViewType(position) == 1) {
            String trip = (String) items.get(position).getObject();
            ((BillInvoike) holder).setDate(trip);

        } else {
            CartProduct ads = (CartProduct) items.get(position).getObject();
            ((ProductHolder) holder).setProduct(ads);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    static class ProductHolder extends RecyclerView.ViewHolder{
        private TextView name, price, quantity;
        private ImageView image;
        private Context context;
        public ProductHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            name = itemView.findViewById(R.id.tvProductCartName);
            price = itemView.findViewById(R.id.tvProductCartPrice);
            quantity = itemView.findViewById(R.id.tvBillingProductQuantity);
            image = itemView.findViewById(R.id.imageCartProduct);
        }
        void setProduct(CartProduct product){
            Glide.with(context).load(product.getImages()).into(image);
            name.setText(product.getProductName());
            price.setText(String.valueOf(product.getProductPrice()));
            quantity.setText(String.valueOf(product.getTotalQuantity()));
        }
    }
    static class DateInvoike extends RecyclerView.ViewHolder{
        private TextView date;
        public DateInvoike(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.textDate);
        }
        void setDate(String t){
            date.setText(t);
        }
    }
    static class BillInvoike extends RecyclerView.ViewHolder{
        private TextView bill;
        public BillInvoike(@NonNull View itemView) {
            super(itemView);
            bill=itemView.findViewById(R.id.textBill);
        }
        void setDate(String t){
            bill.setText(t);
        }
    }
}
