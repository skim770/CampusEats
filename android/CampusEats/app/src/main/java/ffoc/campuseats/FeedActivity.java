package ffoc.campuseats;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
        //final LinearLayout tileLayout = (LinearLayout)findViewById(R.id.feedTile);
        //final TextView[] textViews = new TextView[50];
        final TextView newText   = new TextView(this);
        final Context context = this;
        //DatabaseReference ref = database.getReference();
        //DataSnapshot snapshot = new DataSnapshot(ref);
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



                for(int i = 0; i < 50; i++){


                    TextView dateText = new TextView(context);
                    dateText.setPadding(10,20,0,0);

                    TextView titleText = new TextView(context);
                    titleText.setTextSize(20);
                    titleText.setTextColor(Color.BLACK);
                    titleText.setBackgroundColor(Color.WHITE);
                    titleText.setLayoutParams(titleParams);
                    titleText.setPadding(10,10,10,0);
                    titleText.setWidth(linearLayout.getWidth() - 20);


                    //Rect rectangle = new Rect(2,2,2,2);
                    TextView descText = new TextView(context);
                    descText.setTextSize(12);
                    descText.setBackgroundColor(Color.WHITE);
                    descText.setLayoutParams(descParams);
                    descText.setPadding(10,0,10,10);
                    descText.setWidth(linearLayout.getWidth() - 20);

                    TextView textOutline = new TextView(context);
                    textOutline.setBackgroundColor(Color.GRAY);
                    textOutline.setPadding(10,10,10,10);
                    //textOutline.setWidth(linearLayout.getWidth() - 18);

                    GradientDrawable rect = new GradientDrawable();
                    rect.setColor(Color.GRAY);




                    DataSnapshot iteration = snap.iterator().next();

                    String str = iteration.child("date").getValue().toString();
                    if(dateText.getText() != null && !str.equals(dateText.toString()))
                    {
                        dateText.setTextSize(18);
                        dateText.append(str);

                        linearLayout.addView(dateText);
                    }



                    str = iteration.child("title").getValue().toString();
                    titleText.append(str);
                    linearLayout.addView(titleText);

                    str = iteration.child("category").getValue().toString();
                    descText.append(str);




                    //str = iteration.child("loc").getValue().toString();
                    //feedText.append("\n\n" + "Location: " + str);

                    str = iteration.child("desc").getValue().toString();
                    descText.append("\n\n" + str);

                    //str = iteration.child("likes").getValue().toString();
                    //feedText.append("\n" + "Likes: " + str);

                    //str = iteration.child("status").getValue().toString();
                    //feedText.append("\n" + "Status: " + str);

                    //feedText.append("\n\n");

                    //feedText.setId(i);

                    //textViews[i] = feedText;

                    linearLayout.addView(descText);

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
