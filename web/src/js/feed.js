/**
Copyright 2016 Sung Kim <kr.dev.sk@gmail.com>. All rights reserved.

JSX script that renders ReactDOM for main feed.
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

function renderDOM(posts) {
	ReactDOM.render(
		<EventFeed data={posts} />,
		document.getElementById('posts_container')
	);
}

postsRef.orderByChild('rawDate').on('value', function(snapshot) {
	var posts = [];
	snapshot.forEach(function(post) {
		var eventDay = post.child("rawDate").val().substring(0, 10);
		if (Date.parse(eventDay) >= Date.parse(todayStamp)) {
			posts.push(post.val());
		}
	});
	renderDOM(posts);
});