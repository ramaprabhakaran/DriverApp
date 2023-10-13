package com.osepoo.driverapp;

import  android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log; // Import Log for debugging
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterPage extends AppCompatActivity {
    TextInputEditText editTextUserId, editTextPassword; // Change to userId and password fields
    Button signUp;
    TextView signIn;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference databaseReferenceUserIDs;
    DatabaseReference databaseReferenceCurrentRegisteredUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        getSupportActionBar().hide();
        editTextUserId = findViewById(R.id.userId); // Update to userId field
        editTextPassword = findViewById(R.id.password1);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        signIn = findViewById(R.id.signin);
        signUp = findViewById(R.id.signup1);

        // Get references to the "UserIDs" and "CurrentRegisteredUsers" nodes in the Firebase Realtime Database
        databaseReferenceUserIDs = FirebaseDatabase.getInstance().getReference("UserIDs");
        databaseReferenceCurrentRegisteredUsers = FirebaseDatabase.getInstance().getReference("CurrentRegisteredUsers");

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterPage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userId, password;
                userId = String.valueOf(editTextUserId.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterPage.this, "Enter both User ID and Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if the entered user ID exists in the "UserIDs" node
                databaseReferenceUserIDs.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // User ID exists, proceed with user registration
                            Log.d("Registration", "User ID exists: " + userId);

                            firebaseAuth.createUserWithEmailAndPassword(userId, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                // Registration successful, store user data under "CurrentRegisteredUsers"
                                                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                User user = new UserBuilder().createUser().setEmail(userId).setPassword(password).createUser();

                                                // Push the user data to the "CurrentRegisteredUsers" node with a unique user ID
                                                databaseReferenceCurrentRegisteredUsers.child(currentUserId).setValue(user);

                                                // Registration successful
                                                Log.d("Registration", "Registration successful for user: " + userId);
                                                Toast.makeText(RegisterPage.this, "Registration Successfully.", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(RegisterPage.this, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                // Registration failed, display an error message
                                                Log.e("Registration", "Registration failed: " + task.getException());
                                                Toast.makeText(RegisterPage.this, "Registration Failed.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // User ID does not exist, show an error message
                            Log.d("Registration", "Invalid User ID: " + userId);
                            Toast.makeText(RegisterPage.this, "Invalid User ID", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database errors if needed
                        Log.e("Registration", "Database error: " + databaseError.getMessage());
                    }
                });
            }
        });
    }
}
