package com.example.androidlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ImageView catImageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        catImageView = findViewById(R.id.catImageView);
        progressBar = findViewById(R.id.progressBar);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(new CatImages(this), 0, 5, TimeUnit.SECONDS);
    }

    private static class CatImages implements Runnable {
        private final MainActivity activity;

        public CatImages(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            try {
                JSONObject jsonObject = fetchCatJson();
                Log.d(TAG, "Fetched JSON: " + jsonObject.toString());

                if (jsonObject.has("_id")) {
                    String imageId = jsonObject.getString("_id");
                    String imageUrl = "https://cataas.com/cat/" + imageId;
                    Log.d(TAG, "Constructed image URL: " + imageUrl);

                    File imageFile = new File(activity.getFilesDir(), imageId + ".jpg");
                    Bitmap bitmap;
                    if (imageFile.exists()) {
                        Log.d(TAG, "Loading image from cache: " + imageFile.getAbsolutePath());
                        bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    } else {
                        Log.d(TAG, "Downloading new image from URL: " + imageUrl);
                        bitmap = downloadImage(imageUrl);
                        saveImage(bitmap, imageFile);
                    }

                    activity.runOnUiThread(() -> {
                        activity.catImageView.setImageBitmap(bitmap);
                        activity.updateProgressBar();
                    });
                } else {
                    Log.e(TAG, "JSON response does not contain '_id' key. Full response: " + jsonObject.toString());
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in downloading cat image", e);
            }
        }

        private JSONObject fetchCatJson() throws Exception {
            URL url = new URL("https://cataas.com/cat?json=true");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            StringBuilder jsonBuilder = new StringBuilder();
            int read;
            byte[] buffer = new byte[1024];
            while ((read = inputStream.read(buffer)) != -1) {
                jsonBuilder.append(new String(buffer, 0, read));
            }
            inputStream.close();

            return new JSONObject(jsonBuilder.toString());
        }

        private Bitmap downloadImage(String url) throws Exception {
            URL imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.connect();

            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();

            return bitmap;
        }

        private void saveImage(Bitmap bitmap, File file) throws Exception {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
        }
    }

    private void updateProgressBar() {
        for (int i = 0; i < 100; i++) {
            final int progress = i;
            runOnUiThread(() -> progressBar.setProgress(progress));
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                Log.e(TAG, "Progress update interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
