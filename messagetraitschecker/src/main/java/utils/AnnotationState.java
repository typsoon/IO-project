package utils;

import javax.annotation.processing.RoundEnvironment;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.Objects;

public enum AnnotationState {
    NOT_ANNOTATED, DIRECTLY_ANNOTATED, INHERITED;

    public static AnnotationState getAnnotationState(Element element, Class<? extends Annotation> annotationClass,
            RoundEnvironment roundEnvironment) {
        // FIXME: This filter is dependent on implementation
        var mirror = element.getAnnotationMirrors().stream().filter(el -> Objects.equals(
                el.getAnnotationType().asElement().toString(),
                annotationClass.getName()))
                .findFirst();

        if (mirror.isPresent()) {
            return AnnotationState.DIRECTLY_ANNOTATED;
        }

        return roundEnvironment.getElementsAnnotatedWith(annotationClass).contains(element) ? AnnotationState.INHERITED
                : AnnotationState.NOT_ANNOTATED;
    }
}
