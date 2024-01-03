package com.example.projectn12.fragment.address;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectn12.R;
import com.example.projectn12.adapter.AddressAdapter;
import com.example.projectn12.databinding.FragmentAddressBinding;
import com.example.projectn12.databinding.FragmentCartBinding;
import com.example.projectn12.fragment.shopping.BillFragment;
import com.example.projectn12.models.AddressModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class AddressFragment extends Fragment implements AddressAdapter.SelectedAddress {
    private FragmentAddressBinding binding;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private List<AddressModel> addressModelList;
    private AddressAdapter addressAdapter;
    String mAddress="";

    public AddressFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddressBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.addressRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        addressModelList = new ArrayList<>();
        addressAdapter = new AddressAdapter(getContext(), addressModelList,this);
        binding.addressRecycler.setAdapter(addressAdapter);
        firestore.collection("user").document(auth.getCurrentUser().getUid())
                .collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot doc: task.getResult().getDocuments()){
                                AddressModel addressModel= doc.toObject(AddressModel.class);
                                addressModelList.add(addressModel);
                                addressAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        binding.addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                Add_addressFragment fragment = new Add_addressFragment();
                FragmentTransaction ft= fragmentManager.beginTransaction();
                ft.replace(R.id.shoppingHostFragment, fragment);
                ft.addToBackStack(null).commit();;
            }
        });
        binding.buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillFragment fragment = new BillFragment();
                Bundle bundle = new Bundle();
                bundle.putString("keyDiaChi", mAddress);
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.shoppingHostFragment, fragment);
                ft.addToBackStack(null).commit();
            }
        });
    }

    @Override
    public void setAddress(String address) {
        mAddress=address;
        Log.d("Dia chi", mAddress);

    }
}