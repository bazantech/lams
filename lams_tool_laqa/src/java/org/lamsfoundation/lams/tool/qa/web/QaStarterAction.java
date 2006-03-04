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

/**
 * @author Ozgur Demirtas
 * 
 * Created on 8/03/2005
 * 
 * initializes the tool's authoring mode  
 */

/**
 * Tool path The URL path for the tool should be <lamsroot>/tool/$TOOL_SIG.
 * 
 * CONTENT_LOCKED refers to content being in use or not: Any students answered that content?
 * For future CONTENT_LOCKED ->CONTENT_IN_USE 
 * 
 * QaStarterAction loads the default content and initializes the presentation Map
 * Requests can come either from authoring envuironment or from the monitoring environment for Edit Activity screen
 * 
 * Check QaUtils.createAuthoringUser again User Management Service is ready
 * 
 * */

/**
 *
 * Tool Content:
 *
 * While tool's manage their own content, the LAMS core and the tools work together to create and use the content. 
 * The tool content id (toolContentId) is the key by which the tool and the LAMS core discuss data - 
 * it is generated by the LAMS core and supplied to the tool whenever content needs to be stored. 
 * The LAMS core will refer to the tool content id whenever the content needs to be used. 
 * Tool content will be covered in more detail in following sections.
 *
 * Each tool will have one piece of content that is the default content. 
 * The tool content id for this content is created as part of the installation process. 
 * Whenever a tool is asked for some tool content that does not exist, it should supply the default tool content. 
 * This will allow the system to render the normal screen, albeit with useless information, rather than crashing. 
*/

/**
*
* Authoring URL: 
*
* The tool must supply an authoring module, which will be called to create new content or edit existing content. It will be called by an authoring URL using the following format: ?????
* The initial data displayed on the authoring screen for a new tool content id may be the default tool content.
*
* Authoring UI data consists of general Activity data fields and the Tool specific data fields.
* The authoring interface will have three tabs. The mandatory (and suggested) fields are given. Each tool will have its own fields which it will add on any of the three tabs, as appropriate to the tabs' function.
*
* Basic: Displays the basic set of fields that are needed for the tool, and it could be expected that a new LAMS user would use. Mandatory fields: Title, Instructions.
* Advanced: Displays the extra fields that would be used by experienced LAMS users. Optional fields: Lock On Finish, Make Responses Anonymous
* Instructions: Displays the "instructions" fields for teachers. Mandatory fields: Online instructions, Offline instructions, Document upload.
* The "Define Later" and "Run Offline" options are set on the Flash authoring part, and not on the tool's authoring screens.
*
* Preview The tool must be able to show the specified content as if it was running in a lesson. It will be the learner url with tool access mode set to ToolAccessMode.AUTHOR.
* Export The tool must be able to export its tool content for part of the overall learning design export.
*
* The format of the serialization for export is XML. Tool will define extra namespace inside the <Content> element to add a new data element (type). Inside the data element, it can further define more structures and types as it seems fit.
* The data elements must be "version" aware. The data elements must be "type" aware if they are to be shared between Tools.
*
*/


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
import org.lamsfoundation.lams.tool.qa.QaUtils;
import org.lamsfoundation.lams.tool.qa.service.IQaService;
import org.lamsfoundation.lams.tool.qa.service.QaServiceProxy;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * 
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * 
 * A Map  data structure is used to present the UI.
   It is fetched by subsequent Action classes to manipulate its content and gets parsed in the presentation layer for display.
   
   NOTE: You have to keep in mind that once user can have multiple tool session ids.
 */
