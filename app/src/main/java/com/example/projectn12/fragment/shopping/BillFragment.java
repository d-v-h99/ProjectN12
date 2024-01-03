package com.example.projectn12.fragment.shopping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projectn12.R;
import com.example.projectn12.adapter.BillAdapter;
import com.example.projectn12.adapter.CartProductAdapter;
import com.example.projectn12.databinding.FragmentAddressBinding;
import com.example.projectn12.databinding.FragmentBillBinding;
import com.example.projectn12.models.CartProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BillFragment extends Fragment {
    private FragmentBillBinding binding;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    List<CartProduct> productList;
    BillAdapter billAdapter;

    public BillFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBillBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (bundle != null) {
            String value = bundle.getString("keyDiaChi");
            // Sử dụng giá trị dữ liệu nhận được ở đây
            Log.d("chek", value);
            binding.tvAddress.setText(value);
        }
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmountBill"));
        productList = new ArrayList<CartProduct>();
        billAdapter = new BillAdapter(getContext(), productList);
        binding.rvProducts.setAdapter(billAdapter);
        firestore.collection("AddToCart").document(currentUser.getUid())
                .collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CartProduct myCartModel = document.toObject(CartProduct.class);
                                Log.d("TB1", myCartModel.toString());
                                productList.add(myCartModel);
                                billAdapter.notifyDataSetChanged();
                                //

                            }
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        binding.buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reference đến collection "HistorytoCart"
                CollectionReference historyRef = firestore.collection("HistorytoCart");

                // Reference đến collection "AddToCart"
                CollectionReference collectionRef = firestore.collection("AddToCart")
                        .document(currentUser.getUid())
                        .collection("User");

                // Lấy dữ liệu từ collection "AddToCart" và sao chép sang "HistorytoCart"
                collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Lấy dữ liệu từ document
                                Map<String, Object> data = document.getData();
                                data.put("IDnguoidung",currentUser.getUid());
                                // Thêm dữ liệu vào collection "HistorytoCart"
                                historyRef.add(data)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                // Xóa bản ghi trong collection "AddToCart"
                                                collectionRef.document(document.getId())
                                                        .delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(getContext(), "Sao chép và xóa thành công", Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getContext(), "Lỗi khi xóa bản ghi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Lỗi khi sao chép dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(getContext(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String totalAmount = intent.getStringExtra("totalAmountBill");
            binding.tvTotalPrice.setText(totalAmount);
        }

    };
}