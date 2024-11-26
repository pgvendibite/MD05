package com.example.MD;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hanks.passcodeview.PasscodeView;

public class Passcode extends AppCompatActivity {
    PasscodeView passcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_passcode);

        passcodeView = findViewById(R.id.passcodeView);
        overridePendingTransition(R.anim.enter_activity, R.anim.exit_activity);

        // Set the passcode length and local passcode
        passcodeView.setPasscodeLength(4)
                .setLocalPasscode("1234") // Set your local passcode here
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {
                        // Automatically clear the passcode input after failure
                        clearPasscodeInput();
                        Toast.makeText(getApplicationContext(), "Wrong password!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String number) {
                        Intent i = new Intent(getApplicationContext(), Admin.class);
                        startActivity(i);
                        finish();
                    }
                });
    }

    private void clearPasscodeInput() {
        // Clear the passcode input by resetting the PasscodeView
        passcodeView.setPasscodeLength(4) // Re-setup the passcode view
                .setLocalPasscode("1234") // Reset the local passcode
                .setListener(new PasscodeView.PasscodeViewListener() { // Reattach listener
                    @Override
                    public void onFail() {
                        clearPasscodeInput(); // Clear input again on subsequent failures
                        Toast.makeText(getApplicationContext(), "Wrong password!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(String number) {
                        Intent i = new Intent(getApplicationContext(), Admin.class);
                        startActivity(i);
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
}
