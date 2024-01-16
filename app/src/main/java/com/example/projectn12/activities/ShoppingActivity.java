package com.example.projectn12.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.projectn12.R;
import com.example.projectn12.databinding.ActivityShoppingBinding;
import com.example.projectn12.fragment.shopping.CartFragment;
import com.example.projectn12.fragment.shopping.HomeFragment;
import com.example.projectn12.fragment.shopping.ProfileFragment;
import com.example.projectn12.fragment.shopping.SearchFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ShoppingActivity extends AppCompatActivity {
    private ActivityShoppingBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityShoppingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.homeFragment)  replaceFragment(new HomeFragment());
                else if(item.getItemId()==R.id.profileFragment) replaceFragment(new ProfileFragment());
                else if(item.getItemId()==R.id.searchFragment) replaceFragment(new SearchFragment());
                else replaceFragment(new CartFragment());
                return true;
            }
        });
        //load_data();
        // Lấy giá trị từ Firebase và cập nhật số lượng sản phẩm vào BadgeDrawable




    }

    private void load_data() {
        CartFragment cartFragment = new CartFragment();
        cartFragment.firestore.collection("AddToCart").document(cartFragment.auth.getCurrentUser().getUid())
                .collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int totalAmount = task.getResult().size();
                            Log.d("count1", String.valueOf(totalAmount));
                            BadgeDrawable badge = binding.bottomNavigation.getOrCreateBadge(R.id.cartFragment);
                            badge.setNumber(totalAmount);
                            badge.setBackgroundColor(getResources().getColor(R.color.g_blue));
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(ShoppingActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager(); // Tạo đối tượng fragmentManager để quản lý các fragment trong Activity.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // Bắt đầu một giao dịch fragment bằng cách gọi phương thức .beginTransaction(). Giao dịch cho phép thêm/sửa/xóa/thay thế fragment.
        fragmentTransaction.replace(R.id.shoppingHostFragment, fragment); // Thay thế fragment được truyền vào.
        fragmentTransaction.commit(); // Xác nhận giao dịch.
    }
}