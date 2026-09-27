package thanhdnh.ueh.edu.article_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  private GridView gridView;
  private ProgressBar progressBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }

    // Ánh xạ View
    gridView = findViewById(R.id.gridview);
    progressBar = findViewById(R.id.progressBar);

    // Link RAW của users.json
    String url =
            "https://raw.githubusercontent.com/mdattrnh/Moblie_Lap_2/main/users.json";

    // Load dữ liệu User
    UserData userData =
            new UserData(
                    this,
                    gridView,
                    progressBar
            );

    userData.loadData(url);

    // Khi click vào một User
    gridView.setOnItemClickListener(
            new AdapterView.OnItemClickListener() {

              @Override
              public void onItemClick(
                      AdapterView<?> parent,
                      View view,
                      int position,
                      long id) {

                Intent intent =
                        new Intent(
                                MainActivity.this,
                                UserDetailActivity.class
                        );

                // Truyền id User sang màn Detail
                intent.putExtra("id", id);

                startActivity(intent);
              }
            }
    );
  }
}