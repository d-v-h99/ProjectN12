package com.example.projectn12.fragment.address;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.projectn12.R;
import com.example.projectn12.api.JsonPlaceHolderAPI;
import com.example.projectn12.api.RetrofitClient;
import com.example.projectn12.databinding.FragmentAddAddressBinding;
import com.example.projectn12.databinding.FragmentAddressBinding;
import com.example.projectn12.models.Districts;
import com.example.projectn12.models.Provinces;
import com.example.projectn12.models.Wards;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Add_addressFragment extends Fragment {
    private FragmentAddAddressBinding binding;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private List<String> cityList, districtList1, wardList;
    private ArrayAdapter<String> cityAdapter, districtAdapter, wardAdapter;
    public String Tinh;
    public HashMap<Integer, String> hashMap = new HashMap<>();
    public HashMap<Integer, String> hashMapHuyen = new HashMap<>();
    public HashMap<Integer, String> hashMapXa = new HashMap<>();
    JsonPlaceHolderAPI jsonPlaceHolderApi;
    String result="";
    public Add_addressFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddAddressBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imageAddressClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        // Khởi tạo danh sách và adapter cho Spinner
        cityList = new ArrayList<>();
        districtList1 = new ArrayList<>();
        wardList = new ArrayList<>();

        cityAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, cityList);
        districtAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, districtList1);
        wardAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, wardList);
        // Đặt adapter cho Spinner
        binding.citySpinner.setAdapter(cityAdapter);
        binding.districtSpinner.setAdapter(districtAdapter);
        binding.wardSpinner.setAdapter(wardAdapter);
        jsonPlaceHolderApi = RetrofitClient.getJsonPlaceHolderApi();

        binding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                Tinh = selectedValue;
