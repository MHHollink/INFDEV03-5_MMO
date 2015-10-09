package nl.meh.mmo.client.connection.observer;

/**
 * SocketSubject is the interface for the socketRunnable, the 3 methods are used to connect/disconnect an observer or invoke the active observers 'update()'
 */
public interface SocketSubject {

    void register(SocketObserver o);
    void unregister(SocketObserver o);
    void notifyObservers();
}
