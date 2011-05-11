<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"  %>
<%@ taglib prefix="form"  uri="http://www.springframework.org/tags/form"  %>

<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>

<script>
function showMap(div, latitude, longitude) {
  var myLatlng = new google.maps.LatLng(latitude, longitude);
  var myOptions = {
    zoom: 15,
    center: myLatlng,
    mapTypeControl: false,
    navigationControlOptions: {style: google.maps.NavigationControlStyle.SMALL},
    mapTypeId: google.maps.MapTypeId.ROADMAP
  };
  var map = new google.maps.Map(document.getElementById(div), myOptions);
  var marker = new google.maps.Marker({
      position: myLatlng, 
      map: map, 
      title:"You are here!"
  });
}
</script>

Project [ ${sessionScope.project.name } ] &gt; Items

<spring:url var="backUrl" value="/items/p" />
<spring:url var="newUrl" value="/items/new" />

<div><a href="${backUrl}" title="Back to Project page">Back</a>
<a href="${newUrl}" title="Add Item">Add Item</a></div>

<div>
		<div class="entry header" style="clear:both">
			<div style="float:left;width:5em">Item Name</div>
			<div style="float:left;width:5em">Count</div>
			<div style="float:left;width:5em">Completed?</div>
			<div style="float:left;width:20em">URL</div>
			<div style="float:left;width:20em">Location</div>
			<div style="float:left;width:15em">Actions</div>
		</div>

	<c:forEach items="${requestScope.items}" var="item">
		<spring:url var="removeUrl" value="/items/remove/${item.id}" />
		<spring:url var="editUrl" value="/items/edit/${item.id}" />
		<spring:url var="completeUrl" value="/items/complete/${item.id}" />
		<div class="entry" style="clear:both">
			<div style="float:left;width:5em"><c:out value="${item.name}"/></div>
			<div style="float:left;width:5em"><c:out value="${item.count}" default="&nbsp;" escapeXml="false"/></div>
			<div style="float:left;width:5em"><c:if test="${!item.done }">No</c:if><c:if test="${item.done }">Yes</c:if></div>
			<div style="float:left;width:20em"><a href="${item.url}" title="go" target="_blank">${item.url}</a>&nbsp;</div>
			<div style="float:left;width:20em">
			<c:if test="${!empty item.location }">
			<div id="${item.name}_loc" style="height:200px;width:310px"></div>
			<script>
				var latlon = eval( "(" + '${item.location}' + ")" );
				showMap("${item.name}_loc", latlon.latitude, latlon.longitude );
			</script>
			</c:if>
			&nbsp;</div>
			<div style="float:left;width:5em"><a href="${editUrl}" title="Edit Item">Edit</a></div>
			<div style="float:left;width:5em"><a href="${removeUrl}" title="Remove Item">Remove</a></div>
			<div style="float:left;width:5em"><a href="${completeUrl}" title="Remove Item">Complete</a></div>
		</div>
	</c:forEach>
</div>