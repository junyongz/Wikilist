<%@taglib prefix="spring"  uri="http://www.springframework.org/tags"  %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>

<script>

function success(position) {
	  var s = document.querySelector('#status');
	  
	  if (s.className == 'success') {
	    // not sure why we're hitting this twice in FF, I think it's to do with a cached result coming back    
	    return;
	  }
	  
	  s.innerHTML = "found you!";
	  s.className = 'success';
	  
	  var mapcanvas = document.createElement('div');
	  mapcanvas.id = 'mapcanvas';
	  mapcanvas.style.height = '200px';
	  mapcanvas.style.width = '310px';
	    
	  document.querySelector('article').appendChild(mapcanvas);
	  
	  var latlng = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
	  var myOptions = {
	    zoom: 15,
	    center: latlng,
	    mapTypeControl: false,
	    navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
	    mapTypeId: google.maps.MapTypeId.ROADMAP
	  };
	  var map = new google.maps.Map(document.getElementById("mapcanvas"), myOptions);
	  
	  var marker = new google.maps.Marker({
	      position: latlng, 
	      map: map, 
	      title:"You are here!"
	  });
	
	  var latlongvalue = '{latitude:"'+position.coords.latitude+'", longitude:"'+position.coords.longitude+'"}';
	  document.querySelector('#location').value = latlongvalue;
	}
	
	function attachLocation() {
		document.querySelector('#attachLocation').style.display='block';
		document.querySelector('#attachLocationLink').style.display='none';
		if (navigator.geolocation) {
  			navigator.geolocation.getCurrentPosition(success);
		}
	}

</script>



<spring:url var="backUrl" value="/items/" />
<spring:url var="saveUrl" value="/items/save" />

<p>
Project [ ${sessionScope.project.name } ] &gt; Items &gt; New

<p>
<a href="${backUrl}" title="back to items">&lt; Back</a>

<div id="addItem">
<form:form modelAttribute="item" id="addItemForm" action="${saveUrl}" method="post">
	<form:hidden path="id"/>
	<label for="name">Name</label>
	<form:input path="name" id="name" />
	<form:errors path="name" cssClass="error" />
	<label for="count">Count</label>
	<form:input path="count" id="count" />
	<form:errors path="count" cssClass="error" />
	<label for="url">URL</label>
	<form:input path="url" id="url" />
	<form:errors path="url" cssClass="error" />
	<form:hidden path="location" id="location"/>
	<a id="attachLocationLink" href="#" onclick="javascript:attachLocation();" title="Attach Location">Attach Location</a> 
	<article id="attachLocation" style="display:none">
   	<p>Finding your location: <span id="status">checking...</span></p>
	</article>
	<input type="submit" value="create" />
</form:form>

</div>