public class QaStarterAction extends Action implements QaAppConstants {
	static Logger logger = Logger.getLogger(QaStarterAction.class.getName());
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  								throws IOException, ServletException, QaApplicationException {
	
		Map mapQuestionContent= new TreeMap(new QaComparator());
		
		QaAuthoringForm qaAuthoringForm = (QaAuthoringForm) form;
		qaAuthoringForm.resetRadioBoxes();
		
		request.getSession().setAttribute(IS_DEFINE_LATER,"false");
		
		IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("retrieving qaService from session: " + qaService);

	
		/* needs to be called only once.  */ 
		/* QaUtils.configureContentRepository(request); */
		
	    /*
	     * obtain and setup the current user's data 
	     * get session from shared session. */
	    HttpSession ss = SessionManager.getSession();
	    /* get back login user DTO */
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if ((user == null) || (user.getUserID() == null))
	    {
	    	logger.debug("error: The tool expects userId");
	    	persistError(request,"error.authoringUser.notAvailable");
	    	request.setAttribute(USER_EXCEPTION_USERID_NOTAVAILABLE, new Boolean(true));
	    	return (mapping.findForward(LOAD_QUESTIONS));
	    }

	    ActionForward validateSignature=readSignature(request,mapping);
		logger.debug("validateSignature:  " + validateSignature);
		if (validateSignature != null)
		{
			logger.debug("validateSignature not null : " + validateSignature);
			return validateSignature;
		}

		/*
	     * mark the http session as an authoring activity 
	     */
	    request.getSession().setAttribute(TARGET_MODE,TARGET_MODE_AUTHORING);
	    
	    /*
	     * define tab controllers for jsp
	     */
	    request.getSession().setAttribute(EDITACTIVITY_EDITMODE, new Boolean(false));
		
	    /*
	     * find out whether the request is coming from monitoring module for EditActivity tab or from authoring environment url
	     */
	    String strToolContentId="";
        Long contentID =new Long(WebUtil.readLongParam(request,AttributeNames.PARAM_TOOL_CONTENT_ID));
        
	    /* API test code for copying the content*/
    	String copyToolContent= (String) request.getParameter(COPY_TOOL_CONTENT);
    	logger.debug("copyToolContent: " + copyToolContent);
    	
    	if ((copyToolContent != null) && (copyToolContent.equals("1")))
		{
	    	logger.debug("user request to copy the content");
	    	Long fromContentId=contentID;
	    	logger.debug("fromContentId: " + fromContentId);
	    	
	    	Long toContentId=new Long(9876);
	    	logger.debug("toContentId: " + toContentId);
	    	
	    	try
			{
	    		qaService.copyToolContent(fromContentId, toContentId);	
			}
	    	catch(ToolException e)
			{
	    		logger.debug("error copying the content: " + e);
			}
		}
        
        qaAuthoringForm.setToolContentId(contentID.toString());

		/*
		 * find out if the passed tool content id exists in the db 
		 * present user either a first timer screen with default content data or fetch the existing content.
		 * 
		 * if the toolcontentid does not exist in the db, create the default Map,
		 * there is no need to check if the content is locked in this case.
		 * It is always unlocked since it is the default content.
		*/
		if (!existsContent(contentID.longValue(), qaService)) 
		{
			String defaultContentIdStr=(String) request.getSession().getAttribute(DEFAULT_CONTENT_ID_STR);
			logger.debug("defaultContentIdStr:" + defaultContentIdStr);
            return retrieveContent(request, mapping, qaAuthoringForm, mapQuestionContent, new Long(defaultContentIdStr).longValue());
		}
        else
        {
            return retrieveContent(request, mapping, qaAuthoringForm, mapQuestionContent, contentID.longValue());
        }
	} 
	
	
	
