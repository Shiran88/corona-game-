// Shiran Golbar, 313196974
// Lev Levin, 342480456
package game;

import com.jogamp.newt.event.KeyAdapter;
import com.jogamp.newt.event.KeyEvent;
import events.*;
import math.Axis;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages keyboard interaction. Provides a singleton so that all other objects are
 * able to interact with the same input flow.
 */
public class KeyboardManager extends KeyAdapter implements IKeyObservable {
    static public KeyboardManager singleton = new KeyboardManager();
    static public KeyboardManager getSingleton(){ return singleton; }
    private List<IObserver> observers; // Observers that are interested in keyboard events.
    private KeyboardManager(){
        observers = new ArrayList<IObserver>();

    }
    private KeyEvent lastKeyEvent;

    /**
     * @return the most resent event key.
     */
    public KeyEvent getLastKeyEvent() {
        return lastKeyEvent;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        EventArgs args = new EventArgs("key pressed!");
        Event event = new Event(EventType.KeyPressed, args);
        lastKeyEvent = e;
        this.notifyObservers(event);
    }
    @Override
    public void keyReleased(KeyEvent e) {
        EventArgs args = new EventArgs("key pressed!");
        Event event = new Event(EventType.KeyReleased, args);
        lastKeyEvent = e;
        this.notifyObservers(event);
    }

    @Override
    public void attachObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void detachObserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Event event) {
        for (IObserver o: observers) {
            o.getNotified(event);
        }
    }
}
