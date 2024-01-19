package com.example.projectn12.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectn12.R;
import com.example.projectn12.fragment.address.Add_addressFragment;
import com.example.projectn12.fragment.address.AddressFragment;
import com.example.projectn12.fragment.shopping.ProductDetailsFragment;
import com.example.projectn12.models.AddressModel;
import com.example.projectn12.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    Context context;
    List<AddressModel> addressModelList;
    SelectedAddress selectedAddress;
    private RadioButton selectedRadioBtn;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public AddressAdapter(Context context, List<AddressModel> addressModelList, SelectedAddress selectedAddress) {
        this.context = context;
        this.addressModelList = addressModelList;
        this.selectedAddress = selectedAddress;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressAdapter.AddressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.address.setText(addressModelList.get(position).getUserAddress());
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton clickedRadioBtn = (RadioButton) v;
                int clickedPosition = position;
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    AddressModel clickedAddress = addressModelList.get(clickedPosition);
                    if (!clickedAddress.isSelected()) {
                        // Kiểm tra xem có radio button nào đã được chọn trước đó không
                        if (selectedRadioBtn != null) {
                            selectedRadioBtn.setChecked(false);

                        }

                        clickedRadioBtn.setChecked(true);
                        selectedRadioBtn = clickedRadioBtn;

                        // Đặt lại trạng thái chọn cho các địa chỉ khác
                        for (AddressModel address : addressModelList) {
                            address.setSelected(false);
                        }

                        // Đặt trạng thái chọn cho địa chỉ được nhấn vào
                        clickedAddress.setSelected(true);

                        // Cập nhật địa chỉ đã chọn thông qua SelectedAddress interface
                        selectedAddress.setAddress(clickedAddress.getUserAddress());
                    }
                }
            }
        });
        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestore.collection("user")
                        .document(auth.getCurrentUser().getUid())
                        .collection("Address")
                        .whereEqualTo("userAddress",  holder.address.getText())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        String documentId = document.getId();
                                        firestore.collection("user")
                                                .document(auth.getCurrentUser().getUid())
                                                .collection("Address")
                                                .document(documentId)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        addressModelList.remove(position); // Nếu bạn lưu bản ghi trong list
                                                        notifyItemRemoved(position); // Thông báo xóa trước
                                                        notifyItemRangeChanged(position, addressModelList.size()); // Cập nhật các mục còn lại
                                                       // Toast.makeText(context, "XOa thanh cong", Toast.LENGTH_SHORT).show();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Xảy ra lỗi khi xóa
                                                    }
                                                });
                                    }
                                }
                            }
                        });
            }
        });
//        holder.btnSua.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String a =addressModelList.get(position).getUserAddress();
//                Bundle bundle = new Bundle();
//                bundle.putString("detailedAdrees", a);
//                Add_addressFragment fragment = new Add_addressFragment();
//                fragment.setArguments(bundle);
//                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//                ft.replace(R.id.shoppingHostFragment, fragment);
//                ft.addToBackStack(null).commit();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return addressModelList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView address;
        RadioButton radioButton;
        ImageButton btnSua, btnXoa;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            address=itemView.findViewById(R.id.address_add);
            radioButton = itemView.findViewById(R.id.select_address);
           // btnSua=itemView.findViewById(R.id.btnSua);
            btnXoa=itemView.findViewById(R.id.btnXoa);
        }
    }
    public interface SelectedAddress {
        void setAddress(String address);
    }
}