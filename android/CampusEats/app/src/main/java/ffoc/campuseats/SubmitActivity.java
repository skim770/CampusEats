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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Will on 9/6/2016.
 */
public class SubmitActivity extends AppCompatActivity {
    Button timeButton;
    Button dateButton;
    Button endTimeButton;
    Button endDateButton;
    Button postButton;

    Post post = new Post();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        timeButton = (Button)findViewById(R.id.timeButton);
        dateButton = (Button)findViewById(R.id.dateButton);
        endTimeButton = (Button)findViewById(R.id.endTimeButton);
        endDateButton = (Button)findViewById(R.id.endDateButton);

        postButton = (Button)findViewById(R.id.postButton);
        final EditText titleText = (EditText)findViewById(R.id.titleText);
        final EditText locText = (EditText)findViewById(R.id.locationText);
        final EditText descText = (EditText)findViewById(R.id.descriptionText);

        postButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //push post to database

                post.title = titleText.getText().toString();
                post.loc = locText.getText().toString();
                post.body = descText.getText().toString();
                DatabaseReference postsRef = ref.child("posts");
                String key = postsRef.push().getKey();

                final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

                sdfDate.setTimeZone(TimeZone.getDefault());

                Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 0);

                date = cal.getTime();
                long epoch = System.currentTimeMillis();
                String epochString = "" + epoch;

                String dateString = sdfDate.format(date);
                dateString = dateString.substring(0,22) + ":" + dateString.substring(22,24);



                Calendar setDate = new GregorianCalendar(post.year, post.month, post.day, post.hour, post.minute, 0);

                String setDateString = sdfDate.format(setDate.getTime());
                setDateString = setDateString.substring(0,22) + ":" + setDateString.substring(22,24);

                Calendar setEndDate = new GregorianCalendar(post.endYear, post.endMonth, post.endDay, post.endHour, post.endMinute, 0);
                String setEndDateString = sdfDate.format(setEndDate.getTime());
                setEndDateString = setEndDateString.substring(0,22) + ":" + setEndDateString.substring(22,24);


                FirebaseUser user = firebaseAuth.getCurrentUser();
                String uID= user.getUid();
                String userName = user.getDisplayName();


                Map<String, Object> postValues = new HashMap<String, Object>();
                Map<String, Object> timeValues = new HashMap<String, Object>();
                Map<String, Object> gmtContainer = new HashMap<String, Object>();
                Map<String, Object> timeContainer = new HashMap<String, Object>();
                timeValues.put("date_type", "datetime");
                timeValues.put("end", setEndDateString);
                timeValues.put("rrule", "");
                timeValues.put("start", setDateString);
                timeValues.put("timezone", "America/New_York");
                timeValues.put("timezone_db", "America/New_York");

                postValues.put("author", "User");
                postValues.put("body", post.body);
                //postValues.put("categories", null);
                postValues.put("changed_epoch", epochString);
                postValues.put("changed_gmt", dateString);

                postValues.put("created_epoch", epochString);
                postValues.put("created_gmt", dateString);
                postValues.put("feedback_score", 0);

                postValues.put("email", "");

                postValues.put("fee", "");
                postValues.put("image", "");
                postValues.put("location", post.loc);
                postValues.put("phone", "");
                postValues.put("start", setDateString);

                postValues.put("summary", "");
                postValues.put("summary_withTags", "");

                timeContainer.put("0", timeValues);
                postValues.put("times", timeContainer);

                gmtContainer.put("0", timeValues);
                postValues.put("times_gmt", gmtContainer);

                postValues.put("title", post.title);
                postValues.put("type", "event");
                postValues.put("uid", uID);
                postValues.put("url", "");

                if(post.startDateSet && post.startTimeSet && post.endDateSet && post.endTimeSet && post.title.length() > 0 && post.loc.length() > 0) {
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/posts/" + key, postValues);

                    ref.updateChildren(childUpdates);

                    startActivity(new Intent(SubmitActivity.this, FeedActivity.class));
                } else {
                    startActivity(new Intent(SubmitActivity.this, PopupWarning.class));
                }


            }
        });

    }


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    public void showEndTimePickerDialog(View v) {
        DialogFragment endFragment = new EndTimePickerFragment();
        endFragment.show(getSupportFragmentManager(), "endTimePicker");
    }



    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showEndDatePickerDialog(View v) {
        DialogFragment newFragment = new EndDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "endDatePicker");
    }
    public void setTimeButtonText(int hourOfDay, int minute) {
        timeButton.setText(hourOfDay + ":" + minute);
    }

    public void setEndTimeButtonText(int hourOfDay, int minute) {
        endTimeButton.setText(hourOfDay + ":" + minute);
    }

    public void setDateButtonText(int year, int month, int day) {
        dateButton.setText(month + "/" + day + "/" + year);
    }

    public void setEndDateButtonText(int year, int month, int day) {
        endDateButton.setText(month + "/" + day + "/" + year);
    }

}


