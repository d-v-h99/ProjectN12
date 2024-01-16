package com.example.projectn12.fragment.shopping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


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
        binding.imageCloseCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        FirebaseUser currentUser = auth.getCurrentUser();
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mMessageReceiver,new IntentFilter("MyTotalAmount"));
        binding.rvCart.setLayoutManager(new LinearLayoutManager(getContext()));
        productList = new ArrayList<CartProduct>();
        cartProductAdapter = new CartProductAdapter(getContext(), productList, binding);
        binding.rvCart.setAdapter(cartProductAdapter);
        if(currentUser != null){
            firestore.collection("AddToCart").document(currentUser.getUid())
                    .collection("User")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                if (task.getResult().isEmpty()) {
                                    binding.textview4.setText("Giỏ hàng rỗng");
                                    binding.tvTotalPrice.setText("0 VND");
                                    binding.mainCategoryProgressbar.setVisibility(View.GONE);
                                }else{
                                    for(QueryDocumentSnapshot document: task.getResult()){
                                        CartProduct myCartModel = document.toObject(CartProduct.class);
                                        Log.d("TB1", myCartModel.toString());
                                        productList.add(myCartModel);
                                        cartProductAdapter.notifyDataSetChanged();
                                        //
                                        binding.mainCategoryProgressbar.setVisibility(view.GONE);

                                    }
                                }
                            }else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            // them vao bang hoa don
            //ThemHoaDon();
        }else {
            Toast.makeText(getContext(), "Đăng nhập để xem giỏ hàng", Toast.LENGTH_LONG).show();
        }
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                CartProduct deleteData =null;
                deleteData= productList.get(position);
                productList.remove(position);
                cartProductAdapter.notifyDataSetChanged();
                //
                if(productList.size() == 0){
                    binding.textview4.setText("Giỏ hàng rỗng");
                    binding.tvTotalPrice.setText("0 VND");
                }
                firestore.collection("AddToCart")
                        .document(currentUser.getUid())
                        .collection("User")
                        .whereEqualTo("productName", deleteData.getProductName())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        document.getReference().delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> deleteTask) {
                                                        if (deleteTask.isSuccessful()) {

                                                        } else {
                                                            String errorMessage = deleteTask.getException().getMessage();
                                                            Toast.makeText(getContext(), "Error deleting document: " + errorMessage, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                CartProduct finalDeleteData = deleteData;
                Snackbar.make(binding.rvCart, "Xoá "+ deleteData.getProductName(), Snackbar.LENGTH_LONG)
                        .setAction("Hoàn tác", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                productList.add(position, finalDeleteData);
                                cartProductAdapter.notifyDataSetChanged();
                                //
                                firestore.collection("AddToCart")
                                        .document(currentUser.getUid())
                                        .collection("User")
                                        .add(finalDeleteData)
                                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                if (task.isSuccessful()) {

                                                } else {
                                                    String errorMessage = task.getException().getMessage();
                                                    Toast.makeText(getContext(), "Error adding document: " + errorMessage, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        }).show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.g_red))
                        // .addActionIcon(R.drawable.my_icon)
                        .addSwipeLeftLabel("Xoá")
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.rvCart);

        binding.buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    ThemHoaDon();

//                String totalAmount = binding.tvTotalPrice.getText().toString();
//
//                BillFragment fragment = new BillFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("totalAmount", totalAmount);
//                fragment.setArguments(bundle);
//
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//                ft.replace(R.id.shoppingHostFragment, fragment);
//                ft.addToBackStack(null).commit();
                AddressFragment fragment = new AddressFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.shoppingHostFragment, fragment);
                ft.addToBackStack(null).commit();
            }
        });


    }

    private void ThemHoaDon() {
            String dateCreate = "";
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
            dateCreate = currentDate.format(calForDate.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            String date = dateCreate+" , " + currentTime.format(calForDate.getTime());
            Map<String, Object> invoiceData = new HashMap<>();
            invoiceData.put("products", productList);
            invoiceData.put("DateInvoice",date);
            firestore.collection("Invoice")
                    .document(auth.getCurrentUser().getUid())
                    .collection("User")
                    .add(invoiceData)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                // Xử lý khi    thêm thành công
                               // Toast.makeText(getContext(), "Thêm vào Invoice thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                // Xử lý khi thêm thất bại
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(getContext(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
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