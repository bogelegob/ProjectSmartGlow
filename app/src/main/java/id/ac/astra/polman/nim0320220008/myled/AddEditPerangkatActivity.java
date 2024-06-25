package id.ac.astra.polman.nim0320220008.myled;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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

import id.ac.astra.polman.nim0320220008.myled.model.Lampu;
import yuku.ambilwarna.AmbilWarnaDialog;

public class AddEditPerangkatActivity extends AppCompatActivity {

    private EditText editTextName, editSerialNumber, editJumlahPin;
    private Button buttonSave;
    private DatabaseReference databaseUsers;
    private String userId;
    private View colorPreview;
    private int currentColor;
    private Integer red = 0, blue = 0, green = 0, pin = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_perangkat);

        // Initialize Firebase Database reference
        databaseUsers = FirebaseDatabase.getInstance().getReference("smart_home/lampu");

        // Initialize EditText
        editTextName = findViewById(R.id.editTextName);
        editSerialNumber = findViewById(R.id.editSerialNumber);
        editJumlahPin = findViewById(R.id.editJumlahPin);
        Button buttonPickColor = findViewById(R.id.button_pick_color);
        colorPreview = findViewById(R.id.color_preview);

        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("house_id");
            String creadby = intent.getStringExtra("creadby");
            String rumahId = intent.getStringExtra("rumahId");

            Toast.makeText(AddEditPerangkatActivity.this, creadby, Toast.LENGTH_SHORT).show();
            Toast.makeText(AddEditPerangkatActivity.this, rumahId, Toast.LENGTH_SHORT).show();

            if (userId != null) {
                // Load existing user data if editing
                databaseUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Lampu user = dataSnapshot.getValue(Lampu.class);
                        if (user != null) {
                            editTextName.setText(user.getNama());
                            editSerialNumber.setText(user.getSerial_number());
                            editJumlahPin.setText(user.getPin());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(AddEditPerangkatActivity.this, "Failed to load user.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // Set default color
            currentColor = Color.WHITE;

            buttonPickColor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openColorPicker();
                }
            });

            // Initialize Save Button
            buttonSave = findViewById(R.id.buttonSave);
            buttonSave.setOnClickListener(v -> saveUser(rumahId,creadby));
        }
    }

    private void saveUser(String houseid, String creadby) {
        String name = editTextName.getText().toString().trim();
        String serial = editSerialNumber.getText().toString().trim();
        String pin = editJumlahPin.getText().toString().trim();

        int jumlahpin = Integer.parseInt(pin);

        if (!TextUtils.isEmpty(name)) {
            if (userId == null) {
                // Membaca data user untuk mendapatkan jumlah user saat ini
                databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long userCount = dataSnapshot.getChildrenCount();
                        String newUserId = String.valueOf(userCount + 1); // Generate ID berdasarkan jumlah user + 1
                        String bro = "lampu_id_"+newUserId;

                        Lampu user = new Lampu(bro, houseid, name, "Aktif", creadby,red,green,blue,serial,jumlahpin);
                        // Simpan user ke Firebase Database
                        databaseUsers.child(bro).setValue(user, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@NonNull DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast.makeText(AddEditPerangkatActivity.this, "User saved", Toast.LENGTH_SHORT).show();
                                    finish(); // Tutup activity setelah menyimpan
                                } else {
                                    Toast.makeText(AddEditPerangkatActivity.this, "Failed to save user: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(AddEditPerangkatActivity.this, "Failed to get user count: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Lampu user = new Lampu(userId, houseid, name, "Aktif", creadby,red,green,blue,serial,jumlahpin);
                // Update user di Firebase Database
                databaseUsers.child(userId).setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@NonNull DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(AddEditPerangkatActivity.this, "User updated", Toast.LENGTH_SHORT).show();
                            finish(); // Tutup activity setelah menyimpan
                        } else {
                            Toast.makeText(AddEditPerangkatActivity.this, "Failed to update user: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
        }
    }

    private void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, currentColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // action on cancel
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                currentColor = color;
                updateColorPreview();
            }
        });
        colorPicker.show();
    }

    private void updateColorPreview() {
        colorPreview.setBackgroundColor(currentColor);
        int red = Color.red(currentColor);
        int green = Color.green(currentColor);
        int blue = Color.blue(currentColor);
        Toast.makeText(this, "RGB: (" + red + ", " + green + ", " + blue + ")", Toast.LENGTH_SHORT).show();
        this.red = red;
        this.blue = blue;
        this.green = green;
    }
}
