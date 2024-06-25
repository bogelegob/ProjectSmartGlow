package id.ac.astra.polman.nim0320220008.myled;

import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import id.ac.astra.polman.nim0320220008.myled.model.User;

public class MainActivity_Baru extends AppCompatActivity {

    private ListView listViewUsers;
    private List<User> userList;
    private DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_baru);

        databaseUsers = FirebaseDatabase.getInstance().getReference("smart_home/users");

        listViewUsers = findViewById(R.id.listViewUsers);
        Button buttonAddUser = findViewById(R.id.buttonAddUser);

        userList = new ArrayList<>();

        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity_Baru.this, AddEditUserActivity.class));
            }
        });

        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = userList.get(position);
                Intent intent = new Intent(MainActivity_Baru.this, AddEditUserActivity.class);
                intent.putExtra("USER_ID", user.getUserId());
                startActivity(intent);
            }
        });

        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    userList.add(user);
                }
                UserListAdapter adapter = new UserListAdapter(MainActivity_Baru.this, userList);
                listViewUsers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void updateUser(String userId, String nama, String email, String noTelpon) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("smart_home/users").child(userId);

        User user = new User(userId, nama, email, noTelpon);
        databaseReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity_Baru.this, "User updated successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity_Baru.this, "Failed to update user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteUser(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("smart_home/users").child(userId);

        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity_Baru.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity_Baru.this, "Failed to delete user", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

