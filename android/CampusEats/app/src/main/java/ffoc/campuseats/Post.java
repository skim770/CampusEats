package ffoc.campuseats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Will on 9/6/2016.
 */
public class Post {

    String desc;
    //String time;
    String date;
    String loc;
    String title;
    private Date realDate;
    public String time;

    public Post(String title, String loc, String date, String desc){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

        this.title = title;
        this.loc = loc;
        //date = date.substring(0,22) + date.substring(23, 25);
        this.date = date;
        //this.time = time;
        this.desc = desc;

    }
    public Post() {

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
