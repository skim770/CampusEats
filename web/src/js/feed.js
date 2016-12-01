/**
Copyright 2016 Sung Kim <kr.dev.sk@gmail.com>. All rights reserved.
**/
var userLikes = {};

var EventPost = React.createClass({
	handleClick: function() {
		var data = this.props.post;
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
	handleLike: function(event) {
		var data = this.props.post;
		var key = data.key.split(":")[0];
		var numPostLikes = data.feedback_score;

		if (key in userLikes) {
			delete userLikes[key];
			numPostLikes--;
			$('.' + key + ' > .eventScore').removeClass('liked');
		} else {
			userLikes[key] = key;
			numPostLikes++;
			$('.' + key + ' > .eventScore').addClass('liked');
		}

		firebaseDB.ref('users/' + firebase.auth().currentUser.uid + '/likes').set(userLikes);
		firebaseDB.ref('posts/' + key + '/feedback_score').set(numPostLikes);
		$('.' + key + ' > .eventScore').text("LIKE (" + numPostLikes + ")");
		event.stopPropagation();
	},
	render: function() {
		var eventDay = Date.parse(this.props.post.start.substring(0, 10));
		var isLaterDay = eventDay > prevDateHeader;
		prevDateHeader = eventDay;

		var score = this.props.post.feedback_score != null ? this.props.post.feedback_score : 0;
		var toggled = false;
		if (this.props.post.key.split(":")[0] in userLikes) {
			toggled = true;
		}

		return (
			<div>
			{isLaterDay ? <DateHeader day={eventDay} /> : null}
			<div className={"eventPost " + this.props.post.key.split(":")[0]} onClick={this.handleClick}>
				<table><tbody><tr>
					<td width={this.props.post.image.length > 0 ? "80%" : "100%"}>
						<div className="eventTitle">{this.props.post.title}</div>
						<div className="eventTime">{timeRange(this.props.post.start, this.props.post.end)}</div>
						<div className="eventLoc">{this.props.post.location}</div>
					</td>
					<td width={this.props.post.image.length > 0 ? "20%" : "0%"}>
						<img src={this.props.post.image}/>
					</td>
				</tr></tbody></table>
				<div className="eventDesc">{this.props.post.summary}</div>
				<button className={"eventScore " + (toggled?"liked":"")} onClick={this.handleLike}>LIKE ({score})</button>
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

function renderDOM(posts) {
	usersRef.child(firebase.auth().currentUser.uid).once('value', function(snapshot) {
		if (snapshot.child('likes').val() != null) {
			userLikes = snapshot.child('likes').val();
		}
		ReactDOM.render(
			<EventFeed data={posts} />,
			document.getElementById('posts_container')
		);
	});
}

function fetchFirebasePosts() {
	postsRef.on('value', function(snapshot) {
		var posts = [];
		snapshot.forEach(function(post) {
			var times = post.child('times').val();
			for (var i = 0; i < times.length; i++) {
				var eventDay = times[i].start.substring(0, 10);
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
