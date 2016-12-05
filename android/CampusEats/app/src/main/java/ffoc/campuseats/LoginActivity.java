package ffoc.campuseats;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(myToolbar);
        //Sign Up button listener
        final EditText emailEdit   = (EditText)findViewById(R.id.login_email);
        final EditText passwordEdit   = (EditText)findViewById(R.id.login_password);

        final Button button = (Button) findViewById(R.id.signup_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //create user
                //auth.createUserWithEmailAndPassword(emailEdit.getText().toString(),passwordEdit.getText().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://project-2581007719456375150.firebaseapp.com/src/login/register.html"));
                startActivity(intent);
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
