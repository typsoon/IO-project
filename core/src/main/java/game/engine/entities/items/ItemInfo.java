package game.engine.entities.items;

public record ItemInfo(
        String name,
        //this probably should something more complex (stats for weapon, description for potion etc.)
        String description,
        ItemSpriteID spriteID
) {
}
