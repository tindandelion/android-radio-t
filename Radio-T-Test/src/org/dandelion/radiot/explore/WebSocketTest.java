package org.dandelion.radiot.explore;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketHandler;
import junit.framework.TestCase;
import org.dandelion.radiot.helpers.async.NotificationTrace;
import org.dandelion.radiot.helpers.async.Probe;
import org.hamcrest.Description;

import static org.dandelion.radiot.helpers.async.Poller.assertEventually;
import static org.hamcrest.CoreMatchers.equalTo;

public class WebSocketTest extends TestCase {
    private static final String ECHO_WEB_SOCKET = "ws://echo.websocket.org";
    private WebSocketConnection connection;
    private EchoSocketHandler handler;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        connection = new WebSocketConnection();
        handler = new EchoSocketHandler();
        connection.connect(ECHO_WEB_SOCKET, handler);
    }

    public void testSendAndReceiveMessage() throws Exception {
        assertEventually(isConnected(connection));

        connection.sendTextMessage("Hello");
        handler.hasReceivedMessage("Hello");
    }

    private Probe isConnected(final WebSocketConnection connection) {
        return new Probe() {
            public boolean isConnected;

            @Override
            public boolean isSatisfied() {
                return isConnected;
            }

            @Override
            public void sample() {
                isConnected = connection.isConnected();
            }

            @Override
            public void describeAcceptanceCriteriaTo(Description d) {
                d.appendText("Connected to web socket");
            }

            @Override
            public void describeFailureTo(Description d) {
                d.appendText("Not connected");
            }
        };
    }

    private static class EchoSocketHandler extends WebSocketHandler {
        private final NotificationTrace<String> messages = new NotificationTrace<>();

        public void hasReceivedMessage(String value) throws InterruptedException {
            messages.receivedNotification(equalTo(value));
        }

        @Override
        public void onTextMessage(String payload) {
            messages.append(payload);
        }
    }
}
