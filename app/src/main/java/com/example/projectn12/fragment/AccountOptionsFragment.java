package com.example.projectn12.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectn12.R;
import com.example.projectn12.databinding.FragmentAccountOptionsBinding;


public class AccountOptionsFragment extends Fragment {

    private FragmentAccountOptionsBinding binding;
    private NavController navController;
    public AccountOptionsFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentAccountOptionsBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Truy cập view và thực hiện các thao tác
        //binding.tvRightAddressForShopping.setText("Hello, ViewBinding in Fragment!");

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        binding.buttonRegisterAccountOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_accountOptionsFragment_to_registerFragment);
            }
        });
        binding.buttonLoginAccountOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_accountOptionsFragment_to_loginFragment);
            }
        });
    }
}