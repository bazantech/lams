<%-- 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
License Information: http://lamsfoundation.org/licensing/lams/2.0/

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License version 2 as 
  published by the Free Software Foundation.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt
--%>
 
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="tags-bean" prefix="bean"%> 
<%@ taglib uri="tags-html" prefix="html"%>
<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-fmt" prefix="fmt" %>

<%
String protocol = request.getProtocol();
if(protocol.startsWith("HTTPS")){
	protocol = "https://";
}else{
	protocol = "http://";
}
String pathToRoot = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
String pathToShare = protocol+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/../..";

%>
<body bgcolor="#9DC5EC">
	<c:if test="${!empty activityForm.progressSummary}">
		<c:set var="passonurl">passon.swf?<c:out value="${activityForm.progressSummary}" escapeXml="false"/>&uniqueID=<c:out value="<script language='text/javascript'>document.write(parent.frames['controlFrame'].uniqueID);</script>" escapeXml="false"/></c:set>
		<!-- URL's used in the movie-->
		<!-- text used in the movie-->
		<!--Library-->  
		<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
		 codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,47,0" name="passon"
		 width="1" height="1" align="left" id="passon">
		  <param name="allowScriptAccess" value="sameDomain" />
	
		  <param name="movie" value="<c:out value="${passonurl}" escapeXml="false"/>"/>
		  <param name="quality" value="high">
		  <param name="scale" value="noscale">
		  <param name="bgcolor" value="#FFFFFF">
		  <embed 	
			  src="<c:out value="${passonurl}" escapeXml="false"/>"
			  quality="high" 
			  scale="noscale" 
			  bgcolor="#FFFFFF"  
			  width="1" 
			  height="1" 
			  swliveconnect=true 
			  id="passon" 
			  name="passon" 
			  align=""
			  type="application/x-shockwave-flash" 
			  pluginspage="http://www.macromedia.com/go/getflashplayer" />
		</object>
	</c:if>

	<c:if test="${!empty activityForm.activityURLs}">
		
		<script language="JavaScript" type="text/JavaScript"><!--
			function redirectPage() {
				setTimeout("doRedirect()", 500);
			}
			function doRedirect() {
				var url = "<c:out value='${activityForm.activityURLs[0].url}' escapeXml="false" />";
				if ( url.substring(0,4)!="http" ) {
					if ( url.substring(0,1)=="/" ) {
						url = "<%=pathToRoot%>.."+url;
					} else {
						url = "<%=pathToRoot%>../"+url;
					}
				}
				window.location.href = url;
			}
			window.onload = redirectPage;
			//-->
		</script>
		
		<div align="center">
			<table width="100%" border="0" align="center" summary="This table is being used for layout purposes">
				<tr>
					<td valign="top">
						<H1><fmt:message key="message.activity.loading" /></H1>
					</td>
				</tr>
				<!-- Tool summary is <c:out value="${activityForm.progressSummary}" escapeXml="false" /> -->
			</table>
		</div>
			
	</c:if>
</body>
