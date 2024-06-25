package id.ac.astra.polman.nim0320220008.myled.listadapter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import id.ac.astra.polman.nim0320220008.myled.R;
import id.ac.astra.polman.nim0320220008.myled.adapter.RoomAdapter;
import id.ac.astra.polman.nim0320220008.myled.model.Ruangan;

public class DetailActivity extends AppCompatActivity {

    private ListView houseListView;
    private RoomAdapter houseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        // Get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            String houseId = intent.getStringExtra("house_id");
            String houseName = intent.getStringExtra("house_name");
            String houseAddress = intent.getStringExtra("house_address");

            // Set data to views in activity_detail.xml
            //TextView txtHouseId = findViewById(R.id.txt_house_id);
            TextView txtHouseName = findViewById(R.id.txt_house_name);
            TextView txtHouseAddress = findViewById(R.id.txt_house_address);

            //txtHouseId.setText(houseId);
            txtHouseName.setText(houseName);
            txtHouseAddress.setText(houseAddress);
        }


        houseListView = findViewById(R.id.room_list);

        // Buat daftar rumah contoh
        List<Ruangan> houses = new ArrayList<>();
        houses.add(new Ruangan("1", "A","Ruangan Sendiri","",""));
        houses.add(new Ruangan("2", "B","Ruangan Besar","",""));

        // Buat adapter dan hubungkan dengan ListView
        houseAdapter = new RoomAdapter(this, houses,"");
        houseListView.setAdapter(houseAdapter);
    }
}
