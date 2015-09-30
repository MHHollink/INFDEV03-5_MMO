package nl.marcelhollink.mmorpg.frontend.main.observers;

public interface SocketSubject {

    void register(SocketObserver o);
    void unregister(SocketObserver o);
    void notifyObservers();

}
