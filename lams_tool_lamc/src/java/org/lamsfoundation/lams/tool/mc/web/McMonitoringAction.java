/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc.web;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.notebook.service.CoreNotebookConstants;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.mc.McAppConstants;
import org.lamsfoundation.lams.tool.mc.McComparator;
import org.lamsfoundation.lams.tool.mc.McGeneralAuthoringDTO;
import org.lamsfoundation.lams.tool.mc.McGeneralLearnerFlowDTO;
import org.lamsfoundation.lams.tool.mc.McGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.mc.ReflectionDTO;
import org.lamsfoundation.lams.tool.mc.pojos.McContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.lamsfoundation.lams.tool.mc.pojos.McQueUsr;
import org.lamsfoundation.lams.tool.mc.pojos.McUsrAttempt;
import org.lamsfoundation.lams.tool.mc.service.IMcService;
import org.lamsfoundation.lams.tool.mc.service.McServiceProxy;
import org.lamsfoundation.lams.util.DateUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.action.LamsDispatchAction;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * * @author Ozgur Demirtas
 */
public class McMonitoringAction extends LamsDispatchAction implements McAppConstants {
    private static Logger logger = Logger.getLogger(McMonitoringAction.class.getName());

    /**
     * main content/question content management and workflow logic
     * 
     */
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	return null;
    }

    /**
     * displayAnswers
     */
    public ActionForward displayAnswers(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	String strToolContentID = request.getParameter(AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = mcService.getMcContent(new Long(strToolContentID));
	mcContent.setDisplayAnswers(new Boolean(true));
	
	McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;
	McGeneralMonitoringDTO mcGeneralMonitoringDTO = new McGeneralMonitoringDTO();

	repopulateRequestParameters(request, mcMonitoringForm, mcGeneralMonitoringDTO);

	String toolContentID = mcMonitoringForm.getToolContentID();

	// generate DTO for All sessions
	MonitoringUtil.setupAllSessionsData(request, mcContent, mcService);

	mcGeneralMonitoringDTO.setRequestLearningReport(new Boolean(false).toString());

	mcGeneralMonitoringDTO.setSummaryToolSessions(MonitoringUtil.populateToolSessions(mcContent));
	mcGeneralMonitoringDTO.setDisplayAnswers(new Boolean(mcContent.isDisplayAnswers()).toString());

	/* setting editable screen properties */
	McGeneralAuthoringDTO mcGeneralAuthoringDTO = new McGeneralAuthoringDTO();
	mcGeneralAuthoringDTO.setActivityTitle(mcContent.getTitle());
	mcGeneralAuthoringDTO.setActivityInstructions(mcContent.getInstructions());

	Map mapOptionsContent = new TreeMap(new McComparator());
	Iterator queIterator = mcContent.getMcQueContents().iterator();
	Long mapIndex = new Long(1);
	while (queIterator.hasNext()) {
	    McQueContent mcQueContent = (McQueContent) queIterator.next();
	    if (mcQueContent != null) {
		mapOptionsContent.put(mapIndex.toString(), mcQueContent.getQuestion());

		mapIndex = new Long(mapIndex.longValue() + 1);
	    }
	}

	request.setAttribute(MC_GENERAL_AUTHORING_DTO, mcGeneralAuthoringDTO);

	List<ReflectionDTO> reflectionsContainerDTO = mcService.getReflectionList(mcContent, null);
	request.setAttribute(REFLECTIONS_CONTAINER_DTO, reflectionsContainerDTO);

	if (mcService.studentActivityOccurredGlobal(mcContent)) {
	    // USER_EXCEPTION_NO_TOOL_SESSIONS is set to false
	    mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(false).toString());
	} else {
	    // USER_EXCEPTION_NO_TOOL_SESSIONS is set to true
	    mcGeneralMonitoringDTO.setUserExceptionNoToolSessions(new Boolean(true).toString());
	}

	request.setAttribute(MC_GENERAL_MONITORING_DTO, mcGeneralMonitoringDTO);

	if (!reflectionsContainerDTO.isEmpty()) {
	    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());

	    String userExceptionNoToolSessions = (String) mcGeneralMonitoringDTO.getUserExceptionNoToolSessions();

	    if (userExceptionNoToolSessions.equals("true")) {
		// there are no online student activity but there are reflections
		request.setAttribute(NO_SESSIONS_NOTEBOOK_ENTRIES_EXIST, new Boolean(true).toString());
	    }
	} else {
	    request.setAttribute(NOTEBOOK_ENTRIES_EXIST, new Boolean(false).toString());
	}
	/* ... till here */

	MonitoringUtil.setSessionUserCount(mcContent, mcGeneralMonitoringDTO);

	return (mapping.findForward(LOAD_MONITORING_CONTENT));
    }

    /**
     * allows viewing users reflection data
     */
    public ActionForward openNotebook(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException, ToolException {
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	String uid = request.getParameter("uid");
	String userId = request.getParameter("userId");
	String userName = request.getParameter("userName");
	String sessionId = request.getParameter("sessionId");
	NotebookEntry notebookEntry = mcService.getEntry(new Long(sessionId), CoreNotebookConstants.NOTEBOOK_TOOL,
		MY_SIGNATURE, new Integer(userId));

	McGeneralLearnerFlowDTO mcGeneralLearnerFlowDTO = new McGeneralLearnerFlowDTO();
	if (notebookEntry != null) {
	    // String notebookEntryPresentable = McUtils.replaceNewLines(notebookEntry.getEntry());
	    mcGeneralLearnerFlowDTO.setNotebookEntry(notebookEntry.getEntry());
	    mcGeneralLearnerFlowDTO.setUserName(userName);
	}

	request.setAttribute(MC_GENERAL_LEARNER_FLOW_DTO, mcGeneralLearnerFlowDTO);

	return mapping.findForward(LEARNER_NOTEBOOK);
    }

    /**
     * downloadMarks
     */
    public ActionForward downloadMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws IOException, ServletException {
	MessageService messageService = getMessageService();

	McMonitoringForm mcMonitoringForm = (McMonitoringForm) form;

	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	String toolContentID = mcMonitoringForm.getToolContentID();

	McContent mcContent = mcService.getMcContent(new Long(toolContentID));

	byte[] spreadsheet = null;

	try {
	    spreadsheet = mcService.prepareSessionDataSpreadsheet(mcContent);
	} catch (Exception e) {
	    log.error("Error preparing spreadsheet: ", e);
	    request.setAttribute("errorName", messageService.getMessage("error.monitoring.spreadsheet.download"));
	    request.setAttribute("errorMessage", e);
	    return mapping.findForward("error");
	}

	// construct download file response header
	OutputStream out = response.getOutputStream();
	String fileName = "lams_mcq.xls";
	String mineType = "application/vnd.ms-excel";
	String header = "attachment; filename=\"" + fileName + "\";";
	response.setContentType(mineType);
	response.setHeader("Content-Disposition", header);

	// write response
	try {
	    out.write(spreadsheet);
	    out.flush();
	} finally {
	    try {
		if (out != null)
		    out.close();
	    } catch (IOException e) {
	    }
	}

	return null;
    }

    /**
     * Set Submission Deadline
     */
    public ActionForward setSubmissionDeadline(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	IMcService service = McServiceProxy.getMcService(getServlet().getServletContext());

	Long contentID = WebUtil.readLongParam(request, AttributeNames.PARAM_TOOL_CONTENT_ID);
	McContent mcContent = service.getMcContent(contentID);

	Long dateParameter = WebUtil.readLongParam(request, McAppConstants.ATTR_SUBMISSION_DEADLINE, true);
	Date tzSubmissionDeadline = null;
	if (dateParameter != null) {
	    Date submissionDeadline = new Date(dateParameter);
	    HttpSession ss = SessionManager.getSession();
	    org.lamsfoundation.lams.usermanagement.dto.UserDTO teacher = (org.lamsfoundation.lams.usermanagement.dto.UserDTO) ss
		    .getAttribute(AttributeNames.USER);
	    TimeZone teacherTimeZone = teacher.getTimeZone();
	    tzSubmissionDeadline = DateUtil.convertFromTimeZoneToDefault(teacherTimeZone, submissionDeadline);
	}
	mcContent.setSubmissionDeadline(tzSubmissionDeadline);
	service.updateMc(mcContent);

	return null;
    }
    
    /**
     * Populate user jqgrid table on summary page.
     */
    public ActionForward userMasterDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	
	Long userUid = WebUtil.readLongParam(request, McAppConstants.USER_UID);
	McQueUsr user = mcService.getMcUserByUID(userUid);
	List<McUsrAttempt> userAttempts = mcService.getFinalizedUserAttempts(user);

	if (userAttempts != null) {
	    for (McUsrAttempt userAttempt : userAttempts) {
		MonitoringUtil.escapeQuotes(userAttempt);
	    }
	}

	request.setAttribute(McAppConstants.USER_ATTEMPTS, userAttempts);
	request.setAttribute(McAppConstants.TOOL_SESSION_ID, user.getMcSession().getMcSessionId());
	return (userAttempts == null || userAttempts.isEmpty()) ? null : mapping.findForward(McAppConstants.USER_MASTER_DETAIL);
    }
    
    public ActionForward saveUserMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	if ((request.getParameter(McAppConstants.PARAM_NOT_A_NUMBER) == null)
		&& !StringUtils.isEmpty(request.getParameter(McAppConstants.PARAM_USER_ATTEMPT_UID))) {
	    IMcService mcService = McServiceProxy.getMcService(getServlet().getServletContext());
	    
	    Long userAttemptUid = WebUtil.readLongParam(request, McAppConstants.PARAM_USER_ATTEMPT_UID);
	    Integer newGrade = Integer.valueOf(request.getParameter(McAppConstants.PARAM_GRADE));
	    mcService.changeUserAttemptMark(userAttemptUid, newGrade);
	}

	return null;
    }
    
    // *************************************************************************************
    // Private methods
    // *************************************************************************************

    /**
     */
    protected void repopulateRequestParameters(HttpServletRequest request, McMonitoringForm mcMonitoringForm,
	    McGeneralMonitoringDTO mcGeneralMonitoringDTO) {

	String toolContentID = request.getParameter(TOOL_CONTENT_ID);
	mcMonitoringForm.setToolContentID(toolContentID);
	mcGeneralMonitoringDTO.setToolContentID(toolContentID);

	String responseId = request.getParameter(RESPONSE_ID);
	mcMonitoringForm.setResponseId(responseId);
	mcGeneralMonitoringDTO.setResponseId(responseId);
    }
    
    /**
     * Return ResourceService bean.
     */
    private MessageService getMessageService() {
	return (MessageService) McServiceProxy.getMessageService(getServlet().getServletContext());
    }
}
