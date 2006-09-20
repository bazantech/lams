/****************************************************************
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
 * ****************************************************************
 */
/* $$Id$$ */	
package org.lamsfoundation.lams.tool.qa;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

/**
 * 
 * @author Ozgur Demirtas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * QaQueContent Value Object
 * The value object that maps to our model database table: tl_laqa11_que_content
 * The relevant hibernate mapping resides in: QaQueContent.hbm.xml
 *
 * Holds question content within a particular content  
 */
public class QaQueContent implements Serializable,Comparable, Nullable
{
	static Logger logger = Logger.getLogger(QaQueContent.class.getName());
	
	/** identifier field */
    private Long uid;

    /** nullable persistent field */
    private String question;

    /** nullable persistent field */
    private int displayOrder;
    
    private String feedback;
    
    /** nullable persistent field */
    private org.lamsfoundation.lams.tool.qa.QaContent qaContent;
    
    /** persistent field */
    private Set qaUsrResps;	
    
    /** persistent field */
    private Set qaQueUsers; 
    
    /** Struts form convenience field */
    private String[] userResponses = {};

    /** Struts form convenience field */
    private String otherResponse;

    /** nullable persistent field */
    private boolean isOptional;

    /** nullable persistent field */
    private Long qaContentId;
    
    /** default constructor */
    public QaQueContent()
    {
    	logger.debug(logger + " " + this.getClass().getName() +  "in constructor: QaQueContent()");	
    }
    
    
    public QaQueContent(String question, 
    					int displayOrder, 
						org.lamsfoundation.lams.tool.qa.QaContent qaContent,
						Set qaQueUsers,
						Set qaUsrResps)  
    {
        this.question = question;
        this.displayOrder = displayOrder;
        this.qaContent = qaContent;
        this.qaQueUsers=qaQueUsers;
        this.qaUsrResps=qaUsrResps;
    }
    
    public QaQueContent(String question, 
			int displayOrder, 
			String feedback,
			org.lamsfoundation.lams.tool.qa.QaContent qaContent,
			Set qaQueUsers,
			Set qaUsrResps)  
	{
		this.question = question;
		this.displayOrder = displayOrder;
		this.feedback = feedback;
		this.qaContent = qaContent;
		this.qaQueUsers=qaQueUsers;
		this.qaUsrResps=qaUsrResps;
	}

    
    
