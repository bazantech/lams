package org.lamsfoundation.lams.monitoring.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.learningdesign.Activity;
import org.lamsfoundation.lams.learningdesign.ContributionTypes;
import org.lamsfoundation.lams.learningdesign.Group;
import org.lamsfoundation.lams.learningdesign.Grouping;
import org.lamsfoundation.lams.learningdesign.GroupingActivity;
import org.lamsfoundation.lams.learningdesign.PermissionGateActivity;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IActivityDAO;
import org.lamsfoundation.lams.learningdesign.service.ILearningDesignService;
import org.lamsfoundation.lams.lesson.Lesson;
import org.lamsfoundation.lams.lesson.service.ILessonService;
import org.lamsfoundation.lams.monitoring.dto.ContributeActivityDTO;
import org.lamsfoundation.lams.monitoring.dto.PermissionGateDTO;
import org.lamsfoundation.lams.monitoring.dto.TblGroupDTO;
import org.lamsfoundation.lams.monitoring.dto.TblUserDTO;
import org.lamsfoundation.lams.monitoring.service.IMonitoringFullService;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.service.ICommonAssessmentService;
import org.lamsfoundation.lams.tool.service.ICommonScratchieService;
import org.lamsfoundation.lams.tool.service.ILamsCoreToolService;
import org.lamsfoundation.lams.tool.service.ILamsToolService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.NumberUtil;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

/**
 * Displays TBL monitor.
 *
 * @author Andrey Balan
 */
@Controller
@RequestMapping("/tblmonitor")
public class TblMonitoringController {
    private static Logger log = Logger.getLogger(TblMonitoringController.class);

    @Autowired
    private ILessonService lessonService;
    @Autowired
    private ILearningDesignService learningDesignService;
    @Autowired
    private IMonitoringFullService monitoringService;
    @Autowired
    private ILamsToolService lamsToolService;
    @Autowired
    private ILamsCoreToolService lamsCoreToolService;
    @Autowired
    private IActivityDAO activityDAO;
    @Autowired
    @Qualifier("laasseAssessmentService")
    private ICommonAssessmentService commonAssessmentService;
    @Autowired
    @Qualifier("mcService")
    private ICommonAssessmentService commonMcqService;
    @Autowired
    @Qualifier("scratchieService")
    private ICommonScratchieService commonScratchieService;

    /**
     * Displays addStudent page.
     */
    @RequestMapping("/start")
    public String unspecified(HttpServletRequest request) {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = lessonService.getLesson(lessonId);
	request.setAttribute("lesson", lesson);
	request.setAttribute("totalLearnersNumber", lesson.getAllLearners().size());

	Map<String, Object> activityTypesMeta = learningDesignService
		.getAvailableTBLActivityTypes(lesson.getLearningDesign().getLearningDesignId());
	for (Entry<String, Object> entry : activityTypesMeta.entrySet()) {
	    request.setAttribute(entry.getKey(), entry.getValue());
	}
	return "tblmonitor/tblmonitor";
    }

