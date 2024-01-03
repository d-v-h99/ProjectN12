package com.example.projectn12.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.projectn12.R;

import java.util.List;

public class ViewPager2Images extends RecyclerView.Adapter<ViewPager2Images.ViewPagerHoler> {
    private Context context;
    private List<String> list;

    public ViewPager2Images(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewPagerHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewPager2Images.ViewPagerHoler(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_image_item,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerHoler holder, int position) {
        Glide.with(context).load(list.get(position)).into(holder.newImg);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewPagerHoler  extends RecyclerView.ViewHolder{
        ImageView newImg;
        public ViewPagerHoler(@NonNull View itemView) {
            super(itemView);
            newImg= itemView.findViewById(R.id.imageProductDetails);
        }
    }
}
