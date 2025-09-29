package frontend.concreteviews.gameplayview.listeners;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import frontend.concreteviews.gameplayview.events.GameplayEvents.ExitTheGameEvent;
import game.session.ISendableConsumer;
import network.messages.gameplaystate.ExitTheGameMessage;

public class ExitTheGameEventListener implements EventListener {
    private final ISendableConsumer sendableConsumer;
    private final Runnable moveBack;

    public ExitTheGameEventListener(Runnable moveBack, ISendableConsumer sendableConsumer) {
        this.moveBack = moveBack;
        this.sendableConsumer = sendableConsumer;
    }

    @Override
    public boolean handle(Event event) {
        switch (event) {
            case ExitTheGameEvent exitTheGameEvent -> {
                // Logger.getGlobal().info("I am herrr");
                sendableConsumer.processSendable(new ExitTheGameMessage.Payload());

                moveBack.run();
                return true;
            }
            default -> {

            }
        }

        return false;
    }

}
