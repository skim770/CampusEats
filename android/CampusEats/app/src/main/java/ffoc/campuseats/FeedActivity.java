package ffoc.campuseats;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FeedActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    Query queryRef = ref.child("posts");



    //public

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.feedLayout);

        final TextView[] textViews = new TextView[50];
        final TextView newText   = new TextView(this);
        final Context context = this;
        //DatabaseReference ref = database.getReference();
        //DataSnapshot snapshot = new DataSnapshot(ref);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snap = dataSnapshot.getChildren();

                //DataSnapshot iteration = snap.iterator().next();
                long childCount = dataSnapshot.getChildrenCount();

                for(int i = 0; i < 50; i++){
                    TextView feedText = new TextView(context);
                    DataSnapshot iteration = snap.iterator().next();

                    String str = iteration.child("title").getValue().toString();
                    feedText.append(str);

                    str = iteration.child("date").getValue().toString();
                    feedText.append("\n" + "Date: " + str);

                    str = iteration.child("loc").getValue().toString();
                    feedText.append("\n" + "Location: " + str);

                    str = iteration.child("desc").getValue().toString();
                    feedText.append("\n" + "Description: " + str);

                    str = iteration.child("likes").getValue().toString();
                    feedText.append("\n" + "Likes: " + str);

                    str = iteration.child("status").getValue().toString();
                    feedText.append("\n" + "Status: " + str);

                    feedText.append("\n\n");

                    //feedText.setId(i);

                    textViews[i] = feedText;

                    linearLayout.addView(textViews[i]);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        queryRef.addListenerForSingleValueEvent(valueEventListener);

        if(textViews[0] != null) {
            linearLayout.addView(textViews[0]);
        }
        //feedText.setText(queryRef.toString());


    }
}
