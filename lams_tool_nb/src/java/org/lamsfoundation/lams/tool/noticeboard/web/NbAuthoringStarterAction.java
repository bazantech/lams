/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */

/*
 * Created on May 19, 2005
 * @author mtruong
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.noticeboard.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardConstants;

import org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService;
import org.lamsfoundation.lams.tool.noticeboard.service.NoticeboardServiceProxy;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.util.NbAuthoringUtil;

import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;



/**
 * Creation Date: 19-05-05
 *  
 * ----------------XDoclet Tags--------------------
 * 
 * @struts:action path="/tool/nb/starter/authoring" name="NbAuthoringForm" scope="session" type="org.lamsfoundation.lams.tool.noticeboard.web.NbAuthoringStarterAction"
 *                input="/index.jsp" validate="false" 
 * @struts:action-forward name="basic" path=".nb_basic"
 * ----------------XDoclet Tags--------------------
 */

public class NbAuthoringStarterAction extends Action {
	
	static Logger logger = Logger.getLogger(NbAuthoringAction.class.getName());
	
	/**
	 * This struts action class gets called when the author double clicks
	 * on the tool icon. If the toolContentId already exists in the tool content 
	 * table, the content is then extracted from the content table and is displayed. 
	 * Otherwise, if the toolContentId does not exist in the content table, a new 
	 * record is created with this toolContentId and default content is used.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward, the action path to take once completed.
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws NbApplicationException {
		
		NbAuthoringForm nbForm = (NbAuthoringForm)form;
		
		Long contentId = NbAuthoringUtil.convertToLong(request.getParameter(NoticeboardConstants.TOOL_CONTENT_ID));
		
		if(contentId == null)
		{
			String error = "Tool content id missing. Unable to continue.";
			throw new NbApplicationException(error);
		}
		
		NbAuthoringUtil.cleanSession(request);
		
		request.getSession().setAttribute(NoticeboardConstants.TOOL_CONTENT_ID, contentId);
							
		/*
		 * Retrieve the Service
		 */
		INoticeboardService nbService = NoticeboardServiceProxy.getNbService(getServlet().getServletContext());
		
		if (!contentExists(nbService, contentId))
		{
			//	Pre-fill the form with the default content
			NoticeboardContent nb =	nbService.retrieveNoticeboard(NoticeboardConstants.DEFAULT_CONTENT_ID);
			
			//create a new noticeboard object and prefill with default content, save to database
			NoticeboardContent nbContentNew = new NoticeboardContent();
			
			nbContentNew.setNbContentId(contentId);
			nbContentNew.setDateCreated(new Date(System.currentTimeMillis()));		
			nbContentNew.setTitle(nb.getTitle());
			nbContentNew.setContent(nb.getContent());
			nbContentNew.setOnlineInstructions(nb.getOnlineInstructions());
			nbContentNew.setOfflineInstructions(nb.getOfflineInstructions());
			
			//save new tool content into db
			nbService.saveNoticeboard(nbContentNew);
			
			//initialise the values in the form, so the values will be shown in the jsp
			nbForm.populateFormWithNbContentValues(nbContentNew);
				
			setValueForRichTextContent(request, nbForm);
			setValueForRichTextOnlineInstrn(request, nbForm);
			setValueForRichTextOfflineInstrn(request, nbForm);
		}
		else
		{
			//get the values from the database
			NoticeboardContent nb = nbService.retrieveNoticeboard(contentId);
			
			nbForm.populateFormWithNbContentValues(nb);
			
			setValueForRichTextContent(request, nbForm);
			setValueForRichTextOnlineInstrn(request, nbForm);
			setValueForRichTextOfflineInstrn(request, nbForm);
			
		}
		
		return mapping.findForward(NoticeboardConstants.BASIC_PAGE);
	}
	
	/**
	 * Checks the session to see if the title and content session variables exist or not.
	 * 
	 * @param session The HttpSession to check.
	 * @return true if the parameters title and content exists in the session, false otherwise
	 */
	private boolean contentExists(INoticeboardService service, Long id)
	{
		NoticeboardContent nb = service.retrieveNoticeboard(id);
		if (nb == null)
			return false;
		else
			return true;
		
	}
	/**
	 * If the <code>content</code> attribute of the noticeboard object is NULL
	 * then the session attribute <code>richTextContent</code> is set to an empty string.
	 * Otherwise, <code>richTextContent</code> takes on the the value of <code>content</code>
	 * and is stored in the session 
	 * @param request
	 * @param form
	 */
	private void setValueForRichTextContent(HttpServletRequest request, NbAuthoringForm form)
	{
	    if (form.getContent() != null)
	    {
	        request.getSession().setAttribute(NoticeboardConstants.RICH_TEXT_CONTENT, form.getContent());
		}
		else
		{
		    request.getSession().setAttribute(NoticeboardConstants.RICH_TEXT_CONTENT, "");
		}
	}
	
	private void setValueForRichTextOnlineInstrn(HttpServletRequest request, NbAuthoringForm form)
	{
	    if (form.getOnlineInstructions() != null)
	    {
	        request.getSession().setAttribute(NoticeboardConstants.RICH_TEXT_ONLINE_INSTRN, form.getOnlineInstructions());
		}
		else
		{
		    request.getSession().setAttribute(NoticeboardConstants.RICH_TEXT_ONLINE_INSTRN, "");
		}
	}
	
	private void setValueForRichTextOfflineInstrn(HttpServletRequest request, NbAuthoringForm form)
	{
	    if (form.getOfflineInstructions() != null)
	    {
	        request.getSession().setAttribute(NoticeboardConstants.RICH_TEXT_OFFLINE_INSTRN, form.getOfflineInstructions());
		}
		else
		{
		    request.getSession().setAttribute(NoticeboardConstants.RICH_TEXT_OFFLINE_INSTRN, "");
		}
	}
	
}