    /**
     * Shows Teams page
     */
    @RequestMapping("/teams")
    public String teams(HttpServletRequest request) {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = lessonService.getLesson(lessonId);

	Map<String, Object> activityTypesMeta = learningDesignService
		.getAvailableTBLActivityTypes(lesson.getLearningDesign().getLearningDesignId());
	for (Entry<String, Object> entry : activityTypesMeta.entrySet()) {
	    request.setAttribute(entry.getKey(), entry.getValue());
	}

	boolean isTraAvailable = (request.getAttribute("isScratchieAvailable") != null)
		&& ((Boolean) request.getAttribute("isScratchieAvailable"));
	boolean isIraAssesmentAvailable = request.getAttribute("isIraAssessmentAvailable") != null
		&& ((Boolean) request.getAttribute("isIraAssessmentAvailable"));
	boolean isIraMcqAvailable = request.getAttribute("isIraMcqAvailable") != null
		&& ((Boolean) request.getAttribute("isIraMcqAvailable"));
	Long iraToolActivityId = request.getAttribute("iraToolActivityId") == null ? null
		: (Long) request.getAttribute("iraToolActivityId");
	Long traToolActivityId = request.getAttribute("traToolActivityId") == null ? null
		: (Long) request.getAttribute("traToolActivityId");
	Long leaderselectionToolActivityId = request.getAttribute("leaderselectionToolActivityId") == null ? null
		: (Long) request.getAttribute("leaderselectionToolActivityId");
	Long iraToolContentId = isIraMcqAvailable || isIraAssesmentAvailable
		? activityDAO.find(ToolActivity.class, iraToolActivityId).getToolContentId()
		: null;
	Long traToolContentId = isTraAvailable
		? activityDAO.find(ToolActivity.class, traToolActivityId).getToolContentId()
		: null;

	Set<Long> leaderUserIds = null;
	if (leaderselectionToolActivityId != null) {
	    leaderUserIds = lamsToolService.getLeaderUserId(leaderselectionToolActivityId);
	    ToolActivity leaderSelection = activityDAO.find(ToolActivity.class, leaderselectionToolActivityId);
	    request.setAttribute("leaderSelectionToolContentId", leaderSelection.getToolContentId());
	} else {
	    leaderUserIds = new HashSet<>();
	}

	GroupingActivity groupingActivity = getGroupingActivity(lesson);

	String groupsSetupUrl = lamsCoreToolService.getToolContributionURL(lessonId, groupingActivity);
	request.setAttribute("groupsSetupUrl", groupsSetupUrl);

	Grouping grouping = groupingActivity == null ? null : groupingActivity.getCreateGrouping();
	Set<Group> groups = grouping == null ? null : grouping.getGroups();

	Map<Integer, Integer> iraCorrectAnswerCountByUser = Map.of();
	if (isIraMcqAvailable) {
	    iraCorrectAnswerCountByUser = commonMcqService.countCorrectAnswers(iraToolContentId);
	} else if (isIraAssesmentAvailable) {
	    iraCorrectAnswerCountByUser = commonAssessmentService.countCorrectAnswers(iraToolContentId);
	}

	Set<TblGroupDTO> groupDtos = new TreeSet<>();
	for (Group group : groups) {
	    TblGroupDTO groupDto = new TblGroupDTO(group);
	    groupDtos.add(groupDto);

	    if (group.getUsers() == null) {
		continue;
	    }

	    for (User user : group.getUsers()) {
		TblUserDTO userDto = new TblUserDTO(user.getUserDTO());
		groupDto.getUserList().add(userDto);

		//set up all user leaders
		if (leaderUserIds.contains(user.getUserId().longValue())) {
		    userDto.setGroupLeader(true);
		    groupDto.setGroupLeader(userDto);
		}

		Integer correctAnswerCount = iraCorrectAnswerCountByUser.get(userDto.getUserID());
		if (correctAnswerCount != null) {
		    userDto.setIraCorrectAnswerCount(correctAnswerCount);
		}
	    }

	    if (isTraAvailable && groupDto.getGroupLeader() != null) {
		Integer correctAnswerCount = commonScratchieService.countCorrectAnswers(traToolContentId,
			groupDto.getGroupLeader().getUserID());
		if (correctAnswerCount != null) {
		    groupDto.setTraCorrectAnswerCount(correctAnswerCount);

		    for (TblUserDTO userDto : groupDto.getUserList()) {
			userDto.setTraCorrectAnswerCount(correctAnswerCount);
		    }
		}
	    }
	}
	request.setAttribute("groupDtos", groupDtos);

	double highestIraCorrectAnswerCountAverage = 0;
	double lowestIraCorrectAnswerCountAverage = Double.MAX_VALUE;
	int highestTraCorrectAnswerCount = 0;
	int lowestTraCorrectAnswerCount = Integer.MAX_VALUE;
	long highestСorrectAnswerCountDelta = Long.MIN_VALUE;
	long lowestСorrectAnswerCountDelta = Long.MAX_VALUE;

	int iraGroupsCount = 0;
	int traGroupsCount = 0;
	int deltaCount = 0;
	double iraCorrectAnswerCountAverageSum = 0;
	int traCorrectAnswerSum = 0;
	int deltaSum = 0;

	ArrayNode chartIraDataset = JsonNodeFactory.instance.arrayNode();
	ArrayNode chartTraDataset = JsonNodeFactory.instance.arrayNode();
	ArrayNode chartNamesDataset = JsonNodeFactory.instance.arrayNode();

	for (TblGroupDTO group : groupDtos) {
	    Double iraCorrectAnswerCountAverage = group.getIraCorrectAnswerCountAverage();
	    if (iraCorrectAnswerCountAverage != null) {
		iraCorrectAnswerCountAverageSum += iraCorrectAnswerCountAverage;
		iraGroupsCount++;

		if (iraCorrectAnswerCountAverage > highestIraCorrectAnswerCountAverage) {
		    highestIraCorrectAnswerCountAverage = iraCorrectAnswerCountAverage;
		}
		if (iraCorrectAnswerCountAverage < lowestIraCorrectAnswerCountAverage) {
		    lowestIraCorrectAnswerCountAverage = iraCorrectAnswerCountAverage;
		}
	    }

	    Integer traCorrectAnswerCount = group.getTraCorrectAnswerCount();

	    if (traCorrectAnswerCount != null) {
		traCorrectAnswerSum += traCorrectAnswerCount;
		traGroupsCount++;

		if (traCorrectAnswerCount > highestTraCorrectAnswerCount) {
		    highestTraCorrectAnswerCount = traCorrectAnswerCount;
		}
		if (traCorrectAnswerCount < lowestTraCorrectAnswerCount) {
		    lowestTraCorrectAnswerCount = traCorrectAnswerCount;
		}

		if (iraCorrectAnswerCountAverage != null) {
		    chartIraDataset
			    .add(NumberUtil.formatLocalisedNumber(iraCorrectAnswerCountAverage, (Locale) null, 2));
		    chartTraDataset.add(traCorrectAnswerCount);
		    chartNamesDataset.add(group.getGroupName());

		    long correctAnswerCountPercentDelta = iraCorrectAnswerCountAverage.equals(0d)
			    ? traCorrectAnswerCount * 100
			    : Math.round((traCorrectAnswerCount - iraCorrectAnswerCountAverage) * 100
				    / iraCorrectAnswerCountAverage);
		    group.setCorrectAnswerCountPercentDelta(correctAnswerCountPercentDelta);
		    deltaSum += correctAnswerCountPercentDelta;
		    deltaCount++;

		    if (correctAnswerCountPercentDelta > highestСorrectAnswerCountDelta) {
			highestСorrectAnswerCountDelta = correctAnswerCountPercentDelta;
		    }
		    if (correctAnswerCountPercentDelta < lowestСorrectAnswerCountDelta) {
			lowestСorrectAnswerCountDelta = correctAnswerCountPercentDelta;
		    }
		}
	    }

	}

	if (iraGroupsCount > 1) {
	    request.setAttribute("highestIraCorrectAnswerCountAverage", highestIraCorrectAnswerCountAverage);
	    request.setAttribute("lowestIraCorrectAnswerCountAverage", lowestIraCorrectAnswerCountAverage);
	    request.setAttribute("averageIraCorrectAnswerCountAverage",
		    iraCorrectAnswerCountAverageSum / iraGroupsCount);
	}
	if (traGroupsCount > 1) {
	    request.setAttribute("highestTraCorrectAnswerCount", highestTraCorrectAnswerCount);
	    request.setAttribute("lowestTraCorrectAnswerCount", lowestTraCorrectAnswerCount);
	    request.setAttribute("averageTraCorrectAnswerCount", (double) traCorrectAnswerSum / traGroupsCount);
	}

	if (deltaCount > 1) {
	    request.setAttribute("highestCorrectAnswerCountDelta", highestСorrectAnswerCountDelta);
	    request.setAttribute("lowestCorrectAnswerCountDelta", lowestСorrectAnswerCountDelta);
	    request.setAttribute("averageCorrectAnswerCountDelta", (double) deltaSum / deltaCount);
	}

	if (iraGroupsCount > 0 && traGroupsCount > 0) {
	    request.setAttribute("chartIraDataset", chartIraDataset.toString());
	    request.setAttribute("chartTraDataset", chartTraDataset.toString());
	    request.setAttribute("chartNamesDataset", chartNamesDataset.toString());
	}

	return "tblmonitor/teams";
    }

