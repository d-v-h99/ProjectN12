package com.example.projectn12.fragment.shopping;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import static com.google.common.reflect.Reflection.getPackageName;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.projectn12.R;
import com.example.projectn12.adapter.BillAdapter;
import com.example.projectn12.adapter.CartProductAdapter;
import com.example.projectn12.databinding.FragmentAddressBinding;
import com.example.projectn12.databinding.FragmentBillBinding;
import com.example.projectn12.models.CartProduct;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BillFragment extends Fragment {
    private FragmentBillBinding binding;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    List<CartProduct> productList;
    BillAdapter billAdapter;
    //tb
    Integer idTB=1;
    private static final int NOTIFICATION_PERMISSION_CODE = 100;
    private static final String CHANNEL_ID = "id_noti_01";

    public BillFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBillBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.imageCloseBilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        Bundle bundle = getArguments();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (bundle != null) {
            String value = bundle.getString("keyDiaChi");
            // Sử dụng giá trị dữ liệu nhận được ở đây
            Log.d("chek", value);
            binding.tvAddress.setText(value);
        }
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmountBill"));
        productList = new ArrayList<CartProduct>();
        billAdapter = new BillAdapter(getContext(), productList);
        binding.rvProducts.setAdapter(billAdapter);
        firestore.collection("AddToCart").document(currentUser.getUid())
                .collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CartProduct myCartModel = document.toObject(CartProduct.class);
                                Log.d("TB1", myCartModel.toString());
                                productList.add(myCartModel);
                                billAdapter.notifyDataSetChanged();
                                //

                            }
                        } else {
                            String errorMessage = task.getException().getMessage();
                            Toast.makeText(getContext(), "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        binding.buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("AddToCart")
                        .document(auth.getCurrentUser().getUid())
                        .collection("User")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    document.getReference().delete();
                                }
                                // Xử lý khi xóa thành công
                               // Toast.makeText(getContext(), "Xóa AddToCart thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                // Xử lý khi xóa thất bại
                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(getContext(), "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                checkNotificationPermission();
            }
        });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String totalAmount = intent.getStringExtra("totalAmountBill");
            binding.tvTotalPrice.setText(totalAmount);
        }

    };
    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!NotificationManagerCompat.from(getContext()).areNotificationsEnabled()) {
                showNotificationPermissionDialog();
            } else {
                createNotificationChannel();
                taoTB();
            }
        } else {
            taoTB();
        }
    }

    private void taoTB() {
        //  mở kênh thông báo =>ứng dụng có thể sử dụng nó để gửi thông báo đến người dùng. Việc sử dụng kênh thông báo cho phép ứng dụng điều chỉnh âm thanh, rung, đèn thông báo và các thiết lập khác cho từng loại thông báo mà nó tạo ra. Điều này giúp người dùng có quyền kiểm soát chi tiết thông báo từ ứng dụng và cải thiện trải nghiệm người dùn
        String id = "id_noti_01"; // chuoi dai dien cho kenh tb
        NotificationManager manager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE); // tham chieu he thong quan li tb
//        ktra ung dung co chay nen hay k
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { // phien ban androdi hen tai >= android Oreo => dung phien ban hien tai ho tro day du
            NotificationChannel channel = manager.getNotificationChannel(id); // ktra kenh tb co ton tai hay k
            if (channel == null) {
//               tao kenh tb với id trên, tiêu đề kênh tb, mức độ quan trọng cao nhất
                channel = new NotificationChannel(id, "Channel Title", NotificationManager.IMPORTANCE_HIGH);
                // cai dat tuy chon tb
                channel.setDescription("Mo ta tb");
                channel.enableVibration(true); // bat che do rung
//                dat kieu rung la mang time
                channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
//                hien thi muc do chi tiet tb o man hinh khoa
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                manager.createNotificationChannel(channel); // tao doi tuong tb tren phuong thuc
            }
        }
//        phien ban moi nguoi dung co the tuy tron hien thi tb android => dki if khoi chay tb o tat ca phien ban
//        tao hien thi tb


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), id)
                .setSmallIcon(R.drawable.ic_noti)
                .setContentTitle("Thông báo")
                .setContentText("Mua hàng thành công!!!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{100, 1000, 200, 340})
                .setAutoCancel(false); // khi an vao tb no se k an di

        //Thiết lập PendingIntent được tạo trước đó cho thông báo. Điều này cho phép khi người dùng nhấp vào thông báo, Intent đã được định nghĩa sẽ được thực hiện.
        NotificationManagerCompat m = NotificationManagerCompat.from(requireContext());
        //Lớp NotificationManagerCompat được sử dụng để tạo và quản lý thông báo. Một đối tượng NotificationManagerCompat được khởi tạo từ context được truyền vào. Sau đó, thông báo được hiển thị bằng cách sử dụng phương thức notify() với một ID duy nhất (trong ví dụ này là 1) và thông báo được tạo từ Builder.
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        m.notify(idTB, builder.build()); // tb co id =1 => suy ra chi hien dc tb 1 lan
//        m.notify(new Random().nextInt(), builder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationManager manager = (NotificationManager) requireContext().getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel Title", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Mo ta tb");
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 1000, 200, 340});
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        manager.createNotificationChannel(channel);
    }

    private void showNotificationPermissionDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Quyền thông báo")
                .setMessage("Ứng dụng cần quyền gửi thông báo. Bạn có muốn mở cài đặt để cấp quyền không?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                        intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        taoTB();
                    }
                })
                .show();
    }
}