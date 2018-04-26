package hu.vadasz.peter.knockmessenger.Models.Interfaces;

/**
 * This interface is for using the message sender feature.
 */

public interface MessageSenderInterface {

    /**
     * Sends the message to the target.
     */

    void send(String fromTelephone, String toTelephone);

    /**
     * Deletes the message.
     */

    void clear();

    /**
     * Concatenates a new part to the message.
     * @param messagePart
     */

    void add(String messagePart);

    /**
     * Deletes the last part of the message.
     */

    void deleteLastPart();

    /**
     * Replaces the cursor to the beginning of the text.
     */

    void home();

    /**
     * Replaces the cursor to the end of the text.
     */

    void end();

    /**
     * Checks if the message is empty.
     * @return
     */

    boolean isEmpty();

    /**
     * This method returns the position of the cursor.
     */

    int getCursor();
}
