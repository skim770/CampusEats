package ffoc.campuseats;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by wajra on 10/28/2016.
 */
public class CalendarActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("posts");
    //Query queryRef = ref.child("posts");

    CalendarView calendarView;
    TextView eventName;
    String testString = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        eventName = (TextView) findViewById(R.id.eventName);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.calendar_toolbar);
        setSupportActionBar(myToolbar);

        final ListView listView = (ListView) findViewById(R.id.list_view);
        final ArrayList<Post> posts = new ArrayList<Post>();
        final ArrayList<String> titles = new ArrayList<String>();



        listView.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                Post chosenPost = posts.get(position);
                Intent intent = new Intent(getBaseContext(), EventActivity.class);
                intent.putExtra("TITLE", chosenPost.title);
                intent.putExtra("TIME", chosenPost.realDate.toString());
                intent.putExtra("SUMMARY", chosenPost.summary);
                intent.putExtra("BODY", chosenPost.body);



                startActivity(intent);

            }
        });

        final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        sdfDate.setTimeZone(TimeZone.getDefault());
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        //cal.add(Calendar.MONTH, 0);
        date = cal.getTime();


        long minDate = date.getTime();





       // calendarView.setMinDate(minDate);





        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                //String selectedDate =  year + " / " + month + " / " + day;
                Calendar selectedDate = new GregorianCalendar(year, month, day,0,0,0);

                //Calendar selectedDate = Calendar.getInstance(TimeZone.getDefault());
                selectedDate.setTimeZone(TimeZone.getDefault());
                Calendar endOfSelectedDate = new GregorianCalendar(year, month, day, 23,59,59);
                endOfSelectedDate.setTimeZone(TimeZone.getDefault());
                String selectedDateString = sdfDate.format(selectedDate.getTime());
                selectedDateString = selectedDateString.substring(0,22) + ":" + selectedDateString.substring(22,24);
                String endOfSelectedDateString = sdfDate.format(endOfSelectedDate.getTime());
                endOfSelectedDateString = endOfSelectedDateString.substring(0,21) + ":"
                        + endOfSelectedDateString.substring(22,24);

                testString = selectedDateString.substring(0,10);

                final String dateString = selectedDateString;
                //ref.orderByChild("rawDate").startAt(selectedDateString).endAt(endOfSelectedDateString);
                ref.orderByChild("start").startAt(selectedDateString.substring(0,10)).endAt(endOfSelectedDateString).addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> snap = dataSnapshot.getChildren();

                        CustomAdapter adapter = new CustomAdapter(getBaseContext(), posts);
                        listView.setAdapter(adapter);

                        if(!snap.iterator().hasNext()) {

                            eventName.setText("No events today");
                        } else {
                            eventName.setText("");
                        }
                        posts.clear();
                        for(int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                            DataSnapshot iteration = snap.iterator().next();
                            //String str = iteration.child("title").getValue().toString();
                            String title = iteration.child("title").getValue().toString();
                            String location = iteration.child("location").getValue().toString();
                            String summary = iteration.child("summary").getValue().toString();
                            String body = iteration.child("body").getValue().toString();
                            String id = iteration.getKey().toString();
                            Post post = null;
                            try {
                                post = new Post(title, location, dateString, summary, body);
                            } catch(ParseException e) {
                                e.printStackTrace();
                            }
                            //eventName.append("\n" + str);

                            post.postID = id;
                            posts.add(post);
                            titles.add(post.title);

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
    }

}
