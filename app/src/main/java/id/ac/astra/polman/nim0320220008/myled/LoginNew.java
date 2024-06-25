package id.ac.astra.polman.nim0320220008.myled;

import android.content.Intent;
import android.os.Bundle;
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

import id.ac.astra.polman.nim0320220008.myled.listadapter.MainActivity_Ruangan;
import id.ac.astra.polman.nim0320220008.myled.listadapter.MainActivity_Rumah;
import id.ac.astra.polman.nim0320220008.myled.model.User;

public class LoginNew extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton, donthaveaccount;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_new);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        donthaveaccount = findViewById(R.id.registerButton);


        //database = FirebaseDatabase.getInstance().getReference("smart_home");
        database = FirebaseDatabase.getInstance().getReference("smart_home").child("users");

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (!username.isEmpty() && !password.isEmpty()) {
                loginUser(username, password);
            } else {
                Toast.makeText(LoginNew.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            }
        });

        donthaveaccount.setOnClickListener(v -> {
            startActivity(new Intent(LoginNew.this, AddEditUserActivity.class));
        });
    }

    private void loginUser(String username, String password) {
        database.orderByChild("username").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);
                                if (user != null) {
                                    if (user.getPassword().equals(password)) {
                                        Toast.makeText(LoginNew.this, "Login successful!", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(LoginNew.this, MainActivity_Rumah.class);
                                        intent.putExtra("house_id", user.getUserId());
                                        intent.putExtra("house_name", user.getNama());
                                        intent.putExtra("house_address", user.getUsername());
                                        // Add other necessary data
                                        LoginNew.this.startActivity(intent);

                                        //startActivity(new Intent(LoginNew.this, MainActivity_Baru.class));
                                        // Lakukan tindakan setelah login sukses (misalnya, pindah ke activity lain)
                                    } else {
                                        Toast.makeText(LoginNew.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(LoginNew.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginNew.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class Users {
        private String id;
        private String nama;
        private String noTelpon;
        private String email;
        private String username;
        private String password;
        private String status;
        private String creadby;

        public Users() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getNoTelpon() {
            return noTelpon;
        }

        public void setNoTelpon(String noTelpon) {
            this.noTelpon = noTelpon;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreadby() {
            return creadby;
        }

        public void setCreadby(String creadby) {
            this.creadby = creadby;
        }
    }
}
