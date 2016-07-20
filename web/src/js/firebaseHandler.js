/**
Copyright 2016 Sung Kim <kr.dev.sk@gmail.com>. All rights reserved.
**/

var config = {
	apiKey: "AIzaSyDVe12w7nFUG6gF8J1Drm_v3olm97zZp2E",
    authDomain: "project-2581007719456375150.firebaseapp.com",
    databaseURL: "https://project-2581007719456375150.firebaseio.com",
    storageBucket: "project-2581007719456375150.appspot.com",
};
firebase.initializeApp(config);

var firebaseDB = firebase.database();
var postsRef = firebaseDB.ref('posts');

function loadFeed() {
	postsRef.orderByChild('rawDate').on('value', function(posts) {
		posts.forEach(function(post) {
			console.log(post.child("title").val());
		});
	});
}