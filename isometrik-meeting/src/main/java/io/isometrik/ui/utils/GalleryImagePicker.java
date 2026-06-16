package io.isometrik.ui.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.isometrik.meeting.R;

/**
 * Uses Android's photo picker to select an image without storage permissions.
 */
public class GalleryImagePicker {

  public interface Callback {
    void onImagePicked(@NonNull File imageFile);
  }

  private final AppCompatActivity activity;
  private final Callback callback;
  private final ActivityResultLauncher<PickVisualMediaRequest> pickerLauncher;

  public GalleryImagePicker(@NonNull AppCompatActivity activity, @NonNull Callback callback) {
    this.activity = activity;
    this.callback = callback;
    pickerLauncher = activity.registerForActivityResult(
        new ActivityResultContracts.PickVisualMedia(), this::handlePickedImage);
  }

  public void pickImage() {
    pickerLauncher.launch(new PickVisualMediaRequest.Builder()
        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
        .build());
  }

  private void handlePickedImage(Uri imageUri) {
    if (imageUri == null) {
      Toast.makeText(activity, R.string.ism_image_selection_canceled, Toast.LENGTH_LONG).show();
      return;
    }

    try {
      callback.onImagePicked(copyImageToAppStorage(activity, imageUri));
    } catch (IOException e) {
      Toast.makeText(activity, R.string.ism_image_selection_failure, Toast.LENGTH_LONG).show();
    }
  }

  private static File copyImageToAppStorage(@NonNull Context context, @NonNull Uri imageUri)
      throws IOException {
    File imageFile =
        ImageUtil.createImageFile(String.valueOf(System.currentTimeMillis()), false, context);

    try (InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
        OutputStream outputStream = new FileOutputStream(imageFile)) {
      if (inputStream == null) {
        throw new IOException("Unable to open selected image.");
      }

      byte[] buffer = new byte[8192];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
    }

    return imageFile;
  }
}
