package id.ac.astra.polman.nim0320220008.myled;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import id.ac.astra.polman.nim0320220008.myled.model.Rumah;
import id.ac.astra.polman.nim0320220008.myled.model.User;

public class AddEditHouseActivity extends AppCompatActivity {

    private EditText editTextName, editTextAlamat;
    private Button buttonSave;
    private DatabaseReference databaseUsers;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_rumah);

        // Initialize Firebase Database reference
        databaseUsers = FirebaseDatabase.getInstance().getReference("smart_home/rumah");

        // Initialize EditText
        editTextName = findViewById(R.id.editTextName);
        editTextAlamat = findViewById(R.id.editAlamatAja);

        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("house_id");
            String creadby = intent.getStringExtra("creadby");
            Toast.makeText(AddEditHouseActivity.this, creadby, Toast.LENGTH_SHORT).show();
            if (userId != null) {
                // Load existing user data if editing
                databaseUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Rumah user = dataSnapshot.getValue(Rumah.class);
                        if (user != null) {
                            editTextName.setText(user.getNama());
                            editTextAlamat.setText(user.getAlamat_rumah());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(AddEditHouseActivity.this, "Failed to load user.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // Initialize Save Button
            buttonSave = findViewById(R.id.buttonSave);
            buttonSave.setOnClickListener(v -> saveUser(creadby));
        }
    }

    private void saveUser(String creadby) {
        String name = editTextName.getText().toString().trim();
        String alamat = editTextAlamat.getText().toString().trim();

        if (!TextUtils.isEmpty(name)) {
            if (userId == null) {
                // Membaca data user untuk mendapatkan jumlah user saat ini
                databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long userCount = dataSnapshot.getChildrenCount();
                        String newUserId = String.valueOf(userCount + 1); // Generate ID berdasarkan jumlah user + 1
                        String bro = "rumah_id_"+newUserId;

                        Rumah user = new Rumah(bro, name, alamat, "Aktif", creadby);
                        // Simpan user ke Firebase Database
                        databaseUsers.child(bro).setValue(user, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@NonNull DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast.makeText(AddEditHouseActivity.this, "User saved", Toast.LENGTH_SHORT).show();
                                    finish(); // Tutup activity setelah menyimpan
                                } else {
                                    Toast.makeText(AddEditHouseActivity.this, "Failed to save user: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(AddEditHouseActivity.this, "Failed to get user count: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Rumah user = new Rumah(userId, name, alamat, "Aktif", creadby);
                // Update user di Firebase Database
                databaseUsers.child(userId).setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@NonNull DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(AddEditHouseActivity.this, "User updated", Toast.LENGTH_SHORT).show();
                            finish(); // Tutup activity setelah menyimpan
                        } else {
                            Toast.makeText(AddEditHouseActivity.this, "Failed to update user: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
        }
    }
}
