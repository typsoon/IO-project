package codegen;

import com.palantir.javapoet.CodeBlock;

public record ClassDetailsRec(
        int fieldsStaticSize, CodeBlock writeTheClass,
        int dynamicFieldsBaseSize,
        CodeBlock calculateDynamicSizeCodeBlock,
        CodeBlock loadTheClass) {

    public ClassDetailsRec() {
        this(0, CodeBlock.of(""), 0, CodeBlock.of(""), CodeBlock.of(""));
    }
}
