package ffoc.campuseats;


import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Will on 9/6/2016.
 */
public class SubmitActivity extends AppCompatActivity {
    Button timeButton;
    Button dateButton;
    Button postButton;

    Post post = new Post();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        timeButton = (Button)findViewById(R.id.timeButton);
        dateButton = (Button)findViewById(R.id.dateButton);
        postButton = (Button)findViewById(R.id.postButton);
        final EditText titleText = (EditText)findViewById(R.id.titleText);
        final EditText locText = (EditText)findViewById(R.id.locationText);
        final EditText descText = (EditText)findViewById(R.id.descriptionText);

        postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //push post to database

                post.title = titleText.getText().toString();
                post.loc = locText.getText().toString();
                post.body = locText.getText().toString();
                DatabaseReference postsRef = ref.child("posts");
                String key = postsRef.push().getKey();


                Map<String, Object> postValues = new HashMap<String, Object>();
                postValues.put("title", post.title);
                postValues.put("location", post.loc);
                //postValues.put("date", post.date);
                //postValues.put("time", post.time);
                postValues.put("desc", post.body);

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/posts/" + key, postValues);

                ref.updateChildren(childUpdates);

                startActivity(new Intent(SubmitActivity.this, FeedActivity.class));


            }
        });

    }


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setTimeButtonText(int hourOfDay, int minute) {
        timeButton.setText(hourOfDay + ":" + minute);
    }

    public void setDateButtonText(int year, int month, int day) {
        dateButton.setText(month + "/" + day + "/" + year);
    }
}


