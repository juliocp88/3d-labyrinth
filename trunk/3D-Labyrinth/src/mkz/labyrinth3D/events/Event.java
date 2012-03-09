package mkz.labyrinth3D.events;

import java.util.ArrayList;

/**
 *
 * @author Hans
 */
public class Event
{    
    public static final int LAUNCH_NORMAL = 0;
    public static final int LAUNCH_THREAD = 1;
    
    private static ArrayList<Event> handlers = new ArrayList<Event>();
    
    public static boolean raiseEvent(String event, Object arg)
    {
        return raiseEvent(event, arg, LAUNCH_NORMAL);
    }
    
    public static boolean raiseEvent(String event, final Object arg, int type)
    {
        boolean found = false;
        for (Event item : handlers)
        {
            if(item.event.equals(event))
            {
                if (type == LAUNCH_NORMAL)
                {
                    found = true;
                    item.handler.handleEvent(arg);
                }
                else if (type == LAUNCH_THREAD)
                {
                    found = true;
                    final Event a = item;
                    Thread thread = new Thread(new Runnable() 
                    {
                        public void run()
                        {    
                            a.handler.handleEvent(arg);
                        }
                    });
                    thread.start();
                }
            }
        }
        return found;
    }
    
    public static synchronized boolean addHandler(String event, EventHandler handler)
    {
        for (Event item : handlers)
        {
            if(item.event.equals(event) && item.handler.equals(handler))
            {
                return false;
            }
        }
        Event a = new Event(event, handler);
        handlers.add(a);
        return true;
    }
    
    public static synchronized boolean removeHandler(String event, EventHandler handler)
    {
        for (Event item : handlers)
        {
            if(item.event.equals(event) && item.handler.equals(handler))
            {
                handlers.remove(item);
                return true;
            }
        }
        return false;
    }
    
    private EventHandler handler;
    private String event;    
    
    private Event(String event, EventHandler handler)
    {
        this.event = event;
        this.handler = handler;
    }
}
