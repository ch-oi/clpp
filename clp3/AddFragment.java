package com.example.clp3;

import static android.app.Activity.RESULT_OK;

import static com.google.firebase.appcheck.internal.util.Logger.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.WindowDecorActionBar;
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
    TextView ttv;
    Uri imgUri;

    private String[] items = {"문수지", "2호관", "벽강아트센터"};
    private boolean[] checkedItems = {false, false, false};
    private StringBuilder selectedItems = new StringBuilder();

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        DatabaseReference databaseReference;

        iv = view.findViewById(R.id.iv);
        cu = view.findViewById(R.id.cu);
        cs = view.findViewById(R.id.cs);
        cl = view.findViewById(R.id.cl);
        ttv = view.findViewById(R.id.ttv);


        //cl.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View v) {

               // FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
               // StorageReference rootRef = firebaseStorage.getReference("uploads/");
               // StorageReference imgRef = rootRef.child("20231207010556.png");

               // if (imgRef != null) {
                   // imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       // @Override
                       // public void onSuccess(Uri uri) {
                            //Glide.with(AddFragment.this).load(uri).into(iv);
                       // }
                   // });

               // }
            //}
        //});
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
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
                    Toast.makeText(getContext(), "이미지가 없음", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    @SuppressLint("RestrictedApi")
    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Select Items");
        builder.setMultiChoiceItems(items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checkedItems[which] = isChecked;
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedItems.setLength(0); // Clear previous selections
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        selectedItems.append(items[i]).append(", ");
                    }
                }

                // Remove trailing comma and space
                if (selectedItems.length() > 0) {
                    selectedItems.delete(selectedItems.length() - 2, selectedItems.length());
                }

                ttv.setText(selectedItems.toString());
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing, just close the dialog
            }
        });

        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    imgUri = data.getData();
                    Glide.with(this).load(imgUri).into(iv);
                }
                break;
        }
    }
}
