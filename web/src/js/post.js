document.getElementById('post').addEventListener('click', handlePost, false);
var firstName, lastName, email, score;
firebase.auth().onAuthStateChanged(function(user) {
    if (user) {
        var userId = firebase.auth().currentUser.uid;
        return firebase.database().ref('/users/' + userId).once('value').then(function(snapshot) {
            firstName = snapshot.val()["first_name"];
            lastName = snapshot.val()["last_name"];
            email = snapshot.val().email;
            score = snapshot.val().score;
        });
    }
});

function showUserSubmissionForm() {
    $('#overlay-post').removeClass("none");
    $('#overlay-post').popup('show');
}

function tag(data) {
    return "<p>" + data + "</p>";
}

function formatCP(name, email) {
    return "<p>" + name + "</p>\n\n<p><a href=\"mailto:" + email + ">\"" + email + "</a></p>";
}
    
function handlePost() {    
    var eventTitle = document.getElementById('title').value;
    var location = document.getElementById('location').value;
    var month = Number(document.getElementById('month').value);
    var day = Number(document.getElementById('day').value);
    var year = Number(document.getElementById('year').value);
    var startHour = Number(document.getElementById('stimeH').value);
    var startMinutes = Number(document.getElementById('stimeM').value);
    var startAMPM = document.getElementById('sAMPM').value;
    var endHour = Number(document.getElementById('etimeH').value);
    var endMinutes = Number(document.getElementById('etimeM').value);
    var endAMPM = document.getElementById('eAMPM').value;
    var description = document.getElementById('desc').value;
    var summary = document.getElementById('summ').value;
    var contactName = document.getElementById('cp').value;
    var contactEmail = document.getElementById('cpemail').value;

    if (eventTitle.length < 1) {
        alert('Please enter the event\'s title.');
        return;
    }
    if (location.length < 1) {
        alert('Please enter a valid location.');
        return;
    }
    if (month > 12 || month < 1) {
        alert('Please enter a valid month.');
        return;
    }
    if (day > 31 || day < 1) {
        alert('Please enter a valid day.');
        return;
    }
    if (year < 1000) {
        alert('Please enter a year.');
        return;
    }
    if (startHour > 12 || startHour < 1) {
        alert('Please enter a valid time.');
        return;
    }
    if (endHour > 12 || endHour < 1) {
        alert('Please enter a valid time.');
        return;
    }
    if (startMinutes > 59 || startMinutes < 0) {
        alert('Please enter a valid time.');
        return;
    }
    if (endMinutes > 59 || endMinutes < 0) {
        alert('Please enter a valid time.');
        return;
    }
    if (description.length < 1) {
        alert('Please enter a valid description.');
        return;
    }
    if (summary.length < 1) {
        alert('Please enter a valid description.');
        return;
    }
    if (contactName.length < 1) {
        alert('Please enter a contact name.');
        return;
    }
    if (contactEmail.length < 1) {
        alert('Please enter a contact email.');
        return;
    }

    var today = new Date();
    var epoch = today.getTime();
    var ISOTime = today.toISOString();
    var gmt = ISOTime.substring(0,19).replace("T", " ");

    startHour = startAMPM == "PM" ? startHour + 12 : startHour;
    endHour = endAMPM == "PM" ? endHour + 12 : endHour;
    var startTime = new Date(year, month-1, day, startHour, startMinutes);
    var endTime = new Date(year, month-1, day, endHour, endMinutes);
    var startISO = startTime.toISOString();
    var endISO = endTime.toISOString();
    var startgmt = startISO.substring(0,19).replace("T", " ");
    var start = startISO.substring(0,11);
    start += startTime.getHours() < 10 ? "0"+startTime.getHours() : startTime.getHours();
    start += startISO.substring(13,19)+"-"+endISO.substring(11,16);

    var endgmt = endISO.substring(0,19).replace("T", " ");
    var end = endISO.substring(0,11);
    end += endTime.getHours() < 10 ? "0"+endTime.getHours() : endTime.getHours();
    end += endISO.substring(13,19)+"-"+endISO.substring(11,16);
    var author = firstName + " " + lastName;
    var taggedDesc = tag(description);
    var taggedSumm = tag(summary);
    var contact = formatCP(contactName,contactEmail);
    writeNewPost(author, taggedDesc, epoch, gmt, contact, end, endgmt, location, start, startgmt, eventTitle, taggedSumm, summary);
}

/**
 * Writes a new post on the Firebase database.
 */
function writeNewPost(author, body, epoch, gmt, contact, end, endgmt, location, start, startgmt, title, taggedSumm, summary) {
    var newPostKey = firebase.database().ref().child('posts').push().key;
    var updates = {};
    console.log(author, body, epoch, gmt, contact, end, endgmt, location, start, startgmt, title);
    var postData = {
        author:author,
        body:body,
        changed_epoch: epoch,
        changed_gmt: gmt,
        contact: contact,
        email:"",
        end:end,
        end_gmt:endgmt,
        end_last:end,
        end_last_gmt:endgmt,
        fee: "Free",
        feedback_score: 0,
        image: "",
        location: location,
        phone: "",
        start: start,
        start_gmt: startgmt,
        summary:summary,
        summary_withTags:taggedSumm,
        times: {
          0: {
            date_type: "datetime",
            end: end,
            rrule: "",
            start: start,
            timezone: "America/New_York",
            timezone_db: "America/New_York"
          }
        },
        times_gmt: {
          0: {
            date_type: "datetime",
            end: end,
            rrule: "",
            start: start,
            timezone: "America/New_York",
            timezene_db:"America/New_York"
          }
        },
        title: title,
        type: "event",
        uid: newPostKey,
        url: "",
    };

    updates['/posts/' + newPostKey] = postData;
    firebase.database().ref().update(updates);
    $('#overlay-post').addClass("none");
    $('#overlay-post').popup('hide');
}

function isNumberKey(evt){
    var charCode = (evt.which) ? evt.which : event.keyCode
    if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;
    return true;
}