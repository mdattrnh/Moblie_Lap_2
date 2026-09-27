package thanhdnh.ueh.edu.article_app;

import android.content.Context;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class UserData {

    public static UserList data;

    private Context context;
    private GridView gridView;
    private ProgressBar progressBar;

    public UserData(Context context,
                    GridView gridView,
                    ProgressBar progressBar) {

        this.context = context;
        this.gridView = gridView;
        this.progressBar = progressBar;
    }

    // Tìm User theo id
    public static UserProfile getUserFromId(int id) {

        if (data == null || data.getUsers() == null) {
            return null;
        }

        for (UserProfile user : data.getUsers()) {

            if (user.getId() == id) {
                return user;
            }
        }

        return null;
    }

    // Tải file JSON bằng downloadWithProgress
    public void loadData(String url) {

        Downloader.downloadWithProgress(
                url,
                context.getCacheDir(),
                progressBar,

                new Downloader.DownloadCallback() {

                    @Override
                    public void onSuccess(File file) {

                        try {

                            // Đọc file JSON
                            String json = readText(file);

                            // Parse JSON thành UserList
                            Gson gson = new Gson();

                            data = gson.fromJson(
                                    json,
                                    UserList.class
                            );

                            // Kiểm tra dữ liệu
                            if (data != null
                                    && data.getUsers() != null) {

                                UserAdapter adapter =
                                        new UserAdapter(
                                                data.getUsers(),
                                                context
                                        );

                                // Gán Adapter cho GridView
                                gridView.setAdapter(adapter);

                            } else {

                                Toast.makeText(
                                        context,
                                        "User data is empty",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();

                            Toast.makeText(
                                    context,
                                    "Cannot parse user data",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {

                        e.printStackTrace();

                        Toast.makeText(
                                context,
                                "Cannot download user data",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    // Đọc nội dung file JSON
    private String readText(File file) {

        StringBuilder buffer = new StringBuilder();

        try {

            FileInputStream inputStream =
                    new FileInputStream(file);

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(inputStream)
                    );

            String line;

            while ((line = reader.readLine()) != null) {

                buffer.append(line);
                buffer.append("\n");
            }

            reader.close();
            inputStream.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return buffer.toString();
    }
}