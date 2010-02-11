Array.prototype.remove = function(from, to) {
  var rest = this.slice((to || from) + 1 || this.length);
  this.length = from < 0 ? this.length + from : from;
  return this.push.apply(this, rest);
};

var routeText = [];
var routeAudioUrl = [];
var routePhotoUrl = [];

/**
* Ecah marker info has a corresponding marker information input zone with the 
* css identity defined as a key in the markerInfo array. Making it easy to add
* types of media to the application.
**/
//HAX variable used beacuse javascript doesn't support true associative array
//which in turns makes the elements defined become a part of the array object's properties and methods.


var markerInfo = [];
markerInfo['routeText'] = [];
markerInfo['routeAudioUrl'] = [];
markerInfo['routePhotoUrl'] = [];
markerInfo['routeWebUrl'] = [];
numberOfMedia = 4;

/**
* Check if item exists in an array
* @param array: the array to be searched
* @param checkItem: the item to be checked if is in array
**/
function findInArray(array,checkItem) {
	for(i=0;i<array.length;i++){
		if(array[i]==checkItem)
			return i;		
	}
	return -1;
}

/**
* Main javascript function for the googlemaps
**/
$(document).ready(function(){
	var map = new GMap2(document.getElementById('map'));	
	var startingPoint = new GLatLng(64.138252,-21.926658);
	map.setCenter(startingPoint, 14);
	var bounds = map.getBounds();
	var southWest = bounds.getSouthWest();
	var northEast = bounds.getNorthEast();
	var lngSpan = northEast.lng() - southWest.lng();
	var latSpan = northEast.lat() - southWest.lat();
	var lastPoint;
	var markers = [];
	var numberOfMarkers = 0;
	var poly = [];
	var mapLines = [];
	var MARKER_RADIUS = 0.1;
	var CIRCLE_NODES = 40;
	var LINE_COLOR = "#0000FF"
	var LINE_WIDTH = 5;

	map.addControl(new GLargeMapControl());
	map.disableDoubleClickZoom();  
	
	// lastPoint always contains the last point hovered on map	
	GEvent.addListener(map, "mousemove", function(point) {
		lastPoint = point;
	});
	
	/***********************************************************************
	// MARKER CREATION
	// Add marker upon double click on map, initialize it with onclick event, 
	// link in sidebar and a empty information, but first store the previous 
	// marker information if there is any.	
	*************************************************************************/
	GEvent.addListener(map, "dblclick", function() {
		markerNumber = markers.length;
		// get information about the last point which was hovered and add it on map
		lastPoint = new GLatLng(lastPoint.lat(),lastPoint.lng());
		marker = new GMarker(lastPoint,{draggable: true});
		map.addOverlay(marker);	
		markers.push(marker);
		
		drawCircle(lastPoint,MARKER_RADIUS,CIRCLE_NODES,markerNumber);
		numberOfMarkers = markers.length;
		
		// function calls needed if there is only one marker
		// draw line if there are more than one markers, and store any marker information
		if(numberOfMarkers > 1) {
			storeCurrentMarkerInformation();
			newMarkerInformation(markerNumber);
			drawLine(markers[numberOfMarkers-2].getLatLng(),lastPoint,numberOfMarkers-1,LINE_COLOR,LINE_WIDTH);
			
		}
		else {
			newMarkerInformation(markerNumber);
		}
		
		//***********************************************************************
		// ADDING EVENT LISTENERS TO MARKERS
		/** Upon double click on marker, the marker gets removed from the map and its information as well **/
		GEvent.addListener(marker, "dblclick", function(){
			i = findInArray(markers,this);
			marker = markers[i];

			// Remove a marker and it's information, also need to repopulate the location list and map
			if(markers.length-1 == 0){ // Do not remove the line if this is the only marker
				markers.remove(i);				
				$('.point:eq('+i+')').remove();				
			}
			else if(markers.length-1 > i && i == 0){					
				removeLineOverlay(i+1);			
				markers.remove(i);				
				mapLines.remove(i+1);			
				$('.point:eq('+i+')').remove();				
			}
			else if(markers.length-1 == i){
				removeLineOverlay(i);				
				markers.remove(i);
				mapLines.remove(i);			
				$('.point:eq('+i+')').remove();
			}
			else if(markers.length-1 > i){
				removeLineOverlay(i);
				removeLineOverlay(i+1);				
				mapLines.remove(i+1);	
				markers.remove(i);									
				showLine(i,markers[i]);				
				$('.point:eq('+i+')').remove();
			}
			//### These removals happen no matter what marker is being removed 
			map.removeOverlay(this);
			removeCircleOverlay(i);							
			poly.remove(i);	
			removeMarkerInformation(i);
			if(markers.length != 0){
				retriveMarkerInformation(0);
			}
			//###
			
			// Now need to repopulate the clickable location list with the changed indexing
			refreshLocationList(markers);
		});
				
	
		// Add on click listener	
		GEvent.addListener(marker, "click", function(){	
			i = findInArray(markers,this);
			storeCurrentMarkerInformation();
			retriveMarkerInformation(i);
			marker = markers[i];
		});				
	
		// When the marker is dragged
		GEvent.addListener(marker, "dragstart", function() {
			i = findInArray(markers,this);
			removeCircleOverlay(i);
			
			if(i != 0) {
				removeLineOverlay(i);
			}
			if(markers.length-1 > i || (i == 0 && markers.length > 1)){
				removeLineOverlay(i+1);
			}
		});
	
		// When marker is released after drag
		GEvent.addListener(marker, "dragend", function() {
			i = findInArray(markers,this);
			showCircle(i,markers[i]);
			
			if(i != 0){
				showLine(i,markers[i]);		
			}
			// if the marker has two lines connected
			if(markers.length-1 > i || (i == 0 && markers.length >= 1)){
				showLine(i+1,markers[i+1]);
			}
		});

		// Construct a new list item, initialize it with a click event to move onto the location
	    $("<li class=point/>")
		.html("# "+markers.length).click(function(){
				i = $(".point").index(this);			
				marker = markers[i];
				storeCurrentMarkerInformation(); // before switching first store prior marker info
				setMarkerInformationNumber(i);
				retriveMarkerInformation(i);
				displayPoint(marker);
		})
		.hide()		
		.appendTo("#list")
		.fadeIn();
		
		// Needed to make the routeText array have same indexing as the markers array
		//routeText.push("");

		// Open the html hint input upon marker creation
		//openMarkerInfo(marker,markers.length-1);

			
		// reset the cluetext and id
	}); // Finish of create marker function


	/** Check if there is any error when submission button has been clicked **/
	$('#submit').click(function(e) {							
		storeCurrentMarkerInformation(); // make sure that last marker information has been stored
		if($('#routeName').val()=='') {
			$("#submitError:hidden").slideDown("true");
			e.preventDefault();
		}
		for(i=0;i<routeText.length;i++){
			if(routeText[i]=='') {
				$("#submitError:hidden").slideDown("true");	
				e.preventDefault();
			}
		}
		makeJson();
	});
	
	// Used while debugging
	$('#routeName').keyup(function() {
		makeJson();
	});
	
	/**
	* Refreshes the list of locations
	* @markers : all markers currently visible on map
	**/
	function refreshLocationList(markers){
		$("#list .point").remove();
		
		for(i=0;i<markers.length;i++) {
			$("<li class=point/>")
					.html("# "+(i+1)).click(function(){
					z = $(".point").index(this);			
					marker = markers[z];
					displayPoint(marker);
			})
			.hide()		
			.appendTo("#list")
			.fadeIn();
		}
	}

	/**
	* Pans to the marker provided as a parameter
	**/
	function displayPoint(marker){
		map.panTo(marker.getPoint());
	}
	
	/**
	* Draws a line between two markers and adds it to the map
	* @param first: The starting point of the line
	* @param last: The finishing point of the line
	* @param markerNo: The number of the marker for which the overlay will be bounded too
	* @param lineColor: Hexadecimal color value
	* @param lineWidth: The width of the line to be drawed
	**/
	function drawLine(first,last,markerNo,lineColor,lineWidth){
		var points = [];
		points.push(first);
		points.push(last);	
		mapLines[markerNo] = new GPolyline(points,lineColor,lineWidth);
		map.addOverlay(mapLines[markerNo]);
	}


	function showLine(markerNo,marker) {
		if(markers.length > 1){
			//numberOfMarkers = markers.length-1; //zero indexed	
			var start = markers[markerNo-1].getLatLng();
			var end = marker.getLatLng();
			drawLine(start,end,markerNo,LINE_COLOR,LINE_WIDTH);
		}
	}
	
	function removeLineOverlay(markerNo) {
		map.removeOverlay(mapLines[markerNo]);	
	}


	/**
	* Draws a circle with given radius around a given center.
	*
	* @param center: A GLatLng object
	* @param radius: The circle's radius in KM
	* @param nodes: Number of segments to construct the circle from
	* @param markerNumber: Represents which marker is getting that circle.
	* @param liColor: Hex color reference for the outer line
	* @param liWith: With of the outer line
	* @param liOpa: Outer line opacity 
	* @param fillColor: Circle fill color
	* @param fillOpa: Circle fill opacity
	**/
	// Code borrowed from http://esa.ilmari.googlepages.com/circle.htm - but altered for current usage
	function drawCircle(center, radius, nodes, markerNo, liColor, liWidth, liOpa, fillColor, fillOpa) {
		// Esa 2006
		//calculating km/degree
		var latConv = center.distanceFrom(new GLatLng(center.lat()+0.1, center.lng()))/100;
		var lngConv = center.distanceFrom(new GLatLng(center.lat(), center.lng()+0.1))/100;
	
		//Loop 
		var points = [];
		var step = parseInt(360/nodes)||10;
		for(var i=0; i<=360; i+=step) {
			var pint = new GLatLng(center.lat() + (radius/latConv * Math.cos(i * Math.PI/180)), center.lng() + 
			(radius/lngConv * Math.sin(i * Math.PI/180)));
			points.push(pint);
			bounds.extend(pint); //this is for fit function
		}
		points.push(points[0]); // Closes the circle, thanks Martin
		fillColor = fillColor||liColor||"#0055ff";
		liWidth = liWidth||2;
		poly[markerNo] = new GPolygon(points,liColor,liWidth,liOpa,fillColor,fillOpa);
		map.addOverlay(poly[markerNo]);
	}

	function removeCircleOverlay(markerNo) {		
		map.removeOverlay(poly[markerNo]);							   
	}
	
	function showCircle(markerNo,marker) {
		var point = marker.getLatLng();
		drawCircle(point,MARKER_RADIUS,CIRCLE_NODES,markerNo);
	}		
	
	function makeJson(){
		var gameInfo = '';
		routeType = $('#routeType').val();
		routeName = $('#routeName').val();
		gameInfo = '{';
		gameInfo += '"routeName": "'+routeName+'",';
		gameInfo += '"routeType": "'+routeType+'",';
		
		// make marker info arrays
		k = 0;
		for(var index in markerInfo){
			if(k>=numberOfMedia){ // Associative array HAX, don't advance into other object names
				;
			}
			else {
				k++;
				temp = markerInfo[index];
				gameInfo +='"'+index+'": [';
				for(var i=0 ; i<temp.length;i++){
					if( i == temp.length-1) { // at end of array, close the json array
						gameInfo += '"'+temp[i]+'"';
						gameInfo += '],';
						}
					else {
						gameInfo += '"'+temp[i]+'",';
						}
				}
			}
		}
		
		// make lattitude array
		gameInfo += '"lat": [';
				for(var i=0 ; i<markers.length;i++) {
				if(i == markers.length-1)
					gameInfo += '"'+markers[i].getLatLng().lat()+'"';
				else
					gameInfo += '"'+markers[i].getLatLng().lat()+'",';							
				}			
		gameInfo += '],';
		
	
		// make longitute array
		gameInfo += '"lng": [';
				for(var i=0 ; i<markers.length;i++) {
				if(i == markers.length-1)
					gameInfo += '"'+markers[i].getLatLng().lng()+'"';
				else
					gameInfo += '"'+markers[i].getLatLng().lng()+'",';
				
				}		
		gameInfo += ']';

		gameInfo += '}';		
		$("#jsonData").val(gameInfo);
	}


	/**
	* Opens a html pane when clicked on marker, this pane contains a textarea
	* for user input.
	* @param marker : Marker to open the info for
	* @param markerNo : Number of the marker
	**/
	function openMarkerInfo(marker, markerNo){
		marker.openInfoWindowHtml("<span class='markerInfo'>Type a hint to help getting to next location<br /><textarea class='clueBox' onkeyup='updateHint("+markerNo+");' rows='3' cols='30'>"+routeText[markerNo]+"</textarea>");
	}
	
});


