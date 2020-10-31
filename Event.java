package events;

/**
 * A representation of an Event in my Event system.
 *
 */
public class Event {
	// The type of the event
	private final EventType type;
	
	// Arguments to pass with the event 
	private final EventArgs args;
	
	/**
	 * The constructor.
	 * @param t The type of the event
	 * @param a The event arguments
	 */
	public Event(EventType t, EventArgs a) {
		type = t;
		args = a;
	}
	
	/**
	 * Type getter.
	 * @return The event's type
	 */
	public EventType getType() {
		return type;
	}
	
	/**
	 * Arguments getter.
	 * @return The event's arguments
	 */
	public EventArgs getArgs() {
		return args;
	}
	
	/**
	 * Event description getter.
	 * (Every event should have description, passed
	 *  with the arguments)
	 * @return The description of the event
	 */
	public String getDescription() {
		if (args == null) {
			return "";
		}
		return args.getDescription();
	}
}
