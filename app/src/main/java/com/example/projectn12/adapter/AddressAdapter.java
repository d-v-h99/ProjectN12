package com.example.projectn12.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectn12.R;
import com.example.projectn12.models.AddressModel;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    Context context;
    List<AddressModel> addressModelList;
    SelectedAddress selectedAddress;
    private RadioButton selectedRadioBtn;

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

    }

    @Override
    public int getItemCount() {
        return addressModelList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView address;
        RadioButton radioButton;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            address=itemView.findViewById(R.id.address_add);
            radioButton = itemView.findViewById(R.id.select_address);
        }
    }
    public interface SelectedAddress {
        void setAddress(String address);
    }
}