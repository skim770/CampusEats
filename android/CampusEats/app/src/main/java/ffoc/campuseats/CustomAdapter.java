package ffoc.campuseats;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wajra on 11/22/2016.
 */
public class CustomAdapter extends ArrayAdapter<Post> {
    private final Context context;
    private final ArrayList<Post> values;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    boolean hasFeedbackScore = false;
    Query queryRef = ref.child("posts");
    public CustomAdapter(Context context, ArrayList<Post> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = new ViewHolder();

        final int  pos = position;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        //TextView textView = (TextView) rowView.findViewById(R.id.row_text);
        //textView.setText(values.get(position));

        viewHolder.title = (TextView) rowView.findViewById(R.id.feedTitle);
        viewHolder.time = (TextView) rowView.findViewById(R.id.feedTime);
        viewHolder.likeButton = (ImageButton) rowView.findViewById(R.id.thumbsUp);
        viewHolder.likes = (TextView) rowView.findViewById(R.id.likesText);

        rowView.setTag(viewHolder);

        viewHolder.title.setText(values.get(position).title);
        viewHolder.time.setText(values.get(position).displayDate);
        //Uri uri = Uri.parse(values.get(position).imgUrl);

        //Bitmap bmp = BitmapFactory.decodeStream()
        /*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL(values.get(position).imgUrl);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();


            //viewHolder.img.setImageBitmap(image);
        } catch(IOException e) {
            System.out.println(e);
        }*/

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot snap = dataSnapshot.child(values.get(pos).postID);
                if(snap.hasChild("feedback_score")) {
                    hasFeedbackScore = true;
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        queryRef.addListenerForSingleValueEvent(valueEventListener);

        viewHolder.likeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatabaseReference postsRef = ref.child("posts");



                if(!values.get(pos).wasliked) {
                    values.get(pos).likes++;
                    values.get(pos).wasliked = true;

                    if(hasFeedbackScore) {

                        postsRef.child(values.get(pos).postID).child("feedback_score").setValue(values.get(pos).likes);
                    }

                } else {
                    values.get(pos).likes--;
                    values.get(pos).wasliked = false;
                    if(hasFeedbackScore) {
                        postsRef.child(values.get(pos).postID).child("feedback_score").setValue(values.get(pos).likes);
                    }
                }

                viewHolder.likes.setText("Likes (" + values.get(pos).likes + ")");
            }
        });



        return rowView;
    }


    static class ViewHolder {
        TextView title;
        TextView time;
        ImageButton likeButton;
        TextView likes;
        int position;
    }
}
