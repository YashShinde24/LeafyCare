package com.example.leafy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class StartAccount extends AppCompatActivity {
Button login,signup;
    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;
    LinearLayout btnGoogleSignIn;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_account);
        signup = findViewById(R.id.signup);

        backButton = findViewById(R.id.backArrow);
        backButton.setOnClickListener(v -> onBackPressed());
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartAccount.this,SignUp.class);
                startActivity(intent);
            }
        });
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartAccount.this,SignIn.class);
                startActivity(intent);
            }
        });
        btnGoogleSignIn = findViewById(R.id.googlebtn);

        // Configure sign-in to request the user's basic profile and email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            if (task.isSuccessful()) {
                GoogleSignInAccount account = task.getResult();

                if (account != null) {
                    String name = account.getDisplayName();
                    String email = account.getEmail();
                    String imageUrl = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "";

                    // Save name and email
                    getSharedPreferences("UserPrefs", MODE_PRIVATE)
                            .edit()
                            .putString("name", name)
                            .putString("email", email)
                            .apply();

                    // Download and process image in background thread
                    new Thread(() -> {
                        try {
                            URL url = new URL(imageUrl);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            Bitmap originalBitmap = BitmapFactory.decodeStream(input);

                            // Apply circular crop
                            Bitmap circularBitmap = getCircularBitmap(originalBitmap);

                            // Convert to Base64
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            circularBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] imageBytes = baos.toByteArray();
                            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                            // Save to SharedPreferences
                            SharedPreferences prefs = getSharedPreferences("LeafyPrefs", MODE_PRIVATE);
                            prefs.edit().putString("profile_image", encodedImage).apply();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).start();

                    Toast.makeText(this, "Welcome " + name, Toast.LENGTH_SHORT).show();
                    Log.d("LeafyCare", "Name: " + name);
                    Log.d("LeafyCare", "Email: " + email);
                    Log.d("LeafyCare", "Image URL: " + imageUrl);

                    Intent intent = new Intent(StartAccount.this, Home.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "Google Sign-In failed.", Toast.LENGTH_SHORT).show();
                    Log.e("LeafyCare", "Sign-in failed: " + task.getException());
                }
            }
        }
    }
    private Bitmap getCircularBitmap(Bitmap bitmap) {
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());

        // Create a square cropped bitmap
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap,
                (bitmap.getWidth() - size) / 2,
                (bitmap.getHeight() - size) / 2,
                size, size);

        // Create an output bitmap with a transparent background
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);

        // Draw a circle
        float radius = size / 2f;
        canvas.drawCircle(radius, radius, radius, paint);

        // Set Xfermode to crop the image
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(croppedBitmap, 0, 0, paint);

        return output;
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Change navigation bar color again when resuming the activity
        setNavigationBarColor("#81C784");
        if (!NoInternetDialog.isNetworkAvailable(StartAccount.this)) {
            NoInternetDialog.show(this, new NoInternetDialog.RetryCallback() {
                @Override
                public void onRetry() {
                    // Logic to retry the operation (e.g., API call or refresh action)
                    if (NoInternetDialog.isNetworkAvailable(StartAccount.this)) {
                        // Proceed with your logic, e.g., refresh data or retry the action
                        NoInternetDialog.dismiss(); // Close the dialog
                    } else {
                        // Network still not available, show the dialog again
                        NoInternetDialog.show(StartAccount.this, this);
                    }
                }
            });
        }

    }

    private void setNavigationBarColor(String colorHex) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setNavigationBarColor(Color.parseColor(colorHex)); // Set color
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
