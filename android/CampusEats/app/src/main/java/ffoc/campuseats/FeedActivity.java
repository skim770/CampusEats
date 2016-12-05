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
import java.util.Date;

public class FeedActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    Query queryRef = ref.child("posts");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        final TextView newText   = new TextView(this);
        final Context context = this;


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


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


                CustomAdapter adapter = new CustomAdapter(context, posts);
                listView.setAdapter(adapter);

                if(childCount > 100)
                    childCount = 100;

                Date date = new Date();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);

                date = cal.getTime();


                for(int i = 0; i < childCount; i++) {
                    DataSnapshot iteration = snap.iterator().next();


                    String title = iteration.child("title").getValue().toString();
                    String location = iteration.child("location").getValue().toString();
                    String summary = iteration.child("summary").getValue().toString();
                    String body = iteration.child("body").getValue().toString();
                    String img = iteration.child("image").getValue().toString();
                    String id = iteration.getKey().toString();

                    DataSnapshot times = iteration.child("times_gmt");
                    Iterable<DataSnapshot> iterableTimes = times.getChildren();


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
                        tempPost.postID = id;
                        //check if the date is before today
                        if(!tempPost.realDate.before(date)) {
                            posts.add(tempPost);
                        }



                    }
                    Collections.sort(posts);

                }

                for(int i = 0; i < posts.size(); i++) {
                    titles.add(posts.get(i).title);

                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        queryRef.addListenerForSingleValueEvent(valueEventListener);





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
                startActivity(new Intent(FeedActivity.this, SubmitActivity.class));
                return true;

            case R.id.calendar_button:

                startActivity(new Intent(FeedActivity.this, CalendarActivity.class));
                return true;

            default:

                return super.onOptionsItemSelected(item);


        }
    }
}
