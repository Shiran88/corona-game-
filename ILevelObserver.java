package events;

import events.Event;

public interface ILevelObserver {
    void getNotified(Event event);
}