package ravtrix.foodybuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ravtrix.foodybuddy.R;

public class LoginActivity extends AppCompatActivity {

    Button bLogin, bRegister;
    EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
        bRegister = (Button) findViewById(R.id.bRegister);

        View.OnClickListener loginOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String pw = etPassword.getText().toString();
                User user = new User(email, pw);
                login(); //（user);
            }
        };

        View.OnClickListener registOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(myIntent);
            }
        };

        bLogin.setOnClickListener(loginOnClickListener);
        bRegister.setOnClickListener(registOnClickListener);
    }

    private void login() { //User user) {
        /* TODO server request get user info. PW match ==> homepage   else error msg
        if (userExists) {
        */
            Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(myIntent);
        //} else {
            // errorMSG();
        // }

    }


    private void errorMSG() {
        AlertDialog show = new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Incorrect email or password")
                .setPositiveButton("OK", null)
                .show();
    }

}
