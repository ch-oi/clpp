package com.example.clp3;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ImageFragment extends Fragment {

    TextView tv, tv2, tv3, likeCountTextView;
    EditText editText, editTextt, editTexttt;
    private Button upload, uploadd, uploaddd;
    private ToggleButton button_favorite2;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference likeReference = firebaseDatabase.getInstance().getReference().child("likes");

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        tv = view.findViewById(R.id.tv);
        tv2 = view.findViewById(R.id.tv2);
        tv3 = view.findViewById(R.id.tv3);
        likeCountTextView = view.findViewById(R.id.likeCountTextView);
        button_favorite2 = view.findViewById(R.id.button_favorite2);
        editText = view.findViewById(R.id.editText);
        editTextt = view.findViewById(R.id.editTextt);
        editTexttt = view.findViewById(R.id.editTexttt);
        upload = view.findViewById(R.id.upload);
        uploadd = view.findViewById(R.id.uploadd);
        uploaddd = view.findViewById(R.id.uploaddd);

        button_favorite2.setOnClickListener(buttonView -> {
            boolean isChecked = ((ToggleButton) buttonView).isChecked();
            onLikeToggleChanged(isChecked);});
        loadLikeData();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = editText.getText().toString();
                DatabaseReference myRef = firebaseDatabase.getInstance().getReference().child("data1");
                DatabaseReference childRef = myRef.push();
                childRef.setValue(data);

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        StringBuffer buffer = new StringBuffer();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String data = snapshot.getValue(String.class);
                            buffer.append("user : " + data + "\n");
                        }
                        tv.setText(buffer.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        uploadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data1 = editTextt.getText().toString();
                DatabaseReference myRef2 = firebaseDatabase.getInstance().getReference().child("data2");
                DatabaseReference childRef2 = myRef2.push();
                childRef2.setValue(data1);

                myRef2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        StringBuffer buffer2 = new StringBuffer();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String data1 = snapshot.getValue(String.class);
                            buffer2.append("user1 : " + data1 + "\n");
                        }
                        tv2.setText(buffer2.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        uploaddd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data2 = editTexttt.getText().toString();
                DatabaseReference myRef3 = firebaseDatabase.getInstance().getReference().child("data3");
                DatabaseReference childRef2 = myRef3.push();
                childRef2.setValue(data2);

                myRef3.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        StringBuffer buffer2 = new StringBuffer();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String data2 = snapshot.getValue(String.class);
                            buffer2.append("user2 : " + data2 + "\n");
                        }
                        tv3.setText(buffer2.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return view;

    }
    private void onLikeToggleChanged(boolean isChecked) {
        // 좋아요 상태가 변경될 때 호출되는 메서드
        likeReference.setValue(new LikeModel(getCurrentLikeCount(isChecked), isChecked));
    }
    private void loadLikeData() {
        likeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LikeModel likeModel = dataSnapshot.getValue(LikeModel.class);
                if (likeModel != null) {
                    updateUI(likeModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error loading like data", databaseError.toException());
            }
        });
    }
    private void updateUI(LikeModel likeModel) {
        likeCountTextView.setText(String.valueOf(likeModel.getLikeCount()) + " Likes");
        //button_favorite2.setChecked(likeModel.isLiked());
    }
    private int getCurrentLikeCount(boolean isChecked) {
        // 현재 좋아요 개수를 반환
        // 실제로는 사용자의 데이터를 기반으로 판단해야 함
        return isChecked ? 3 : 2;
    }

}
