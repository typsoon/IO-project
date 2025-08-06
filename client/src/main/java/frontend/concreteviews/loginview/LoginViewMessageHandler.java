package frontend.concreteviews.loginview;

import java.io.IOException;
import java.util.logging.Logger;

import network.client.ClientSideSocketWrapper;
import network.messages.Sendable;
import network.messages.loginstate.LogInResponse;
import utility.CyclePerformer;
import viewmodel.AbstractViewManager;

public class LoginViewMessageHandler implements CyclePerformer {
    private final Logger logger = Logger.getGlobal();
    private final ClientSideSocketWrapper clientSideSocketWrapper;
    private final AbstractViewManager viewManager;

    public LoginViewMessageHandler(ClientSideSocketWrapper clientSideSocketWrapper, AbstractViewManager viewManager) {
        this.clientSideSocketWrapper = clientSideSocketWrapper;
        this.viewManager = viewManager;
    }

    @Override
    public void performCycle() {
        try {

            var sendables = clientSideSocketWrapper.getSendables();

            if (sendables.size() > 0) {
                Logger.getGlobal().finest("I am here, size %d".formatted(sendables.size()));
            }

            for (Sendable sendable : sendables) {
                switch (sendable) {
                    case LogInResponse.Payload logInResponse -> {

                        var response_payload = logInResponse.authToken();

                        if (response_payload.isEmpty()) {
                            logger.info("Invalid credentials");
                            return;
                        }

                        logger.info("Succesfully logged in");
                        viewManager.moveToGameClient(clientSideSocketWrapper);
                    }

                    default -> throw new IllegalStateException(
                            "We should not have received this message right now %s".formatted(sendable));
                }
            }
        } catch (IOException e) {
            Logger.getGlobal().severe("IOException caught!");
        }
    }

}
