# CampusEats on Web

CampusEats is hosted at https://project-2581007719456375150.firebaseapp.com

## Install Node.js and npm via Homebrew.
	$ brew install node
	$ node -v 	// currently v6.3.0
	$ npm -v 	// currently v3.10.6

## Dependencies
All the dependencies used to run the node server are installed in node_modules.
- firebase 			: Firebase core web library.
- xmlhttprequest 	: Node wrapper for fetching XML data.
- xmldom			: Node module for converting plain text to XML.
- htmlparser		: Parse plain text to HTML. 
- node-schedule		: Scheduler to fire method calls in time intervals.

Firebase tools are used to initialize, modify, and deploy the web server.
	$ npm install -g firebase-tools // be careful with this, DO NOT MODIFY if possible.

## Run Node Server
	$ node node_server.js