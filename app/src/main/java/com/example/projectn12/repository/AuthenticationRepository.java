package com.example.projectn12.repository;

import static java.security.AccessController.getContext;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.projectn12.R;
import com.example.projectn12.databinding.FragmentLoginBinding;
import com.example.projectn12.databinding.FragmentRegisterBinding;
import com.example.projectn12.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthenticationRepository {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Context context;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    ;
    private NavController navController;

    public AuthenticationRepository() {

    }

    public void login(String email, String pass, Context context, FragmentLoginBinding binding) {
        navController = Navigation.findNavController(binding.getRoot());
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    binding.buttonLoginLogin.revertAnimation();
                    navController.navigate(R.id.action_loginFragment_to_shoppingActivity);
                } else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    binding.buttonLoginLogin.revertAnimation();
                }
            }
        });
    }

    public void register(User user, String pass, Context context, FragmentRegisterBinding binding) {
        navController = Navigation.findNavController(binding.getRoot());
        // binding.getRoot() sẽ trả về View gốc của fragment được tham chiếu bởi FragmentRegisterBinding
        auth.createUserWithEmailAndPassword(user.getEmail(), pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user1 = auth.getCurrentUser();
                if (user1 != null) {
                    String uid = user1.getUid();
                    saveUserInfo(uid, user);
                }

                Toast.makeText(context, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                binding.buttonRegisterRegister.revertAnimation();
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
            } else {
                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                binding.buttonRegisterRegister.revertAnimation();
            }
        });
    }

    public void logout() {
        auth.signOut();
    }

    public void saveUserInfo(String userID, User user) {
        firestore.collection("user")
                .document(userID)
                .set(user);
    }
    public void resetPass(String email, Context context, FragmentLoginBinding binding){
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Vui Long check mail!!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}
