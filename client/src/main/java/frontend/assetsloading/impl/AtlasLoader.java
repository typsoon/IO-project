package frontend.assetsloading.impl;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import frontend.assetsloading.TexturesProvider;
import game.engine.entities.EntityGroupID;

public class AtlasLoader implements TexturesProvider {
    record StateEntry(String stateName, Float frameDuration) {
    }

    record EntityTextureData(
            String textureGroupData,
            List<StateEntry> stateEntries) {
    }

    private final static String atlasPath = "graphics/graphicsAtlas.atlas";
    private final static String entitiesDataPath = "graphics/entitygroups/entities.yaml";
    private final static TextureAtlas atlas;

    // TODO: make this an enummap
    private final static EnumMap<EntityGroupID, Function<Float, TextureRegion>[]> mapper;

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

            @SuppressWarnings("unchecked")
            var list = (Function<Float, TextureRegion>[]) new Function[data.stateEntries
                    .size()];
            mapper.put(enumVal, list);

            for (int i = 0; i < data.stateEntries.size(); i++) {
                var entry = data.stateEntries.get(i);

                var adress = getAdress(enumVal.name(), entry.stateName);
                if (entry.frameDuration != 0) {
                    var regions = atlas.findRegions(adress);

                    var animation = new Animation<TextureRegion>(entry.frameDuration,
                            regions, PlayMode.LOOP);

                    if (regions.isEmpty()) {
                        throw new IllegalStateException("No matching regions found for adress %s".formatted(adress));
                    }
                    list[i] = stateTime -> animation.getKeyFrame(stateTime);
                } else {
                    var region = atlas.findRegion(adress);

                    if (region == null) {
                        throw new IllegalStateException("No matching region found for adress %s".formatted(adress));
                    }
                    list[i] = stateTime -> region;
                }
            }
        }
    }

    @Override
    public <T extends Enum<T>> TextureRegion getTextureRegion(EntityGroupID groupID, Enum<T> state, float stateTime) {
        return mapper.get(groupID)[state.ordinal()].apply(stateTime);
    }
}
