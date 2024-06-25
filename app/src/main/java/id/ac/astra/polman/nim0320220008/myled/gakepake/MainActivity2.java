package id.ac.astra.polman.nim0320220008.myled.gakepake;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.ac.astra.polman.nim0320220008.myled.R;

public class MainActivity2 extends AppCompatActivity {

    private EditText numLedsEditText;
    private EditText redEditText;
    private EditText greenEditText;
    private EditText blueEditText;
    private Button updateButton;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        numLedsEditText = findViewById(R.id.numLedsEditText);
        redEditText = findViewById(R.id.redEditText);
        greenEditText = findViewById(R.id.greenEditText);
        blueEditText = findViewById(R.id.blueEditText);
        updateButton = findViewById(R.id.updateButton);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("LED");

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLedConfiguration();
            }
        });

        // Read initial configuration from Firebase
        readLedConfiguration();
    }

    private void updateLedConfiguration() {
        String numLeds = numLedsEditText.getText().toString();
        String red = redEditText.getText().toString();
        String green = greenEditText.getText().toString();
        String blue = blueEditText.getText().toString();

        if (numLeds.isEmpty() || red.isEmpty() || green.isEmpty() || blue.isEmpty()) {
            Toast.makeText(this, "Please enter all values", Toast.LENGTH_SHORT).show();
            return;
        }

        int numLedsInt = Integer.parseInt(numLeds);
        int redInt = Integer.parseInt(red);
        int greenInt = Integer.parseInt(green);
        int blueInt = Integer.parseInt(blue);

        //databaseReference.child("NUM_LEDS").setValue(numLedsInt);

        //for (int i = 0; i < numLedsInt; i++) {
        databaseReference.child("COLORS").child(String.valueOf(numLedsInt-1)).child("RED").setValue(redInt);
        databaseReference.child("COLORS").child(String.valueOf(numLedsInt-1)).child("GREEN").setValue(greenInt);
        databaseReference.child("COLORS").child(String.valueOf(numLedsInt-1)).child("BLUE").setValue(blueInt);
        //}

        Toast.makeText(this, "LED configuration updated", Toast.LENGTH_SHORT).show();
    }

    private void readLedConfiguration() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Integer numLeds = snapshot.child("NUM_LEDS").getValue(Integer.class);
                    if (numLeds != null) {
                        numLedsEditText.setText(String.valueOf(numLeds));
                    }

                    for (int i = 0; i < numLeds; i++) {
                        Integer red = snapshot.child("COLORS").child(String.valueOf(i)).child("RED").getValue(Integer.class);
                        Integer green = snapshot.child("COLORS").child(String.valueOf(i)).child("GREEN").getValue(Integer.class);
                        Integer blue = snapshot.child("COLORS").child(String.valueOf(i)).child("BLUE").getValue(Integer.class);

                        if (red != null && green != null && blue != null) {
                            redEditText.setText(String.valueOf(red));
                            greenEditText.setText(String.valueOf(green));
                            blueEditText.setText(String.valueOf(blue));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity2.this, "Failed to read data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


