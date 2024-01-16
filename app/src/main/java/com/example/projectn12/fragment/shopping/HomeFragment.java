package com.example.projectn12.fragment.shopping;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.projectn12.R;
import com.example.projectn12.adapter.HomeViewpagerAdapter;
import com.example.projectn12.databinding.FragmentHomeBinding;
import com.example.projectn12.databinding.FragmentRegisterBinding;
import com.example.projectn12.fragment.categories.AccessoryFragment;
import com.example.projectn12.fragment.categories.ChairFragment;
import com.example.projectn12.fragment.categories.FurnitureFragment;
import com.example.projectn12.fragment.categories.MainCategoryFragment;
import com.example.projectn12.fragment.categories.TableFragment;
import com.example.projectn12.models.serProduct;
import com.example.projectn12.repository.AuthenticationRepository;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private ImageView search ;
    private EditText valsearch;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayList<Fragment> categoriesFragments = new ArrayList<>();
        categoriesFragments.add(new MainCategoryFragment());
        categoriesFragments.add(new ChairFragment());
        categoriesFragments.add(new TableFragment());
        categoriesFragments.add(new AccessoryFragment());
        categoriesFragments.add(new FurnitureFragment());
        HomeViewpagerAdapter viewPager2Adapter = new HomeViewpagerAdapter(categoriesFragments, getChildFragmentManager(), getLifecycle());
        binding.viewpagerHome.setAdapter(viewPager2Adapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewpagerHome, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Main");
                    break;
                case 1:
                    tab.setText("Chair");
                    break;
                case 2:
                    tab.setText("Table");
                    break;
                case 3:
                    tab.setText("Accessory");
                    break;
                case 4:
                    tab.setText("Furniture");
                    break;
            }
        }).attach();
        search = binding.seach;
        valsearch = binding.valsearch;
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchProduct =valsearch.getText().toString();
                if(searchProduct.isEmpty()){
                    Toast.makeText(getContext(), "Vui lòng tên sản phẩm", Toast.LENGTH_SHORT).show();
                }else{
                    serProduct a = new serProduct();
                    a.getSeachProduct(searchProduct , (MainCategoryFragment) categoriesFragments.get(0));
                }
            }
        });
    }
}