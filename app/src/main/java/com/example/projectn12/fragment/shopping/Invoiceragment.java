package com.example.projectn12.fragment.shopping;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectn12.R;
import com.example.projectn12.adapter.InvoikeAdapter;
import com.example.projectn12.databinding.FragmentInvoiceragmentBinding;
import com.example.projectn12.models.CartProduct;
import com.example.projectn12.models.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Invoiceragment extends Fragment {
    private FragmentInvoiceragmentBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    public Invoiceragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInvoiceragmentBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imageCloseBilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        if(auth.getCurrentUser() != null){
            CollectionReference invoiceCollection = firestore.collection("Invoice")
                    .document(auth.getCurrentUser().getUid())
                    .collection("User");

            invoiceCollection.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Item> items = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Extract the values
                                    String dateInvoice = document.getString("DateInvoice");
                                    // Process the extracted data as needed
                                    Log.d("Invoice Data", "DateInvoice: " + dateInvoice);
                                    // Process productMapList to extract individual product details
                                    List<CartProduct> productList = new ArrayList<>();

                                    // Trích xuất danh sách sản phẩm
                                    List<HashMap<String, Object>> productMapList = (List<HashMap<String, Object>>) document.get("products");
                                    for (HashMap<String, Object> productMap : productMapList) {
                                        String name = (String) productMap.get("productName");
                                        Long price = (Long) productMap.get("productPrice");
                                        Long quantity = (Long) productMap.get("totalQuantity");
                                        String images = (String) productMap.get("images");
                                        CartProduct product = new CartProduct(name, Math.toIntExact(price), Math.toIntExact(quantity), images);
                                        productList.add(product);
                                        Log.d("Prod", product.toString());
                                    }
                                    items.add(new Item(0, dateInvoice));
                                    Integer total=0;
                                    // Duyệt qua danh sách sản phẩm trong hoá đơn
                                    for (CartProduct product : productList) {
                                        // Thêm mục sản phẩm vào danh sách items
                                        total+=product.getProductPrice()*product.getTotalQuantity();
                                        items.add(new Item(2, product));
                                    }
                                    items.add(new Item(1,String.valueOf(total)));
                                    binding.recyclerview.setAdapter(new InvoikeAdapter(items));
                                }
                            } else {
                                Log.w("InvoiceData", "Error getting documents.", task.getException());
                            }
                        }
                    });

        }
    }
}