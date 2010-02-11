<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 

      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript" src="http://www.google.com/jsapi?key=ABQIAAAADly3sU72O1MLzIITqgcgRRSGno-oF5LX4ZAUAedHnT2hX-BFkBRR0udFKNaVVaSs5YH8BJtf-9Z-0g"></script>
<script type="text/javascript" charset="utf-8">
    google.load("maps", "2.x");
    google.load("jquery", "1.3.1");
</script>

<script type="text/javascript" src="<?=base_url()?>/javascript/createRoute.js"></script>

<link href="<?=base_url()?>css/createRoute.css" type="text/css" rel="stylesheet" />

</head>

<body bgcolor="#fff">
<H1>Create new route</H1>
<b>Directions</b><br />
<ul>
	<li>Double click to add a location</li>
	<li>Double click on a location to remove it from the game</li>
	<li>Single click on a location to add or change hint</li>
	<li>The blue circle is the coverage of the location, avoid having overlapping locations</li>
</ul>
If you are adding your last location, you can add some congratiulation note for the<br /> user who solves the quest.
<div id="wrapper">
	<div id="mapWrap">
		<div style="color:#CCCCCC">
			<div id="map"></div>
		</div>
	</div>
	<div id="listWrap">
		<ul id="list"></ul>
	</div>
</div>

<div id="inputFields">
	<div id="submitError" style="display:none;">Route name is required</div>
		<? echo form_open('route/addRoute'); ?>	
		<label> <b>Route name</b> </label><br />
		<input type="text" id="routeName" name="routeName" /><br />
		<select name="routeType" id="routeType">
			<option value="1">Historical route</option>
			<option value="2">Hiking</option>
		</select>
		<input type="text" id="jsonData" name="jsonData"/>
		<input type="submit" id="submit" name="submit" value="Create route"/>
		<? echo form_close('');?>
	</div>
	<div>
		<input type="text" id="markerNumber" name="markerNumber"/>
		<label>Text at current marker</label><br />
		<textarea id="routeText" name="routeText" rows="4" cols="75" /></textarea>
		<label>Photo at current marker</label><br />
		<input type="text" id="routePhotoUrl" name="routePhotoUrl" /><br />		
		<label>Audio at current marker</label><br />
		<input type="text" id="routeAudioUrl" name="routeAudioUrl" /><br />
		<label>Web link at current marker</label><br />
		<input type="text" id="routeWebUrl" name="routeWebUrl" /><br />		
	</div>
	</body>
</html>