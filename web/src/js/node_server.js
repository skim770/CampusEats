/**
Copyright 2016 Sung Kim <kr.dev.sk@gmail.com>. All rights reserved.
**/

var gtCalURL = "http://www.calendar.gatech.edu/feeds/events.xml";
var scheduler = require("node-schedule");
var xmlHttpRequest = require("xmlhttprequest").XMLHttpRequest;
var DOMParser = require("xmldom").DOMParser;
var HTMLParser = require("htmlparser");
var sys = require("util");

var firebase = require("firebase");
firebase.initializeApp({
	serviceAccount: "../../resources/firebase/node_server_private_key.json",
	databaseURL: "https://project-2581007719456375150.firebaseio.com/"
});

function fetchGTCal() {
	var request = new xmlHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState === request.DONE && request.status === 200) {
			parseGTCal(request.responseText);
		}
	}
	request.open("GET", gtCalURL, false);
	request.send();
}

function parseGTCal(data) {
	var posts = [];
	var domParser = new DOMParser();
	var xmlData = domParser.parseFromString(data, 'text/xml');
	var items = xmlData.getElementsByTagName("item");
	for (i = 0; i < items.length; i++) {
		var item = items[i];
		var rawHtml = item.getElementsByTagName("description")[0].textContent;
		var htmlHandler = new HTMLParser.DefaultHandler(function (error, dom) {
			if (error) {
				console.log(error.message);
			}
		});
		var htmlParser = new HTMLParser.Parser(htmlHandler);
		htmlParser.parseComplete(rawHtml);

		var eventDesc = "";
		var eventRawDates = [];
		var eventFormatDates = [];
		var eventLoc = "";
		for (domInd = 0; domInd < htmlHandler.dom.length; domInd++) {
			if (htmlHandler.dom[domInd].children.length == 1) {
				eventDesc = htmlHandler.dom[domInd].children[0].children[0].children[0].children[0].raw;
			} else {
				if (htmlHandler.dom[domInd].children[0].children[0].raw === 'Location:&nbsp;') {
					eventLoc = htmlHandler.dom[domInd].children[1].children[0].children[0].raw;
				} else if (htmlHandler.dom[domInd].children[0].children[0].raw === 'Date:&nbsp;') {
					var dateEntries = htmlHandler.dom[domInd].children[1].children;
					for (dateInd = 0; dateInd < dateEntries.length; dateInd++) {
						eventRawDates.push(dateEntries[dateInd].children[0].attribs.content);
						eventFormatDates.push(dateEntries[dateInd].children[0].children[0].raw);
					}
				} else {
					console.log("Anomaly in DOM object detected.");
				}
			}
		}

		for (day = 0; day < eventRawDates.length; day++) {
			var post = {
				title: item.getElementsByTagName("title")[0].textContent,
				link: item.getElementsByTagName("link")[0].textContent,
				desc: eventDesc,
				rawDate: eventRawDates[day],
				date: eventFormatDates[day],
				loc: eventLoc,
				likes: 0,
				cost: 0,
				sourceID: "GTCal",
				reports: "DNE",
				status: "Active",
				pubDate: item.getElementsByTagName("pubDate")[0].textContent,
				category: item.getElementsByTagName("category")[0].textContent,
				comments: item.getElementsByTagName("comments")[0].textContent
			}
			posts.push(post);
		}
	}
	updateFirebasePosts(posts);
}

function updateFirebasePosts(posts) {
	var postsPending = posts;
	var postRef = firebase.database().ref("posts");
	postRef.orderByChild("sourceID").equalTo("GTCal").once("value").then(function(snapshot) {
		snapshot.forEach(function(snapshotEntry) {
			for (i = 0; i < postsPending.length; i++) {
				var snapshotFound = false;
				if (postsPending[i].rawDate == snapshotEntry.child("rawDate").val() 
						&& postsPending[i].link == snapshotEntry.child("link").val()) {
					postsPending.splice(i, 1);
					snapshotFound = true;
					break;
				}
			}
			if (!snapshotFound) {
				postRef.child(snapshotEntry.key).remove();
			}
		});
	});
	for (i = 0; i < postsPending.length; i++) {
		var newPostKey = postRef.push().key;
		postRef.child(newPostKey).set(postsPending[i]);
	}
}

function refreshGTCalData() { fetchGTCal(); }

// Refresh GT Calendar data at 5 AM (EST) everyday.
scheduler.scheduleJob({hour: 5, minute: 0}, function() {
	console.log(new Date().toString() + " : GT Calendar refreshing began.");
	refreshGTCalData();
	console.log(new Date().toString() + " : GT Calendar refreshing finished.");
});
