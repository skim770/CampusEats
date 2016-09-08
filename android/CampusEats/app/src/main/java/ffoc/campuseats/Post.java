package ffoc.campuseats;

/**
 * Created by Will on 9/6/2016.
 */
public class Post {

    String desc;
    String time;
    String date;
    String loc;
    String title;
    public Post(String title, String loc, String date, String time) {
        this.title = title;
        this.loc = loc;
        this.date = date;
        this.time = time;

    }
    public Post() {

    }
}
