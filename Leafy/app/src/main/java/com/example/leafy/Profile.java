package com.example.leafy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Profile extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int CAPTURE_IMAGE = 2;
    private TextView tvDisplayName, tvEmail, tvPhoneNumber;
    private ImageView profileImage;
    private ImageButton editProfileButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            onBackPressed(); // Navigates back to the previous activity
        });
        tvDisplayName = findViewById(R.id.tvDisplayName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        profileImage = findViewById(R.id.proimage);
        editProfileButton = findViewById(R.id.editProfileButton);

        tvDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Editpage.class);
                intent.putExtra("field", "display_name");
                intent.putExtra("title", "Edit Display Name");
                startActivity(intent);
            }
        });

        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Editpage.class);
                intent.putExtra("field", "email");
                intent.putExtra("title", "Edit Email Address");
                startActivity(intent);
            }
        });

        tvPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Editpage.class);
                intent.putExtra("field", "phone");
                intent.putExtra("title", "Edit Phone Number");
                startActivity(intent);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }

    private void selectImage() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooserIntent = Intent.createChooser(pickIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{captureIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = null;
            Uri selectedImageUri = null;

            if (requestCode == PICK_IMAGE && data != null) {
                selectedImageUri = data.getData();
            } else if (requestCode == CAPTURE_IMAGE && data != null) {
                bitmap = (Bitmap) data.getExtras().get("data");
            }

            if (selectedImageUri != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    bitmap = rotateImageIfRequired(bitmap, selectedImageUri); // Fix Rotation
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bitmap != null) {
                Bitmap circularBitmap = getCircularBitmap(bitmap); // Convert to circular
                saveImageToPrefs(circularBitmap, getApplicationContext()); // Save to SharedPreferences
            }

        }
    }

    // Method to check and fix image rotation
    private Bitmap rotateImageIfRequired(Bitmap bitmap, Uri imageUri) throws IOException {
        InputStream input = getContentResolver().openInputStream(imageUri);
        ExifInterface ei = new ExifInterface(input);

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        input.close();

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                break;
            default:
                return bitmap;
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    // Method to convert a bitmap into a circular shape
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
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        tvDisplayName.setText(sharedPreferences.getString("name", "User's Name"));
        tvEmail.setText(sharedPreferences.getString("email", "user@example.com"));
        tvPhoneNumber.setText(sharedPreferences.getString("phone", "+91 9999999999"));

        // Load saved profile image
        Bitmap savedBitmap = getImageFromPrefs(getApplicationContext());
        if (savedBitmap != null) {
            profileImage.setPadding(0, 0, 0, 0); // Remove padding if any
            profileImage.setImageBitmap(savedBitmap);
        }
    }


    public void saveImageToPrefs(Bitmap bitmap, Context context) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); // Compress to PNG
        byte[] imageBytes = baos.toByteArray();
        String imageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        SharedPreferences prefs = context.getSharedPreferences("LeafyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("profile_image", imageBase64);
        editor.apply();
    }
    public Bitmap getImageFromPrefs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("LeafyPrefs", Context.MODE_PRIVATE);
        String imageBase64 = prefs.getString("profile_image", null);

        if (imageBase64 != null) {
            byte[] imageBytes = Base64.decode(imageBase64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        }
        return null;
    }


}