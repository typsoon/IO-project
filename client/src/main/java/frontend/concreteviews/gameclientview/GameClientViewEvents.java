package frontend.concreteviews.gameclientview;

import com.badlogic.gdx.scenes.scene2d.Event;

public class GameClientViewEvents {
    static class CreateRoomEvent extends Event {
        private final String name;

        public String getName() {
            return name;
        }

        public CreateRoomEvent(String name) {
            this.name = name;
        }
    }
}
