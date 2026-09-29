package thanhdnh.ueh.edu.article_app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class UserDetailActivity extends AppCompatActivity {

  private ImageView ivAvatar;

  private TextView tvUsername;
  private TextView tvEmail;
  private TextView tvDecs;
  private TextView tvHobby;

  private Button btnBack;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_detail);

    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }

    // Ánh xạ View
    btnBack = findViewById(R.id.btn_back);

    ivAvatar = findViewById(R.id.iv_detail_avatar);

    tvUsername = findViewById(R.id.tv_detail_username);
    tvEmail = findViewById(R.id.tv_detail_email);
    tvDecs = findViewById(R.id.tv_detail_decs);
    tvHobby = findViewById(R.id.tv_detail_hobby);

    // Nút Back
    btnBack.setOnClickListener(v -> finish());

    // Nhận id từ MainActivity
    int id = (int) getIntent().getLongExtra("id", 0);

    // Tìm user theo id
    UserProfile user = UserData.getUserFromId(id);

    if (user == null) {
      finish();
      return;
    }

    // Hiển thị dữ liệu
    tvUsername.setText(user.getUsername());

    tvEmail.setText(
            "Email: " + user.getEmail()
    );

    tvDecs.setText(
            user.getDecs()
    );

    tvHobby.setText(
            user.getHobby()
    );

    // Load avatar từ URL
    Picasso.get()
            .load(user.getAvatar_url())
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .fit()
            .centerCrop()
            .into(ivAvatar);
  }
}