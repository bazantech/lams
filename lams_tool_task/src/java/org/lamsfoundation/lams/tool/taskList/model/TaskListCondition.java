/**************************************************************** 
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org) 
 * ============================================================= 
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/ 
 * 
 * This program is free software; you can redistribute it and/or modify 
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation. 
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA 
 * 
 * http://www.gnu.org/licenses/gpl.txt 
 * **************************************************************** 
 */  
 
/* $Id$ */  
package org.lamsfoundation.lams.tool.taskList.model;  

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;
import org.lamsfoundation.lams.tool.taskList.util.TaskListToolContentHandler;
 
/**
 * The main entity class of TaskList tool. Contains all the data related to the whole tool.
 * 
 * @author Andrey Balan
 *
 * @hibernate.class  table="tl_latask10_condition"
 */
public class TaskListCondition implements Cloneable{
	
	private static final Logger log = Logger.getLogger(TaskListCondition.class);
	
	//key 
	private Long uid;

//	private TaskList taskList;
	//unique name
	private String name;
	private int sequenceId;
	
	//taskList Items
	private Set taskListItems;
	
	/**
	 * Default contruction method. 
	 */
  	public TaskListCondition(){
  		taskListItems = new HashSet();
  	}
  	
  	//  **********************************************************
  	//		Function method for TaskList
  	//  **********************************************************
  	
	/**
	 * {@Override} 
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + sequenceId;
		result = prime * result	+ ((taskListItems == null) ? 0 : taskListItems.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	/**
	 * {@Override} 
	 */
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final TaskListCondition other = (TaskListCondition) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (sequenceId != other.sequenceId)
			return false;
		if (taskListItems == null) {
			if (other.taskListItems != null)
				return false;
		} else if (!taskListItems.equals(other.taskListItems))
			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}
  	
	/**
	 * {@Override} 
	 */
  	public Object clone(){
  		
  		TaskListCondition taskList = null;
  		try{
  			taskList = (TaskListCondition) super.clone();
  			taskList.setUid(null);
  			//clone taskListItems
  			if(taskListItems != null){
  				Iterator iter = taskListItems.iterator();
  				Set set = new HashSet();
  				while(iter.hasNext()){
  					TaskListItem item = (TaskListItem)iter.next(); 
  					TaskListItem newItem = (TaskListItem) item.clone();
  					//just clone old file without duplicate it in repository
					set.add(newItem);
  				}
  				taskList.taskListItems = set;
  			}
		} catch (CloneNotSupportedException e) {
			log.error("When clone " + TaskListCondition.class + " failed");
		}
  		
  		return taskList;
  	}
  	
	//**********************************************************
	// Get/set methods
	//**********************************************************

	/**
	 * Returns <code>TaskListCondition</code> id.
	 * 
	 * @return tasklistCondition id
	 * 
	 * @hibernate.id column="condition_uid" generator-class="native"
	 */
	public Long getUid() {
		return uid;
	}

	/**
	 * Sets <code>TaskList</code> id.
	 * 
	 * @param uid tasklistCondition id
	 */
	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * Returns condition's name.
	 * 
	 * @return condition's name.
	 *
	 * @hibernate.property
	 * 		column="name"
	 *
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the condition's name. Should be unique for the parent tasklist.
	 * 
	 * @param title condition's name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return set of TaskListItems
	 * 
	 * @return set of TaskListItems
	 * 
	 * @hibernate.set lazy="true"
	 * 				  inverse="false"
	 * 				  cascade="none"
     * 				  table = "tl_latask10_condition_tl_item"
     * @hibernate.collection-key column="condition_uid"
     * @hibernate.collection-many-to-many column="uid" class="org.lamsfoundation.lams.tool.taskList.model.TaskListItem"
	 */
	public Set getTaskListItems() {
		return taskListItems;
	}
	
	/**
	 * Sets set of TaskListItems.
	 * 
	 * @param taskListItems set of TaskListItems
	 */
	public void setTaskListItems(Set taskListItems) {
		this.taskListItems= taskListItems;
	}

//	
//	/**
//	 * Returns taskList to which this condition applies. 
//	 * 
//	 * @return taskList to which this condition applies
//	 * 
//     * @hibernate.many-to-one
//     *     	cascade="none"
//     * 		column="taskList_uid"
//	 */
//	public TaskList getTaskList() {
//		return taskList;
//	}
//	/**
//	 * Sets taskList to which this condition applies.
//	 * 
//	 * @param taskList taskList to which this condition applies
//	 */
//	public void setTaskList(TaskList taskList) {
//		this.taskList = taskList;
//	}
	
    /**
	 * Returns condition's sequence number. Order is very important for
	 * conditions as the conditions will be tested in the order shown on the
	 * screen.
	 * 
	 * @return condition's sequence number
	 * 
	 * @hibernate.property column="sequence_id"
	 */
	public int getSequenceId() {
		return sequenceId;
	}
    /**
     * Sets condition's sequence number. Order is very important for
	 * conditions as the conditions will be tested in the order shown on the
	 * screen.
     * 
     * @param sequenceId condition's sequence number
     */
	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

}

 