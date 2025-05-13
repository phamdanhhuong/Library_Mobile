package com.phamhuong.library.fragment.profile;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.android.material.textfield.TextInputLayout;
import com.phamhuong.library.R;
import com.phamhuong.library.model.ApiResponse;
import com.phamhuong.library.model.RetrofitClient;
import com.phamhuong.library.model.UserLoginInfo;
import com.phamhuong.library.service.APIService;
import com.phamhuong.library.service.DatabaseHelper;

import java.io.File;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentEditProfile extends Fragment {
    TextInputLayout fullName;
    String fullNameString;
    TextInputLayout phoneNumber;
    String phoneNumberString;
    TextInputLayout email;
    String emailString;
    TextInputLayout username;
    String usernameString;
    ImageView imgAvatar;
    String avatarUrl;
    APIService apiService;
    Button btnSave;
    Cloudinary cloudinary;
    private static final int REQUEST_PERMISSION = 100;
    private static final int REQUEST_PICK_IMAGE = 101;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        fullName = view.findViewById(R.id.txtFullName);
        phoneNumber = view.findViewById(R.id.txtPhoneNumber);
        email = view.findViewById(R.id.txtEmail);
        username = view.findViewById(R.id.txtUsername);
        imgAvatar = view.findViewById(R.id.img_avatar);
        btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> {
            updateInfo(fullName.getEditText().getText().toString(), phoneNumber.getEditText().getText().toString());
        });
        imgAvatar.setOnClickListener(v -> checkPermissionAndPickImage());
        progressDialog = new ProgressDialog(getActivity());
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dv8sxlyly",
                "api_key", "421839787612154",
                "api_secret", "UYkTyyXAU91n1ixxLoDtMYxV_j0"));
        return view;
    }

    private void checkPermissionAndPickImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION);
            } else {
                openImagePicker();
            }
        } else {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            } else {
                openImagePicker();
            }
        }
    }
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                String imagePath = getRealPathFromURI(selectedImageUri);
                uploadImageToCloudinary(imagePath);
            }
        }
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) return null;

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
    private void uploadImageToCloudinary(String imagePath) {
        progressDialog.setMessage("Đang upload avatar...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new Thread(() -> {
            try {
                Map uploadResult = cloudinary.uploader().upload(new File(imagePath), ObjectUtils.asMap(
                        "resource_type", "image"
                ));
                String imageUrl = (String) uploadResult.get("secure_url");

                getActivity().runOnUiThread(() -> {
                    // Hiển thị luôn ảnh mới
                    progressDialog.dismiss();
                    updateAvatar(imageUrl);
                });

            } catch (Exception e) {
                progressDialog.dismiss();
                e.printStackTrace();
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Lỗi upload ảnh!", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        UserLoginInfo userLoginInfo = dbHelper.getLoginInfoSQLite();
        if (userLoginInfo != null) {
            avatarUrl = userLoginInfo.getAvatarUrl();
            fullNameString = userLoginInfo.getFullName();
            phoneNumberString = userLoginInfo.getPhoneNumber();
            emailString = userLoginInfo.getEmail();
            usernameString = userLoginInfo.getUsername();
        } else {
            // Xử lý trường hợp không có thông tin đăng nhập
            Log.e("FragmentUserInfo", "Không tìm thấy thông tin người dùng.");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Glide.with(this)
                    .load(avatarUrl)
                    .placeholder(R.drawable.avatar1)
                    .error(R.drawable.avatar1)
                    .into(imgAvatar);
        } else {
            imgAvatar.setImageResource(R.drawable.avatar1);
        }
        fullName.getEditText().setText(fullNameString);
        phoneNumber.getEditText().setText(phoneNumberString);
        email.getEditText().setText(emailString);
        username.getEditText().setText(usernameString);
    }

    public void updateInfo(String fullName, String phoneNumber){
        Map<String, String> requestBody = Map.of(
                "username", usernameString,
                "phone_number", phoneNumber,
                "full_name", fullName
        );
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Call<ApiResponse> call = apiService.updateInfo(requestBody);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                        dbHelper.updateInfo(usernameString, fullName, phoneNumber);
                        requireActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    public void updateAvatar(String url){
        Map<String, String> requestBody = Map.of(
                "username", usernameString,
                "url", url
        );
        apiService = RetrofitClient.getRetrofit().create(APIService.class);
        Call<ApiResponse> call = apiService.avatarUpdate(requestBody);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
                        dbHelper.updateAvatarUrl(usernameString, url);
                        requireActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
}
