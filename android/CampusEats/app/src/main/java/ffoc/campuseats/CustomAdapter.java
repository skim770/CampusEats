package ffoc.campuseats;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    public CustomAdapter(Context context, ArrayList<Post> values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);

        //TextView textView = (TextView) rowView.findViewById(R.id.row_text);
        //textView.setText(values.get(position));

        viewHolder.title = (TextView) rowView.findViewById(R.id.feedTitle);
        viewHolder.time = (TextView) rowView.findViewById(R.id.feedTime);

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


        return rowView;
    }


    static class ViewHolder {
        TextView title;
        TextView time;
        int position;
    }
}
