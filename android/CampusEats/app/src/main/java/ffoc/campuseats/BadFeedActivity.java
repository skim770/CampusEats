package ffoc.campuseats;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import ffoc.campuseats.CalendarXMLParser.Entry;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class BadFeedActivity extends Activity {


    private static final String URL =
            "http://www.calendar.gatech.edu/feeds/events.xml";
            //"http://www.google.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bad_activity_feed);


    }

    @Override
    public void onStart() {
        super.onStart();
        //final TextView displayXML = (TextView)findViewById(R.id.xmlDisplay);


        /*String str = new String();
        try {
            str = LoadCalendarXML(URL);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        //new DownloadXmlTask().execute(URL);
        //System.out.println(str);
        /*try {
            displayXML.setText(LoadCalendarXML(URL));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private void loadPage() {
        new DownloadXmlTask().execute(URL);
    }
    private class DownloadXmlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                return LoadCalendarXML(urls[0]);
            } catch (IOException e) {
                return "connection error";
            } catch (XmlPullParserException e) {
                return "xml error";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            /*String str = new String();
            try {
                str = LoadCalendarXML(URL);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            final TextView displayXML = (TextView)findViewById(R.id.xmlDisplay);
            try {
                displayXML.setText(LoadCalendarXML(URL));
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    private String LoadCalendarXML(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;
        CalendarXMLParser xmlParser = new CalendarXMLParser();
        List<Entry> entries = null;
        String title = null;
        String url = null;
        String description = null;
        Calendar rightNow = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

        // Checks whether the user set the preference to include summary text
        //SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        //boolean pref = sharedPrefs.getBoolean("summaryPref", false);

        StringBuilder outString = new StringBuilder();
        //htmlString.append("<h3>" + getResources().getString(R.string.page_title) + "</h3>");
        //htmlString.append("<em>" + getResources().getString(R.string.updated) + " " +
                //formatter.format(rightNow.getTime()) + "</em>");

        try {
            stream = downloadUrl(urlString);
            entries = xmlParser.parse(stream);
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        // StackOverflowXmlParser returns a List (called "entries") of Entry objects.
        // Each Entry object represents a single post in the XML feed.
        // This section processes the entries list to combine each entry with HTML markup.
        // Each entry is displayed in the UI as a link that optionally includes
        // a text summary.
        for (Entry entry : entries) {
            //htmlString.append("<p><a href='");
            outString.append(entry.link);
            outString.append(entry.title);
            // If the user set the preference to include summary text,
            // adds it to the display.
            //if (pref) {
            outString.append(entry.description);
            //}
        }
        return outString.toString();
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(100000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
    }
}
