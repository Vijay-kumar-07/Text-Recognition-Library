package com.hyundai.textrecognition;

import android.content.Context;
import android.graphics.Bitmap;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class TextRecognitionProcessor {
    private final TextRecognizer recognizer;

    public TextRecognitionProcessor(Context context) {
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
    }

    public void recognizeTextFromImage(Bitmap bitmap, final TextRecognitionCallback callback) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        recognizer.process(image)
                .addOnSuccessListener(text -> callback.onSuccess(text))
                .addOnFailureListener(e -> callback.onFailure(e));
    }

    public interface TextRecognitionCallback {
        void onSuccess(Text text);
        void onFailure(Exception e);
    }
}
