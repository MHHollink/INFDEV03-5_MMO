package nl.meh.mmo.client.connection.observer;

public interface SocketSubject {

    void register(SocketObserver o);
    void unregister(SocketObserver o);
    void notifyObservers();
}
