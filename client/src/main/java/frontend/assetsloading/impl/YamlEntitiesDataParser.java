package frontend.assetsloading.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import frontend.assetsloading.impl.AtlasLoader.EntityTextureData;
import frontend.assetsloading.impl.AtlasLoader.StateEntry;

public class YamlEntitiesDataParser {
    static Map<String, EntityTextureData> getNameToTextureDataMap(String path) {
        Yaml yaml = new Yaml();
        InputStream inputStream = YamlEntitiesDataParser.class
                .getClassLoader()
                .getResourceAsStream(path);

        Map<String, List<LinkedHashMap<String, Double>>> data = yaml.load(inputStream);

        // var logger = Logger.getGlobal();
        // var stringBuilder = new StringBuilder();
        // for (var entry : data.entrySet()) {
        // stringBuilder.append("Category: " + entry.getKey() + '\n');
        // for (var animation : entry.getValue()) {
        // for (var animEntry : animation.entrySet()) {
        // stringBuilder.append(" " + animEntry.getKey() + " = " + animEntry.getValue()
        // + '\n');
        // }
        // }
        // }
        // logger.info(stringBuilder.toString());
        //
        Map<String, EntityTextureData> answer = new HashMap<>();

        for (var range : data.entrySet()) {
            List<StateEntry> list = new ArrayList<>();

            answer.put(range.getKey(),
                    new EntityTextureData(range.getKey(), list));

            for (var stateEntry : range.getValue()) {
                var actualEntry = stateEntry.entrySet().iterator().next();

                Float frameDuration = actualEntry.getValue().floatValue();
                list.add(new StateEntry(actualEntry.getKey(), frameDuration));
            }
        }

        return answer;
    }
}
