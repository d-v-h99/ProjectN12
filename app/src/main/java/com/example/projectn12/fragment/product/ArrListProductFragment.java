package com.example.projectn12.fragment.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectn12.R;
import com.example.projectn12.adapter.ArrProduct;
import com.example.projectn12.databinding.FragmentArrListProductBinding;
import com.example.projectn12.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class ArrListProductFragment extends Fragment {

    private FragmentArrListProductBinding binding;
    private ArrProduct bestProductAdapter;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArrListProductBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.arrProduct.setLayoutManager(new GridLayoutManager(getContext(), 1,GridLayoutManager.VERTICAL, false));
        ArrayList<Product> listBestProduct = new ArrayList<>();
//        bestProductAdapter = new ArrProduct(getContext(), listBestProduct,binding ,getActivity().getSupportFragmentManager());
        bestProductAdapter = new ArrProduct(getContext(), listBestProduct , getActivity().getSupportFragmentManager());
        binding.arrProduct.setAdapter(bestProductAdapter);
        db= FirebaseFirestore.getInstance();
        // best product

        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product a = document.toObject(Product.class);
                                a.setIdProduct(document.getId().toString());
                                listBestProduct.add(a);
                                bestProductAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.d("Firestore", "Error getting documents: " + task.getException());
                        }
                    }
                });


    }
}