package org.lamsfoundation.lams.events;

import java.security.InvalidParameterException;
import java.util.Set;

/**
 * Provides tools for managing events and notifing users.
 * @author Marcin Cieslak
 *
 */
public interface IEventNotificationService {

	/**
	 * Scope for the events that are common for the whole LAMS environment.
	 */
	public static final String CORE_EVENTS_SCOPE = "CORE";

	/**
	 * User should be notified only once. Used when subscribing a user to an event.
	 */
	public static final long PERIODICITY_SINGLE = 0;

	/**
	 * User should be notified daily. Used when subscribing a user to an event.
	 */
	public static final long PERIODICITY_DAILY = 24 * 60 * 60;

	/**
	 * User should be notified weekly. Used when subscribing a user to an event.
	 */
	public static final long PERIODICITY_WEEKLY = IEventNotificationService.PERIODICITY_DAILY * 7;

	/**
	 * User should be notified monthly. Used when subscribing a user to an event.
	 */
	public static final long PERIODICITY_MONTHLY = IEventNotificationService.PERIODICITY_WEEKLY * 4;

	/**
	 * Allows sending mail to users using the configured SMTP server.
	 * Currently it is the only delivery method available.
	 */
	public static final AbstractDeliveryMethod DELIVERY_METHOD_MAIL = DeliveryMethodMail.getInstance();

	/**
	 * Creates an event and saves it into the database.
	 * @param scope scope of the event
	 * @param name name of the event
	 * @param eventSessionId session ID of the event
	 * @param defaultSubject subject of the message send to users; it can be altered when triggering the event
	 * @param defaultMessage body of the message send to users; it can be altered when triggering the event
	 * @return <code>true</code> if the event did not exist and was correctly created
	 * @throws InvalidParameterException if scope was <code>null</code> or name was blank
	 */
	public abstract boolean createEvent(String scope, String name, Long eventSessionId, String defaultSubject,
			String defaultMessage) throws InvalidParameterException;

	/**
	 * Deletes an event.
	 * @param scope scope of the event
	 * @param name name of the event
	 * @param eventSessionId session ID of the event
	 * @return <code>true</code> if the event existed and was deleted
	 * @throws InvalidParameterException if scope was <code>null</code> or name was blank
	 */
	public abstract boolean deleteEvent(String scope, String name, Long eventSessionId) throws InvalidParameterException;;

	/**
	 * Checks if event with the given parameters exists in the database.
	 * @param scope scope of the event
	 * @param name name of the event
	 * @param eventSessionId session ID of the event
	 * @return <code>true</code> if the event exists
	 * @throws InvalidParameterException if scope was <code>null</code> or name was blank
	 */
	public abstract boolean eventExists(String scope, String name, Long eventSessionId) throws InvalidParameterException;

	/**
	 * Gets the available delivery methods that can be used when subscribing an user to an event.
	 * @return set of available delivery methods in the system
	 */
	public abstract Set<AbstractDeliveryMethod> getAvailableDeliveryMethods();

	/**
	 * Checks if an user is subscribed to the given event.
	 * @param scope scope of the event
	 * @param name name of the event
	 * @param eventSessionId session ID of the event
	 * @param userId ID of the user
	 * @return <code>true</code> if the event exists and the user is subscribed (with at least one delivery method) to the event
	 * @throws InvalidParameterException if scope or user ID were <code>null</code>, name was blank or event does not exist
	 */
	public abstract boolean isSubscribed(String scope, String name, Long eventSessionId, Long userId)
			throws InvalidParameterException;

	/**
	 * Sends a single message to the given users.If it fails, an event is created for the needs of the resending mechanism.
	 * @param userId ID of users to send the message to
	 * @param deliveryMethod method of messaged delivery to use
	 * @param subject subject of the message to send
	 * @param message body of the message to send
	 * @return <code>true</code> if the message was succefully send to the user
	 * @throws InvalidParameterException if userId or delivery method are <code>null</code>
	 */
	public abstract boolean sendMessage(Long userId, AbstractDeliveryMethod deliveryMethod, String subject, String message)
			throws InvalidParameterException;

	/**
	 * 
	 * Sends a single message to the given user. If it fails, an event is created for the needs of the resending mechanism.
	 * @param userId IDs of users to send the message to
	 * @param deliveryMethod method of messaged delivery to use
	 * @param subject subject of the message to send
	 * @param message body of the message to send
	 * @return <code>true</code> if the message was succefully send to all the users; as in the current implementation a separate thread is used for sending messages, this method always returns <code>true</code> 
	 * @throws InvalidParameterException if userId array or delivery method are <code>null</code>
	 */
	public abstract boolean sendMessage(Long[] userId, AbstractDeliveryMethod deliveryMethod, String subject, String message)
			throws InvalidParameterException;

