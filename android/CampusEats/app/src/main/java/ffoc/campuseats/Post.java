package ffoc.campuseats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    String imgUrl;
    Date realDate;
    public String time;

    int year;
    int month;
    int day;
    int hour;
    int minute;
    int second = 0;

    int endYear;
    int endMonth;
    int endDay;
    int endHour;
    int endMinute;
    int endSecond = 0;

    boolean startTimeSet = false;
    boolean startDateSet = false;
    boolean endTimeSet = false;
    boolean endDateSet = false;

    String displayDate = "";

    String fullDateString;
    String postID;
    int likes = 0;
    boolean wasliked = false;



    public Post(String title, String loc, String date, String summary, String body) throws ParseException {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        SimpleDateFormat postDate = new SimpleDateFormat("h:mm a EEE, MMM d");

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

        displayDate = postDate.format(realDate);

    }
    public Post() {

    }

    @Override
    public int compareTo(Post post) {
        return realDate.compareTo(post.realDate);
    }



    public void buildDate(int year, int month, int day, int hour, int minute, int second) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        Calendar date = new GregorianCalendar(year, month, day, hour, minute, second);

        String dateString = sdfDate.format(date.getTime());
        fullDateString = dateString.substring(0,22) + ":" + dateString.substring(22,24);



    }
}
