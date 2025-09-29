package dummylaunchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

import frontend.assetsloading.ITextureManager;
import game.actions.IAction;
import game.gamestates.IGameState;
import game.session.IActionReceiver;
import game.session.ISubscribablePlayerConnector;
import network.client.ClientSideSocketWrapper;
import network.messages.Message;
import network.utils.ConnectionData;
import utils.ISendable;
import viewmodel.IViewManager;

public class Utility {
    public static class DummySocketWrapper implements ClientSideSocketWrapper {
        private final Collection<ISendable> pendingSendables;
        private final ISubscribablePlayerConnector playerConnector;
        private final IActionReceiver actionReceiver;

        public DummySocketWrapper(Collection<ISendable> pendingSendables, IActionReceiver actionReceiver,
                ISubscribablePlayerConnector playerConnector) {
            this.pendingSendables = pendingSendables;
            this.playerConnector = playerConnector;
            this.actionReceiver = actionReceiver;
        }

        @Override
        public Collection<ISendable> getSendables() throws IOException {
            var answer = new ArrayList<ISendable>();

            synchronized (pendingSendables) {
                answer.addAll(pendingSendables);
                pendingSendables.clear();
            }

            return Collections.unmodifiableCollection(answer);
        }

        @Override
        public void dispatchMessage(Message message) throws IOException, ConnectionEndedException {
            if (message.getSendable() instanceof IAction action) {
                actionReceiver.sendAction(playerConnector, action);
            } else {
                Logger.getGlobal().severe("That was not an IAction");
            }
        }

        @Override
        public void close() throws Exception {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'close'");
        }

        @Override
        public EstablishConnectionResult establishConnection(ConnectionData connectionData) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'establishConnection'");
        }
    }

    public static class NoInteractionViewManager implements IViewManager {
        @Override
        public void start() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'start'");
        }

        @Override
        public void moveToPlayView() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'moveToPlayView'");
        }

        @Override
        public void moveToMainMenu() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'moveToMainMenu'");
        }

        @Override
        public void moveToSettings() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'moveToSettings'");
        }

        @Override
        public void moveToLoginView(ClientSideSocketWrapper clientSideSocketWrapper) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'moveToLoginView'");
        }

        @Override
        public ITextureManager getTextureManager() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getTextureManager'");
        }

        @Override
        public void moveToGameClient(ClientSideSocketWrapper clientSideSocketWrapper, int id) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'moveToGameClient'");
        }

        @Override
        public void moveToGameplay(ClientSideSocketWrapper clientSideSocketWrapper, int id,
                Collection<IGameState> initialGameStates) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'moveToGameplay'");
        }
    }

}