	/**
	 * Registeres an user for notification of the event.
	 * If a subscription with given user ID and delivery method already exists,
	 * only periodicity is updated.
	 * @param scope scope of the event
	 * @param name name of the event
	 * @param eventSessionId session ID of the event
	 * @param userId ID of the user
	 * @param deliveryMethod method of messaged delivery to use
	 * @param periodicity how often the user should be notified (in seconds)
	 * @throws InvalidParameterException if scope, userId or delivery method are <code>null</code>, or name is blank
	 */
	public abstract boolean subscribe(String scope, String name, Long eventSessionId, Long userId,
			AbstractDeliveryMethod deliveryMethod, Long periodicity) throws InvalidParameterException;

	/**
	 * Triggers the event with the default (or previously set) subject and message. Each subscribed user is notified.
	 * @param scope scope of the event
	 * @param name name of the event
	 * @param eventSessionId session ID of the event
	 * @throws InvalidParameterException if scope is <code>null</code> or name is blank
	 */
	public abstract boolean trigger(String scope, String name, Long eventSessionId) throws InvalidParameterException;

	/**
	 * Triggers the event with the default subject and message, modifying placeholders (<code>{0}, {1}, {2}</code>...) in the message body with the <code>parameterValues</code>. Each subscribed user is notified.
	 * @param scope scope of the event
	 * @param name name of the event
	 * @param eventSessionId session ID of the event
	 * @param parameterValues values that should replace placeholders in the message body; for each object its text representation is acquired by <code>toString()</code> method; then, the first string replaces <code>{0}</code> tag, second one the <code>{1}</code> tag and so on.
	 * @throws InvalidParameterException if scope is <code>null</code> or name is blank
	 */
	public abstract boolean trigger(String scope, String name, Long eventSessionId, Object[] parameterValues)
			throws InvalidParameterException;

	/**
	 * Triggers the event with given subject and message. Each subscribed user is notified. Default message and subject are overridden.
	 * @param scope scope of the event
	 * @param name name of the event
	 * @param eventSessionId session ID of the event
	 * @param subject subject of the message to send
	 * @param message body of the message to send
	 * @throws InvalidParameterException if scope is <code>null</code> or name is blank
	 */
	public abstract boolean trigger(String scope, String name, Long eventSessionId, String subject, String message)
			throws InvalidParameterException;

	/**
	 * Notifies only a single user of the event using the default subject and message. Does not set the event as "triggered".
	 * @param scope scope of the event
	 * @param name name of the event
	 * @param eventSessionId session ID of the event
	 * @param userId ID of the user
	 * @throws InvalidParameterException if scope or userId are <code>null</code> or name is blank
	 */
	public abstract boolean triggerForSingleUser(String scope, String name, Long eventSessionId, Long userId)
			throws InvalidParameterException;

	/**
	 * Notifies only a single user of the event using the default subject and message, modifying placeholders (<code>{0}, {1}, {2}</code>...) in the message body with the <code>parameterValues</code>. Does not set the event as "triggered".
	 * @param scope scope of the event
	 * @param name name of the event
	 * @param eventSessionId session ID of the event
	 * @param userId ID of the user
	 * @param parameterValues values that should replace placeholders in the message body; for each object its text representation is acquired by <code>toString()</code> method; then, the first string replaces <code>{0}</code> tag, second one the <code>{1}</code> tag and so on.
	 * @throws InvalidParameterException if scope or userId are <code>null</code> or name is blank
	 */
	public boolean triggerForSingleUser(String scope, String name, Long eventSessionId, Long userId, Object[] parameterValues)
			throws InvalidParameterException;

	/**
	 * Notifies only a single user of the event using the given subject and message. Does not set the event as "triggered". Default subject and message are NOT overridden.
	 * @param scope scope of the event
	 * @param name name of the event
	 * @param eventSessionId session ID of the event
	 * @param userId ID of the user
	 * @param subject subject of the message to send
	 * @param message body of the message to send
	 * @throws InvalidParameterException if scope or userId are <code>null</code> or name is blank
	 */
	public abstract boolean triggerForSingleUser(String scope, String name, Long eventSessionId, Long userId, String subject,
			String message) throws InvalidParameterException;

	/**
	 * Unregister an user from notification of the event.
	 * @param scope scope of the event
	 * @param name name of the event
	 * @param eventSessionId session ID of the event
	 * @param userId ID of the user
	 * @throws InvalidParameterException if scope or userId are <code>null</code> or name is blank
	 */
	public abstract boolean unsubscribe(String scope, String name, Long eventSessionId, Long userId)
			throws InvalidParameterException;

	/**
	 * Unregister delivery method of the user from notification of the event.
	 * @param scope scope of the event
	 * @param name name of the event
	 * @param eventSessionId session ID of the event
	 * @param userId ID of the user
	 * @param deliveryMethod delivery method which should be unregistered
	 * @throws InvalidParameterException if scope, userId or delivery method are <code>null</code> or name is blank
	 */
	public abstract boolean unsubscribe(String scope, String name, Long eventSessionId, Long userId,
			AbstractDeliveryMethod deliveryMethod) throws InvalidParameterException;
}