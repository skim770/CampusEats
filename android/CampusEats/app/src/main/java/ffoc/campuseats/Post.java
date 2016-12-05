package ffoc.campuseats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Will on 9/6/2016.
 */
public class Post implements Comparable<Post>{

    String summary;
    String body;
    //String time;
    String date;
    String loc;
    String title;
    Date realDate;
    public String time;

    public Post(String title, String loc, String date, String summary, String body) throws ParseException {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        this.title = title;
        this.loc = loc;
        String fixedDate = date.substring(0,22) + date.substring(23, 25);
        try {
            realDate = sdfDate.parse(fixedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.date = date;
        //this.time = time;
        this.summary = summary;
        this.body = body;

    }
    public Post() {

    }

    @Override
    public int compareTo(Post post) {
        return realDate.compareTo(post.realDate);
    }

    /*public Date getDate(){


        return date;

    }

    public void setDateTime(String datetime) throws ParseException {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        this.date = sdfDate.parse(datetime);
    }

    public void setDateTime(Date datetime) {

        this.date = datetime;
    }*/
}
