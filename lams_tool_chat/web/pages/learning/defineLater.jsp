<!DOCTYPE html>

<%@ include file="/common/taglibs.jsp"%>

<lams:html>
		
		<c:set var="lams"> <lams:LAMSURL /> </c:set>
		<c:set var="tool"> <lams:WebAppURL /> </c:set>
	
	<lams:head>
		<title>
			<fmt:message key="activity.title" />
		</title>
		<link href="${tool}includes/css/chat.css" rel="stylesheet" type="text/css">
		<link href="<lams:LAMSURL/>css/defaultHTML_learner.css" rel="stylesheet" type="text/css">
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/bootstrap.min.js"></script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/jquery.js"></script>
		<script type="text/javascript">
		var MODE = "${MODE}", TOOL_SESSION_ID = '<c:out value="${param.toolSessionID}" />', APP_URL = '<lams:WebAppURL />', LEARNING_ACTION = "<c:url value='learning/learning.do'/>", LAMS_URL = '<lams:LAMSURL/>';
		</script>
		<script type="text/javascript" src="<lams:LAMSURL/>includes/javascript/portrait.js"></script>
		<lams:JSImport src="includes/javascript/learning.js" relative="true" />
		
	</lams:head>
	<body class="stripes">
			
						
		<c:set var="title" scope="request">
				<fmt:message key="activity.title" />
		</c:set>		
		
		<lams:Page type="learner" title="${title}">
			<lams:DefineLater defineLaterMessageKey="message.defineLaterSet" />
		</lams:Page>



			
	</body>
</lams:html>
