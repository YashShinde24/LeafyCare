package com.example.leafy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leafy.ml.ModelUnquant;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Diseases extends Fragment {
    int imageSize = 224;
    private static final int PICK_IMAGE = 1;
    private static final int CAPTURE_IMAGE = 2;

    public Diseases() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diseases, container, false);
        LinearLayout image = view.findViewById(R.id.checkNowBtn);

        ImageView infoIcon = view.findViewById(R.id.infoIcon);
        infoIcon.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("How to Use")
                    .setMessage("Tap the 'Check Now' button to open your camera.\n\nCapture a clear image of your plant. We'll analyze it and show you the health diagnosis in the next screen.")
                    .setPositiveButton("Got it", null)
                    .show();
        });

        image.setOnClickListener(v -> selectImage());

        return view;
    }

    private void selectImage() {
        // Intent for selecting image from the gallery
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        // Intent for capturing image with the camera
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Creating a chooser intent to allow the user to select or capture an image
        Intent chooserIntent = Intent.createChooser(pickIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{captureIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    public void classifyImage(Bitmap image1) {
        Dialog loadingDialog = new Dialog(requireContext());
        loadingDialog.setContentView(R.layout.dialog_loading); // Create this XML layout
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        new android.os.Handler().postDelayed(() -> {
            try {
                ModelUnquant model = ModelUnquant.newInstance(requireContext());

                // Preprocess
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
                ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
                byteBuffer.order(ByteOrder.nativeOrder());

                int[] intValue = new int[imageSize * imageSize];
                image1.getPixels(intValue, 0, image1.getWidth(), 0, 0, image1.getWidth(), image1.getHeight());

                int pixel = 0;
                for (int i = 0; i < imageSize; i++) {
                    for (int j = 0; j < imageSize; j++) {
                        int val = intValue[pixel++];
                        byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                        byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                        byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                    }
                }

                inputFeature0.loadBuffer(byteBuffer);

                // Run model
                ModelUnquant.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

                float[] confidence = outputFeature0.getFloatArray();
                String[] classes = {"Black Spot", "Healthy", "Mealybugs", "Powdery", "Rust"};

                int maxPos = 0;
                float maxConfidence = 0;
                for (int i = 0; i < confidence.length; i++) {
                    if (confidence[i] > maxConfidence) {
                        maxConfidence = confidence[i];
                        maxPos = i;
                    }
                }

                String detectedDisease = classes[maxPos];
                loadingDialog.dismiss(); // Hide the loading dialog before showing result

                if (detectedDisease.equals("Healthy")) {
                    showDiseaseDetectedDialog("Healthy");
                } else {
                    showDiseaseDetectedDialog(detectedDisease);
                }

                model.close();

            } catch (IOException e) {
                e.printStackTrace();
                loadingDialog.dismiss();
                Toast.makeText(requireContext(), "Error in processing image", Toast.LENGTH_SHORT).show();
            }
        }, 5000); // Delay for 2.5 seconds

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                if (data != null) {
                    // Handle camera image
                    if (data.getExtras() != null && data.getExtras().get("data") != null) {
                        Bitmap image1 = (Bitmap) data.getExtras().get("data");
                        int dimension = Math.min(image1.getWidth(), image1.getHeight());
                        image1 = ThumbnailUtils.extractThumbnail(image1, dimension, dimension);
                        image1 = Bitmap.createScaledBitmap(image1, imageSize, imageSize, false);
                        classifyImage(image1);
                    } else {
                        // Handle gallery image
                        Uri uri = data.getData();
                        try {
                            Bitmap image1 = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                            image1 = Bitmap.createScaledBitmap(image1, imageSize, imageSize, false);
                            classifyImage(image1);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    }
    private void showDiseaseDetectedDialog(String diseaseName) {
        // Create the dialog object
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_disease_detected);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Add custom animations to the dialog
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogStyle;

        // UI elements from the dialog layout
        ImageView icon = dialog.findViewById(R.id.icon);
        TextView title = dialog.findViewById(R.id.title);
        TextView message = dialog.findViewById(R.id.msg);
        Button okButton = dialog.findViewById(R.id.ok);

        // Set dynamic content based on the disease detected
        if (diseaseName.equals("Healthy")) {
            title.setText("Plant Status");
            message.setText("Your plant looks healthy! 🎉\nNo diseases detected.");
            okButton.setText("Ok");
            icon.setImageResource(R.drawable.ic_disease_detected); // Set an appropriate healthy plant icon
        } else {
            title.setText("Disease Detected!");
            message.setText("We’ve identified \"" + diseaseName + "\" Disease in your plant.");
            icon.setImageResource(R.drawable.ic_disease_detected); // Set an appropriate disease icon
        }

        // Show the dialog
        dialog.show();

        // Handle button click to dismiss the dialog or navigate to DiseaseInfoActivity
        okButton.setOnClickListener(v -> {
            dialog.dismiss();
            if (!diseaseName.equals("Healthy")) {
                // Redirect to DiseaseInfoActivity with disease name
                Intent intent = new Intent(requireContext(), Disease_Info.class);
                intent.putExtra("disease_name", diseaseName);
                startActivity(intent);
            }
        });
    }

}
