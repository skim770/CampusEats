package ffoc.campuseats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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
        return rowView;
    }


    static class ViewHolder {
        TextView title;
        TextView time;
        int position;
    }
}
