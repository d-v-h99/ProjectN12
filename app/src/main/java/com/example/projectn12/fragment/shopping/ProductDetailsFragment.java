package com.example.projectn12.fragment.shopping;

import static android.content.Intent.getIntent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectn12.R;
import com.example.projectn12.adapter.ViewPager2Images;
import com.example.projectn12.databinding.FragmentMainCategoryBinding;
import com.example.projectn12.databinding.FragmentProductDetailsBinding;
import com.example.projectn12.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsFragment extends Fragment {
    private FragmentProductDetailsBinding binding;
    Product product = null;
    ViewPager2Images viewPagerAdapter;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            Object obj = arguments.getSerializable("detailed");
            if (obj instanceof Product) {
                product = (Product) obj;
            }
        }
        if (product != null) {
            viewPagerAdapter = new ViewPager2Images(getContext(), product.getImages());
            binding.viewPagerProductImages.setAdapter(viewPagerAdapter);
            //Glide.with(getContext()).load(product.getImages().get(0)).into(binding.cardProductImages);
            binding.tvProductName.setText(product.getName());
            binding.tvProductPrice.setText(String.valueOf(product.getPrice()));
            binding.tvProductDescription.setText(product.getDescription());
            binding.imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requireActivity().onBackPressed();
                }
            });
            binding.buttonAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToCart();
                }
            });
        }
    }

    private void addToCart() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // Người dùng đã đăng nhập, tiến hành thêm vào giỏ hàng
            String saveCurrentTime, saveCurrentDate;
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            saveCurrentTime = currentTime.format(calForDate.getTime());
            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("images", product.getImages().get(0));
            cartMap.put("productName", binding.tvProductName.getText().toString());
            cartMap.put("productPrice", binding.tvProductPrice.getText().toString());
            cartMap.put("currentTime", saveCurrentTime);
            cartMap.put("currentDate", saveCurrentDate);
            cartMap.put("totalQuantity", 1);
            firestore.collection("AddToCart")
                    .document(currentUser.getUid())
                    .collection("User")
                    .add(cartMap)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_LONG).show();
                                // Hoàn tất thêm vào giỏ hàng
                            } else {
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}