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
import id.ac.astra.polman.nim0320220008.myled.AddEditUserActivity;
import id.ac.astra.polman.nim0320220008.myled.LoginNew;
import id.ac.astra.polman.nim0320220008.myled.MainActivity_Baru;
import id.ac.astra.polman.nim0320220008.myled.R;
import id.ac.astra.polman.nim0320220008.myled.UserListAdapter;
import id.ac.astra.polman.nim0320220008.myled.adapter.HouseAdapter;
import id.ac.astra.polman.nim0320220008.myled.adapter.RoomAdapter;
import id.ac.astra.polman.nim0320220008.myled.model.Ruangan;
import id.ac.astra.polman.nim0320220008.myled.model.Rumah;
import id.ac.astra.polman.nim0320220008.myled.model.User;

public class MainActivity_Rumah extends AppCompatActivity {

    private ListView houseListView;
    private List<Rumah> housess;
    private HouseAdapter houseAdapter;
    private Button addButton;
    private TextView user;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bro);

        databaseUsers = FirebaseDatabase.getInstance().getReference("smart_home/rumah");

        houseListView = findViewById(R.id.house_list);
        housess = new ArrayList<>();


        // Buat daftar rumah contoh
//        List<Rumah> houses = new ArrayList<>();
//        houses.add(new Rumah("1", "Rumah Kampung","Di Kota","",""));
//        houses.add(new Rumah("2", "Rumah Kota","Di Kampung","",""));
//
//        // Buat adapter dan hubungkan dengan ListView
//        houseAdapter = new HouseAdapter(this, houses);
//        houseListView.setAdapter(houseAdapter);

        user = findViewById(R.id.user_welcome);
        Intent intent = getIntent();
        if (intent != null) {
            String houseId = intent.getStringExtra("house_id");
            String houseName = intent.getStringExtra("house_name");
            String houseAddress = intent.getStringExtra("house_address");
            addButton = findViewById(R.id.add_house_button);
            user.setText("Welcome, "+houseName);
            // Query to filter rooms based on houseId
            Query query = databaseUsers.orderByChild("creadby").equalTo(houseId);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    housess.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Rumah ruangan = postSnapshot.getValue(Rumah.class);
                        housess.add(ruangan);
                    }
                    HouseAdapter adapter = new HouseAdapter(MainActivity_Rumah.this, housess, houseId,houseName);
                    houseListView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors
                }
            });

            addButton.setOnClickListener(v -> {
                // Open detail activity or show detail dialog
                Intent hoho = new Intent(MainActivity_Rumah.this, AddEditHouseActivity.class);
                hoho.putExtra("creadby", houseId);
                hoho.putExtra("owner", houseName);

                // Add other necessary data
                MainActivity_Rumah.this.startActivity(hoho);
                //startActivity(new Intent(MainActivity_Rumah.this, AddEditHouseActivity.class));
            });
        }

//        databaseUsers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                housess.clear();
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    Rumah user = postSnapshot.getValue(Rumah.class);
//                    housess.add(user);
//                }
//                HouseAdapter adapter = new HouseAdapter(MainActivity_Rumah.this, housess);
//                houseListView.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });
    }
}
