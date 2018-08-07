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

package org.lamsfoundation.lams.web.outcome;

import org.apache.struts.action.ActionForm;

public class OutcomeForm extends ActionForm {
    private static final long serialVersionUID = -3707697605837331410L;

    private Long outcomeId;
    private Integer organisationId;
    private Long scaleId;
    private String name;
    private String code;
    private String description;

    public Long getOutcomeId() {
	return outcomeId;
    }

    public void setOutcomeId(Long outcomeId) {
	this.outcomeId = outcomeId;
    }

    public Integer getOrganisationId() {
	return organisationId;
    }

    public void setOrganisationId(Integer organisationId) {
	this.organisationId = organisationId;
    }

    public Long getScaleId() {
	return scaleId;
    }

    public void setScaleId(Long scaleId) {
	this.scaleId = scaleId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getCode() {
	return code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }
}