package ravtrix.foodybuddy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ravtrix.foodybuddy.R;

public class RegisterActivity extends AppCompatActivity {
    EditText etUserName, etEmail, etPassword, etREPW;
    Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUserName = (EditText) findViewById(R.id.etUserName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etREPW = (EditText) findViewById(R.id.etREPW);
        bRegister = (Button) findViewById(R.id.bRegister);

        View.OnClickListener newOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = etUserName.getText().toString();
                String email = etEmail.getText().toString();
                String pw = etPassword.getText().toString();
                String rePW = etREPW.getText().toString();
                if(pw.equals(rePW)) {
                    User user = new User(userName, email, pw);
                    regist(user);
                }

            }
        };

        bRegister.setOnClickListener(newOnClickListener);
    }

    private void regist(User user) {
        //TODO: regist new user

        Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(myIntent);
    }
}
