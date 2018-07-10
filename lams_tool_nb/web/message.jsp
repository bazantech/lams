<%@ include file="/includes/taglibs.jsp"%>



<script type="text/javascript">
	
	function disableFinishButton() {
		var elem = document.getElementById("finishButton");
		elem.disabled = true;
	}
	 function submitForm(methodName){
                var f = document.getElementById('messageForm');
                f.submit();
        }
</script>


<div id="content">
	
	<h1>
		<fmt:message key="activity.title" />
	</h1>
	
	<c:set var="hasErrors">
		<form:errors path='*'/>
	</c:set>
	<c:if test="${not empty hasErrors}">
		<lams:Alert id="error" type="danger" close="false">
		<form:errors path="*"/>
		</lams:Alert>
	</c:if>
	
	<div class="align-right space-bottom-top">
		<form:form modelAttribute="messageForm" action="/learner" target="_self" onsubmit="disableFinishButton();" id="messageForm">
			<form:hidden path="toolSessionID" />
			<form:hidden path="mode" />
			<form:hidden path="method" value="finish"/>
			<c:choose>
				<c:when test="${reflectOnActivity}">
					<input type="submit" name="method" class="button">
						<fmt:message key="button.continue" />
					</input>
				</c:when>
				<c:otherwise>
					<a href="#nogo" class="button" id="finishButton" onclick="submitForm('finish')">
						<span class="nextActivity">
							<c:choose>
								<c:when test="${activityPosition.last}">
									<fmt:message key="button.submit" />
								</c:when>
								<c:otherwise>
									<fmt:message key="button.finish" />
								</c:otherwise>
							</c:choose>
						</span>
					</a>
				</c:otherwise>
			</c:choose>
		</form:form>
	</div>

</div>
