package nl.meh.mmo.client.connection.observer;

/**
 * SocketObserver is used as a listener for the socket messages. Once a message is received over the socket, update wil send it to all active observers
 */
public interface SocketObserver {

    void update(String s);

}
