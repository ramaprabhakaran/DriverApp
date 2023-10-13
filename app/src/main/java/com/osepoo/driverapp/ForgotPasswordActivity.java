package com.osepoo.driverapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import android.content.Intent;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private TextInputEditText newPasswordEditText;
    private TextInputEditText confirmPasswordEditText;
    private Button resetPasswordButton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.forgot_email);
        newPasswordEditText = findViewById(R.id.new_password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        resetPasswordButton = findViewById(R.id.reset_password_button);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve the entered email address, new password, and confirm password
                String email = emailEditText.getText().toString().trim();
                String newPassword = newPasswordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                    // Ensure all fields are filled
                    Toast.makeText(ForgotPasswordActivity.this, "Fill in all fields",
                            Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    // Check if new password and confirm password match
                    Toast.makeText(ForgotPasswordActivity.this, "Passwords do not match",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Send a password reset email to the provided email address
                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Password reset email sent successfully
                                        // Now, update the user's password in Firebase
                                        firebaseAuth.signInWithEmailAndPassword(email, newPassword)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            // Password updated successfully
                                                            Toast.makeText(ForgotPasswordActivity.this,
                                                                    "Password reset and updated successfully",
                                                                    Toast.LENGTH_SHORT).show();

                                                            // Redirect to the login page
                                                            Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish(); // Close the activity
                                                        } else {
                                                            // Failed to update password
                                                            Toast.makeText(ForgotPasswordActivity.this,
                                                                    "Failed to update password",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        // Failed to send password reset email
                                        Toast.makeText(ForgotPasswordActivity.this,
                                                "Failed to send password reset email. " +
                                                        "Check the email address.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
