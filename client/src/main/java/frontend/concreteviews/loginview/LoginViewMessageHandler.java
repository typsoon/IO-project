package frontend.concreteviews.loginview;

import java.io.IOException;
import java.util.logging.Logger;

import network.client.ClientSideSocketWrapper;
import game.utility.ISendable;
import network.messages.loginstate.LogInResponse;
import utility.ICyclePerformer;
import viewmodel.IViewManager;

public class LoginViewMessageHandler implements ICyclePerformer {
    private final Logger logger = Logger.getGlobal();
    private final ClientSideSocketWrapper clientSideSocketWrapper;
    private final IViewManager viewManager;

    public LoginViewMessageHandler(ClientSideSocketWrapper clientSideSocketWrapper, IViewManager viewManager) {
        this.clientSideSocketWrapper = clientSideSocketWrapper;
        this.viewManager = viewManager;
    }

    @Override
    public void performCycle() {
        try {
            var sendables = clientSideSocketWrapper.getSendables();

            for (ISendable sendable : sendables) {
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
