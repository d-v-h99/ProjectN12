package com.example.projectn12.fragment.categories;

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
import com.example.projectn12.adapter.BestProductAdapter;
import com.example.projectn12.databinding.FragmentChairBinding;
import com.example.projectn12.databinding.FragmentTableBinding;
import com.example.projectn12.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TableFragment extends Fragment {
    private FragmentTableBinding binding;
    private BestProductAdapter bestProductAdapter;
    private List<Product> listBestProduct;
    FirebaseFirestore db;
    public TableFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTableBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rvBestDealsProducts.setLayoutManager(new GridLayoutManager(getContext(), 2,GridLayoutManager.VERTICAL, false));
        listBestProduct = new ArrayList<>();
        bestProductAdapter = new BestProductAdapter(getContext(), listBestProduct, getActivity().getSupportFragmentManager());
        binding.rvBestDealsProducts.setAdapter(bestProductAdapter);
        db=FirebaseFirestore.getInstance();
        // best product

        db.collection("Products")
                .whereEqualTo("category", "table")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product a = document.toObject(Product.class);
//                                    listProduct.add(a);
//                                    specialProductsAdapter.notifyDataSetChanged();
                                // best product
                                listBestProduct.add(a);
                                bestProductAdapter.notifyDataSetChanged();
                                binding.mainCategoryProgressbar.setVisibility(view.GONE);

                            }
                        } else {
                            Log.d("Firestore", "Error getting documents: " + task.getException());
                        }
                    }
                });
    }
}