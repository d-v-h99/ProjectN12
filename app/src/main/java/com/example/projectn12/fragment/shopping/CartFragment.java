package com.example.projectn12.fragment.shopping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projectn12.R;
import com.example.projectn12.activities.ShoppingActivity;
import com.example.projectn12.adapter.CartProductAdapter;
import com.example.projectn12.databinding.FragmentCartBinding;
import com.example.projectn12.fragment.address.AddressFragment;
import com.example.projectn12.models.CartProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    List<CartProduct> productList;
    CartProductAdapter cartProductAdapter;
    public FirebaseAuth auth = FirebaseAuth.getInstance();
    public FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FragmentManager fragmentManager;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FirebaseUser currentUser = auth.getCurrentUser();
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mMessageReceiver,new IntentFilter("MyTotalAmount"));
        binding.rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<CartProduct>();
        cartProductAdapter = new CartProductAdapter(getContext(), productList, binding);
        binding.rvCart.setAdapter(cartProductAdapter);
        firestore.collection("AddToCart").document(currentUser.getUid())
                .collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document: task.getResult()){
                                CartProduct myCartModel = document.toObject(CartProduct.class);
                                Log.d("TB1", myCartModel.toString());
                                productList.add(myCartModel);
                                cartProductAdapter.notifyDataSetChanged();
                                //

                            }

                        }else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        binding.buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalAmount = binding.tvTotalPrice.getText().toString();

                BillFragment fragment = new BillFragment();
                Bundle bundle = new Bundle();
                bundle.putString("totalAmount", totalAmount);
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.shoppingHostFragment, fragment);
                ft.addToBackStack(null).commit();
            }
        });


    }
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String totalAmount = intent.getStringExtra("totalAmount");
            binding.tvTotalPrice.setText(totalAmount);
        }

    };
}