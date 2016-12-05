/**
Copyright 2016 Sung Kim <kr.dev.sk@gmail.com>. All rights reserved.
**/

// var gtCalURL = "http://www.calendar.gatech.edu/feeds/events.xml";
var gtCalURL = "http://hg.gatech.edu/node/583270/xml";
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
	var posts = {};
	var domParser = new DOMParser();
	var xmlData = domParser.parseFromString(data, 'text/xml');
	var nodes = xmlData.getElementsByTagName("node");

	for (i = 0; i < nodes.length; i++) {
		var id = nodes[i].getAttribute("id");

		var times = []
		var nodeTimeItems = nodes[i].getElementsByTagName("times")[0].getElementsByTagName("item");
		for (j = 0; j < nodeTimeItems.length; j++) {
			times.push({
				start: nodeTimeItems[j].getElementsByTagName("value")[0].textContent,
				end: nodeTimeItems[j].getElementsByTagName("value2")[0].textContent,
				rrule: nodeTimeItems[j].getElementsByTagName("rrule")[0].textContent,
				timezone: nodeTimeItems[j].getElementsByTagName("timezone")[0].textContent,
				timezone_db: nodeTimeItems[j].getElementsByTagName("timezone_db")[0].textContent,
				date_type: nodeTimeItems[j].getElementsByTagName("date_type")[0].textContent,
			});
		}

		var timesGMT = []
		var nodeTimeItemsGMT = nodes[i].getElementsByTagName("gmt_times")[0].getElementsByTagName("item");
		for (j = 0; j < nodeTimeItems.length; j++) {
			timesGMT.push({
				start: nodeTimeItems[j].getElementsByTagName("value")[0].textContent,
				end: nodeTimeItems[j].getElementsByTagName("value2")[0].textContent,
				rrule: nodeTimeItems[j].getElementsByTagName("rrule")[0].textContent,
				timezone: nodeTimeItems[j].getElementsByTagName("timezone")[0].textContent,
				timezone_db: nodeTimeItems[j].getElementsByTagName("timezone_db")[0].textContent,
				date_type: nodeTimeItems[j].getElementsByTagName("date_type")[0].textContent,
			});
		}

		var imageLink = "";
		var media = nodes[i].getElementsByTagName("hg_media")[0];
		if (typeof media.getElementsByTagName("item")[0] != 'undefined') {
			imageLink = media.getElementsByTagName("item")[0].getElementsByTagName("image_full_path")[0].textContent;
		}

		var related = []
		var nodeRelated = nodes[i].getElementsByTagName("related")[0].getElementsByTagName("link");
		for (j = 0; j < nodeRelated.length; j++) {
			related.push({
				title: nodeRelated[j].getElementsByTagName("title")[0].textContent,
				url: nodeRelated[j].getElementsByTagName("url")[0].textContent
			});
		}

		var categories = {}
		var nodeCategories = nodes[i].getElementsByTagName("categories")[0].getElementsByTagName("category");
		for (j = 0; j < nodeCategories.length; j++) {
			categories[nodeCategories[j].getAttribute("tid")] = nodeCategories[j].textContent;
		}

		var eventTerms = {}
		var nodeEvtTerms = nodes[i].getElementsByTagName("event_terms")[0].getElementsByTagName("term");
		for (j = 0; j < nodeEvtTerms.length; j++) {
			eventTerms[nodeEvtTerms[j].getAttribute("tid")] = nodeEvtTerms[j].textContent;
		}

		var eventAudience = {}
		var nodeEvtAud = nodes[i].getElementsByTagName("event_audience")[0].getElementsByTagName("term");
		for (j = 0; j < nodeEvtAud.length; j++) {
			nodeEvtAud[nodeEvtAud[j].getAttribute("tid")] = nodeEvtAud[j].textContent;
		}

		var keywords = {}
		var nodeKeywords = nodes[i].getElementsByTagName("keywords")[0].getElementsByTagName("keyword");
		for (j = 0; j < nodeKeywords.length; j++) {
			nodeKeywords[nodeKeywords[j].getAttribute("tid")] = nodeKeywords[j].textContent;
		}

		var post = {
			title: nodes[i].getElementsByTagName("title")[0].textContent,
			uid: nodes[i].getElementsByTagName("uid")[0].textContent,
			body: nodes[i].getElementsByTagName("body")[0].textContent,
			author: nodes[i].getElementsByTagName("author")[0].textContent,
			created_epoch: nodes[i].getElementsByTagName("created")[0].textContent,
			created_gmt: nodes[i].getElementsByTagName("gmt_created")[0].textContent,
			changed_epoch: nodes[i].getElementsByTagName("changed")[0].textContent,
			changed_gmt: nodes[i].getElementsByTagName("gmt_changed")[0].textContent,
			summary: nodes[i].getElementsByTagName("teaser")[0].textContent,
			type: nodes[i].getElementsByTagName("type")[0].textContent,
			summary_withTags: nodes[i].getElementsByTagName("summary")[0].textContent,
			start: nodes[i].getElementsByTagName("start")[0].textContent,
			end: nodes[i].getElementsByTagName("end")[0].textContent,
			end_last: nodes[i].getElementsByTagName("end_last")[0].textContent,
			start_gmt: nodes[i].getElementsByTagName("gmt_start")[0].textContent,
			end_gmt: nodes[i].getElementsByTagName("gmt_end")[0].textContent,
			end_last_gmt: nodes[i].getElementsByTagName("gmt_end_last")[0].textContent,
			times: times,
			times_gmt: timesGMT,
			phone: nodes[i].getElementsByTagName("phone")[0].textContent,
			url: nodes[i].getElementsByTagName("url")[0].textContent,
			email: nodes[i].getElementsByTagName("email")[0].textContent,
			contact: nodes[i].getElementsByTagName("contact")[0].textContent,
			fee: nodes[i].getElementsByTagName("fee")[0].textContent,
			location: nodes[i].getElementsByTagName("location")[0].textContent,
			image: imageLink,
			related: related,
			categories: categories,
			event_terms: eventTerms,
			event_audience: eventAudience,
			keywords: keywords
		}
		posts[id] = post;
	}
	updateFirebasePosts(posts);
}

function updateFirebasePosts(posts) {
	var postRef = firebase.database().ref("posts");
	postRef.update(posts);
}

function refreshGTCalData() { 
	console.log(new Date().toString() + " : GT Calendar refreshing began.");
	fetchGTCal(); 
	console.log(new Date().toString() + " : GT Calendar refreshing finished.");
}
refreshGTCalData();

// Refresh GT Calendar data at 5 AM (EST) everyday.
// scheduler.scheduleJob({hour: 5, minute: 0}, function() {
// 	refreshGTCalData();
// });
