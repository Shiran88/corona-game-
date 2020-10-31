package events;

public interface ILevelObservable {
    void attachObserver(ILevelObserver observer);
    void detachObserver(ILevelObserver observer);
    void notifyObservers(Event event);
}
