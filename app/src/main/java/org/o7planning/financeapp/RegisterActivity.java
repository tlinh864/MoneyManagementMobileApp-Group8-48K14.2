package org.o7planning.financeapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullName, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private ImageView ivShowPassword, ivShowConfirmPassword, ivBack;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize UI components
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        ivShowPassword = findViewById(R.id.iv_show_password);
        ivShowConfirmPassword = findViewById(R.id.iv_show_confirm_password);
        ivBack = findViewById(R.id.iv_back);

        // Set click listeners
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = etFullName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                } else {
                    // Send verification code via SMS
                    // For demo purposes, navigate to verification screen
                    Intent intent = new Intent(RegisterActivity.this, VerificationActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }
            }
        });

        ivShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Hide password
                    etPassword.setTransformationMethod(new PasswordTransformationMethod());
                    ivShowPassword.setImageResource(R.drawable.ic_eye_closed);
                } else {
                    // Show password
                    etPassword.setTransformationMethod(null);
                    ivShowPassword.setImageResource(R.drawable.ic_eye_open);
                }
                isPasswordVisible = !isPasswordVisible;
                // Move cursor to the end of text
                etPassword.setSelection(etPassword.getText().length());
            }
        });

        ivShowConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConfirmPasswordVisible) {
                    // Hide password
                    etConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                    ivShowConfirmPassword.setImageResource(R.drawable.ic_eye_closed);
                } else {
                    // Show password
                    etConfirmPassword.setTransformationMethod(null);
                    ivShowConfirmPassword.setImageResource(R.drawable.ic_eye_open);
                }
                isConfirmPasswordVisible = !isConfirmPasswordVisible;
                // Move cursor to the end of text
                etConfirmPassword.setSelection(etConfirmPassword.getText().length());
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
