package com.example.projectn12.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projectn12.R;
import com.example.projectn12.databinding.FragmentAccountOptionsBinding;
import com.google.firebase.auth.FirebaseAuth;

public class AccountOptionsFragment extends Fragment {

    private FragmentAccountOptionsBinding binding;
    private NavController navController;
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getActiveNetworkInfo() == null) {
                binding.buttonLoginAccountOptions.setEnabled(false);
                Toast.makeText(context, "Kết nối INTERNET thất bại!", Toast.LENGTH_LONG).show();
            }else{
                binding.buttonLoginAccountOptions.setEnabled(true);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(wifiReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(wifiReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountOptionsBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
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

        if (auth.getCurrentUser() != null) {
            // navController.navigate(R.id.action_accountOptionsFragment_to_shoppingActivity);
        }
    }
}
