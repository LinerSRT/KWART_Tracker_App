package com.liner.familytracker.LoginRegister;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.liner.familytracker.ColorUtils;
import com.liner.familytracker.CoreUtils.CoreActivity;
import com.liner.familytracker.DatabaseModels.CircleModel;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class CreateProfileActivity extends CoreActivity {
    private EditText nameField;
    private Button nextStep;
    private ImageView addPhotoView;
    private CircleImageView profilePhoto, mapPhotoView;
    private RelativeLayout addPhotoLayout;
    boolean nameValid = false;
    boolean photoUploaded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        nameField = findViewById(R.id.nameEditText);
        nextStep = findViewById(R.id.nextStep);
        profilePhoto = findViewById(R.id.profilePhotoView);
        addPhotoView = findViewById(R.id.addPhotoView);
        addPhotoLayout = findViewById(R.id.addPhotoLayout);
        mapPhotoView = findViewById(R.id.mapPhotoView);


        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String result = editable.toString().trim();
                if(validName(result)){
                    nextStep.setBackgroundColor(ColorUtils.getThemeColor(CreateProfileActivity.this, R.attr.colorPrimary));
                    nameValid = true;
                } else {
                    nextStep.setBackgroundColor(ColorUtils.getThemeColor(CreateProfileActivity.this, R.attr.backgroundColorDark));
                    nameValid = false;
                }
            }
        });
        addPhotoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCrop();
            }
        });


        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nameValid){
                    if(!photoUploaded){
                        //final LDialogUtility dialogUtility = new LDialogUtility(CreateProfileActivity.this);
                        //dialogUtility.createSimpleDialog(null, "Внимание!", "Без фото вас будет труднее найти на карте. Продолжить?", "Да", new View.OnClickListener() {
                        //    @Override
                        //    public void onClick(View view) {
                        //        final LDialogUtility progressDialog = new LDialogUtility(CreateProfileActivity.this);
                        //        progressDialog.createProgressDialog("Создание аккаунта...");
                        //        progressDialog.show();
                        //        currentUserDatabase.child("userName").setValue(nameField.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        //            @Override
                        //            public void onComplete(@NonNull Task<Void> task) {
                        //                firebaseAuth.createUserWithEmailAndPassword(getIntent().getStringExtra("email"), getIntent().getStringExtra("password")).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        //                    @Override
                        //                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //                        currentUserDatabase.child("registerFinished").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                        //                            @Override
                        //                            public void onComplete(@NonNull Task<Void> task) {
                        //                                firebaseAuth.signInWithEmailAndPassword(getIntent().getStringExtra("email"), getIntent().getStringExtra("password")).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        //                                    @Override
                        //                                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //                                        progressDialog.close();
                        //                                        startActivity(new Intent(CreateProfileActivity.this, CircleConfigActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        //                                                .putExtra("password", getIntent().getStringExtra("password"))
                        //                                                .putExtra("phone_number", getIntent().getStringExtra("phone_number"))
                        //                                                .putExtra("email", getIntent().getStringExtra("email"))
                        //                                                .putExtra("userName", nameField.getText().toString().trim()));
                        //                                    }
                        //                                });
                        //                            }
                        //                        });
                        //                    }
                        //                });
                        //            }
                        //        });
                        //    }
                        //}, "Остаться", new View.OnClickListener() {
                        //    @Override
                        //    public void onClick(View view) {
                        //        dialogUtility.close();
                        //    }
                        //});
                        //dialogUtility.show();
                    } else {
                        //final LDialogUtility progressDialog = new LDialogUtility(CreateProfileActivity.this);
                        //progressDialog.createProgressDialog("Создание аккаунта...");
                        //progressDialog.show();
                        //currentUserDatabase.child("userName").setValue(nameField.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        //    @Override
                        //    public void onComplete(@NonNull Task<Void> task) {
                        //        firebaseAuth.createUserWithEmailAndPassword(getIntent().getStringExtra("email"), getIntent().getStringExtra("password")).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        //            @Override
                        //            public void onComplete(@NonNull Task<AuthResult> task) {
                        //                currentUserDatabase.child("registerFinished").setValue("true").addOnCompleteListener(new OnCompleteListener<Void>() {
                        //                    @Override
                        //                    public void onComplete(@NonNull Task<Void> task) {
                        //                        firebaseAuth.signInWithEmailAndPassword(getIntent().getStringExtra("email"), getIntent().getStringExtra("password")).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        //                            @Override
                        //                            public void onComplete(@NonNull Task<AuthResult> task) {
                        //                                progressDialog.close();
                        //                                startActivity(new Intent(CreateProfileActivity.this, CircleConfigActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        //                                        .putExtra("password", getIntent().getStringExtra("password"))
                        //                                        .putExtra("phone_number", getIntent().getStringExtra("phone_number"))
                        //                                        .putExtra("email", getIntent().getStringExtra("email"))
                        //                                        .putExtra("userName", nameField.getText().toString().trim()));
                        //                            }
                        //                        });
                        //                    }
                        //                });
                        //            }
                        //        });
                        //    }
                        //});
                    }
                 }
            }
        });
    }

    @Override
    public void onCircleDataChanged(CircleModel circleModel) {

    }

    @Override
    public void onUserDataChanged(UserModel userModel) {

    }

    @Override
    public void onUserLoggedOut() {

    }

    @Override
    public void onUserSignedIn() {

    }

    private boolean validName(String s) {
        return !TextUtils.isEmpty(s) && nameField.getText().toString().trim().length() > 3;
    }

    private void launchCrop(){
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri resultUri = CropImage.getActivityResult(data).getUri();
                Picasso.with(this).load(resultUri).into(profilePhoto);
                Picasso.with(this).load(resultUri).into(mapPhotoView);

                Bitmap thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setMaxWidth(720)
                            .setMaxHeight(1280)
                            .setQuality(75)
                            .compressToBitmap(new File(resultUri.getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //uploadPhotoDialog = new LDialogUtility(CreateProfileActivity.this);
                //uploadPhotoDialog.createProgressDialog("Загрузка фото...");
                //uploadPhotoDialog.show();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                final byte[] thumb_byte = byteArrayOutputStream.toByteArray();

                new AsyncPhotoLoader(thumb_byte).execute();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class AsyncPhotoLoader extends AsyncTask<Void, Void, Void>{
        byte[] photoBytes;
        AsyncPhotoLoader(byte[] bytes){
            this.photoBytes = bytes;
        }
        @Override
        protected Void doInBackground(Void... voids) {

            StorageReference filepath = storageReference.child("user_photos").child(firebaseUser.getUid() + ".jpg");
            UploadTask uploadTask = filepath.putBytes(photoBytes);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                    if (thumb_task.isSuccessful()){
                        storageReference.child("user_photos").child(firebaseUser.getUid() + ".jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull final Task<Uri> task) {
                                currentUserDatabase.child("photoUrl").setValue(task.getResult().toString());
                                photoUploaded = true;
                                //uploadPhotoDialog.close();
                            }
                        });
                    }
                }
            });
            return null;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
