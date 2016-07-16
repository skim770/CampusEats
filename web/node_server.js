var gtCalURL = "http://www.calendar.gatech.edu/feeds/events.xml";
var posts;

var firebase = require("firebase");
firebase.initializeApp({
	serviceAccount: "firebaseServiceAccountCred/node_server_private_key.json",
	databaseURL: "https://project-2581007719456375150.firebaseio.com/"
});

var db = firebase.database();
var ref = db.ref("posts");
ref.once("value", function(snapshot) {
	console.log(snapshot.val());
});

// var http = require("http");

function fetchGTCal() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (request.readyState === XMLHttpRequest.DONE && request.status === 200) {
			console.log(request.responseXML);
			parseGTCal(request.responseXML);
		}
	}
	request.open("GET", gtCalURL, true);
	request.send();
}

function parseGTCal(xmlData) {
	var items = xmlData.getElementsByTagName("item");
	for (i = 0; i < items.childElementCount; i++) {
		var item = items[i];
		var post = {
			title: item.getElementsByTagName("title")[0].textContent,
			link: item.getElementsByTagName("link")[0].textContent,
			desc: item.getElementsByTagName("description")[0].textContent,
			pubDate: item.getElementsByTagName("pubDate")[0].textContent,
			category: item.getElementsByTagName("category")[0].textContent,
			comments: item.getElementsByTagName("comments")[0].textContent
		}
	}
}

fetchGTCal();

// http.createServer(function (request, response) {
// 	// Send the HTTP header 
//    // HTTP Status: 200 : OK
//    // Content Type: text/plain
//    response.writeHead(200, {'Content-Type': 'text/plain'});
   
//    // Send the response body as "Hello World"
//    response.end('Hello World\n');
// }).listen(8081);