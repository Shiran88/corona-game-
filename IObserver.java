package events;

/**
 * The interface for an observer.
 *
 */
public interface IObserver {
    /**
     * Alert the listener that an event happened.
     * @param e The event
     */
    public void getNotified(Event e);
}
