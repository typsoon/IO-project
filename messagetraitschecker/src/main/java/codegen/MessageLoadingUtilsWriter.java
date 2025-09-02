package codegen;

import java.util.HashSet;
import java.util.Set;

import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.TypeName;

public class MessageLoadingUtilsWriter {
    public static record ArrayLoadingUtilityMethod(String methodName, TypeName typeName, CodeBlock loadCodeBlock) {
    }

    private final Set<ArrayLoadingUtilityMethod> allUsedArrayLoadingMethods = new HashSet<>();

    public MessageLoadingUtilsWriter() {
    }
}
