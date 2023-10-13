package com.osepoo.driverapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    Button signIn;
    TextView signUp;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        getSupportActionBar().hide();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        signIn = findViewById(R.id.signin);


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(editTextEmail.getText());
                String password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "Enter both email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    if (currentUser != null) {
                                        // Get the email ID of the logged-in user
                                        String driverEmail = currentUser.getEmail();

                                        // Check if the driverEmail matches the email of any driver
                                        if ("charanchar@gmail.com".equals(driverEmail)) {
                                            // This is driver 0000
                                            // Redirect to the path "0000" in the real-time database
                                            Intent intent = new Intent(MainActivity.this, HomePage.class);
                                            startActivity(intent);
                                            finish();

                                        } else if ("rameshrameshram0987@gmail.com".equals(driverEmail)) {
                                            // This is driver 0001
                                            // Redirect to the path "0001" in the real-time database
                                            Intent intent = new Intent(MainActivity.this, HomePage.class);
                                            startActivity(intent);
                                            finish();

                                        } else if ("tamizhjai@gmail.com".equals(driverEmail)) {
                                            // This is driver 0001
                                            // Redirect to the path "0001" in the real-time database
                                            Intent intent = new Intent(MainActivity.this, HomePage.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}



