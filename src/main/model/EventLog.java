package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

// Represents a log of events
// Singleton Design Pattern to ensure single instance of EventLog, with a global point of access
public class EventLog implements Iterable<Event> {

    // The only EventLog in the system
    private static EventLog theLog;
    private Collection<Event> events;

    // Prevents external construction
    private EventLog() {
        events = new ArrayList<Event>();
    }

    // Accessor function that gets the only instance of EventLog, or creates it if it does not already exist
    public static EventLog getInstance() {
        if (theLog == null) {
            theLog = new EventLog();
        }
        return theLog;
    }

    // Adds an event to the event log
    public void logEvent(Event e) {
        events.add(e);
    }

    // Clears the event log and logs it
    public void clear() {
        events.clear();
        logEvent(new Event("Event log cleared."));
    }

    @Override
    public Iterator<Event> iterator() {
        return events.iterator();
    }
}
