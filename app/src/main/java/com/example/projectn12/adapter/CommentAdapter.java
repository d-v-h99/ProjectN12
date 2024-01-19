package com.example.projectn12.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectn12.R;
import com.example.projectn12.databinding.FragmentProductDetailsBinding;
import com.example.projectn12.models.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;
    private List<Comment> mData;
    private FragmentProductDetailsBinding binding;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String tempContent = "";

    public CommentAdapter(Context mContext, List<Comment> mData, FragmentProductDetailsBinding binding) {
        this.mContext = mContext;
        this.mData = mData;
        this.binding = binding;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment, parent, false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        //Glide.with(mContext).load(mData.get(position).getUimg()).into(holder.img_user);
        holder.tv_name.setText(mData.get(position).getUname());
        holder.tv_content.setText(mData.get(position).getContent());
        holder.tv_date.setText(mData.get(position).getTimestamp());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.editTextText2.setText(mData.get(position).getContent());

            }
        });
        holder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempContent = mData.get(position).getContent();
                LayIDPost();
            }
        });
        holder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayIDPost1(holder.tv_content.getText().toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
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
                                Log.d("checkid", tempContent);
                                getCommentIDByContent(documentId,tempContent);
                            }
                        }
                    }
                });
    }
    private void getCommentIDByContent(String IDPost, String commentText) {
        DatabaseReference commentReference = firebaseDatabase.getReference("Comment").child(IDPost);
        commentReference.orderByChild("content").equalTo(commentText).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String commentID = snapshot.getKey();
                   updateComment(IDPost, commentID);
                     //deleteComment(IDPost, commentID);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void LayIDPost1(String t) {
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
                                getCommentIDByContent1(documentId,t);
                            }
                        }
                    }
                });
    }
    private void getCommentIDByContent1(String IDPost, String commentText) {
        Log.d("checklll", IDPost + " - "+commentText);
        DatabaseReference commentReference = firebaseDatabase.getReference("Comment").child(IDPost);
        commentReference.orderByChild("content").equalTo(commentText).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String commentID = snapshot.getKey();
                   // updateComment(IDPost, commentID);
                    deleteComment(IDPost, commentID);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void updateComment(String IDPost, String commentID) {
        Calendar calForDate = Calendar.getInstance();
        String dateCreate = "";
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");

        dateCreate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");

        String date = dateCreate + " , " + currentTime.format(calForDate.getTime());

        DatabaseReference commentReference = firebaseDatabase.getReference("Comment").child(IDPost).child(commentID);
        commentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                if (comment != null && comment.getUid().equals(auth.getCurrentUser().getUid())) {
                    // UID matches, proceed with update
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("content", binding.editTextText2.getText().toString());
                    updateData.put("timestamp", date);

                    commentReference.updateChildren(updateData)
                            .addOnSuccessListener(aVoid -> {
                            })
                            .addOnFailureListener(e -> {
                                showMessage("Failed to update comment: " + e.getMessage());
                            });
                } else {
                    showMessage("Bạn không thể sửa bình luận của người khác");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    private void deleteComment(String IDPost, String commentID) {
        DatabaseReference commentReference = firebaseDatabase.getReference("Comment").child(IDPost).child(commentID);
        commentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                if (comment != null && comment.getUid().equals(auth.getCurrentUser().getUid())) {
                    commentReference.removeValue()
                            .addOnSuccessListener(aVoid -> {
                            })
                            .addOnFailureListener(e -> {
                                showMessage("Failed to delete comment: " + e.getMessage());
                            });

                } else {
                    showMessage("Bạn không thể xoá bình luận của người khác");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }



    private void showMessage(String message) {

        Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();

    }


    public class CommentViewHolder extends RecyclerView.ViewHolder{

      ImageButton btnSua, btnXoa;
        TextView tv_name,tv_content,tv_date;

        public CommentViewHolder(View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.comment_username);
            tv_content = itemView.findViewById(R.id.comment_content);
            tv_date = itemView.findViewById(R.id.comment_date);
            btnSua=itemView.findViewById(R.id.btnSua);
            btnXoa=itemView.findViewById(R.id.btnXoa);
        }
    }






}