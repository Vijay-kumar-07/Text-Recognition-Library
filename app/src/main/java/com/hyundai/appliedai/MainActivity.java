package com.hyundai.appliedai;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import androidx.annotation.Nullable;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_VIDEO_CAPTURE = 2;
    private Bitmap capturedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showCaptureOptions(); // Call to show capture options when the app starts
    }

    private void showCaptureOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an option")
                .setItems(new String[]{"Take Photo", "Record Video", "Text Recognition"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            openCameraForPhoto();
                        } else if (which == 1) {
                            openCameraForVideo();
                        } else if (which == 2) {
                            openTextRecognition();
                        }
                    }
                })
                .show();
    }
    private void openCameraForPhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void openCameraForVideo() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    private void openTextRecognition() {
        if (capturedImage != null) {
            recognizeTextFromImage(capturedImage);
        } else {
            Toast.makeText(this, "No image available for text recognition", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            capturedImage = (Bitmap) extras.get("data");
            Toast.makeText(this, "Photo Captured", Toast.LENGTH_SHORT).show();
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Toast.makeText(this, "Video Recorded", Toast.LENGTH_SHORT).show();
        }
    }
    private void recognizeTextFromImage(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        // Use the default text recognizer for Latin-based languages
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        recognizer.process(image)
                .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text text) {
                        String resultText = text.getText();
                        Toast.makeText(MainActivity.this, "Text: " + resultText, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new com.google.android.gms.tasks.OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(MainActivity.this, "Text Recognition Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        Button cameraButton = findViewById(R.id.btn_camera);
//        cameraButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(MainActivity.this,
//                            new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
//                } else {
//                    showCaptureOptions();
//                }
//            }
//        });
//    }
//
//    private void showCaptureOptions() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Choose an option")
//                .setItems(new String[]{"Take Photo", "Record Video", "Text Recognition"}, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == 0) {
//                            openCameraForPhoto();
//                        } else if (which == 1) {
//                            openCameraForVideo();
//                        } else if (which == 2) {
//                            openTextRecognition();
//                        }
//                    }
//                })
//                .show();
//    }
//
//    private void openTextRecognition() {
//        // Call TextRecognitionProcessor from the imported library
//        TextRecognitionProcessor processor = new TextRecognitionProcessor(this);
//        // Assume we have a Bitmap image from camera to process
//        processor.recognizeTextFromImage(bitmap, new TextRecognitionProcessor.TextRecognitionCallback() {
//            @Override
//            public void onSuccess(Text text) {
//                // Handle successful text recognition, e.g., display detected text
//                Toast.makeText(MainActivity.this, "Text: " + text.getText(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Toast.makeText(MainActivity.this, "Text Recognition Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//    private void openCameraForPhoto() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        } else {
//            Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void openCameraForVideo() {
//        Intent recordVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        if (recordVideoIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(recordVideoIntent, REQUEST_VIDEO_CAPTURE);
//        } else {
//            Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
//        }
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CAMERA_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                showCaptureOptions();
//            } else {
//                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//}