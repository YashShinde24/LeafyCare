package com.example.leafy;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

public class NoInternetDialog {

    private static AlertDialog dialog;

    public interface RetryCallback {
        void onRetry();
    }

    // Method to check if network is available
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static void show(Context context, RetryCallback callback) {
        if (dialog != null && dialog.isShowing()) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.TransparentDialogStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.activity_internet_error_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);

        TextView tvErrorMessage = view.findViewById(R.id.tv_error_message);
        Button retryButton = view.findViewById(R.id.btn_retry);

        // Styled message
        String message = "Oops! It seems like you're not connected to the internet.\nPlease check your connection and try again.";
        SpannableString styledMessage = new SpannableString(message);

        styledMessage.setSpan(new StyleSpan(Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledMessage.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        styledMessage.setSpan(new RelativeSizeSpan(1.2f), 70, 100, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        styledMessage.setSpan(new ForegroundColorSpan(Color.BLUE), 70, 100, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvErrorMessage.setText(styledMessage);

        retryButton.setOnClickListener(v -> {
            dialog.dismiss();
            if (callback != null) {
                callback.onRetry();
            }
        });

        dialog = builder.create();
        dialog.show();

        // Check if network is available after showing the dialog
        if (!isNetworkAvailable(context)) {
            // Network is unavailable, show the dialog
            dialog.show();
        } else {
            // If network is available, dismiss the dialog if needed
            dismiss();
        }
    }

    public static void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
