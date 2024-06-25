package id.ac.astra.polman.nim0320220008.myled.gakepake;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import id.ac.astra.polman.nim0320220008.myled.R;

public class MainActivity extends AppCompatActivity {

    // Firebase Database reference
    private DatabaseReference mDatabase;

    // UI elements
    private TextView txtData1, txtData2, txtData3;
    private Switch btnDevice1, btnDevice2, btnDevice3;

    private Button buttonPickColor;
    private TextView textViewColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_4);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI elements
        txtData1 = findViewById(R.id.txtData1);
        txtData2 = findViewById(R.id.txtData2);
        txtData3 = findViewById(R.id.txtData3);
        btnDevice1 = findViewById(R.id.btnDevice1);
        btnDevice2 = findViewById(R.id.btnDevice2);
        btnDevice3 = findViewById(R.id.btnDevice3);

        // Set listeners for switches
        btnDevice1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Update database on switch change
                mDatabase.child("devices").child("device1").setValue(isChecked ? "on" : "off");
            }
        });

        btnDevice2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDatabase.child("devices").child("device2").setValue(isChecked ? "on" : "off");
            }
        });

        btnDevice3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mDatabase.child("devices").child("device3").setValue(isChecked ? "on" : "off");
            }
        });

        // Read initial states from database
        readFromDatabase();
    }

    // Method to read initial states from database
    private void readFromDatabase() {
        mDatabase.child("devices").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String deviceName = snapshot.getKey();
                    String deviceState = snapshot.getValue(String.class);

                    // Update UI based on database values
                    updateUI(deviceName, deviceState);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    // Method to update UI based on database values
    private void updateUI(String deviceName, String deviceState) {
        switch (deviceName) {
            case "device1":
                txtData1.setText(deviceState.equals("on") ? "Lampu Ruang Tamu" : "Lamp is Off");
                btnDevice1.setChecked(deviceState.equals("on"));
                break;
            case "device2":
                txtData2.setText(deviceState.equals("on") ? "Lampu Ruang Tamu" : "Lamp is Off");
                btnDevice2.setChecked(deviceState.equals("on"));
                break;
            case "device3":
                txtData3.setText(deviceState.equals("on") ? "Lampu Ruang Tamu" : "Lamp is Off");
                btnDevice3.setChecked(deviceState.equals("on"));
                break;
        }
    }
}