<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="f"  uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"  %>
<%@ taglib prefix="form"  uri="http://www.springframework.org/tags/form"  %>


<spring:url value="/resources/js/jquery-1.4.4.min.js" var="jqueryjs" />
<spring:url value="/chat/post" var="chatPostUrl" />
<spring:url value="/chat/list" var="chatListUrl" />

<script type="text/javascript" src="${jqueryjs }"></script>
<script type="text/javascript">
	function prepareChatRoom(username) {
		$('#chataction').show();
		$('#chatsend').click(function() {
			var msg = $('#chatbox').val();
			$('#chatwindow').append('<div>me: ').append(msg).append('</div>');
			$('#chatbox').val('');
			$.ajax({
				url: '${chatPostUrl}',
				data: 'toUser=' + username + '&contents=' + msg,
				dataType: 'json'
			});
		});
		
		loadMessages(username);
	}

	function loadMessages(username) {
		var lastMessageTime = $('#lastMessage').data(username);
		if (!lastMessageTime) {
			lastMessageTime = new Date().valueOf();
			$('#lastMessage').data(username, lastMessageTime);
		}
		
		$.ajax({
			url: '${chatListUrl}',
			data: 'fromUser=' + username + '&since=' + lastMessageTime,
			dataType: 'json',
			success: function(data) {
				if (data && data.length > 0) {
					$.each(data, function(i,v) {
						$('#chatwindow').append('<div>').append(username).append(': ').append(v.contents).append('</div>');	
					});
					$('#lastMessage').data(username, data[data.length-1].timestamp);
				}
			}
		});
		setTimeout(function() {loadMessages(username);}, 2500);
	}
	
</script>

<div id="chatroom" style="margin:0px auto;width:800px">
<div id="lastMessage" style="display:none"></div>
<div id="users" style="float:left;border:2px solid black;width:196px">
	<c:forEach items="${requestScope.users }" var="user">
	<div style="float:left;width:20em"><a href="#" onclick="prepareChatRoom('<c:out value="${user.name }"/>');"><c:out value="${user.name }"/></a></div>
	</c:forEach>
</div>
<div id="chatview" style="float:right;border:2px solid black;width:596px">
<div id="chatwindow" style="height:200px"></div>
<div id="chataction" style="display:none">
<textarea rows="1" cols="50" id="chatbox"></textarea><button id="chatsend">Send</button>
</div>
</div>
</div>