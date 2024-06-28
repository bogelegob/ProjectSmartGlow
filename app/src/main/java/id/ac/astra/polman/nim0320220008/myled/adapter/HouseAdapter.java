package id.ac.astra.polman.nim0320220008.myled.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.ac.astra.polman.nim0320220008.myled.listadapter.DetailActivity;
import id.ac.astra.polman.nim0320220008.myled.R;
import id.ac.astra.polman.nim0320220008.myled.listadapter.MainActivity_Ruangan;
import id.ac.astra.polman.nim0320220008.myled.model.Rumah;
import id.ac.astra.polman.nim0320220008.myled.model.User;

public class HouseAdapter extends ArrayAdapter<Rumah> {

    private Activity context;
    private List<Rumah> userList;
    private String creadby;
    private String owner;
    public HouseAdapter(Context context, List<Rumah> houses,String creadby, String owner) {
        super(context, R.layout.layout_home_list, houses);
        this.context = (Activity) context;
        this.userList = houses;
        this.creadby = creadby;
        this.owner = owner;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        //Rumah house = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_home_list, parent, false);
        }

        // Lookup view for data population
        ImageView imgDevice = convertView.findViewById(R.id.imgDevice1);
        TextView txtData = convertView.findViewById(R.id.txtData1);


        TextView txtDataDetail = convertView.findViewById(R.id.txtDataDetail1);
        //Switch btnDevice = convertView.findViewById(R.id.btnDevice1);

        Rumah oi = userList.get(position);
        // Populate the data into the template view using the data object
        if (oi != null) {
            // Here you can set the values based on the house object
            txtData.setText(oi.getNama());
            txtDataDetail.setText(oi.getAlamat_rumah());
            // Set image resource if you have any
            //imgDevice.setImageResource(R.drawable.shofabaru);
        }

        // Set onClickListener for the convertView (item in the list)
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to show house detail
                showHouseDetail(oi,creadby);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }

    private void showHouseDetail(Rumah house, String creadby) {
        // Open detail activity or show detail dialog
        Intent intent = new Intent(getContext(), MainActivity_Ruangan.class);
        intent.putExtra("house_id", house.getRumahId());
        intent.putExtra("creadby", creadby);
        intent.putExtra("house_name", house.getNama());
        intent.putExtra("house_address", house.getAlamat_rumah());
        intent.putExtra("owner", owner);
        // Add other necessary data
        getContext().startActivity(intent);
    }
}
