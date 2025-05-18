package messagetraits;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.*;
import com.google.auto.service.AutoService;

// import javax.tools.Diagnostic;

@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SupportedAnnotationTypes("messagetraits.MessageTraits")
@AutoService(Processor.class)
public class MessageTraitsChecker extends AbstractProcessor {
    private String errorPrefix = "Class %s is ill-formed: %s";
    private String notFinalNotClassErrMsg = "A class implementing Message should be final";
    private String invalidIdErrorMsg = """
            A concrete class implementing Message should have a public static final int field with the name id and default value,
            """;
    private String idsShouldBeUniqueMsg = "id field should have unique values among different classes implementing Message";

    private final void errorMsg(Element el, String msg) {
        processingEnv.getMessager().printError(errorPrefix.formatted(el, msg));
    }

    private final Map<Byte, Object> ids = new HashMap<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        for (Element element : env.getElementsAnnotatedWith(MessageTraits.class)) {
            // processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Class:
            // %s".formatted(element));
            if (element.getKind() == INTERFACE || element.getModifiers().contains(ABSTRACT)) {
                continue;
            }

            if (element.getKind() != CLASS) {
                errorMsg(element, notFinalNotClassErrMsg);
            }

            if (!element.getModifiers()
                    .containsAll(List.of(FINAL))) {

                errorMsg(element, notFinalNotClassErrMsg);
                continue;

            }

            var id = element.getEnclosedElements()
                    .stream()
                    .filter(el -> el.getSimpleName().toString().equals("id"))
                    .findFirst();

            if (!id.isPresent()) {
                errorMsg(element, invalidIdErrorMsg);
                continue;
            }

            Object idVal;
            if (id.get() instanceof VariableElement idVar
                    && idVar.getModifiers().containsAll(List.of(PUBLIC, STATIC, FINAL))
                    && (idVal = idVar.getConstantValue()) != null
                    && idVal instanceof Byte byteIdVal) {

                if (ids.containsKey(byteIdVal)) {
                    errorMsg(element, idsShouldBeUniqueMsg);
                } else {
                    ids.put(byteIdVal, byteIdVal);
                }
            } else {
                errorMsg(element, invalidIdErrorMsg);
            }
        }

        return true;
    }
}
