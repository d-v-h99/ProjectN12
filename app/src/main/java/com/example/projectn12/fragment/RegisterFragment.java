package com.example.projectn12.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectn12.databinding.FragmentRegisterBinding;
import com.example.projectn12.models.User;
import com.example.projectn12.repository.AuthenticationRepository;


public class RegisterFragment extends Fragment {
    private AuthenticationRepository repository;
    private FragmentRegisterBinding binding;
    private NavController navController;

    public RegisterFragment() {
        // Lấy Context của Fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        repository = new AuthenticationRepository();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController  = Navigation.findNavController(view);
        binding.buttonRegisterRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String firstname = binding.edFirstNameRegister.getText().toString();
                String lastname = binding.edLastNameRegister.getText().toString();
                String email = binding.edEmailRegister.getText().toString();
                String pass = binding.edPasswordRegister.getText().toString();
                User temp = new User(firstname,lastname,email);
                binding.buttonRegisterRegister.startAnimation();
                repository.register(temp,pass, getContext(), binding);

               // binding.buttonRegisterRegister.revertAnimation();

            }
        });


       // binding.buttonRegisterRegister.startAnimation();

    }
}