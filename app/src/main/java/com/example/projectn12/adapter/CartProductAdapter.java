package com.example.projectn12.adapter;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projectn12.R;
import com.example.projectn12.databinding.FragmentCartBinding;
import com.example.projectn12.fragment.address.AddressFragment;
import com.example.projectn12.models.CartProduct;
import com.example.projectn12.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.CartHolder> {
    Context context;
    private List<CartProduct> list;
    FragmentCartBinding binding;
    int totalAmount = 0;
    int totalQuantity = 1;


    public CartProductAdapter(Context context, List<CartProduct> list, FragmentCartBinding binding) {
        this.context = context;
        this.list = list;
        this.binding = binding;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartProductAdapter.CartHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(context).load(list.get(position).getImages()).into(holder.newImg);
        holder.newName.setText(list.get(position).getProductName());
        holder.quantity.setText(String.valueOf(list.get(position).getTotalQuantity()));
        holder.newPrice.setText(chuyenDoiTien(String.valueOf((int) (Float.valueOf(list.get(position).getProductPrice()) * 1))));
        totalAmount += Float.valueOf(list.get(position).getProductPrice());
        holder.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer dem = Integer.valueOf(holder.quantity.getText().toString());
                dem++;
                holder.quantity.setText(String.valueOf(dem));
                //  holder.newPrice.setText(chuyenDoiTien(String.valueOf((int) (Float.valueOf(list.get(position).getProductPrice()) * totalQuantity))));
                totalAmount += Float.valueOf(Float.valueOf(list.get(position).getProductPrice()) * dem);
                Intent intent = new Intent("MyTotalAmount");
                intent.putExtra("totalAmount", chuyenDoiTien(String.valueOf(totalAmount)));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionRef = db.collection("AddToCart")
                        .document(currentUser.getUid())
                        .collection("User");
                collectionRef.whereEqualTo("productName", list.get(position).getProductName())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String documentId = document.getId();
                                        // Sử dụng documentId để truy cập vào bản ghi cần thay đổi

                                        // Thực hiện thay đổi giá trị và cập nhật bản ghi
                                        collectionRef.document(documentId).get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                CartProduct cartProduct = document.toObject(CartProduct.class);
                                                                if (cartProduct != null) {
                                                                    int newQuantity = cartProduct.getTotalQuantity() + 1;
                                                                    collectionRef.document(documentId).update("totalQuantity", newQuantity)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        Log.d("Sua doi tuong1", "thanh cong");
                                                                                        holder.quantity.setText(String.valueOf(newQuantity));
                                                                                    } else {
                                                                                        Log.d("Sua doi tuong", "that bai");
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        } else {
                                                            Exception error = task.getException();
                                                            Log.d("Sua doi tuong", error.getMessage());
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Log.d("Sua doi tuong", errorMessage);
                                }
                            }
                        });

            }
        });
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer dem = Integer.valueOf(holder.quantity.getText().toString());
                if (dem > 1) {
                    dem--;
                    holder.quantity.setText(String.valueOf(dem));
                    //  holder.newPrice.setText(chuyenDoiTien(String.valueOf((int) (Float.valueOf(list.get(position).getProductPrice()) * totalQuantity))));
                    totalAmount += Float.valueOf(Float.valueOf(list.get(position).getProductPrice()) * dem);
                    Intent intent = new Intent("MyTotalAmount");
                    intent.putExtra("totalAmount", chuyenDoiTien(String.valueOf(totalAmount)));
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    CollectionReference collectionRef = db.collection("AddToCart")
                            .document(currentUser.getUid())
                            .collection("User");
                    collectionRef.whereEqualTo("productName", list.get(position).getProductName())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            String documentId = document.getId();
                                            // Sử dụng documentId để truy cập vào bản ghi cần thay đổi

                                            // Thực hiện thay đổi giá trị và cập nhật bản ghi
                                            collectionRef.document(documentId).get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot document = task.getResult();
                                                                if (document.exists()) {
                                                                    CartProduct cartProduct = document.toObject(CartProduct.class);
                                                                    if (cartProduct != null) {
                                                                        int newQuantity = cartProduct.getTotalQuantity() - 1;
                                                                        collectionRef.document(documentId).update("totalQuantity", newQuantity)
                                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        if (task.isSuccessful()) {
                                                                                            Log.d("Sua doi tuong1", "thanh cong");
                                                                                            holder.quantity.setText(String.valueOf(newQuantity));
                                                                                        } else {
                                                                                            Log.d("Sua doi tuong", "that bai");
                                                                                        }
                                                                                    }
                                                                                });
                                                                    }
                                                                }
                                                            } else {
                                                                Exception error = task.getException();
                                                                Log.d("Sua doi tuong", error.getMessage());
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        String errorMessage = task.getException().getMessage();
                                        Log.d("Sua doi tuong", errorMessage);
                                    }
                                }
                            });

                }
            }
        });
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("totalAmount", chuyenDoiTien(String.valueOf(totalAmount)));
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        binding.buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AddressFragment fragment = new AddressFragment();
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.shoppingHostFragment, fragment);
                ft.addToBackStack(null).commit();
            }
        });


    }



@Override
public int getItemCount(){
        return list.size();
        }

public class CartHolder extends RecyclerView.ViewHolder {
    ImageView newImg;
    TextView newName, newPrice, quantity;
    ImageView addItem, removeItem;

    public CartHolder(@NonNull View itemView) {
        super(itemView);
        newImg = itemView.findViewById(R.id.imageCartProduct);
        newName = itemView.findViewById(R.id.tvProductCartName);
        newPrice = itemView.findViewById(R.id.tvProductCartPrice);
        addItem = itemView.findViewById(R.id.imagePlus);
        removeItem = itemView.findViewById(R.id.imageMinus);
        quantity = itemView.findViewById(R.id.tvCartProductQuantity);
    }

}

    String chuyenDoiTien(String tien) {
        float amount = Float.parseFloat(tien);
        DecimalFormat decimalFormat = new DecimalFormat("#,### VND");
        String formattedAmount = decimalFormat.format(amount);
        return formattedAmount;
    }

}
