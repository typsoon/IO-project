package codegen;

import static com.palantir.javapoet.TypeName.BYTE;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.palantir.javapoet.ArrayTypeName;
import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.TypeName;

public class CodegenConfig {
    public static final int maxDepth = 20;
    public static final boolean UNKNOWN_TYPE_MEANS_ENUM = true;
    public static final String generatedClassNameFormat = "%sGenerated";

    public static final String tempSizeFieldName = "msgSize";

    public static final String consumerParName = "consumer";
    public static final String idFieldName = "id";
    public static final String staticSizeFieldName = "staticSize";
    public static final String encodeAndWriteMethodName = "encodeAndWrite";
    public static final String getDynamicSizeMethodName = "getDynamicSize";
    public static final String getArrayFromSupplierMethodName = "getArrayFromSupplier";
    public static final String getSendableMethodName = "getSendable";
    public static final String decodeMethodName = "decode";

    public static final String decodeFromRecordMethodName = "decodeFromRecord";
    public static final String decodeFromRecordParName = "record";

    public static final String producerParName = "producer";

    public static final String recordDecodersMapName = "recordDecoders";
    public static final String decodersMapName = "decoders";

    public static final String generatedClassesLoaderName = "GeneratedClassesData";

    public static final String iSendableClassPath = "utils.ISendable";
    public static final String fixedSizeArrayClassPath = "utils.FixedSizeArrayWrapper";

    public static record TypeNameData(
            Function<String, CodeBlock> consumerMethod, Function<String, CodeBlock> producerMethod,
            int size,
            Optional<Function<String, CodeBlock>> updateDynamicSizeCodeblock) {
    }

    public static final String answerVarName = "answer";

    public static final int MESSAGE_CODE_SIZE = Byte.BYTES;
    public static final int DYNAMIC_SIZE = -1;

    // We use byte to represent string size when sending a message
    public static final TypeName MESSAGE_SIZE_TYPE = BYTE;
    public static final int STRING_SIZE_VALUE_SIZE = Byte.BYTES;

    public static final Map<TypeName, TypeNameData> typeToTypeData = new HashMap<>();

    static {
        typeToTypeData.put(TypeName.BYTE,
                new TypeNameData(
                        name -> CodeBlock.of("$N.putByte($L)", consumerParName, name),
                        name -> CodeBlock.of("$N.getByte()", producerParName),
                        Byte.BYTES,
                        Optional.empty()));

        typeToTypeData.put(TypeName.INT, new TypeNameData(
                name -> CodeBlock.of("$N.putInt($L)", consumerParName, name),
                name -> CodeBlock.of("$N.getInt()", producerParName),
                Integer.BYTES,
                Optional.empty()));

        typeToTypeData.put(TypeName.FLOAT, new TypeNameData(
                name -> CodeBlock.of("$N.putFloat($L)", consumerParName, name),
                name -> CodeBlock.of("$N.getFloat()", producerParName),
                Float.BYTES,
                Optional.empty()));

        typeToTypeData.put(TypeName.BOOLEAN, new TypeNameData(
                name -> CodeBlock.of("$N.putBoolean($L)", consumerParName, name),
                name -> CodeBlock.of("$N.getBoolean()", producerParName),
                Byte.BYTES,
                Optional.empty()));

        typeToTypeData.put(TypeName.get(String.class), new TypeNameData(
                name -> CodeBlock.of("$N.putString($L)", consumerParName, name),
                name -> CodeBlock.of("$N.getString()", producerParName),
                DYNAMIC_SIZE,
                Optional.of(name -> CodeBlock.of("$N.length()", name))));

        // FIXME: this is ugly - it depends on file structure
        typeToTypeData.put(ClassName.bestGuess("game.utility.Point2F"), new TypeNameData(
                name -> CodeBlock.of("$N.putPoint2F($L)", consumerParName, name),
                name -> CodeBlock.of("$N.getPoint2F()", producerParName),
                2 * Float.BYTES,
                Optional.empty()));

        typeToTypeData.put(ClassName.bestGuess("game.utility.Vector2F"), new TypeNameData(
                name -> CodeBlock.of("$N.putVector2F($L)", consumerParName, name),
                name -> CodeBlock.of("$N.getVector2F()", producerParName),
                2 * Float.BYTES,
                Optional.empty()));

    }

    static final TypeNameData getTypeNameData(TypeName typeName) {
        if (typeName instanceof ArrayTypeName) {
            throw new UnsupportedOperationException("Not implemented");
        } else {
            var mappedVal = typeToTypeData.get(typeName);

            // TODO: fixme, how to check if that is an enum, It's possible that a separate
            // annotation will be needed to
            // mark enums that can be passed in messages
            // && field.type().getClass().isEnum())
            if (UNKNOWN_TYPE_MEANS_ENUM) {
                if (mappedVal == null) {
                    mappedVal = new CodegenConfig.TypeNameData(
                            name -> CodeBlock.of("$N.putEnum($L)", consumerParName, name),
                            name -> CodeBlock.of("$N.getEnum($T.values())", producerParName, typeName),
                            Integer.BYTES,
                            Optional.empty());
                }
            } else {
                if (mappedVal == null) {
                    throw new IllegalStateException(
                            "Unsupported type: %s, not found among keys %s".formatted(typeName,
                                    typeToTypeData.keySet()));
                }
            }
            return mappedVal;
        }
    }
}
