package io.isometrik.meeting.callbacks;

import org.jetbrains.annotations.NotNull;

import io.isometrik.meeting.Isometrik;
import io.isometrik.meeting.events.user.DeleteUserEvent;
import io.isometrik.meeting.events.user.UpdateUserEvent;

/**
 * The abstract class for the user event callback, with methods for user blocked/unblock, user
 * delete/update events.
 */
public abstract class UserEventCallback {

  /**
   * User updated.
   *
   * @param isometrik the isometrik
   * @param updateUserEvent the update user event
   */
  public abstract void userUpdated(@NotNull Isometrik isometrik,
      @NotNull UpdateUserEvent updateUserEvent);

  /**
   * User deleted.
   *
   * @param isometrik the isometrik
   * @param deleteUserEvent the delete user event
   */
  public abstract void userDeleted(@NotNull Isometrik isometrik,
      @NotNull DeleteUserEvent deleteUserEvent);
}
