package com.example.clp1;

import static android.app.Activity.RESULT_OK;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AddFragment extends Fragment {

    ImageView iv;
    private Button cl, cs, cu;
    Uri imgUri;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        iv = view.findViewById(R.id.iv);
        cu = view.findViewById(R.id.cu);
        cs = view.findViewById(R.id.cs);
        cl = view.findViewById(R.id.cl);


        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                StorageReference rootRef = firebaseStorage.getReference();
                StorageReference imgRef = rootRef.child("자신감_지킴이_화이트_pc.png");

                if (imgRef != null) {
                    imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(AddFragment.this).load(uri).into(iv);
                        }
                    });

                }
            }
        });

        cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 10);
            }
        });

        cu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
                String filename = sdf.format(new Date()) + ".png";
                StorageReference imgRef = firebaseStorage.getReference("uploads/" + filename);


                if (imgUri != null) {
                    UploadTask uploadTask = imgRef.putFile(imgUri);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "이미지 업로드 성공", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // 업로드 실패 처리
                            Log.e(TAG, "이미지 업로드 실패", e);
                            Toast.makeText(getContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // imgUri가 null인 경우 처리, 예를 들어 오류 메시지를 표시할 수 있습니다.
                    Log.e(TAG, "Image URI is null");
                }
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    Uri imgUri = data.getData();
                    Glide.with(this).load(imgUri).into(iv);
                }
                break;
        }
    }
}
