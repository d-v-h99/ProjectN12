package com.example.projectn12.fragment.product;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.projectn12.databinding.FragmentAddProductBinding;
import com.example.projectn12.models.Product;
import com.example.projectn12.models.serProduct;

import java.util.ArrayList;
import java.util.List;


public class AddProduct extends Fragment {

    private FragmentAddProductBinding binding;
    Button chooseImg;
    EditText nameInPut, priceInPut , description;
    Button upload;
    ImageView ImgView;
    List<Uri> chooseImgList = new ArrayList<>();
    serProduct dbProduct = new serProduct();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddProductBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameInPut = binding.nameInPut;
        priceInPut = binding.priceInPut;
        description = binding.description;
        chooseImg = binding.chooseImg;
        upload = binding.upload;
        ImgView = binding.ImgView;
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product newProduct = new Product();
                String nameProduct = nameInPut.getText().toString();
                String priceProduct = priceInPut.getText().toString();
                String descriptionProduct = description.getText().toString();
                if(nameProduct.isEmpty() ||priceProduct.isEmpty() || descriptionProduct.isEmpty())
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        float priceProductFloat = Float.parseFloat(priceInPut.getText().toString());
                        newProduct.setDataProduct(nameProduct, descriptionProduct , priceProductFloat , chooseImgList);
                        dbProduct.setNewProduct(newProduct);
                        dbProduct.saveProductToDatabase(getContext());
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Vui lòng nhập giá tiền là số", Toast.LENGTH_SHORT).show();
                    }

                }



            }
        });
        chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getClipData() != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri uriImg = data.getClipData().getItemAt(i).getUri();
                    chooseImgList.add(uriImg);
                    Glide.with(this)
                            .load(uriImg)
                            .override(300, 200)
                            .into(ImgView);
                }
            }

        }
    }
}