/**
Copyright 2016 Sung Kim <kr.dev.sk@gmail.com>. All rights reserved.

JSX script that renders ReactDOM for main feed.
**/

/**
category, comments, cost, date, desc, likes, link, loc,
pubDate, rawDate, reports, sourceID, status, title
**/

var EventPost = React.createClass({
	render: function() {
		var sourceID;
		if (this.props.post.sourceID === "GTCal") {
			sourceID = <div className="sourceId">{this.props.post.category}</div>;
		} else {
			sourceID = <div className="sourceId">{this.props.post.sourceID}</div>;
		}
		return (
			<div className="eventPost">
				<div className="eventTitle">{this.props.post.title}</div>
				{sourceID}
				<div className="eventDesc">{this.props.post.desc}</div>
			</div>
		);
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
				{postNodes}
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
		posts.push(post.val());
	});
	renderDOM(posts);
});