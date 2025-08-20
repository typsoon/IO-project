package frontend.assetsloading.impl;

import java.util.EnumMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import frontend.assetsloading.TexturesProvider;
import frontend.gamestate.EntityVisibleState;
import game.engine.entities.EntityGroupID;

public class AtlasLoader implements TexturesProvider {
    record StateEntry(String stateName, Float frameDuration) {
    }

    record EntityTextureData(
            String textureGroupData,
            List<StateEntry> stateEntries) {
    }

    private final static String atlasPath = "graphics/atlasdata/graphicsAtlas.atlas";
    private final static String entitiesDataPath = "graphics/entitygroups/entities.yaml";
    private final static TextureAtlas atlas;

    // TODO: make this an enummap
    private final static EnumMap<EntityGroupID, EnumMap<EntityVisibleState, Function<Float, TextureRegion>>> mapper;

    private final static String atlasAdressesPrefix = "entitygroups";

    private static String getAdress(String entityGroupID, String stateName) {
        return "%s/%s/%s".formatted(atlasAdressesPrefix, entityGroupID.toLowerCase(), stateName.toLowerCase());
    }

    static {
        atlas = new TextureAtlas(Gdx.files.internal(atlasPath));
        var entitiesDataMap = YamlEntitiesDataParser.getNameToTextureDataMap(entitiesDataPath);

        mapper = new EnumMap<>(EntityGroupID.class);
        for (var enumVal : EntityGroupID.values()) {
            var data = entitiesDataMap.get(enumVal.name().toLowerCase());
            if (data == null) {
                throw new IllegalStateException("There is no data about %s in entities file".formatted(enumVal));
            }

            var map = new EnumMap<EntityVisibleState, Function<Float, TextureRegion>>(EntityVisibleState.class);
            mapper.put(enumVal, map);

            for (var entry : data.stateEntries) {
                // NOTE: checking whether the stateEntry even exists in our enum
                EntityVisibleState state;
                try {
                    state = EntityVisibleState.valueOf(entry.stateName.toUpperCase());
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }

                var adress = getAdress(enumVal.name(), entry.stateName);
                if (entry.frameDuration != 0) {
                    var regions = atlas.findRegions(adress);

                    var animation = new Animation<TextureRegion>(entry.frameDuration,
                            regions, PlayMode.LOOP);

                    if (regions.isEmpty()) {
                        throw new IllegalStateException("No matching regions found for adress %s".formatted(adress));
                    }
                    map.put(state, stateTime -> animation.getKeyFrame(stateTime));
                } else {
                    var region = atlas.findRegion(adress);

                    if (region == null) {
                        throw new IllegalStateException("No matching region found for adress %s".formatted(adress));
                    }
                    map.put(state, stateTime -> region);
                }
            }
        }
    }

    @Override
    public TextureRegion getTextureRegion(EntityGroupID groupID, EntityVisibleState state, float stateTime) {
        return Objects.requireNonNull(mapper.get(groupID).get(state)).apply(stateTime);
    }
}
