package events;

public interface IObservable {
    void attachObserver(IObserver observer);
    void detachObserver(IObserver observer);
    void notifyObservers(Event event);
    //

}

