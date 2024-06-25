package id.ac.astra.polman.nim0320220008.myled.gakepake;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import id.ac.astra.polman.nim0320220008.myled.R;
import id.ac.astra.polman.nim0320220008.myled.model.User;


public class RumahListAdapter extends ArrayAdapter<User> {
    private Activity context;
    private List<User> userList;

    public RumahListAdapter(Activity context, List<User> userList) {
        super(context, R.layout.layout_user_list, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.layout_user_list, null, true);

        TextView textViewName = listViewItem.findViewById(R.id.textViewName);
        TextView textViewEmail = listViewItem.findViewById(R.id.textViewEmail);

        User user = userList.get(position);

        textViewName.setText(user.getNama());
        textViewEmail.setText(user.getEmail());

        return listViewItem;
    }
    @Override
    public long getItemId(int position) {
        return position; // or use user id if available
    }

}