	/**
	 * retrives the existing content information from the db and prepares the data for presentation purposes.
	 * ActionForward retrieveExistingContent(HttpServletRequest request, ActionMapping mapping, QaAuthoringForm qaAuthoringForm, Map mapQuestionContent, long toolContentId)
	 *  
	 * @param request
	 * @param mapping
	 * @param qaAuthoringForm
	 * @param mapQuestionContent
	 * @param toolContentId
	 * @return ActionForward
	 */
	protected ActionForward retrieveContent(HttpServletRequest request, ActionMapping mapping, QaAuthoringForm qaAuthoringForm, Map mapQuestionContent, long toolContentId)
	{
		logger.debug("starting retrieveExistingContent for toolContentId: " + toolContentId);
		IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		
		logger.debug("getting existing content with id:" + toolContentId);
	    QaContent qaContent = qaService.retrieveQa(toolContentId);
		logger.debug("QaContent: " + qaContent);
		
		boolean studentActivity=qaService.studentActivityOccurredGlobal(qaContent);
		logger.debug("studentActivity on content: " + studentActivity);
		if (studentActivity)
		{
			logger.debug("forward to warning screen as the content is not allowed to be modified.");
			ActionMessages errors= new ActionMessages();
			errors.add(Globals.ERROR_KEY, new ActionMessage("error.content.inUse"));
			saveErrors(request,errors);
			logger.debug("forwarding to:" + LOAD);
			return (mapping.findForward(LOAD));
		}
		
		QaUtils.setDefaultSessionAttributes(request, qaContent, qaAuthoringForm);
        QaUtils.populateUploadedFilesData(request, qaContent, qaService);
	    request.getSession().setAttribute(IS_DEFINE_LATER, 					new Boolean(qaContent.isDefineLater()));
	    
	    
	    /*
		 * get the existing question content
		 */
		logger.debug("setting existing content data from the db");
		mapQuestionContent.clear();
		Iterator queIterator=qaContent.getQaQueContents().iterator();
		Long mapIndex=new Long(1);
		logger.debug("mapQuestionContent: " + mapQuestionContent);
		while (queIterator.hasNext())
		{
			QaQueContent qaQueContent=(QaQueContent) queIterator.next();
			if (qaQueContent != null)
			{
				logger.debug("question: " + qaQueContent.getQuestion());
	    		mapQuestionContent.put(mapIndex.toString(),qaQueContent.getQuestion());
	    		/**
	    		 * make the first entry the default(first) one for jsp
	    		 */
	    		if (mapIndex.longValue() == 1)
	    			request.getSession().setAttribute(DEFAULT_QUESTION_CONTENT, qaQueContent.getQuestion());
	    		mapIndex=new Long(mapIndex.longValue()+1);
			}
		}
		logger.debug("Map initialized with existing contentid to: " + mapQuestionContent);
		
		logger.debug("callling presentInitialUserInterface for the existing content.");
		return presentInitialUserInterface(request, mapping, qaAuthoringForm, mapQuestionContent);
	}

	
	/**
	 * each tool has a signature. QA tool's signature is stored in MY_SIGNATURE. The default tool content id and 
	 * other depending content ids are obtained in this method.
	 * if all the default content has been setup properly the method persists DEFAULT_CONTENT_ID in the session.
	 * 
	 * readSignature(HttpServletRequest request, ActionMapping mapping)
	 * @param request
	 * @param mapping
	 * @return ActionForward
	 */
	public ActionForward readSignature(HttpServletRequest request, ActionMapping mapping)
	{
		IQaService qaService = QaServiceProxy.getQaService(getServlet().getServletContext());
		logger.debug("retrieving qaService from session: " + qaService);
		/*
		 * retrieve the default content id based on tool signature
		 */
		long defaultContentID=0;
		try
		{
			logger.debug("attempt retrieving tool with signatute : " + MY_SIGNATURE);
            defaultContentID=qaService.getToolDefaultContentIdBySignature(MY_SIGNATURE);
			logger.debug("retrieved tool default contentId: " + defaultContentID);
			if (defaultContentID == 0)
			{
				logger.debug("default content id has not been setup");
				persistError(request,"error.defaultContent.notSetup");
		    	request.setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true));
				return (mapping.findForward(LOAD_QUESTIONS));	//TODO: forward to error page
			}
		}
		catch(Exception e)
		{
			logger.debug("error getting the default content id: " + e.getMessage());
			persistError(request,"error.defaultContent.notSetup");
	    	request.setAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP, new Boolean(true));
			return (mapping.findForward(LOAD_QUESTIONS)); //TODO: forward to error page
		}

		
		/* retrieve uid of the content based on default content id determined above */
		long contentUID=0;
		try
		{
			logger.debug("retrieve uid of the content based on default content id determined above: " + defaultContentID);
			QaContent qaContent=qaService.loadQa(defaultContentID);
			if (qaContent == null)
			{
				logger.debug("Exception occured: No default content");
	    		persistError(request,"error.defaultContent.notSetup");
	    		return (mapping.findForward(LOAD_QUESTIONS));
	    		//McUtils.cleanUpSessionAbsolute(request);
				//return (mapping.findForward(ERROR_LIST));
			}
			logger.debug("using qaContent: " + qaContent);
			logger.debug("using mcContent uid: " + qaContent.getUid());
			contentUID=qaContent.getUid().longValue();
			logger.debug("contentUID: " + contentUID);
		}
		catch(Exception e)
		{
			logger.debug("Exception occured: No default question content");
			persistError(request,"error.defaultContent.notSetup");
			return (mapping.findForward(LOAD_QUESTIONS));
			//McUtils.cleanUpSessionAbsolute(request);
			//return (mapping.findForward(ERROR_LIST));
		}

		
		/* retrieve uid of the default question content  */
		long queContentUID=0;
		try
		{
			logger.debug("retrieve the default question content based on default content UID: " + queContentUID);
			QaQueContent qaQueContent=qaService.getToolDefaultQuestionContent(contentUID);
			logger.debug("using mcQueContent: " + qaQueContent);
			if (qaQueContent == null)
			{
				logger.debug("Exception occured: No default question content");
	    		persistError(request,"error.defaultQuestionContent.notAvailable");
	    		return (mapping.findForward(LOAD_QUESTIONS));
	    		//McUtils.cleanUpSessionAbsolute(request);
				//return (mapping.findForward(ERROR_LIST));
			}
			logger.debug("using qaQueContent uid: " + qaQueContent.getUid());
			//request.getSession().setAttribute(DEFAULT_QUESTION_UID, new Long(queContentUID));
			request.getSession().setAttribute(DEFAULT_QUESTION_CONTENT, qaQueContent.getQuestion());
		}
		catch(Exception e)
		{
			logger.debug("Exception occured: No default question content");
    		persistError(request,"error.defaultQuestionContent.notAvailable");
    		return (mapping.findForward(LOAD_QUESTIONS));
    		//McUtils.cleanUpSessionAbsolute(request);
			//return (mapping.findForward(ERROR_LIST));
		}
		
		logger.debug("QA tool has the default content id: " + defaultContentID);
		request.getSession().setAttribute(DEFAULT_CONTENT_ID_STR, new Long(defaultContentID).toString());
		return null;
	}
	
	
	/**
	 * presents the final Map to the jsp
	 * ActionForward presentInitialUserInterface(HttpServletRequest request, ActionMapping mapping, QaAuthoringForm qaAuthoringForm, Map mapQuestionContent)
	 * 
	 * @param request
	 * @param mapping
	 * @param qaAuthoringForm
	 * @param mapQuestionContent
	 * @return
	 */
	protected ActionForward presentInitialUserInterface(HttpServletRequest request, ActionMapping mapping, QaAuthoringForm qaAuthoringForm, Map mapQuestionContent)
	{
		logger.debug("starting presentInitialUserInterface...");
		request.getSession().setAttribute(MAP_QUESTION_CONTENT, mapQuestionContent);
		logger.debug("starter initialized the Comparable Map: " + request.getSession().getAttribute("mapQuestionContent") );

		/*
		 * load questions page
		 */
		logger.debug("RENDER_MONITORING_EDITACTIVITY: " + request.getAttribute(RENDER_MONITORING_EDITACTIVITY));
		qaAuthoringForm.resetUserAction();
		return (mapping.findForward(LOAD_QUESTIONS));
	}
	
	
	/**
	 * existsContent(long toolContentId)
	 * @param long toolContentId
	 * @return boolean
	 * determine whether a specific toolContentId exists in the db
	 */
	protected boolean existsContent(long toolContentId, IQaService qaService)
	{
		QaContent qaContent=qaService.loadQa(toolContentId);
	    if (qaContent == null) 
	    	return false;
	    
		return true;	
	}
	
	
	/**
	 * mark the request scope to generate monitoring summary screen
	 * 
	 * ActionForward startMonitoringSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
															throws IOException, ServletException, QaApplicationException 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws QaApplicationException
	 */
	public ActionForward startMonitoringSummary(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
															throws IOException, ServletException, QaApplicationException 
	{
		return execute(mapping, form, request, response);
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