    /**
     * Shows Gates page
     */
    @RequestMapping("/gates")
    public String gates(HttpServletRequest request) {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);

	List<PermissionGateDTO> permissionGates = new ArrayList<>();

	List<ContributeActivityDTO> contributeActivities = monitoringService.getAllContributeActivityDTO(lessonId);
	if (contributeActivities != null) {
	    for (ContributeActivityDTO contributeActivity : contributeActivities) {

		if (contributeActivity.getContributeEntries() != null) {

		    //check if there is any persmission gates entries
		    for (ContributeActivityDTO.ContributeEntry contributeEntry : contributeActivity
			    .getContributeEntries()) {
			if (ContributionTypes.PERMISSION_GATE.equals(contributeEntry.getContributionType())) {

			    Long activityId = contributeActivity.getActivityID();
			    Activity activity = monitoringService.getActivityById(activityId);
			    PermissionGateDTO gateDto = new PermissionGateDTO((PermissionGateActivity) activity);

			    gateDto.setUrl(contributeEntry.getURL());
			    gateDto.setComplete(contributeEntry.getIsComplete());

			    int waitingLearnersCount = lessonService.getCountLearnersHaveAttemptedActivity(activity);
			    gateDto.setWaitingLearnersCount(waitingLearnersCount);

			    permissionGates.add(gateDto);
			    break;
			}
		    }
		}
	    }
	}

