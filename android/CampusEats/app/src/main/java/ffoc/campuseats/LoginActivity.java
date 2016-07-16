package ffoc.campuseats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class LoginActivity extends AppCompatActivity {

    FirebaseApp firebaseApp = FirebaseApp.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Sign Up button listener
        final EditText emailEdit   = (EditText)findViewById(R.id.login_email);
        final EditText passwordEdit   = (EditText)findViewById(R.id.login_password);

        final Button button = (Button) findViewById(R.id.signup_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //create user
                auth.createUserWithEmailAndPassword(emailEdit.getText().toString(),passwordEdit.getText().toString());
            }
        });

        final Button signInButton = (Button) findViewById(R.id.signin_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //sign in user
                auth.signInWithEmailAndPassword(emailEdit.getText().toString(),passwordEdit.getText().toString());
                startActivity(new Intent(LoginActivity.this, FeedActivity.class));
            }
        });
    }
}
