function fetchGTCal() {
	var gtCalURL = "http://www.calendar.gatech.edu/feeds/events.xml";
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
	
}