//                Toast.makeText(getContext(), "Bien tinh " + Tinh, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(), selectedValue, Toast.LENGTH_SHORT).show();
                hashMapHuyen.clear();
                getDistricts(selectedValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = parent.getItemAtPosition(position).toString();
                //Toast.makeText(getContext(), selectedValue, Toast.LENGTH_SHORT).show();
                hashMapXa.clear();
                getWards(selectedValue);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.wardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = binding.citySpinner.getSelectedItem().toString();
                String selectedDistrict = binding.districtSpinner.getSelectedItem().toString();
                String selectedWard = parent.getItemAtPosition(position).toString();
                result = selectedCity + " , " + selectedDistrict + " , " + selectedWard;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getProvinces();
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name= binding.edState.getText().toString();
                String phone= binding.edPhone.getText().toString();
                String sonha=binding.edSoNha.getText().toString();
                String temp = name + " , "+ phone +" , "+ result+" | "+sonha;
               // Toast.makeText(getContext(), "DiaChi " + temp, Toast.LENGTH_SHORT).show();
                Map<String, String> map=new HashMap<>();
                map.put("userAddress",temp);
                firestore.collection("user").document(auth.getCurrentUser().getUid())
                        .collection("Address").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Add dia chi thanh cong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

    }

    private void getProvinces() {
        Call<List<Provinces>> call = jsonPlaceHolderApi.getProvinces();
        call.enqueue(new Callback<List<Provinces>>() {
            @Override
            public void onResponse(Call<List<Provinces>> call, Response<List<Provinces>> response) {
                if (!response.isSuccessful()) {
                    binding.edAddressTitle.setText("Code: " + response.code());
                    return;
                }
                List<Provinces> Listprovinces = response.body();
                List<String> cities = new ArrayList<>();
                for (Provinces a : Listprovinces) {
                    hashMap.put(Integer.parseInt(a.getCode()), a.getName());
                    cities.add(a.getName());
                }
                // Cập nhật danh sách tỉnh thành
                cityList.clear();
                cityList.addAll(hashMap.values());
                cityAdapter.notifyDataSetChanged();
                for (Map.Entry<Integer, String> entry : hashMap.entrySet()) {
                    String key = entry.getKey().toString();
                    String value = entry.getValue();
                    Log.d("Check", "Key: " + key + ", Value: " + value);
                }
            }

            @Override
            public void onFailure(Call<List<Provinces>> call, Throwable t) {
                binding.edAddressTitle.setText(t.getMessage());
            }
        });
    }
    private void getDistricts(String t) {
        String key = getKeyfromValue(t);
        //Toast.makeText(MainActivity.this, "Bientinhngoai + "+Tinh, Toast.LENGTH_SHORT).show();
        if (key != null) {
            Log.d("idTinh", key);
            // Tiếp tục xử lý với khóa trong phương thức này
        } else {
            Log.d("idTinh", "Khóa không tồn tại trong HashMap");
        }
        Call<Provinces> call=jsonPlaceHolderApi.getDistricts(Integer.parseInt(key),2);
        call.enqueue(new Callback<Provinces>() {
            @Override
            public void onResponse(Call<Provinces> call, Response<Provinces> response) {
                if (!response.isSuccessful()) {
                    binding.edFullName.setText("Code: " + response.code());
                    return;
                }
                Provinces provinces=response.body();
                List<Districts> districtsList = provinces.getDistricts();
                for (Districts district : districtsList) {
                    hashMapHuyen.put((district.getCode()), district.getName());
                }
                // Cập nhật danh sách tỉnh thành
                districtList1.clear();
                districtList1.addAll(hashMapHuyen.values());
                districtAdapter.notifyDataSetChanged();
                for (Map.Entry<Integer, String> entry : hashMapHuyen.entrySet()) {
                    String key = entry.getKey().toString();
                    String value = entry.getValue();
                    Log.d("CheckHuyen", "Key: " + key + ", Value: " + value);
                }

            }

            @Override
            public void onFailure(Call<Provinces> call, Throwable t) {
                binding.edFullName.setText(t.getMessage());
            }
        });
    }
    private void getWards(String selectedValue) {
        String key= getKeyfromValue1(selectedValue);
        if (key != null) {
            Log.d("idXa", key);
            // Tiếp tục xử lý với khóa trong phương thức này
        } else {
            Log.d("idXa", "Khóa không tồn tại trong HashMap");
        }
        Call<Districts> call=jsonPlaceHolderApi.getWards(Integer.parseInt(key),2);
        call.enqueue(new Callback<Districts>() {
            @Override
            public void onResponse(Call<Districts> call, Response<Districts> response) {
                if (!response.isSuccessful()) {
                    binding.edStreet.setText("Code: " + response.code());
                    return;
                }
                Districts districts=response.body();
                List<Wards> wardsList1= districts.getWards();
                for(Wards a: wardsList1){
                    hashMapXa.put(a.getCode(), a.getName());
                }
                wardList.clear();
                wardList.addAll(hashMapXa.values());
                wardAdapter.notifyDataSetChanged();
                for (Map.Entry<Integer, String> entry : hashMapXa.entrySet()) {
                    String key = entry.getKey().toString();
                    String value = entry.getValue();
                    Log.d("CheckXa", "Key: " + key + ", Value: " + value);
                }
            }

            @Override
            public void onFailure(Call<Districts> call, Throwable t) {

            }
        });
    }
    public String getKeyfromValue(String t) {
        String key = null;
        for (Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            if (entry.getValue().equals(t)) {
                key = entry.getKey().toString();
                break; // Thoát khỏi vòng lặp sau khi tìm thấy khóa
            }
        }
        return key;

    }
    public String getKeyfromValue1(String t) {
        String key = null;
        for (Map.Entry<Integer, String> entry : hashMapHuyen.entrySet()) {
            if (entry.getValue().equals(t)) {
                key = entry.getKey().toString();
                break; // Thoát khỏi vòng lặp sau khi tìm thấy khóa
            }
        }
        return key;
    }
}