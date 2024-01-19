package com.example.projectn12.fragment.shopping;

import static android.content.Intent.getIntent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectn12.R;
import com.example.projectn12.adapter.CommentAdapter;
import com.example.projectn12.adapter.ViewPager2Images;
import com.example.projectn12.databinding.FragmentMainCategoryBinding;
import com.example.projectn12.databinding.FragmentProductDetailsBinding;
import com.example.projectn12.models.Comment;
import com.example.projectn12.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ProductDetailsFragment extends Fragment {
    private FragmentProductDetailsBinding binding;
    Product product = null;
    ViewPager2Images viewPagerAdapter;
    FirebaseAuth auth = FirebaseAuth.getInstance();
     FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    CommentAdapter commentAdapter;
    List<Comment> listComment;

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
            binding.tvProductPrice.setText(chuyenDoiTien(String.valueOf(Math.round(product.getPrice()))));
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
        // comment
        // truy van lay ten id bai viet
        LayIDPost1();
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              // PushComment();
                LayIDPost();
            }
        });


    }

    private void loadRCVComment(String idPost) {
        binding.RvComment.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseReference commentRef = firebaseDatabase.getReference("Comment").child(idPost);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap:dataSnapshot.getChildren()) {
                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment) ;


                }

                commentAdapter = new CommentAdapter(getContext(),listComment, binding);
                binding.RvComment.setAdapter(commentAdapter);
                binding.mainCategoryProgressbar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void LayIDPost1() {
        FirebaseUser currentUser = auth.getCurrentUser();
        Task<QuerySnapshot> cartCollection = firestore.collection("Products")
                .whereEqualTo("name", binding.tvProductName.getText())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String documentId = document.getId();
                                Log.d("checkid", documentId);
                                loadRCVComment(documentId);
                            }
                        }
                    }
                });
    }


    private void PushComment(String idPost) {
        DatabaseReference commentReference = firebaseDatabase.getReference("Comment").child(idPost).push();
        final Comment[] comment = {null};
        String dateCreate = "";
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        dateCreate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String date = dateCreate + " , " + currentTime.format(calForDate.getTime());

        DocumentReference documentReference = firestore.collection("user").document(auth.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        String username = documentSnapshot.getString("firstName");
                        String lastname = documentSnapshot.getString("lastName");

                        // Tạo nickname từ giá trị của username và lastname
                        String nickname = username + " " + lastname;
                        comment[0] = new Comment(binding.editTextText2.getText().toString(), auth.getCurrentUser().getUid(), nickname, date);
                        commentReference.setValue(comment[0]).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //showMessage("Comment added");
                                binding.editTextText2.setText("");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showMessage("Failed to add comment: " + e.getMessage());
                            }
                        });

                    }
                } else {
                    showMessage("Failed to get document: " + task.getException().getMessage());
                }
            }
        });

    }
    private void showMessage(String message) {

        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();

    }

    private void LayIDPost() {
        FirebaseUser currentUser = auth.getCurrentUser();
        Task<QuerySnapshot> cartCollection = firestore.collection("Products")
                .whereEqualTo("name", binding.tvProductName.getText())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String documentId = document.getId();
                                Log.d("checkid", documentId);
                                PushComment(documentId);
                            }
                        }
                    }
                });
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
            cartMap.put("productPrice", Float.valueOf(product.getPrice()).intValue());
            cartMap.put("currentTime", saveCurrentTime);
            cartMap.put("currentDate", saveCurrentDate);
            cartMap.put("totalQuantity", 1);
            CollectionReference cartCollection = firestore.collection("AddToCart").document(currentUser.getUid()).collection("User");
            cartCollection.whereEqualTo("productName", product.getName())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {
                                    // Sản phẩm chưa tồn tại, thêm vào giỏ hàng
                                    cartCollection.add(cartMap)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Toast.makeText(getContext(), "Sản phẩm đã tồn tại trong giỏ hàng", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else{
            Toast.makeText(getContext(), "Vui lòng đăng nhập để mua hàng", Toast.LENGTH_LONG).show();
        }
    }
    String chuyenDoiTien(String tien) {
        float amount = Float.parseFloat(tien);
        DecimalFormat decimalFormat = new DecimalFormat("#,### VND");
        return decimalFormat.format(amount);
    }
}