<%/****************************************************************
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
			 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
			 * USA
			 * 
			 * http://www.gnu.org/licenses/gpl.txt
			 * ****************************************************************
			 */

			/**
			 * Standard Head Items
			 *	Author: Fiona Malikoff
			 *	Description: Includes all the standard head items e.g. the 
			 * lams css files, sets the content type, standard javascript files.
			 */

		%>
<%@ tag body-content="empty"%>

<%@ taglib uri="tags-core" prefix="c"%>
<%@ taglib uri="tags-lams" prefix="lams"%>
<%@ taglib uri="tags-fmt" prefix="fmt"%>

<c:set var="lams">
	<lams:LAMSURL />
</c:set>
<c:set var="tool">
	<lams:WebAppURL />
</c:set>

<meta http-equiv="content-type" content="text/html; charset=UTF-8">

<lams:css />
<!--[if IE]>
<style type="text/css">
@import url(${tool}includes/css/ie-styles.css);
</style>
<![endif]-->

<link href="${tool}includes/css/fckeditor_style.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="${lams}includes/javascript/common.js"></script>
<script type="text/javascript" src="${lams}fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="${lams}includes/javascript/fckcontroller.js"></script>
<script type="text/javascript" src="${tool}includes/javascript/tabcontroller.js"></script>
<script type="text/javascript" src="${tool}includes/javascript/xmlrequest.js"></script>

