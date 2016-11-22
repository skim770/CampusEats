/**
Copyright 2016 Sung Kim <kr.dev.sk@gmail.com>. All rights reserved.
**/

/**
category, comments, cost, date, desc, likes, link, loc,
pubDate, rawDate, reports, sourceID, status, title
**/

var dayOfWeek = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
var monthOfYear = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

// Extract and create today's yyyy-mm-dd manually, otherwise
// todayStamp will take into account seconds/milliseconds.
var today = new Date();
var todayStamp = today.getFullYear() + "-";
if (today.getMonth() + 1 < 10) {
	todayStamp += "0";
}
todayStamp += (today.getMonth() + 1) + "-";
if (today.getDate() < 10) {
	todayStamp += "0";
}
todayStamp += today.getDate();
var prevDateHeader = Date.parse(todayStamp);

var EventPost = React.createClass({
	handleClick: function(data) {
		$('#overlay-event-detail').removeClass("none");
		if (data.image != null && data.image.length > 0) {
			document.getElementById("overlay-event-detail").style.width = "calc(100% - 140px)";
			$('#overlay-img-container').removeClass("none");
			$('#overlay-img-container').addClass("col-md-6");
			$('#overlay-img').removeClass("none");
			$('#overlay-contents').addClass("col-md-6");
			$('#overlay-img').attr('src', data.image);
		} else {
			document.getElementById("overlay-event-detail").style.width = "50%";
			$('#overlay-img-container').addClass("none");
			$('#overlay-img-container').removeClass("col-md-6");
			$('#overlay-img').addClass("none");
			$('#overlay-contents').removeClass("col-md-6");
		}
		$('#overlay-event-title').text(data.title);
		$('#overlay-event-location').html("<i class=\"material-icons\">location_on</i>  " + data.location);
		$('#overlay-event-time').html("<i class=\"material-icons\">access_time</i>  " + dateTimeRange(data.start_gmt, data.end_gmt));
		$('#overlay-event-body').html(data.body);
		if (data.contact != null && data.contact.length > 0) {
			$('.event-contact-info').removeClass("none");
			$('#overlay-event-contact').html(data.contact);
		} else {
			$('.event-contact-info').addClass("none");
		}
		$('#overlay-event-detail').popup('show');
	},
	render: function() {
		var post;
		var eventDay = Date.parse(this.props.post.start.substring(0, 10));
		var isLaterDay = eventDay > prevDateHeader;
		prevDateHeader = eventDay;

		var infoColWidth = this.props.post.image.length > 0 ? "80%" : "100%";
		var imgColWidth = this.props.post.image.length > 0 ? "20%" : "0%";

		return post = (
			<div>
			{isLaterDay ? <DateHeader day={eventDay} /> : null}
			<div className="eventPost" onClick={this.handleClick.bind(this, this.props.post)}>
				<table><tr>
					<td width={infoColWidth}>
						<div className="eventTitle">{this.props.post.title}</div>
						<div className="eventTime">{timeRange(this.props.post.start, this.props.post.end)}</div>
						<div className="eventLoc">{this.props.post.location}</div>
					</td>
					<td width={imgColWidth}>
						<img src={this.props.post.image}/>
					</td>
				</tr></table>
				<div className="eventDesc">{this.props.post.summary}</div>
			</div>
			</div>
		);
	}
});

var EventFeed = React.createClass({
	render: function() {
		var postNodes = this.props.data.map(function(post) {
			return (
				<EventPost key={post['key']} post={post} />
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
			dayString = "Today - ";
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

function timeRange(start, end) {
	var startAmPm = " AM";
	var startHr = parseInt(start.substring(11, 13));
	if (startHr >= 12) {
		startAmPm = " PM";
		if (startHr > 12) startHr = startHr - 12;
	}
	var endAmPm = " AM";
	var endHr = parseInt(end.substring(11, 13));
	if (endHr >= 12) {
		endAmPm = " PM";
		if (endHr > 12) endHr = endHr - 12;
	}
	return startHr + start.substring(13, 16) + startAmPm + "  -  " + endHr + end.substring(13, 16) + endAmPm;
}

function dateTimeRange(startGMT, endGMT) {
	var startMonth = monthOfYear[parseInt(startGMT.substring(5,7)) - 1];
	var startDay = startGMT.substring(8, 10);
	var startHr = parseInt(startGMT.substring(11, 13));
	var startMin = startGMT.substring(14, 16);
	var startAmPm = " AM";
	if (startHr >= 12) {
		startAmPm = " PM";
		if (startHr > 12) startHr = startHr - 12;
	}
	var endMonth = monthOfYear[parseInt(endGMT.substring(5,7)) - 1];
	var endDay = endGMT.substring(8, 10);
	var endHr = parseInt(endGMT.substring(11, 13));
	var endMin = endGMT.substring(14, 16);
	var endAmPm = " AM";
	if (endHr >= 12) {
		endAmPm = " PM";
		if (endHr > 12) endHr = endHr - 12;
	}
	return startMonth + " " + startDay + ", " + startHr + ":" + startMin + " " + startAmPm + "\t-\t" + endMonth + " " + endDay + ", " + endHr + ":" + endMin + " " + endAmPm;
}

function renderDOM(posts) {
	ReactDOM.render(
		<EventFeed data={posts} />,
		document.getElementById('posts_container')
	);
}

function fetchFirebasePosts() {
	postsRef.on('value', function(snapshot) {
		var posts = [];
		snapshot.forEach(function(post) {
			var times = post.child('times').val();
			for (var i = 0; i < times.length; i++) {
				// var eventDay = times[i].start.substring(0, 10);
				// if (Date.parse(eventDay) >= Date.parse(todayStamp)) {
					var postInstance = post.val();
					postInstance.key = post.key + ":" + i;
					postInstance.start = times[i].start;
					postInstance.end = times[i].end;
					posts.push(postInstance);
				// }
			}
		});
		posts.sort(function(a, b) {
			return a.start.localeCompare(b.start);
		});
		renderDOM(posts);
	});
}
fetchFirebasePosts();

/*
 * Month View
 */
YUI().use('calendar', 'datatype-date', 'cssbutton', function (Y) {
    var calendar = new Y.Calendar({
        contentBox: "#month_calendar",
        width: '75%',
        showPrevMonth: true,
        showNextMonth: true,
        date: new Date() }).render();

    var dtdate = Y.DataType.Date;

    calendar.on("selectionChange", function (ev) {
        var newDate = ev.newSelection[0];
        var rawDate = dtdate.format(newDate);
        Y.one("#selecteddate").setHTML(rawDate);
    });
});