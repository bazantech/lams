/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/
package org.lamsfoundation.lams.tool.qa.web;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.qa.QaAppConstants;
import org.lamsfoundation.lams.tool.qa.QaApplicationException;
import org.lamsfoundation.lams.tool.qa.QaComparator;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * 
 * @author Ozgur Demirtas
 *
 * <lams base path>/<tool's learner url>&userId=<learners user id>&toolSessionId=123&mode=teacher
 * 
 * Since the toolSessionId is passed, we will derive toolContentId from the toolSessionId
 *
 * This class is used to load the default content and initialize the presentation Map for Learner mode 
 * 
 * createToolSession will not be called once the tool is deployed.
 * 
 * It is important that ALL the session attributes created in this action gets removed by: QaUtils.cleanupSession(request) 
 * 
 */

/**
 * Tool Session:
 *
 * A tool session is the concept by which which the tool and the LAMS core manage a set of learners interacting with the tool. 
 * The tool session id (toolSessionId) is generated by the LAMS core and given to the tool.
 * A tool session represents the use of a tool for a particulate activity for a group of learners. 
 * So if an activity is ungrouped, then one tool session exist for for a tool activity in a learning design.
 *
 * More details on the tool session id are covered under monitoring.
 * When thinking about the tool content id and the tool session id, it might be helpful to think about the tool content id 
 * relating to the definition of an activity, whereas the tool session id relates to the runtime participation in the activity.
 * 
 */

/**
 * 
 * Learner URL:
 * The learner url display the screen(s) that the learner uses to participate in the activity. 
 * When the learner accessed this user, it will have a tool access mode ToolAccessMode.LEARNER.
 *
 * It is the responsibility of the tool to record the progress of the user. 
 * If the tool is a multistage tool, for example asking a series of questions, the tool must keep track of what the learner has already done. 
 * If the user logs out and comes back to the tool later, then the tool should resume from where the learner stopped.
 * When the user is completed with tool, then the tool notifies the progress engine by calling 
 * org.lamsfoundation.lams.learning.service.completeToolSession(Long toolSessionId, User learner).
 *
 * If the tool's content DefineLater flag is set to true, then the learner should see a "Please wait for the teacher to define this part...." 
 * style message.
 * If the tool's content RunOffline flag is set to true, then the learner should see a "This activity is not being done on the computer. 
 * Please see your instructor for details."
 *
 * ?? Would it be better to define a run offline message in the tool? We have instructions for the teacher but not the learner. ??
 * If the tool has a LockOnFinish flag, then the tool should lock learner's entries once they have completed the activity. 
 * If they return to the activity (e.g. via the progress bar) then the entries should be read only.
 *
 */

/**
 * 
 * verifies that the content id passed to the tool is numeric and does refer to an existing content.
 */

public class QaLearningStarterAction extends Action implements QaAppConstants {
	static Logger logger = Logger.getLogger(QaLearningStarterAction.class.getName());

	/* holds the question contents for a given tool session and relevant content */
	protected Map mapQuestions= new TreeMap(new QaComparator());
	