    public QaQueContent(String question, 
			int displayOrder, 
			Set qaQueUsers,
			Set qaUsrResps)  
    {
		this.question = question;
		this.displayOrder = displayOrder;
		this.qaQueUsers=qaQueUsers;
		this.qaUsrResps=qaUsrResps;
	}
    
    
    /** minimal constructor */
    public QaQueContent(Set qaQueUsers,
    					Set qaUsrResps) 
    {
        this.qaQueUsers = qaQueUsers;
        this.qaUsrResps = qaUsrResps;
    }
    
    
    /*
    public static QaQueContent newInstance(QaQueContent queContent,
    										QaContent newQaContent,
    										QaQueContent parentQuestion)
    {
    	QaQueContent newQueContent = new QaQueContent(queContent.getQuestion(),
													  queContent.getDisplayOrder(),
													  newQaContent,
                                                      new TreeSet(),
                                                      new TreeSet());
    	logger.debug(logger + " " + "QaQueContent" +  " " + "returning newQueContent: " + newQueContent);
    	return newQueContent;
    }
    
    */
    public static QaQueContent newInstance(QaQueContent queContent,
			QaContent newQaContent,
			QaQueContent parentQuestion)
    {
		QaQueContent newQueContent = new QaQueContent(queContent.getQuestion(),
							  queContent.getDisplayOrder(),
							  queContent.getFeedback(),
							  newQaContent,
		                      new TreeSet(),
		                      new TreeSet());
		logger.debug(logger + " " + "QaQueContent" +  " " + "returning newQueContent: " + newQueContent);
		return newQueContent;
	}

    
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("qaQueContentId: ", getUid())
			.append("question: ", getQuestion())
			.append("displayOrder: ", getDisplayOrder())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof QaQueContent) ) return false;
        QaQueContent castOther = (QaQueContent) other;
        return new EqualsBuilder()
            .append(this.getUid(), castOther.getUid())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getUid())
            .toHashCode();
    }
  
    
 	/**
	 * @return Returns the displayOrder.
	 */
	public int getDisplayOrder() {
		return displayOrder;
	}
	/**
	 * @param displayOrder The displayOrder to set.
	 */
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	/**
	 * @return Returns the qaContent.
	 */
	public org.lamsfoundation.lams.tool.qa.QaContent getQaContent() {
		return qaContent;
	}
	/**
	 * @param qaContent The qaContent to set.
	 */
	public void setQaContent(org.lamsfoundation.lams.tool.qa.QaContent qaContent) {
		this.qaContent = qaContent;
	}
	
	/**
	 * @return Returns the qaQueUsers.
	 */
	public Set getQaQueUsers() {
        if (this.qaQueUsers == null)
            setQaQueUsers(new TreeSet());
		return qaQueUsers;
	}
	/**
	 * @param qaQueUsers The qaQueUsers to set.
	 */
	public void setQaQueUsers(Set qaQueUsers) {
		this.qaQueUsers = qaQueUsers;
	}
	/**
	 * @return Returns the qaUsrResps.
	 */
	public Set getQaUsrResps() {
		if (this.qaUsrResps == null)
			setQaUsrResps(new TreeSet());
		return qaUsrResps;
	}
	/**
	 * @param qaUsrResps The qaUsrResps to set.
	 */
	public void setQaUsrResps(Set qaUsrResps) {
		this.qaUsrResps = qaUsrResps;
	}
	/**
	 * @return Returns the question.
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question The question to set.
	 */
	public void setQuestion(String question) {
		this.question = question;
	}
	
	
    public String[] getUserResponses()
    {
        return userResponses;
    }

    /**
     * @param userResponse The userResponse to set.
     */
    public void setUserResponses(String[] userResponse)
    {
        this.userResponses = userResponse;
    }

    /**
     * @return Returns the otherResponse.
     */
    public String getOtherResponse()
    {
        return otherResponse == null ? null : otherResponse.trim();
    }

    /**
     * @param otherResponse The otherResponse to set.
     */
    public void setOtherResponse(String otherResponse)
    {
        this.otherResponse = otherResponse;
    }

    
    /** 
     * @hibernate.property column="isOptional" length="1"
     *         
     */
    public boolean getIsOptional()
    {
        return this.isOptional;
    }

    public void setIsOptional(boolean isOptional)
    {
        this.isOptional = isOptional;
    }

    
    /**
     * Validate whether there is a response available for current question.
     * This method only validate struts convient field at the moment.
     * @return whether the resonse is available or not.
     */
    private boolean isResponseAvailable()
    {
        if (this.getUserResponses().length == 0
                && this.getOtherResponse() == null)
            return false;
        return this.getUserResponses().length != 0
                || !this.getOtherResponse().equals("");

    }
    
    public void setUpOtherResponse(String username)
    {
        for (Iterator i = this.getQaQueUsers().iterator(); i.hasNext();)
        {
        	QaQueUsr qUser = (QaQueUsr) i.next();

            if ((qUser.getUsername().trim()).equals(username))
                this.setOtherResponse(qUser.getOtherResponse());
        }
    }
    
    
    /**
     * Convenient method to check out the response for other field.
     * @return true if other response field is not null and is not equals to
     * 			empty String
     */
    public boolean isOtherResponseAvailable()
    {
        if (this.otherResponse != null && !this.otherResponse.equals(""))
            return true;
        return false;
    }
    
    public boolean isNull()
    {
        return false;
    }
    
    public int compareTo(Object o)
    {
        QaQueContent queContent = (QaQueContent) o;
        
        //if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
        /*
        if (uid == null)
        	return 1;
		else
			return (int) (uid.longValue() - queContent.uid.longValue());
			*/
       return 1;
    }

    
	/**
	 * @return Returns the qaContentId.
	 */
	public Long getQaContentId() {
		return qaContentId;
	}
	/**
	 * @param qaContentId The qaContentId to set.
	 */
	public void setQaContentId(Long qaContentId) {
		this.qaContentId = qaContentId;
	}
	/**
	 * @return Returns the uid.
	 */
	public Long getUid() {
		return uid;
	}
	/**
	 * @param uid The uid to set.
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}
    
    /**
     * @param isOptional The isOptional to set.
     */
    public void setOptional(boolean isOptional) {
        this.isOptional = isOptional;
    }
    /**
     * @return Returns the feedback.
     */
    public String getFeedback() {
        return feedback;
    }
    /**
     * @param feedback The feedback to set.
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
