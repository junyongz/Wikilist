<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"  %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<spring:url var="backUrl" value="/projects/" />
<spring:url var="saveUrl" value="/projects/save" />
<p>
Projects &gt; New
<p>
<a href="${backUrl}" title="back to projects">&lt;Back</a>

<div id="addProject">
<form:form commandName="project" id="addProjectForm" action="${saveUrl}" method="post">
	<label for="name">Name</label>
	<form:input path="name" id="name" />
	<form:errors path="name" cssClass="error" />
	<input type="submit" value="create" />
	<form:hidden path="id"/>
</form:form>

</div>