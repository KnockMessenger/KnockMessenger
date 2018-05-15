package hu.vadasz.peter.knockmessenger.Controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import hu.vadasz.peter.knockmessenger.Controllers.Exceptions.CanNotDeleteCharacterException;
import hu.vadasz.peter.knockmessenger.Controllers.Exceptions.DeviceIsOfflineException;
import hu.vadasz.peter.knockmessenger.Controllers.Exceptions.EmptyMessageException;
import hu.vadasz.peter.knockmessenger.DataPersister.Managers.MessageDataManager;
import hu.vadasz.peter.knockmessenger.MessageSending.MessageSender;
import hu.vadasz.peter.knockmessenger.Tools.InternetConnectionChecker;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessageSendingControllerTest {

    public static final int MAX_TIME = 1000;
    public static final boolean OK = true;

    @Mock
    private MessageSender.MessageSendingVisualizer visualizer;

    @Mock
    private MessageDataManager messageDataManager;

    @Mock
    private MessageSendingController.MessageSendingControllerListener listener;

    @Mock
    private InternetConnectionChecker internetConnectionChecker;

    @Mock
    private MessageSender messageSender;

    private MessageSendingController messageSendingController;

    @Test(timeout = MAX_TIME, expected = CanNotDeleteCharacterException.class)
    public void testDelete_ShouldThrowException() throws CanNotDeleteCharacterException {
        messageSendingController = new MessageSendingController(visualizer, messageDataManager, listener, internetConnectionChecker);
        when(messageSender.isEmpty()).thenReturn(true);
        messageSendingController.setMessageSender(messageSender);
        messageSendingController.delete();
    }

    @Test(timeout = MAX_TIME, expected = DeviceIsOfflineException.class)
    public void testSend_ShouldThrowDeviceIsOfflineException() throws EmptyMessageException, DeviceIsOfflineException {
        when(internetConnectionChecker.checkConnection()).thenReturn(false);
        messageSendingController = new MessageSendingController(visualizer, messageDataManager, listener, internetConnectionChecker);
        messageSendingController.setMessageSender(messageSender);
        messageSendingController.send("0123", "0123");
    }

    @Test(timeout = MAX_TIME, expected = EmptyMessageException.class)
    public void testSend_ShouldThrowEmptyMessageException() throws EmptyMessageException, DeviceIsOfflineException {
        when(internetConnectionChecker.checkConnection()).thenReturn(true);
        messageSendingController = new MessageSendingController(visualizer, messageDataManager, listener, internetConnectionChecker);
        when(messageSender.isEmpty()).thenReturn(true);
        messageSendingController.setMessageSender(messageSender);
        messageSendingController.send("0123", "0123");
    }

}