package dummylaunchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import game.utility.ISendable;
import network.client.ClientSideSocketWrapper;
import network.client.DuplexSocketWrapper;
import network.messages.Message;
import viewmodel.ITextureManager;
import viewmodel.IViewManager;

public class Utility {
    public static class DummySocketWrapper implements DuplexSocketWrapper {
        private final Collection<ISendable> pendingSendables;

        public DummySocketWrapper(Collection<ISendable> pendingSendables) {
            this.pendingSendables = pendingSendables;
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
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'dispatchMessage'");
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
        public void moveToLoginView(ClientSideSocketWrapper clientSideSocketWrapper) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'moveToLoginView'");
        }

        @Override
        public void moveToGameClient(ClientSideSocketWrapper clientSideSocketWrapper) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'moveToGameClient'");
        }

        @Override
        public ITextureManager getTextureManager() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getTextureManager'");
        }
    }

}
