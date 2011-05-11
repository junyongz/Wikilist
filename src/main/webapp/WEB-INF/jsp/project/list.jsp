<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"  %>
<%@ taglib prefix="form"  uri="http://www.springframework.org/tags/form"  %>

<spring:url var="exploreUrl" value="/projects/explore" />
<spring:url var="newUrl" value="/projects/new" />

<p>
Projects
<p>
<a href="${exploreUrl}" title="Explore project">Explore</a> | <a href="${newUrl}" title="Create new project">New Project</a>

<div>
	<c:forEach items="${requestScope.projects}" var="project">
		<spring:url var="itemsUrl" value="/items/${project.id}" />
		<spring:url var="editUrl" value="/projects/edit/${project.id}" />
		<spring:url var="removeUrl" value="/projects/remove/${project.id}" />
		<div class="entry" style="clear:both">
			<%-- <div style="float:left"><c:out value="${project.id}" /></div> --%>
			<div style="float:left;width:20em"><c:out value="${project.name}" /></div>
			<div style="float:left;width:5em"><a href="${itemsUrl}" title="view your items">Items</a></div>
			<div style="float:left;width:5em"><a href="${editUrl}" title="edit project">Edit</a></div>
			<div style="float:left;width:5em"><a href="${removeUrl}" title="remove project">Remove</a></div>
		</div>
	</c:forEach>
</div>