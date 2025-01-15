package io.isometrik.meeting.listeners;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.callbacks.ConnectionEventCallback;
import io.isometrik.meeting.events.connection.ConnectEvent;
import io.isometrik.meeting.events.connection.ConnectionFailedEvent;
import io.isometrik.meeting.events.connection.DisconnectEvent;
import io.isometrik.meeting.response.error.IsometrikError;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Connection listener manager.
 */
public class ConnectionListenerManager {
  private final List<ConnectionEventCallback> listeners;
  private final Isometrik isometrik;

  /**
   * Instantiates a new Connection listener manager.
   *
   * @param isometrikInstance the isometrik instance
   * @see io.isometrik.meeting.Isometrik
   */
  public ConnectionListenerManager(Isometrik isometrikInstance) {
    this.listeners = new ArrayList<>();
    this.isometrik = isometrikInstance;
  }

  /**
   * Add listener.
   *
   * @param listener the ConnectionEventCallback{@link io.isometrik.meeting.callbacks.ConnectionEventCallback}
   * listener to be added
   * @see io.isometrik.meeting.callbacks.ConnectionEventCallback
   */
  public void addListener(ConnectionEventCallback listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }

  /**
   * Remove listener.
   *
   * @param listener the ConnectionEventCallback{@link io.isometrik.meeting.callbacks.ConnectionEventCallback}
   * listener to be added
   * @see io.isometrik.meeting.callbacks.ConnectionEventCallback
   */
  public void removeListener(ConnectionEventCallback listener) {
    synchronized (listeners) {
      listeners.remove(listener);
    }
  }

  /**
   * @return list of ConnectionEventCallback{@link io.isometrik.meeting.callbacks.ConnectionEventCallback}
   * listeners currently registered
   * @see io.isometrik.meeting.callbacks.ConnectionEventCallback
   */
  private List<ConnectionEventCallback> getListeners() {
    List<ConnectionEventCallback> tempCallbackList;
    synchronized (listeners) {
      tempCallbackList = new ArrayList<>(listeners);
    }
    return tempCallbackList;
  }

  /**
   * announce a ConnectEvent to listeners.
   *
   * @param connectEvent ConnectEvent{@link io.isometrik.meeting.events.connection.ConnectEvent} which
   * will be broadcast to listeners.
   * @see io.isometrik.meeting.events.connection.ConnectEvent
   */
  public void announce(ConnectEvent connectEvent) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.connected(this.isometrik, connectEvent);
    }
  }

  /**
   * announce a DisconnectEvent to listeners.
   *
   * @param disconnectEvent DisconnectEvent{@link io.isometrik.meeting.events.connection.DisconnectEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.meeting.events.connection.DisconnectEvent
   */
  public void announce(DisconnectEvent disconnectEvent) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.disconnected(this.isometrik, disconnectEvent);
    }
  }

  /**
   * announce a ConnectionFailedEvent to listeners.
   *
   * @param connectionFailedEvent ConnectionFailedEvent{@link io.isometrik.meeting.events.connection.ConnectionFailedEvent}
   * which will be broadcast to listeners.
   * @see io.isometrik.meeting.events.connection.ConnectionFailedEvent
   */
  public void announce(ConnectionFailedEvent connectionFailedEvent) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.failedToConnect(this.isometrik, connectionFailedEvent);
    }
  }

  /**
   * announce a IsometrikError to listeners.
   *
   * @param isometrikError IsometrikError{@link io.isometrik.meeting.response.error.IsometrikError}
   * which will be broadcast to listeners.
   * @see io.isometrik.meeting.response.error.IsometrikError
   */
  public void announce(IsometrikError isometrikError) {
    for (ConnectionEventCallback connectionEventCallback : getListeners()) {
      connectionEventCallback.connectionFailed(this.isometrik, isometrikError);
    }
  }
}
