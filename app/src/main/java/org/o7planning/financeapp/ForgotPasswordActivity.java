package org.o7planning.financeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnSendCode;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize UI components
        etEmail = findViewById(R.id.et_email);
        btnSendCode = findViewById(R.id.btn_send_code);
        ivBack = findViewById(R.id.iv_back);

        // Set click listeners
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                } else {
                    // Send verification code via SMS
                    // For demo purposes, navigate to verification screen
                    Intent intent = new Intent(ForgotPasswordActivity.this, VerificationActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("isResetPassword", true);
                    startActivity(intent);
                }
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
