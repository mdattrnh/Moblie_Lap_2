package thanhdnh.ueh.edu.article_app;

import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Downloader {

  public interface DownloadCallback {
    void onSuccess(File file);

    void onFailure(Exception e);
  }

  public static void downloadWithProgress(
          String inputUrl,
          File cacheDir,
          ProgressBar progressBar,
          DownloadCallback callback) {

    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
            .url(inputUrl)
            .build();

    Handler mainHandler = new Handler(Looper.getMainLooper());

    mainHandler.post(() -> {
      progressBar.setVisibility(ProgressBar.VISIBLE);
      progressBar.setProgress(0);
    });

    client.newCall(request).enqueue(new Callback() {

      @Override
      public void onFailure(Call call, IOException e) {

        mainHandler.post(() -> {
          progressBar.setVisibility(ProgressBar.GONE);
          callback.onFailure(e);
        });
      }

      @Override
      public void onResponse(Call call, Response response) {

        if (!response.isSuccessful() || response.body() == null) {

          mainHandler.post(() -> {
            progressBar.setVisibility(ProgressBar.GONE);
            callback.onFailure(
                    new IOException("Download failed")
            );
          });

          return;
        }

        long totalBytes = response.body().contentLength();

        try {

          File file = File.createTempFile(
                  "users_",
                  ".json",
                  cacheDir
          );

          InputStream inputStream =
                  response.body().byteStream();

          OutputStream outputStream =
                  new FileOutputStream(file);

          byte[] buffer = new byte[1024];

          long downloadedBytes = 0;

          int bytesRead;

          while ((bytesRead = inputStream.read(buffer)) != -1) {

            outputStream.write(
                    buffer,
                    0,
                    bytesRead
            );

            downloadedBytes += bytesRead;

            if (totalBytes > 0) {

              int progress =
                      (int) (
                              downloadedBytes
                                      * 100
                                      / totalBytes
                      );

              mainHandler.post(
                      () -> progressBar.setProgress(progress)
              );
            }
          }

          outputStream.flush();
          outputStream.close();
          inputStream.close();

          mainHandler.post(() -> {

            progressBar.setProgress(100);

            progressBar.setVisibility(
                    ProgressBar.GONE
            );

            callback.onSuccess(file);
          });

        } catch (Exception e) {

          mainHandler.post(() -> {

            progressBar.setVisibility(
                    ProgressBar.GONE
            );

            callback.onFailure(e);
          });
        }
      }
    });
  }
}