	/*holds the answers */  
	protected Map mapAnswers= new TreeMap(new QaComparator());
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, QaApplicationException {

		QaLearningForm qaQaLearningForm = (QaLearningForm) form;
		
		request.getSession().setAttribute(CURRENT_QUESTION_INDEX, "1");
		request.getSession().setAttribute(CURRENT_ANSWER, "");

		/*initialize available question display modes in the session */
		request.getSession().setAttribute(QUESTION_LISTING_MODE_SEQUENTIAL,QUESTION_LISTING_MODE_SEQUENTIAL);
	    request.getSession().setAttribute(QUESTION_LISTING_MODE_COMBINED, QUESTION_LISTING_MODE_COMBINED);
	    
		IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
	    logger.debug("retrieving qaService: " + qaService);
	    
	    /*mark the http session as a learning activity  */
	    request.getSession().setAttribute(TARGET_MODE,TARGET_MODE_LEARNING);
	    
	    QaUtils.persistTimeZone(request);
	    
	    /*validate learning mode parameters*/
	    ActionForward validateParameters=validateParameters(request, mapping);
	    logger.debug("validateParamaters: " + validateParameters);
	    if (validateParameters != null)
	    {
	    	return validateParameters;
	    }
	    
	    String userId=(String)request.getSession().getAttribute(USER_ID);
		logger.debug("userId: " + userId);
	    
	    /*
	     * use the incoming tool session id and later derive toolContentId from it. 
	     */
		Long toolSessionId=(Long)request.getSession().getAttribute(TOOL_SESSION_ID);
		logger.debug("toolSessionId: " + toolSessionId);
		
	    /* API test code , from  here*/
	    String createToolSession=request.getParameter("createToolSession");
		logger.debug("createToolSession: " + createToolSession);
		if ((createToolSession != null) && createToolSession.equals("1"))
		{	try
			{
				logger.debug("creating test session with toolSessionId:" + toolSessionId);
				qaService.createToolSession(toolSessionId, "toolSessionName", new Long(9876));
				return (mapping.findForward(LEARNING_STARTER));
			}
			catch(ToolException e)
			{
				//McUtils.cleanUpSessionAbsolute(request);
				logger.debug("tool exception: "  + e);
			}
		}

		String removeToolSession=request.getParameter("removeToolSession");
		logger.debug("removeToolSession: " + removeToolSession);
		if ((removeToolSession != null) && removeToolSession.equals("1"))
		{	try
			{
				qaService.removeToolSession(toolSessionId);
				return (mapping.findForward(LEARNING_STARTER));
			}
			catch(ToolException e)
			{
				//McUtils.cleanUpSessionAbsolute(request);
				logger.debug("tool exception"  + e);
			}
		}

		String learnerId=request.getParameter("learnerId");
		logger.debug("learnerId: " + learnerId);
		if (learnerId != null) 
		{	try
			{
				String nextUrl=qaService.leaveToolSession(toolSessionId, new Long(learnerId));
				logger.debug("nextUrl: "+ nextUrl);
				return (mapping.findForward(LEARNING_STARTER));
			}
			catch(ToolException e)
			{
				//McUtils.cleanUpSessionAbsolute(request);
				logger.debug("tool exception"  + e);
			}
		}
		/* API test code , till here*/
		

	    /*
	     * By now, the passed tool session id MUST exist in the db by calling:
	     * public void createToolSession(Long toolSessionId, Long toolContentId) by the core.
	     *  
	     * make sure this session exists in tool's session table by now.
	     */
		
	    if (!QaUtils.existsSession(toolSessionId.longValue(), qaService)) 
		{
		    	logger.debug("error: The tool expects mcSession.");
		    	persistError(request,"error.toolSession.notAvailable");
				return (mapping.findForward(ERROR_LIST_LEARNER));
		}
	    
		
		/*
		 * by now, we made sure that the passed tool session id exists in the db as a new record
		 * Make sure we can retrieve it and relavent content
		 */
		
	    
		QaSession qaSession=qaService.retrieveQaSessionOrNullById(toolSessionId.longValue());
	    logger.debug("retrieving qaSession: " + qaSession);
	    /*
	     * find out what content this tool session is referring to
	     * get the content for this tool session (many to one mapping)
	     */
	    
	    /*
	     * Each passed tool session id points to a particular content. Many to one mapping.
	     */
		QaContent qaContent=qaSession.getQaContent();
	    logger.debug("using qaContent: " + qaContent);
	    if (qaContent == null)
	    {
	    	logger.debug("error: The tool expects qaContent.");
	    	persistError(request,"error.toolContent.notAvailable");
	    	//McUtils.cleanUpSessionAbsolute(request);
			return (mapping.findForward(ERROR_LIST_LEARNER));
	    }

	    request.getSession().setAttribute(AttributeNames.PARAM_TOOL_CONTENT_ID, qaContent.getQaContentId());
	    logger.debug("using TOOL_CONTENT_ID: " + qaContent.getQaContentId());
	    	    
	    /*
	     * The content we retrieved above must have been created before in Authoring time. 
	     * And the passed tool session id already refers to it.
	     */
	    
	    logger.debug("ACTIVITY_TITLE: " + qaContent.getTitle());
	    request.getSession().setAttribute(ACTIVITY_TITLE,qaContent.getTitle());
	    
	    logger.debug("ACTIVITY_INSTRUCTIONS: " + qaContent.getInstructions());
	    request.getSession().setAttribute(ACTIVITY_INSTRUCTIONS,qaContent.getInstructions());
	    
		logger.debug("REPORT_TITLE_LEARNER: " + qaContent.getReportTitle());
	    request.getSession().setAttribute(REPORT_TITLE_LEARNER,qaContent.getReportTitle());
	    
	    request.getSession().setAttribute(END_LEARNING_MESSAGE,qaContent.getEndLearningMessage());
	    logger.debug("END_LEARNING_MESSAGE: " + qaContent.getEndLearningMessage());
	    /*
	     * Is the tool activity been checked as Run Offline in the property inspector?
	     */
	    logger.debug("IS_TOOL_ACTIVITY_OFFLINE: " + qaContent.isRunOffline());
	    request.getSession().setAttribute(IS_TOOL_ACTIVITY_OFFLINE, new Boolean(qaContent.isRunOffline()).toString());
	    
	    logger.debug("IS_USERNAME_VISIBLE: " + qaContent.isUsernameVisible());
	    request.getSession().setAttribute(IS_USERNAME_VISIBLE, new Boolean(qaContent.isUsernameVisible()));
	    /*
	     * Is the tool activity been checked as Define Later in the property inspector?
	     */
	    logger.debug("IS_DEFINE_LATER: " + qaContent.isDefineLater());
	    request.getSession().setAttribute(IS_DEFINE_LATER, new Boolean(qaContent.isDefineLater()));
	    
	    /*
	     * Learning mode requires this setting for jsp to generate the user's report 
	     */
	    request.getSession().setAttribute(CHECK_ALL_SESSIONS_COMPLETED, new Boolean(false));
	    	    
	    logger.debug("IS_QUESTIONS_SEQUENCED: " + qaContent.isQuestionsSequenced());
	    String feedBackType="";
    	if (qaContent.isQuestionsSequenced())
    	{
    		request.getSession().setAttribute(QUESTION_LISTING_MODE, QUESTION_LISTING_MODE_SEQUENTIAL);
    		feedBackType=FEEDBACK_TYPE_SEQUENTIAL;
    	}
	    else
	    {
	    	request.getSession().setAttribute(QUESTION_LISTING_MODE, QUESTION_LISTING_MODE_COMBINED);
    		feedBackType=FEEDBACK_TYPE_COMBINED;
	    }
	    logger.debug("QUESTION_LISTING_MODE: " + request.getSession().getAttribute(QUESTION_LISTING_MODE));
	    
    	/*
    	 * fetch question content from content
    	 */
    	Iterator contentIterator=qaContent.getQaQueContents().iterator();
    	while (contentIterator.hasNext())
    	{
    		QaQueContent qaQueContent=(QaQueContent)contentIterator.next();
    		if (qaQueContent != null)
    		{
    			int displayOrder=qaQueContent.getDisplayOrder();
        		if (displayOrder != 0)
        		{
        			/*
    	    		 *  add the question to the questions Map in the displayOrder
    	    		 */
            		mapQuestions.put(new Integer(displayOrder).toString(),qaQueContent.getQuestion());
        		}
    		}
    	}
		
    	request.getSession().setAttribute(MAP_ANSWERS, mapAnswers);
    	request.getSession().setAttribute(MAP_QUESTION_CONTENT_LEARNER, mapQuestions);
    	logger.debug("mapQuestions has : " + mapQuestions.size() + " entries.");
    	
    	request.getSession().setAttribute(TOTAL_QUESTION_COUNT, new Long(mapQuestions.size()).toString());
    	String userFeedback= feedBackType + request.getSession().getAttribute(TOTAL_QUESTION_COUNT) + QUESTIONS;
    	request.getSession().setAttribute(USER_FEEDBACK, userFeedback);
    	
    	
    	
    	
    	/* Is the request for a preview by the author?
    	Preview The tool must be able to show the specified content as if it was running in a lesson. 
		It will be the learner url with tool access mode set to ToolAccessMode.AUTHOR 
		3 modes are:
			author
			teacher
			learner
		*/
    	/* ? CHECK THIS: how do we determine whether preview is requested? Mode is not enough on its own.*/
	    
	    /*handle PREVIEW mode*/
	    String mode=(String) request.getSession().getAttribute(LEARNING_MODE);
	    logger.debug("mode: " + mode);
    	if ((mode != null) && (mode.equals("author")))
    	{
    		/*complete this section */
    		logger.debug("Author requests for a preview of the content.");
			logger.debug("existing qaContent:" + qaContent);
    		
			return (mapping.findForward(LEARNING_STARTER)); 
    	}
    	
    	/* by now, we know that the mode is either teacher or learner
    	 * check if the mode is teacher and request is for Learner Progress
    	 */
		logger.debug("userId: " + userId);
		if ((userId != null) && (mode.equals("teacher")))
		{
    		/*complete this section */
			logger.debug("request is for learner progress");
			return (mapping.findForward(LEARNING_STARTER));
		}
    	
		/* by now, we know that the mode is learner*/
	    /* find out if the content is set to run offline or online. If it is set to run offline , the learners are informed about that. */
	    boolean isRunOffline=QaUtils.isRunOffline(qaContent);
	    logger.debug("isRunOffline: " + isRunOffline);
	    if (isRunOffline == true)
	    {
	    	logger.debug("warning to learner: the activity is offline.");
	    	persistError(request,"label.learning.runOffline");
	    	//McUtils.cleanUpSessionAbsolute(request);
			return (mapping.findForward(ERROR_LIST_LEARNER));
	    }
    	
    	
    	
    	
    	/*
	     * Verify that userId does not already exist in the db.
	     * If it does exist, that means, that user already responded to the content and 
	     * his answers must be displayed  read-only
	     * 
	     * NEW: if the user's tool session id AND user id exists in the tool tables go to learner's report.
	     */
	    QaQueUsr qaQueUsr=qaService.loadQaQueUsr(new Long(userId));
	    logger.debug("QaQueUsr:" + qaQueUsr);
	    if (qaQueUsr != null)
	    {
	    	String localToolSession=qaQueUsr.getQaSessionId().toString(); 
	    	logger.debug("localToolSession: " + localToolSession);
	    	
	    	Long incomingToolSessionId=(Long)request.getSession().getAttribute(AttributeNames.PARAM_TOOL_SESSION_ID);
	    	logger.debug("incomingToolSessionId: " + incomingToolSessionId);
	    	
	    	/* now we know that this user has already responsed before*/
	    	if (localToolSession.equals(incomingToolSessionId.toString()))
	    	{
		    	logger.debug("the learner has already responsed to this content, just generate a read-only report.");
		    	
		    	return (mapping.findForward(LEARNER_REPORT));	    		
	    	}
	    }
    	/*
    	 * present user with the questions.
    	 */
		logger.debug("forwarding to: " + LOAD_LEARNER);
		return (mapping.findForward(LOAD_LEARNER));	
	}
	

