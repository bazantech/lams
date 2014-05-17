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
package org.lamsfoundation.lams.tool.mc.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;

/**
 * Interface for the McQueContent DAO, defines methods needed to access/modify mc question content
 * 
 * @author Ozgur Demirtas
 */
public interface IMcQueContentDAO {
    
    /**
     * <p>
     * Return the persistent instance of a McQueContent with the given identifier <code>uid</code>, returns null if not
     * found.
     * </p>
     * 
     * @param uid
     * @return McQueContent
     */
    McQueContent getMcQueContentByUID(Long uid);

    /**
     * <p>
     * Return the persistent instance of a McQueContent with the given identifier <code>question</code> and
     * <code>mcContentUid</code>, returns null if not found.
     * </p>
     * 
     * @param question
     * @param mcContentUid
     * @return McQueContent
     */
    McQueContent getQuestionContentByQuestionText(final String question, final Long mcContentUid);

    /**
     * <p>
     * Return the persistent instance of a McQueContent with the given identifier <code>displayOrder</code> and
     * <code>mcContentUid</code>, returns null if not found.
     * </p>
     * 
     * @param displayOrder
     * @param mcContentUid
     * @return McQueContent
     */
    McQueContent getQuestionContentByDisplayOrder(final Long displayOrder, final Long mcContentUid);

    /**
     * <p>
     * Return a list of McQueContent with the given identifier <code>question</code> and <code>mcContentUid</code>,
     * returns null if not found.
     * </p>
     * 
     * @param mcContentUid
     * @return List
     */
    List<McQueContent> getQuestionsByContentUid(final long mcContentId);

    /**
     * <p>
     * Return a list of McQueContent with the given identifier <code>question</code> and <code>mcContentUid</code>,
     * returns null if not found.
     * </p>
     * 
     * @param mcContentUid
     * @return List
     */
    List refreshQuestionContent(final Long mcContentId);

    /**
     * <p>
     * resets McQueContent with the given identifier <code>mcContentUid</code>
     * </p>
     * 
     * @param mcContentUid
     */
    void resetAllQuestions(final Long mcContentUid);

    /**
     * <p>
     * removes McQueContent with the given identifier <code>mcContentUid</code>
     * </p>
     * 
     * @param mcContentUid
     */
    void removeQuestionContentByMcUid(final Long mcContentUid);

    /**
     * <p>
     * saves McQueContent with the given identifier <code>mcQueContent</code>
     * </p>
     * 
     * @param mcQueContent
     */
    void saveMcQueContent(McQueContent mcQueContent);

    /**
     * <p>
     * updates McQueContent with the given identifier <code>mcQueContent</code>
     * </p>
     * 
     * @param mcQueContent
     */
    void updateMcQueContent(McQueContent mcQueContent);

    /**
     * <p>
     * saves McQueContent with the given identifier <code>mcQueContent</code>
     * </p>
     * 
     * @param mcQueContent
     */
    void saveOrUpdateMcQueContent(McQueContent mcQueContent);

    /**
     * <p>
     * removes McQueContent with the given identifier <code>uid</code>
     * </p>
     * 
     * @param uid
     */
    void removeMcQueContentByUID(Long uid);

    /**
     * <p>
     * removes McQueContent with the given identifier <code>mcQueContent</code>
     * </p>
     * 
     * @param mcQueContent
     * @return
     */
    void removeMcQueContent(McQueContent mcQueContent);

    /**
     * <p>
     * used to get the next available display order with the given identifier <code>mcContentId</code>
     * </p>
     * 
     * @param mcQueContent
     * @return
     */
    List getNextAvailableDisplayOrder(final long mcContentId);

    McQueContent findMcQuestionContentByUid(Long uid);

    List getAllQuestionEntriesSorted(final long qaContentId);
    
    void releaseQuestionFromCache(McQueContent question);

}
