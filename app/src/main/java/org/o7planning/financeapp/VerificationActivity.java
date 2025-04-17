package org.o7planning.financeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class VerificationActivity extends AppCompatActivity {

    private EditText etVerificationCode;
    private Button btnVerify;
    private ImageView ivBack;
    private boolean isResetPassword = false;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        // Get intent data
        if (getIntent() != null) {
            email = getIntent().getStringExtra("email");
            isResetPassword = getIntent().getBooleanExtra("isResetPassword", false);
        }

        // Initialize UI components
        etVerificationCode = findViewById(R.id.et_verification_code);
        btnVerify = findViewById(R.id.btn_verify);
        ivBack = findViewById(R.id.iv_back);

        // Set click listeners
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etVerificationCode.getText().toString().trim();

                if (code.isEmpty()) {
                    Toast.makeText(VerificationActivity.this, "Vui lòng nhập mã xác thực", Toast.LENGTH_SHORT).show();
                } else {
                    // Verify the code
                    // For demo purposes, assume the code is correct
                    if (isResetPassword) {
                        Intent intent = new Intent(VerificationActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {
                        Toast.makeText(VerificationActivity.this, "Xác thực thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VerificationActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
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