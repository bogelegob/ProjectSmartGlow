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

import id.ac.astra.polman.nim0320220008.myled.model.User;

public class AddEditUserActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPhone, editTextUsername, editTextPassword;
    private Button buttonSave;
    private DatabaseReference databaseUsers;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_user);

        // Initialize Firebase Database reference
        databaseUsers = FirebaseDatabase.getInstance().getReference("smart_home/users");

        // Initialize EditText
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);

        Intent intent = getIntent();
        if (intent != null) {
            userId = intent.getStringExtra("USER_ID");
            if (userId != null) {
                // Load existing user data if editing
                databaseUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            editTextName.setText(user.getNama());
                            editTextEmail.setText(user.getEmail());
                            editTextPhone.setText(user.getNoTelpon());
                            editTextUsername.setText(user.getUsername());
                            editTextPassword.setText(user.getPassword());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(AddEditUserActivity.this, "Failed to load user.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        // Initialize Save Button
        buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(v -> saveUser());
    }

    private void saveUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(name)) {
            if (userId == null) {
                // Membaca data user untuk mendapatkan jumlah user saat ini
                databaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        long userCount = dataSnapshot.getChildrenCount();
                        String newUserId = String.valueOf(userCount + 1); // Generate ID berdasarkan jumlah user + 1
                        String bro = "user_id_"+newUserId;

                        User user = new User(bro, name, email, phone, username, "Aktif",password);
                        // Simpan user ke Firebase Database
                        databaseUsers.child(bro).setValue(user, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@NonNull DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if (databaseError == null) {
                                    Toast.makeText(AddEditUserActivity.this, "User saved", Toast.LENGTH_SHORT).show();
                                    finish(); // Tutup activity setelah menyimpan
                                } else {
                                    Toast.makeText(AddEditUserActivity.this, "Failed to save user: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(AddEditUserActivity.this, "Failed to get user count: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                User user = new User(userId, name, email, phone, username, "Aktif",password);
                // Update user di Firebase Database
                databaseUsers.child(userId).setValue(user, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@NonNull DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(AddEditUserActivity.this, "User updated", Toast.LENGTH_SHORT).show();
                            finish(); // Tutup activity setelah menyimpan
                        } else {
                            Toast.makeText(AddEditUserActivity.this, "Failed to update user: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } else {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
        }
    }
}
