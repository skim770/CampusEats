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