	/**
	 * validates the learning mode parameters
	 * @param request
	 * @param mapping
	 * @return ActionForward
	 */
	protected ActionForward validateParameters(HttpServletRequest request, ActionMapping mapping)
	{
		/*
	     * obtain and setup the current user's data 
	     */
		
	    String userID = "";
	    /* get session from shared session.*/
	    HttpSession ss = SessionManager.getSession();
	    /* get back login user DTO*/
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if ((user == null) || (user.getUserID() == null))
	    {
	    	logger.debug("error: The tool expects userId");
	    	persistError(request,"error.learningUser.notAvailable");
	    	//McUtils.cleanUpSessionAbsolute(request);
			return (mapping.findForward(ERROR_LIST_LEARNER));
	    }else
	    	userID = user.getUserID().toString();
	    
	    logger.debug("retrieved userId: " + userID);
    	request.getSession().setAttribute(USER_ID, userID);
		
	    
	    /*
	     * process incoming tool session id and later derive toolContentId from it. 
	     */
    	String strToolSessionId=request.getParameter(AttributeNames.PARAM_TOOL_SESSION_ID);
	    long toolSessionId=0;
	    if ((strToolSessionId == null) || (strToolSessionId.length() == 0)) 
	    {
	    	persistError(request, "error.toolSessionId.required");
	    	//McUtils.cleanUpSessionAbsolute(request);
			return (mapping.findForward(ERROR_LIST_LEARNER));
	    }
	    else
	    {
	    	try
			{
	    		toolSessionId=new Long(strToolSessionId).longValue();
		    	logger.debug("passed TOOL_SESSION_ID : " + new Long(toolSessionId));
		    	request.getSession().setAttribute(TOOL_SESSION_ID,new Long(toolSessionId));	
			}
	    	catch(NumberFormatException e)
			{
	    		persistError(request, "error.sessionId.numberFormatException");
	    		logger.debug("add error.sessionId.numberFormatException to ActionMessages.");
	    		//McUtils.cleanUpSessionAbsolute(request);
				return (mapping.findForward(ERROR_LIST_LEARNER));
			}
	    }
	    
	    /*mode can be learner, teacher or author */
	    String mode=request.getParameter(MODE);
	    logger.debug("mode: " + mode);
	    
	    if ((mode == null) || (mode.length() == 0)) 
	    {
	    	persistError(request, "error.mode.required");
	    	//McUtils.cleanUpSessionAbsolute(request);
			return (mapping.findForward(ERROR_LIST_LEARNER));
	    }
	    
	    if ((!mode.equals("learner")) && (!mode.equals("teacher")) && (!mode.equals("author")))
	    {
	    	persistError(request, "error.mode.invalid");
	    	//McUtils.cleanUpSessionAbsolute(request);
			return (mapping.findForward(ERROR_LIST_LEARNER));
	    }
		logger.debug("session LEARNING_MODE set to:" + mode);
	    request.getSession().setAttribute(LEARNING_MODE, mode);
	    
	    return null;
	}
	
	
	/**
     * persists error messages to request scope
     * @param request
     * @param message
     */
	public void persistError(HttpServletRequest request, String message)
	{
		ActionMessages errors= new ActionMessages();
		errors.add(Globals.ERROR_KEY, new ActionMessage(message));
		logger.debug("add " + message +"  to ActionMessages:");
		saveErrors(request,errors);	    	    
	}
}  
