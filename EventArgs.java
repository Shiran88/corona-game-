package events;

/**
 * A default container for the Event's arguments.
 *
 */
public class EventArgs {
	// The description of the event
	private final String description;
	
	/**
	 * The constructor.
	 * @param desc The event's description
	 */
	public EventArgs(String desc) {
		description = desc;
	}
	
	/**
	 * Description getter.
	 * @return The description of the event
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * JSON format converter for the arguments.
	 * @return The JSON representation of the arguments
	 */
	public String toJSON() {
		return "{}";
	}
}
