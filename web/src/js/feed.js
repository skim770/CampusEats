/**
Copyright 2016 Sung Kim <kr.dev.sk@gmail.com>. All rights reserved.

JSX script that renders ReactDOM for main feed.
**/

var EventPost = React.createClass({
	render: function() {
		return (
			<div className="eventPost">
				<h4>{this.props.post.title}</h4>
				<p>{this.props.post.desc}</p>
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