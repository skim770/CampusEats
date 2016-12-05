package ffoc.campuseats;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

/**
 * Created by wajra on 11/22/2016.
 */
public class EventActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);


        TextView title = (TextView) findViewById(R.id.titleText);
        title.setText(getIntent().getStringExtra("TITLE"));

        TextView time = (TextView) findViewById(R.id.timeText);
        time.setText(getIntent().getStringExtra("TIME"));

        TextView summary = (TextView) findViewById(R.id.summaryText);
        summary.setText(getIntent().getStringExtra("SUMMARY"));

        TextView body = (TextView) findViewById(R.id.bodyText);
        body.setText(Html.fromHtml(getIntent().getStringExtra("BODY")));





    }

}