	request.setAttribute("permissionGates", permissionGates);
	return "tblmonitor/gates";
    }

    /**
     * Shows forum page
     */
    @RequestMapping("/forum")
    public String forum(HttpServletRequest request) {
	long forumActivityId = WebUtil.readLongParam(request, "activityId");
	ToolActivity forumActivity = (ToolActivity) monitoringService.getActivityById(forumActivityId);

	int attemptedLearnersNumber = lessonService.getCountLearnersHaveAttemptedOrCompletedActivity(forumActivity);
	request.setAttribute("attemptedLearnersNumber", attemptedLearnersNumber);

	Set<ToolSession> toolSessions = forumActivity.getToolSessions();
	request.setAttribute("toolSessions", toolSessions);

	return "tblmonitor/forum";
    }

    /**
     * Shows peerreview page
     */
    @RequestMapping("/peerreview")
    public String peerreview(HttpServletRequest request) {
	long peerreviewActivityId = WebUtil.readLongParam(request, "activityId");
	ToolActivity peerreviewActivity = (ToolActivity) monitoringService.getActivityById(peerreviewActivityId);

	int attemptedLearnersNumber = lessonService
		.getCountLearnersHaveAttemptedOrCompletedActivity(peerreviewActivity);
	request.setAttribute("attemptedLearnersNumber", attemptedLearnersNumber);

	Set<ToolSession> toolSessions = peerreviewActivity.getToolSessions();
	request.setAttribute("toolSessions", toolSessions);

	return "tblmonitor/peerreview";
    }

    /**
     * Shows sequence diagram page
     */
    @RequestMapping("/sequence")
    public String sequence(HttpServletRequest request) {
	long lessonId = WebUtil.readLongParam(request, AttributeNames.PARAM_LESSON_ID);
	Lesson lesson = lessonService.getLesson(lessonId);
	request.setAttribute("lesson", lesson);
	return "tblmonitor/sequence";
    }

    /**
     * Shows AEs page
     */
    @RequestMapping("/aes")
    public String aes(HttpServletRequest request, Model model) {
	String[] toolContentIds = request.getParameter("aeToolContentIds").split(",");
	String[] toolTypes = request.getParameter("aeToolTypes").split(",");
	String[] activityTitles = request.getParameter("aeActivityTitles").split("\\,");

	model.addAttribute("aeToolContentIds", toolContentIds);
	model.addAttribute("aeToolTypes", toolTypes);
	model.addAttribute("aeActivityTitles", activityTitles);

	return "tblmonitor/aes";
    }

    private GroupingActivity getGroupingActivity(Lesson lesson) {
	Set<Activity> activities = new TreeSet<>();

	/*
	 * Hibernate CGLIB is failing to load the first activity in the sequence as a ToolActivity for some mysterious
	 * reason Causes a ClassCastException when you try to cast it, even if it is a ToolActivity.
	 *
	 * THIS IS A HACK to retrieve the first tool activity manually so it can be cast as a ToolActivity - if it is
	 * one
	 */
	Activity firstActivity = monitoringService
		.getActivityById(lesson.getLearningDesign().getFirstActivity().getActivityId());
	activities.add(firstActivity);
	activities.addAll(lesson.getLearningDesign().getActivities());

	for (Activity activity : activities) {
	    if (activity instanceof GroupingActivity) {
		return (GroupingActivity) activity;
	    }
	}

	return null;
    }
}