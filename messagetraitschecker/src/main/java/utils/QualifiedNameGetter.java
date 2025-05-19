package utils;

import java.lang.annotation.Annotation;
import java.util.Objects;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

import messagetraits.MessageTraits;

public class QualifiedNameGetter {
    private final String getQualifiedName(Types typeUtils, AnnotationValue val) {
        var typeMirror = (TypeMirror) val.getValue();
        var typeElement = (TypeElement) typeUtils.asElement(typeMirror);
        return typeElement.getQualifiedName().toString();
    }

    public String getMessageTraitsQName(RoundEnvironment roundEnvironment) {
        var ourElement = roundEnvironment.getElementsAnnotatedWith(MessageTraits.class).stream()
                .filter(el -> AnnotationState.getAnnotationState(el, MessageTraits.class,
                        roundEnvironment) == AnnotationState.DIRECTLY_ANNOTATED)
                .findFirst()
                .orElseThrow(IllegalStateException::new);

        return ((TypeElement) ourElement).getQualifiedName().toString();
    }

    public ConsumerProducer getConsumerProducer(ProcessingEnvironment processingEnv, Element element,
            Class<? extends Annotation> annotationClass) {

        var elementUtils = processingEnv.getElementUtils();
        var typeUtils = processingEnv.getTypeUtils();

        var elementsIter = elementUtils.getAllAnnotationMirrors(element)
                .stream()
                .filter(el -> Objects.equals(
                        el.getAnnotationType().asElement().toString(),
                        annotationClass.getName()))
                .map(el -> el.getElementValues())
                .findFirst();

        if (elementsIter.isEmpty()) {
            return null;
        }

        var elementsMapValsIter = elementsIter.get().values().iterator();

        try {
            String consumerName = getQualifiedName(typeUtils, elementsMapValsIter.next());
            String producerName = getQualifiedName(typeUtils, elementsMapValsIter.next());

            return new ConsumerProducer(consumerName, producerName);

        } catch (ClassCastException e) {
            return null;
        }
    }
}
