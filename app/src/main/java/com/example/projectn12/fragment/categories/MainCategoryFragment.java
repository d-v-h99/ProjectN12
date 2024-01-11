package com.example.projectn12.fragment.categories;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectn12.R;
import com.example.projectn12.adapter.BestProductAdapter;
import com.example.projectn12.adapter.SpecialProductsAdapter;
import com.example.projectn12.databinding.FragmentMainCategoryBinding;
import com.example.projectn12.databinding.FragmentRegisterBinding;
import com.example.projectn12.models.Product;
import com.example.projectn12.repository.AuthenticationRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MainCategoryFragment extends Fragment {
    private FragmentMainCategoryBinding binding;
    private SpecialProductsAdapter specialProductsAdapter;
    private List<Product> listProduct, listBestProduct;
    private BestProductAdapter bestProductAdapter;
    private NavController navController;
    FirebaseFirestore db;

    public MainCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainCategoryBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rvSpecialProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        listProduct = new ArrayList<>();
        specialProductsAdapter = new SpecialProductsAdapter(getContext(), listProduct);
        binding.rvSpecialProducts.setAdapter(specialProductsAdapter);
        //best product
        binding.rvBestDealsProducts.setLayoutManager(new GridLayoutManager(getContext(), 2,GridLayoutManager.VERTICAL, false));
        listBestProduct = new ArrayList<>();
        bestProductAdapter = new BestProductAdapter(getContext(), listBestProduct, getActivity().getSupportFragmentManager());
        binding.rvBestDealsProducts.setAdapter(bestProductAdapter);
        db=FirebaseFirestore.getInstance();
        // best product

        db.collection("Products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TB123", document.toString());
                                Object picturesObject = document.get("pictures");

                                if (picturesObject instanceof List) {
                                    try {
                                        List<String> picturesList = (List<String>) picturesObject;
                                        float floatValue = Float.parseFloat(document.get("price").toString());
                                        Product a = new Product(document.get("name").toString() , document.get("category").toString() ,floatValue , 1  ,document.get("description").toString() , picturesList);
                                        listProduct.add(a);
                                        specialProductsAdapter.notifyDataSetChanged();
                                        listBestProduct.add(a);
                                        bestProductAdapter.notifyDataSetChanged();
                                        binding.mainCategoryProgressbar.setVisibility(view.GONE);
                                    } catch (NumberFormatException e) {

                                    }

                                }else{
                                    Product a = document.toObject(Product.class);
                                    listProduct.add(a);
                                    specialProductsAdapter.notifyDataSetChanged();
                                    // best product
                                    listBestProduct.add(a);
                                    bestProductAdapter.notifyDataSetChanged();
                                    binding.mainCategoryProgressbar.setVisibility(view.GONE);
                                }

                            }
                        } else {
                            Log.d("Firestore", "Error getting documents: " + task.getException());
                        }
                    }
                });

    }
}