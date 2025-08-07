package codegen;

import static com.palantir.javapoet.TypeName.BYTE;

import java.util.HashMap;
import java.util.Map;

import com.palantir.javapoet.TypeName;

public class CodegenConfig {
    public static final String generatedClassNameFormat = "%sGenerated";

    public static final String consumerParName = "consumer";
    public static final String idFieldName = "id";
    public static final String staticSizeFieldName = "staticSize";
    public static final String encodeAndWriteMethodName = "encodeAndWrite";
    public static final String getDynamicSizeMethodName = "getDynamicSize";
    public static final String getSendableMethodName = "getSendable";
    public static final String decodeMethodName = "decode";

    public static final String decodeFromRecordMethodName = "decodeFromRecord";
    public static final String decodeFromRecordParName = "record";

    public static final String producerParName = "producer";

    public static final String recordDecodersMapName = "recordDecoders";
    public static final String decodersMapName = "decoders";

    public static final String generatedClassesLoaderName = "GeneratedClassesData";

    public static record TypeNameData(
            String consumerMethod, String producerMethod,
            int size) {
    }

    public static final String answerVarName = "answer";

    public static final int MESSAGE_CODE_SIZE = Byte.BYTES;
    public static final int DYNAMIC_SIZE = -1;

    // We use byte to represent string size when sending a message
    public static final TypeName MESSAGE_SIZE_TYPE = BYTE;
    public static final int STRING_SIZE_VALUE_SIZE = Byte.BYTES;

    public static final Map<TypeName, TypeNameData> typeToTypeData = new HashMap<>();

    static {
        typeToTypeData.put(TypeName.BYTE, new TypeNameData("putByte($L)", "getByte()", Byte.BYTES));
        typeToTypeData.put(TypeName.INT, new TypeNameData("putInt($N)", "getInt()", Integer.BYTES));
        typeToTypeData.put(TypeName.get(String.class),
                new TypeNameData("putString($N)", "getString()", DYNAMIC_SIZE));
    }

}
