package com.example.projectn12.fragment.shopping;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projectn12.activities.MainActivity;
import com.example.projectn12.R;
import com.example.projectn12.databinding.FragmentProfileBinding;
import com.example.projectn12.fragment.product.AddProduct;
import com.example.projectn12.fragment.product.ArrListProductFragment;
import com.example.projectn12.models.Comment;
import com.example.projectn12.repository.AuthenticationRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private AuthenticationRepository repository;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private NavController navController;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();

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
        // lay profile
        DocumentReference documentReference = firestore.collection("user").document(auth.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("firstName");
                        String lastname = documentSnapshot.getString("lastName");

                        // Tạo nickname từ giá trị của username và lastname
                        String nickname = username + " " + lastname;
                        binding.tvUserName.setText("Xin chào: " + nickname);

                    }
                }
            }
        });
        //

        binding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("tel:01692149072");
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(uri);
                startActivity(intent);

            }
        });
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrListProductFragment fragment = new ArrListProductFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.shoppingHostFragment, fragment);
                ft.addToBackStack(null).commit();
            }
        });
        binding.btnchuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddProduct fragment = new AddProduct();
                AddProduct.check = false;
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