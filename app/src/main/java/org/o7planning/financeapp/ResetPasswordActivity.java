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

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etNewPassword, etConfirmPassword;
    private Button btnResetPassword;
    private ImageView ivShowNewPassword, ivShowConfirmPassword, ivBack;
    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Get intent data
        if (getIntent() != null) {
            email = getIntent().getStringExtra("email");
        }

        // Initialize UI components
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnResetPassword = findViewById(R.id.btn_reset_password);
        ivShowNewPassword = findViewById(R.id.iv_show_new_password);
        ivShowConfirmPassword = findViewById(R.id.iv_show_confirm_password);
        ivBack = findViewById(R.id.iv_back);

        // Set click listeners
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ResetPasswordActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                } else {
                    // Reset password
                    // For demo purposes, navigate to success screen
                    Intent intent = new Intent(ResetPasswordActivity.this, PasswordSuccessActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        ivShowNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNewPasswordVisible) {
                    // Hide password
                    etNewPassword.setTransformationMethod(new PasswordTransformationMethod());
                    ivShowNewPassword.setImageResource(R.drawable.ic_eye_closed);
                } else {
                    // Show password
                    etNewPassword.setTransformationMethod(null);
                    ivShowNewPassword.setImageResource(R.drawable.ic_eye_open);
                }
                isNewPasswordVisible = !isNewPasswordVisible;
                // Move cursor to the end of text
                etNewPassword.setSelection(etNewPassword.getText().length());
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
