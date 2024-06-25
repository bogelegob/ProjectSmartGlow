package id.ac.astra.polman.nim0320220008.myled.listadapter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import id.ac.astra.polman.nim0320220008.myled.AddEditHouseActivity;
import id.ac.astra.polman.nim0320220008.myled.AddEditRoomActivity;
import id.ac.astra.polman.nim0320220008.myled.AddEditUserActivity;
import id.ac.astra.polman.nim0320220008.myled.LoginNew;
import id.ac.astra.polman.nim0320220008.myled.MainActivity_Baru;
import id.ac.astra.polman.nim0320220008.myled.R;
import id.ac.astra.polman.nim0320220008.myled.adapter.HouseAdapter;
import id.ac.astra.polman.nim0320220008.myled.adapter.RoomAdapter;
import id.ac.astra.polman.nim0320220008.myled.model.Ruangan;
import id.ac.astra.polman.nim0320220008.myled.model.Rumah;

public class MainActivity_Ruangan extends AppCompatActivity {

    private ListView houseListView;
    private RoomAdapter houseAdapter;
    private List<Ruangan> ruanganList;
    private Button addButton;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        databaseUsers = FirebaseDatabase.getInstance().getReference("smart_home/ruangan");

        houseListView = findViewById(R.id.room_list);
        ruanganList = new ArrayList<>();

        addButton = findViewById(R.id.add_room_button);

        // Get data from intent
        Intent intent = getIntent();
        if (intent != null) {
            String houseId = intent.getStringExtra("house_id");
            String creadby = intent.getStringExtra("creadby");
            String houseName = intent.getStringExtra("house_name");
            String houseAddress = intent.getStringExtra("house_address");

            // Set data to views in activity_detail.xml
            //TextView txtHouseId = findViewById(R.id.txt_house_id);
            TextView txtHouseName = findViewById(R.id.txt_house_name);
            TextView txtHouseAddress = findViewById(R.id.txt_house_address);

            //txtHouseId.setText(houseId);
            txtHouseName.setText(houseName);
            txtHouseAddress.setText(houseAddress);

            // Query to filter rooms based on houseId
            Query query = databaseUsers.orderByChild("rumahId").equalTo(houseId);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ruanganList.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Ruangan ruangan = postSnapshot.getValue(Ruangan.class);
                        ruanganList.add(ruangan);
                    }
                    RoomAdapter adapter = new RoomAdapter(MainActivity_Ruangan.this, ruanganList,creadby);
                    houseListView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors
                }
            });

            addButton.setOnClickListener(v -> {
                // Open detail activity or show detail dialog
                Intent hoho = new Intent(MainActivity_Ruangan.this, AddEditRoomActivity.class);
                hoho.putExtra("rumahId", houseId);
                hoho.putExtra("creadby", creadby);
                // Add other necessary data
                MainActivity_Ruangan.this.startActivity(hoho);
                //startActivity(new Intent(MainActivity_Ruangan.this, AddEditRoomActivity.class));
            });
        }

        //houseListView = findViewById(R.id.room_list);

        // Buat daftar rumah contoh
//        List<Ruangan> houses = new ArrayList<>();
//        houses.add(new Ruangan("1", "A","Ruangan Sendiri","",""));
//        houses.add(new Ruangan("2", "B","Ruangan Besar Sekali","",""));
//
//        // Buat adapter dan hubungkan dengan ListView
//        houseAdapter = new RoomAdapter(this, houses);
//        houseListView.setAdapter(houseAdapter);

//        databaseUsers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                ruanganList.clear();
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    Ruangan user = postSnapshot.getValue(Ruangan.class);
//                    ruanganList.add(user);
//                }
//                RoomAdapter adapter = new RoomAdapter(MainActivity_Ruangan.this, ruanganList);
//                houseListView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
    }
}
