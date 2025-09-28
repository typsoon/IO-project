package codegen;

import static codegen.CodegenConfig.decodeFromRecordMethodName;
import static codegen.CodegenConfig.decodeFromRecordParName;
import static codegen.CodegenConfig.getTypeNameData;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.MethodSpec;
import com.palantir.javapoet.ParameterSpec;

public class ProducerGenerator {
    public MethodSpec getProduceMethod(Iterable<FieldSpec> fieldSpecs, String producerQualifiedName,
            TypeElement messageTypeMirror) {
        StringJoiner args = new StringJoiner(", ");
        List<Object> formatArgs = new ArrayList<>();
        formatArgs.add(String.format(CodegenConfig.generatedClassNameFormat, messageTypeMirror.getSimpleName()));

        var producerPar = ParameterSpec
                .builder(ClassName.bestGuess(producerQualifiedName), CodegenConfig.producerParName)
                .addModifiers(FINAL)
                .build();

        var methodBuilder = MethodSpec.methodBuilder(CodegenConfig.decodeMethodName)
                .addModifiers(PUBLIC, FINAL, STATIC)
                .addParameter(producerPar)
                .returns(ClassName.get(messageTypeMirror.asType()));

        fieldSpecs.forEach(field -> {
            var mappedVal = getTypeNameData(field.type());

            args.add("$L");
            formatArgs.add(mappedVal.producerMethod().apply(field.name()));
        });

        String format = "return new $L(" + args + ")";

        var codeBlock = CodeBlock.of(format, formatArgs.toArray());

        methodBuilder.beginControlFlow("try").addStatement(codeBlock)
                .nextControlFlow("catch ($T e)", Exception.class)
                .addStatement("throw new $T($N)", IllegalStateException.class, "e")
                .endControlFlow();

        return methodBuilder.build();
    }

    public MethodSpec getProduceMethod(ClassDetailsRec classDetails, String producerQualifiedName,
            TypeElement messageTypeMirror, ProcessingEnvironment processingEnv) {
        var producerPar = ParameterSpec
                .builder(ClassName.bestGuess(producerQualifiedName), CodegenConfig.producerParName)
                .addModifiers(FINAL)
                .build();

        var methodBuilder = MethodSpec.methodBuilder(CodegenConfig.decodeMethodName)
                .addModifiers(PUBLIC, FINAL, STATIC)
                .addParameter(producerPar)
                .returns(ClassName.get(messageTypeMirror.asType()));

        var generatedClassName = String.format(CodegenConfig.generatedClassNameFormat,
                messageTypeMirror.getSimpleName());

        Pattern pattern = Pattern.compile("(.*?)\\((.*)\\)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(classDetails.loadTheClass().toString());
        final CodeBlock recordInsides;
        try {
            matcher.matches();
            recordInsides = CodeBlock.of(matcher.group(2));

        } catch (Exception e) {
            processingEnv.getMessager().printNote(
                    "%s %s".formatted(messageTypeMirror.asType().toString(),
                            classDetails.loadTheClass().toString()));

            processingEnv.getMessager().printNote("%d %s".formatted(matcher.groupCount(),
                    matcher.group(2)));

            throw e;
        }

        methodBuilder.beginControlFlow("try")
                .addStatement("return new $L($L)", generatedClassName, recordInsides)
                .nextControlFlow("catch ($T e)", Exception.class)
                .addStatement("throw new $T($N)", IllegalStateException.class, "e")
                .endControlFlow();

        return methodBuilder.build();
    }

    public MethodSpec getProduceFromRecordMethod(Iterable<FieldSpec> fieldSpecs, TypeMirror record,
            TypeElement messageTypeElement) {
        var parameter = ParameterSpec.builder(Object.class, decodeFromRecordParName).addModifiers(FINAL).build();

        var recName = "castedRec";
        var methodBuilder = MethodSpec.methodBuilder(decodeFromRecordMethodName)
                .addModifiers(PUBLIC, STATIC, FINAL)
                .returns(ClassName.get(messageTypeElement.asType()))
                .addParameter(parameter)
                .beginControlFlow(CodeBlock.of("if($N instanceof $T $L)", decodeFromRecordParName, record, recName));

        StringJoiner args = new StringJoiner(", ");
        List<Object> formatArgs = new ArrayList<>();
        formatArgs.add(String.format(CodegenConfig.generatedClassNameFormat, messageTypeElement.getSimpleName()));

        fieldSpecs.forEach(field -> {
            args.add("$L");
            formatArgs.add(CodeBlock.of("$N.$N()", recName, field.name()));
        });

        String format = "return new $L(" + args + ")";

        methodBuilder
                .addStatement(format, formatArgs.toArray())
                .nextControlFlow("else")
                .addStatement("throw new $T(\"$L\")", IllegalStateException.class,
                        "Invalid type passed to %s".formatted(decodeFromRecordMethodName))
                .endControlFlow();

        return methodBuilder.build();
    }
}
