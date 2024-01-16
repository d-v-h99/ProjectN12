package com.example.projectn12.fragment.shopping;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectn12.MainActivity;
import com.example.projectn12.R;
import com.example.projectn12.activities.ShoppingActivity;
import com.example.projectn12.databinding.FragmentProductDetailsBinding;
import com.example.projectn12.databinding.FragmentProfileBinding;
import com.example.projectn12.fragment.AccountOptionsFragment;
import com.example.projectn12.fragment.product.AddProduct;
import com.example.projectn12.repository.AuthenticationRepository;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private AuthenticationRepository repository;
    private NavController navController;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        repository = new AuthenticationRepository();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnchuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProduct fragment = new AddProduct();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.shoppingHostFragment, fragment);
                ft.addToBackStack(null).commit();
            }
        });
        binding.btnHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Invoiceragment fragment = new Invoiceragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.shoppingHostFragment, fragment);
                ft.addToBackStack(null).commit();
            }
        });
        binding.btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repository.logout();
                //navController.navigate(R.id.action_profileFragment_to_accountOptionsFragment);
                // Trong Fragment của Activity A
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });
    }
}