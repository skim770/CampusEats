package ffoc.campuseats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.shapes.Shape;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.nio.channels.Selector;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class FeedActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    Query queryRef = ref.child("posts");

    //static Post chosenPost;



    //public

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_feed);
        setContentView(R.layout.activity_listview);
        //final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.feedLayout);

        final TextView newText   = new TextView(this);
        final Context context = this;

        //final Button newPostButton = (Button) findViewById(R.id.newPostButton);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        /*newPostButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                startActivity(new Intent(FeedActivity.this, SubmitActivity.class));
            }
        });*/

        final ArrayList<Post> posts = new ArrayList<>();
        final ArrayList<String> titles = new ArrayList<>();
        final ListView listView = (ListView) findViewById(R.id.list_view);



        listView.setOnItemClickListener( new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {

                Post chosenPost = posts.get(position);
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("TITLE", chosenPost.title);
                intent.putExtra("TIME", chosenPost.realDate.toString());
                intent.putExtra("SUMMARY", chosenPost.summary);
                intent.putExtra("BODY", chosenPost.body);



                startActivity(intent);

            }
        });

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snap = dataSnapshot.getChildren();

                long childCount = dataSnapshot.getChildrenCount();

                //set layout parameters for the title/description, will be added to the textviews in the for loop
                LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                titleParams.setMargins(20, 20, 20, 0);


                LinearLayout.LayoutParams descParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                descParams.setMargins(20, 0, 20, 20);

                //ArrayList<Post> posts = new ArrayList<>();
                //ArrayList<String> titles = new ArrayList<>();
                //final ListView listView = (ListView) findViewById(R.id.list_view);
                CustomAdapter adapter = new CustomAdapter(context, posts);
                listView.setAdapter(adapter);

                if(childCount > 100)
                    childCount = 100;




                for(int i = 0; i < childCount; i++) {
                    DataSnapshot iteration = snap.iterator().next();

                    String str = iteration.child("start").getValue().toString();
                    int index = 0;

                    String title = iteration.child("title").getValue().toString();
                    String location = iteration.child("location").getValue().toString();
                    String summary = iteration.child("summary").getValue().toString();
                    String body = iteration.child("body").getValue().toString();
                    String img = iteration.child("image").getValue().toString();

                    DataSnapshot times = iteration.child("times_gmt");
                    Iterable<DataSnapshot> iterableTimes = times.getChildren();

                    //Post tPost = new Post(title, location, "2015-11-26");

                    while(iterableTimes.iterator().hasNext()) {

                        DataSnapshot timeSnap = iterableTimes.iterator().next();
                        String time = timeSnap.child("start").getValue().toString();

                        Post tempPost = null;
                        try {
                            tempPost = new Post(title, location, time, summary, body);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        tempPost.imgUrl = img;
                        posts.add(tempPost);
                        //titles.add(tempPost.realDate.toString());



                    }
                    Collections.sort(posts);

                }

                for(int i = 0; i < posts.size(); i++) {
                    titles.add(posts.get(i).title);

                }



                /*for(int i = 0; i < posts.size(); i++){


                    TextView dateText = new TextView(context);
                    dateText.setPadding(10,20,0,0);

                    TextView titleText = new TextView(context);
                    titleText.setTextSize(20);
                    titleText.setTextColor(Color.BLACK);
                    titleText.setBackgroundColor(Color.WHITE);
                    titleText.setLayoutParams(titleParams);
                    titleText.setPadding(10,10,10,0);
                    //titleText.setWidth(linearLayout.getWidth() - 20);


                    //Rect rectangle = new Rect(2,2,2,2);
                    TextView descText = new TextView(context);
                    descText.setTextSize(12);
                    descText.setBackgroundColor(Color.WHITE);
                    descText.setLayoutParams(descParams);
                    descText.setPadding(10,0,10,10);
                    //descText.setWidth(linearLayout.getWidth() - 20);

                    TextView textOutline = new TextView(context);
                    textOutline.setBackgroundColor(Color.GRAY);
                    textOutline.setPadding(10,10,10,10);
                    //textOutline.setWidth(linearLayout.getWidth() - 18);

                    GradientDrawable rect = new GradientDrawable();
                    rect.setColor(Color.GRAY);




                    String date = posts.get(i).date;
                    if(dateText.getText() != null && !date.equals(dateText.toString()))
                    {
                        dateText.setTextSize(18);
                        dateText.append(date);

                        //linearLayout.addView(dateText);
                    }



                    //String title = posts.get(i).title;
                    //titleText.append(title);
                    //linearLayout.addView(titleText);

                    //str = iteration.child("category").getValue().toString();
                    //descText.append(str);




                    //str = iteration.child("loc").getValue().toString();
                    //feedText.append("\n\n" + "Location: " + str);

                    //String summary = posts.get(i).desc;
                    //descText.append("\n\n" + summary);

                    //str = iteration.child("likes").getValue().toString();
                    //feedText.append("\n" + "Likes: " + str);

                    //str = iteration.child("status").getValue().toString();
                    //feedText.append("\n" + "Status: " + str);

                    //feedText.append("\n\n");

                    //feedText.setId(i);

                    //textViews[i] = feedText;

                    //linearLayout.addView(descText);

                }*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        queryRef.addListenerForSingleValueEvent(valueEventListener);


        //feedText.setText(queryRef.toString());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_event_buton:
                // User chose the "Settings" item, show the app settings UI...
                startActivity(new Intent(FeedActivity.this, SubmitActivity.class));
                return true;

            case R.id.calendar_button:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                startActivity(new Intent(FeedActivity.this, CalendarActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);


        }
    }
}
