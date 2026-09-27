package thanhdnh.ueh.edu.article_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {

  private ArrayList<UserProfile> userList;
  private Context context;

  public UserAdapter(
          ArrayList<UserProfile> userList,
          Context context) {

    this.userList = userList;
    this.context = context;
  }

  @Override
  public int getCount() {
    return userList.size();
  }

  @Override
  public Object getItem(int position) {
    return userList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return userList.get(position).getId();
  }

  @Override
  public View getView(
          int position,
          View convertView,
          ViewGroup parent) {

    ViewHolder holder;

    if (convertView == null) {

      LayoutInflater inflater =
              LayoutInflater.from(context);

      convertView = inflater.inflate(
              R.layout.user_disp_tpl,
              parent,
              false
      );

      holder = new ViewHolder();

      holder.ivAvatar =
              convertView.findViewById(
                      R.id.iv_avatar
              );

      holder.tvUsername =
              convertView.findViewById(
                      R.id.tv_username
              );

      convertView.setTag(holder);

    } else {

      holder =
              (ViewHolder) convertView.getTag();
    }

    UserProfile user =
            userList.get(position);

    holder.tvUsername.setText(
            user.getUsername()
    );

    Picasso.get()
            .load(user.getAvatar_url())
            .placeholder(android.R.drawable.ic_menu_gallery)
            .error(android.R.drawable.ic_menu_report_image)
            .fit()
            .centerCrop()
            .into(holder.ivAvatar);

    return convertView;
  }

  private static class ViewHolder {

    ImageView ivAvatar;
    TextView tvUsername;
  }
}