package com.phamhuong.library.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.phamhuong.library.R;

public class CustomDialogHelper {

    public static void showCustomDialogSuccess(Context context, String title, String message,
                                        DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflate layout tùy chỉnh
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.custom_dialog_layout, null);
        builder.setView(customView);

        // Tìm các view trong layout tùy chỉnh
        ImageView imgIcon = customView.findViewById(R.id.imgPopup);
        TextView dialogTitle = customView.findViewById(R.id.dialogTitle);
        TextView dialogMessage = customView.findViewById(R.id.dialogMessage);
        Button positiveButton = customView.findViewById(R.id.okButton);

        imgIcon.setImageResource(R.drawable.ic_success);
        // Thiết lập dữ liệu cho các view
        dialogTitle.setText(title);
        dialogMessage.setText(message);

        // Tạo và hiển thị Dialog
        AlertDialog alertDialog = builder.create();
        // Thiết lập OnClickListener cho các nút
        positiveButton.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }
    public static void showCustomDialogFail(Context context, String title, String message,
                                         DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Inflate layout tùy chỉnh
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView = inflater.inflate(R.layout.custom_dialog_layout, null);
        builder.setView(customView);

        // Tìm các view trong layout tùy chỉnh
        ImageView imgIcon = customView.findViewById(R.id.imgPopup);
        TextView dialogTitle = customView.findViewById(R.id.dialogTitle);
        TextView dialogMessage = customView.findViewById(R.id.dialogMessage);
        Button positiveButton = customView.findViewById(R.id.okButton);

        imgIcon.setImageResource(R.drawable.ic_warning);
        // Thiết lập dữ liệu cho các view
        dialogTitle.setText(title);
        dialogMessage.setText(message);

        // Tạo và hiển thị Dialog
        AlertDialog alertDialog = builder.create();
        // Thiết lập OnClickListener cho các nút
        positiveButton.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }
    public static void showWishlistSuccessPopup(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_dialog_layout, null);
        builder.setView(view);

        ImageView imgIcon = view.findViewById(R.id.imgPopup);
        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        TextView dialogMessage = view.findViewById(R.id.dialogMessage);
        Button positiveButton = view.findViewById(R.id.okButton);

        imgIcon.setImageResource(R.drawable.ic_wishlist_added);
        dialogTitle.setText("Đã thêm vào Yêu thích!");
        dialogMessage.setText("Cuốn sách đã được thêm vào danh sách yêu thích của bạn.");

        AlertDialog alertDialog = builder.create();
        positiveButton.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }

    public static void showWishlistFailurePopup(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_dialog_layout, null);
        builder.setView(view);

        ImageView imgIcon = view.findViewById(R.id.imgPopup);
        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        TextView dialogMessage = view.findViewById(R.id.dialogMessage);
        Button positiveButton = view.findViewById(R.id.okButton);

        imgIcon.setImageResource(R.drawable.ic_wishlist_failed); // Thay thế bằng icon wishlist thất bại
        dialogTitle.setText("Thêm thất bại!");
        dialogMessage.setText("Không thể thêm cuốn sách vào danh sách yêu thích. Vui lòng thử lại sau.");

        AlertDialog alertDialog = builder.create();
        positiveButton.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }


    public static void showRenewSuccessPopup(Context context, String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_dialog_layout, null);
        builder.setView(view);

        ImageView imgIcon = view.findViewById(R.id.imgPopup);
        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        TextView dialogMessage = view.findViewById(R.id.dialogMessage);
        Button positiveButton = view.findViewById(R.id.okButton);

        imgIcon.setImageResource(R.drawable.ic_renew_success);
        dialogTitle.setText("Gia hạn thành công!");
        dialogMessage.setText(errorMessage);

        AlertDialog alertDialog = builder.create();
        positiveButton.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }

    public static void showRenewFailurePopup(Context context, String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_dialog_layout, null);
        builder.setView(view);

        ImageView imgIcon = view.findViewById(R.id.imgPopup);
        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        TextView dialogMessage = view.findViewById(R.id.dialogMessage);
        Button positiveButton = view.findViewById(R.id.okButton);

        imgIcon.setImageResource(R.drawable.ic_renew_failure);
        dialogTitle.setText("Gia hạn thất bại!");
        dialogMessage.setText(errorMessage);

        AlertDialog alertDialog = builder.create();
        positiveButton.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }
    public static void showReservationSuccessPopup(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_dialog_layout, null);
        builder.setView(view);

        ImageView imgIcon = view.findViewById(R.id.imgPopup);
        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        TextView dialogMessage = view.findViewById(R.id.dialogMessage);
        Button dialogButton = view.findViewById(R.id.okButton);

        imgIcon.setImageResource(R.drawable.ic_reservation_success);
        dialogTitle.setText("Đặt lịch thành công!");
        dialogMessage.setText("Yêu cầu đặt lịch của bạn đã được gửi thành công.");

        AlertDialog alertDialog = builder.create();
        dialogButton.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }

    public static void showReservationFailurePopup(Context context, String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_dialog_layout, null);
        builder.setView(view);

        ImageView imgIcon = view.findViewById(R.id.imgPopup);
        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        TextView dialogMessage = view.findViewById(R.id.dialogMessage);
        Button dialogButton = view.findViewById(R.id.okButton);

        imgIcon.setImageResource(R.drawable.ic_reservation_failed);
        dialogTitle.setText("Đặt lịch thất bại!");
        dialogMessage.setText(errorMessage != null ? errorMessage : "Không thể đặt lịch mượn sách. Vui lòng thử lại sau.");

        AlertDialog alertDialog = builder.create();
        dialogButton.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }
    public static void showNoBookSelectedForReservationPopup(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_dialog_layout, null); // Tạo một layout warning riêng
        builder.setView(view);

        ImageView imgIcon = view.findViewById(R.id.imgPopup);
        TextView dialogTitle = view.findViewById(R.id.dialogTitle);
        TextView dialogMessage = view.findViewById(R.id.dialogMessage);
        Button dialogButton = view.findViewById(R.id.okButton);

        imgIcon.setImageResource(R.drawable.ic_warning); // Cần tạo icon warning
        dialogTitle.setText("Chưa chọn sách!");
        dialogMessage.setText("Vui lòng chọn ít nhất một cuốn sách để đặt lịch.");

        AlertDialog alertDialog = builder.create();
        dialogButton.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
    }
}