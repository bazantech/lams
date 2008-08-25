package org.lamsfoundation.lams.events.dao.hibernate;

import java.security.InvalidParameterException;
import java.util.List;

import org.lamsfoundation.lams.events.Event;
import org.lamsfoundation.lams.events.dao.EventDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

class EventDAOHibernate extends HibernateDaoSupport implements EventDAO {

	protected static final String GET_EVENT_QUERY = "FROM " + Event.class.getName()
			+ " AS e WHERE e.scope=? AND e.name=? AND e.eventSessionId=? AND e.failTime IS NULL";

	protected static final String GET_EVENTS_TO_RESEND_QUERY = "SELECT DISTINCT e FROM " + Event.class.getName()
			+ " AS e LEFT JOIN FETCH e.subscriptions WHERE e.failTime IS NOT NULL OR "
			+ "(e.subscriptions.periodicity > 0 AND (NOW()- e.subscriptions.lastOperationTime >= e.subscriptions.periodicity))";

	public Event getEvent(String scope, String name, Long sessionId) throws InvalidParameterException {
		List<Event> events = getHibernateTemplate().find(EventDAOHibernate.GET_EVENT_QUERY,
				new Object[] { scope, name, sessionId });
		if (events.size() > 1) {
			throw new InvalidParameterException("Two events with the same parameters exist in the database.");
		}
		if (events.size() == 0) {
			return null;
		}
		return events.get(0);
	}

	public List<Event> getEventsToResend() {
		return getHibernateTemplate().find(EventDAOHibernate.GET_EVENTS_TO_RESEND_QUERY);
	}

	public void deleteEvent(Event event) {
		getHibernateTemplate().delete(event);
	}

	public void saveEvent(Event event) {
		getHibernateTemplate().saveOrUpdate(event);
	}
}