package com.example.projectn12.models;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.projectn12.fragment.categories.MainCategoryFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class serProduct {
    ArrayList<Product> listdata;
    Product newProduct ;
    private FirebaseFirestore firestore ;
    private StorageReference storageRef;
    private HashMap<String , Object > productMap ;
    public void setListdata(ArrayList<Product> listdata) {
        this.listdata = listdata;
    }
    public Product getNewProduct() {
        return newProduct;
    }
    public void setNewProduct(Product newProduct) {
        this.newProduct = newProduct;
    }
    public ArrayList<Product> getListdata() {
        return listdata;
    }
    public serProduct( ) {
        this.listdata = new ArrayList<>();
        newProduct = new Product();
        productMap = new HashMap<>();
        this.firestore = FirebaseFirestore.getInstance();
        this.storageRef = FirebaseStorage.getInstance().getReference();

    }
    public void saveProductToDatabase( Context a){
        if (newProduct.getUriImg().size() == 0){
            saveProductDatabaseNoneImg(a);
        }else{
            saveProductDatabaseWithImg(a);
        }

    }
    public void saveProductDatabaseWithImg( Context a){
        List<Uri> arrUriProduct = newProduct.getUriImg();
        List<String> arrUrlProduct = new ArrayList<>();
        for(int i=0 ; i< newProduct.getUriImg().size() ; i++){
            if(i == newProduct.getUriImg().size() - 1){
                String randomKey = UUID.randomUUID().toString();
                StorageReference mountainImagesRef = storageRef.child("images/" + randomKey);
                mountainImagesRef.putFile(arrUriProduct.get(i))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mountainImagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    arrUrlProduct.add(imageUrl);
                                    newProduct.setImages(arrUrlProduct);
                                    saveProductDatabaseNoneImg(a);
                                    Log.d("thanh cong Url" , "thanh cong");
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("that bai" , "that bai");
                            }
                        });
            }
            else {
                String randomKey = UUID.randomUUID().toString();
                StorageReference mountainImagesRef = storageRef.child("images/" + randomKey);
                mountainImagesRef.putFile(arrUriProduct.get(i))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mountainImagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    arrUrlProduct.add(imageUrl);
                                    Log.d("thanh cong Url" , "thanh cong");
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("that bai" , "that bai");
                            }
                        });
            }
        }


    }
    public void saveProductDatabaseNoneImg( Context a){
        productMap.put("name" , this.newProduct.getName() );
        productMap.put("price" , this.newProduct.getPrice() );
        productMap.put("description" , this.newProduct.getDescription() );
        productMap.put("images" , this.newProduct.getImages() );
        productMap.put("category" , this.newProduct.getCategory() );
        firestore.collection("Products")
                .add(productMap)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(a, "thêm thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = task.getException().getMessage();
                        }
                    }
                });

    }
    public void getSeachProduct(String nameProduct , MainCategoryFragment search){
        ArrayList<Product> listProductSearch = new ArrayList<>();
        firestore.collection("Products")
                .whereGreaterThanOrEqualTo("name", nameProduct)
                .whereLessThan("name", nameProduct + '\uf8ff')
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product a = document.toObject(Product.class);
                                listProductSearch.add(a);
                                Log.d("ProductSearch", listProductSearch.toString());
                            }
                            search.searchProduct(listProductSearch);
                        } else {
                            Log.d("that bai product" , "that bai product" );
                        }
                    }
                });

    }
    public void updateProductToDatabase( Context a){
        if (newProduct.getUriImg().size() == 0){
            updateProductDatabaseNoneImg(a);
        }else{
            updateProductDatabaseWithImg(a);
        }

    }
    public void updateProductDatabaseWithImg( Context a){
        List<Uri> arrUriProduct = newProduct.getUriImg();
        List<String> arrUrlProduct = new ArrayList<>();
        for(int i=0 ; i< newProduct.getUriImg().size() ; i++){
            if(i == newProduct.getUriImg().size() - 1){
                String randomKey = UUID.randomUUID().toString();
                StorageReference mountainImagesRef = storageRef.child("images/" + randomKey);
                mountainImagesRef.putFile(arrUriProduct.get(i))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mountainImagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    arrUrlProduct.add(imageUrl);
                                    newProduct.setImages(arrUrlProduct);
                                    updateProductDatabaseNoneImg(a);
                                    Log.d("thanh cong Url" , "thanh cong");
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("that bai" , "that bai");
                            }
                        });
            }
            else {
                String randomKey = UUID.randomUUID().toString();
                StorageReference mountainImagesRef = storageRef.child("images/" + randomKey);
                mountainImagesRef.putFile(arrUriProduct.get(i))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mountainImagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    arrUrlProduct.add(imageUrl);
                                    Log.d("thanh cong Url" , "thanh cong");
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("that bai" , "that bai");
                            }
                        });
            }
        }


    }
    public void updateProductDatabaseNoneImg( Context a){
        productMap.put("name" , this.newProduct.getName() );
        productMap.put("price" , this.newProduct.getPrice() );
        productMap.put("description" , this.newProduct.getDescription() );
        productMap.put("images" , this.newProduct.getImages() );
        productMap.put("category" , this.newProduct.getCategory() );
        firestore.collection("Products")
                .document(this.newProduct.getIdProduct())
                .update(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(a, "Sửa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = task.getException().getMessage();
                            // Xử lý lỗi nếu có
                        }
                    }
                });

    }
    public void delProduct(Context a){
        firestore.collection("Products")
                .document(this.newProduct.getIdProduct())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(a, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMessage = task.getException().getMessage();
                            // Xử lý lỗi nếu có
                        }
                    }
                });
    }

}
