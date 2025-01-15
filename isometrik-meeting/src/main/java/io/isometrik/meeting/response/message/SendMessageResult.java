package io.isometrik.meeting.response.message;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * The class to parse send message result of sending a message in a meeting query
 * SendMessageQuery{@link io.isometrik.meeting.builder.message.SendMessageQuery}.
 *
 * @see io.isometrik.meeting.builder.message.SendMessageQuery
 */
public class SendMessageResult implements Serializable {

  @SerializedName("messageId")
  @Expose
  private String messageId;

  @SerializedName("msg")
  @Expose
  private String message;

  /**
   * Gets message.
   *
   * @return the response message received
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets message id.
   *
   * @return the id of the message sent
   */
  public String getMessageId() {
    return messageId;
  }
}
