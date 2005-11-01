	<!---------------------------Advance Tab Content ------------------------>	
	<div id='content_a'  class="tabbody content_a">
	<h1><fmt:message key="label.authoring.heading.advance" /></h1>
	<h2><fmt:message key="label.authoring.heading.advance.desc" /></h2>
	<table class="forms">
		<!-- Instructions Row -->
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="forum.lockWhenFinished" value="1">
					<fmt:message key="label.authoring.advance.lock.on.finished" />
				</html:checkbox>
			</td>
		</tr>
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="forum.allowEdit" value="1">
					<fmt:message key="label.authoring.advance.allow.edit" />
				</html:checkbox>
			</td>
		</tr>
		<tr>
			<td colspan="2 class="formcontrol">
				<html:checkbox property="forum.allowRichEditor" value="1">
					<fmt:message key="label.authoring.advance.use.richeditor" />
				</html:checkbox>
			</td>
		</tr>
		<!-- Button Row -->
		<tr><td colspan="2"><html:errors/></td></tr>
		<tr>
			<td><html:button property="cancel" onclick="window.close()" styleClass="button">
				<fmt:message key="label.authoring.cancel.button" />
			</html:button></td>
			<td >
			<html:submit property="save" styleClass="button">
				<fmt:message key="label.authoring.save.button" />
			</html:submit></td>
		</tr>
	</table>
	</div>