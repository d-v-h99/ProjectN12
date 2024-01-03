package com.example.projectn12.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.projectn12.R;
import com.example.projectn12.databinding.FragmentLoginBinding;
import com.example.projectn12.databinding.FragmentRegisterBinding;
import com.example.projectn12.repository.AuthenticationRepository;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class LoginFragment extends Fragment {
    private AuthenticationRepository repository;
    private NavController navController;
    private FragmentLoginBinding binding;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        repository = new AuthenticationRepository();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.edEmailLogin.getText().toString();
                String pass = binding.edPasswordLogin.getText().toString();
                binding.buttonLoginLogin.startAnimation();
                repository.login(email, pass, getContext(), binding);
            }
        });
        binding.tvForgotPasswordLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupBottomSheetDialog();
            }
        });
    }
    public void setupBottomSheetDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), R.style.DialogStyle);
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.reset_passowrd_dialog, null);
        dialog.setContentView(view);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(dialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet));
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        dialog.show();
        EditText dialog_Email = view.findViewById(R.id.edResetPassword);
        Button dialog_btnSend=view.findViewById(R.id.buttonSendResetPassword);
        Button dialog_btnCancel = view.findViewById(R.id.buttonCancelResetPassword);
        dialog_btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = dialog_Email.getText().toString();
                repository.resetPass(email,getContext(), binding);
                dialog.dismiss();

            }
        });
        dialog_btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}