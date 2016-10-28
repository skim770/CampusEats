YUI().use('calendar', 'datatype-date', 'cssbutton', function (Y) {

    // Create a new instance of calendar, placing it in
    // #mycalendar container, setting its width to 340px,
    // the flags for showing previous and next month's
    // dates in available empty cells to true, and setting
    // the date to today's date.
    var calendar = new Y.Calendar({
        contentBox: "#mycalendar",
        width: '340px',
        showPrevMonth: true,
        showNextMonth: true,
        date: new Date() }).render();

    // Get a reference to Y.DataType.Date
    var dtdate = Y.DataType.Date;

    // Listen to calendar's selectionChange event.
    calendar.on("selectionChange", function (ev) {

        // Get the date from the list of selected
        // dates returned with the event (since only
        // single selection is enabled by default,
        // we expect there to be only one date)
        var newDate = ev.newSelection[0];
        var rawDate = dtdate.format(newDate);
        // Format the date and output it to a DOM
        // element.
        Y.one("#selecteddate").setHTML(rawDate);

        // refresh feed -------------------- from feed.js by Sung Kim

        var dayOfWeek = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
        var monthOfYear = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

        // Extract and create today's yyyy-mm-dd manually, otherwise
        // todayStamp will take into account seconds/milliseconds.
        var today = new Date();
        var todayStamp = newDate.getFullYear() + "-";
        if (newDate.getMonth() + 1 < 10) {
            todayStamp += "0";
        }
        todayStamp += newDate.getMonth() + 1 + "-";
        if (newDate.getDate() < 10) {
            todayStamp += "0";
        }
        todayStamp += newDate.getDate();
        var prevDateHeader = Date.parse(todayStamp);

        var EventPost = React.createClass({
            render: function() {
                var sourceID;
                if (this.props.post.sourceID === "GTCal") {
                    sourceID = <div className="sourceId">{this.props.post.category}</div>;
                } else {
                    sourceID = <div className="sourceId">{this.props.post.sourceID}</div>;
                }

                var post;
                var eventDay = Date.parse(this.props.post.rawDate.substring(0, 10));
                var dateHeader = <div className="dateHeader">{eventDay}</div>;
                if (eventDay > prevDateHeader) {
                    post = (
                        <div>
                            <DateHeader day={eventDay} />
                            <div className="eventPost">
                                <div className="eventTitle">{this.props.post.title}</div>
                                {sourceID}
                                <div className="eventDesc">{this.props.post.desc}</div>
                            </div>
                        </div>
                    );
                    prevDateHeader = eventDay;
                } else {
                    post = (
                        <div className="eventPost">
                            <div className="eventTitle">{this.props.post.title}</div>
                            {sourceID}
                            <div className="eventDesc">{this.props.post.desc}</div>
                        </div>
                    );
                }
                return post;
            }
        });

        var EventFeed = React.createClass({
            render: function() {
                var postNodes = this.props.data.map(function(post) {
                    return (
                        <EventPost post={post} />
                    );
                });
                return (
                    <div className="eventFeed">
                        <DateHeader day="Today" />
                        {postNodes}
                    </div>
                );
            }
        });

        var DateHeader = React.createClass({
            render: function() {
                var dayString = "";
                var day = new Date(this.props.day);
                day.setTime(day.getTime() + (1000 * 60 * 60 * 24));
                if (this.props.day === "Today") {
                    day = new Date();
                    if(dtdate.format(day) === todayStamp){
                        dayString = "Today - ";
                    }
                    else{
                        day = newDate;
                        dayString = "";
                    }
                }
                dayString += dayOfWeek[day.getDay()]
                    + ", " + monthOfYear[day.getMonth()]
                    + " " + (day.getDate())
                    + ", " + day.getFullYear();
                return (
                    <div className="dateHeader">
                        {dayString}
                    </div>
                );
            }
        });

        function renderDOM(posts) {
            ReactDOM.render(
                <EventFeed data={posts} />,
                document.getElementById('posts_container')
            );
        }


        postsRef.orderByChild('rawDate').on('value', function (snapshot) {
            var posts = [];
            snapshot.forEach(function (post) {
                var eventDay = post.child("rawDate").val().substring(0, 10);
                if (Date.parse(eventDay) == Date.parse(todayStamp)) {
                    posts.push(post.val());
                }
            });
            console.log("ReactDOM rendering " + posts.length + " items.");
            renderDOM(posts);
            if(posts.length == 0){
                Y.one("#noevent").setHTML("No events this day.");
            }
        });
    });
});