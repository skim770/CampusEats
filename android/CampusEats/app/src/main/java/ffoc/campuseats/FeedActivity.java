package ffoc.campuseats;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
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

        final TextView feedText   = (TextView) findViewById(R.id.feedDisplay);
        //DatabaseReference ref = database.getReference();
        //DataSnapshot snapshot = new DataSnapshot(ref);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snap = dataSnapshot.getChildren();

                //DataSnapshot iteration = snap.iterator().next();
                long childCount = dataSnapshot.getChildrenCount();

                for(long i = 0; i < childCount; i++){
                    DataSnapshot iteration = snap.iterator().next();

                    String str = iteration.child("title").getValue().toString();
                    feedText.append(str);

                    str = iteration.child("date").getValue().toString();
                    feedText.append("\n" + "Date: " + str);

                    str = iteration.child("location").getValue().toString();
                    feedText.append("\n" + "Location: " + str);

                    str = iteration.child("description").getValue().toString();
                    feedText.append("\n" + "Description: " + str);

                    str = iteration.child("likes").getValue().toString();
                    feedText.append("\n" + "Likes: " + str);

                    str = iteration.child("status").getValue().toString();
                    feedText.append("\n" + "Status: " + str);

                    feedText.append("\n\n");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        queryRef.addListenerForSingleValueEvent(valueEventListener);
        //feedText.setText(queryRef.toString());


    }
}