/**
* Sets the markerNumber in the hidden field
**/
function setMarkerInformationNumber(markerNumber){
	$('#markerNumber').val(markerNumber);
	//put the marker info into the hidden field	
}


/**
* Stores the text, audio and photo information for the indexed marker
**/
function storeCurrentMarkerInformation(){
	k = 1;
	markerNo = $('#markerNumber').val();
	for(var index in markerInfo){
		if(k==numberOfMedia+1)
			break;
		k++;

		markerInfo[index][markerNo] = $('#'+index).val();
		}
}

function retriveMarkerInformation(markerNumber){
	k = 1;
	$('#markerNumber').val(markerNumber);	
	for(var index in markerInfo){

		if(k==numberOfMedia+1)
			break;
		k++;

		$('#'+index).val(markerInfo[index][markerNumber]);
	}
}

function newMarkerInformation(markerNumber){
	k = 1;	
	$('#markerNumber').val(markerNumber);
	
	for(var index in markerInfo){	
		if(k==numberOfMedia+1)
			break;
		k++;

		markerInfo[index][markerNumber] = '';
		$('#'+index).val('');
	}
	storeCurrentMarkerInformation();
}

function removeMarkerInformation(markerNo){
	k = 1;
	for(var index in markerInfo){
		if(k==numberOfMedia+1)
			break;
		k++;
		temp = markerInfo[index];
		temp.remove(markerNo);
	}
}