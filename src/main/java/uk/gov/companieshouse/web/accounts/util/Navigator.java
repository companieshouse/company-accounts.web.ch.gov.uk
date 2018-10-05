package uk.gov.companieshouse.web.accounts.util;

import java.lang.annotation.Annotation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.exception.MissingAnnotationException;

public class Navigator {

    // Private constructor to prevent instantiation
    private Navigator() {}

    public static String getNextControllerRedirect(Class clazz, String... pathVars) {
        Annotation nextControllerAnnotation = AnnotationUtils.findAnnotation(clazz, NextController.class);
        if (nextControllerAnnotation == null) {
            throw new MissingAnnotationException("Missing @NextController annotation on class " + clazz.toString());
        }

        Class nextControllerClass = ((NextController) nextControllerAnnotation).value();
        if (nextControllerClass == null) {
            throw new MissingAnnotationException("Missing @NextController value on class " + clazz.toString());
        }

        Annotation requestMappingAnnotation = AnnotationUtils.findAnnotation(nextControllerClass, RequestMapping.class);
        if (requestMappingAnnotation == null) {
            throw new MissingAnnotationException("Missing @RequestMapping annotation on class " + nextControllerClass.toString());
        }

        String[] mappings = ((RequestMapping) requestMappingAnnotation).value();
        if (mappings.length <= 0) {
            throw new MissingAnnotationException("Missing @RequestMapping value on class " + nextControllerClass.toString());
        }

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + new UriTemplate(mappings[0]).expand((Object[]) pathVars);
    }

    public static String getPreviousControllerPath(Class clazz, String... pathVars) {
        Annotation previousControllerAnnotation = AnnotationUtils.findAnnotation(clazz, PreviousController.class);
        if (previousControllerAnnotation == null) {
            throw new MissingAnnotationException("Missing @PreviousController annotation on class " + clazz.toString());
        }

        Class previousControllerClass = ((PreviousController) previousControllerAnnotation).value();
        if (previousControllerClass == null) {
            throw new MissingAnnotationException("Missing @PreviousController value on class " + clazz.toString());
        }

        Annotation requestMappingAnnotation = AnnotationUtils.findAnnotation(previousControllerClass, RequestMapping.class);
        if (requestMappingAnnotation == null) {
            throw new MissingAnnotationException("Missing @RequestMapping annotation on class " + previousControllerClass.toString());
        }

        String[] mappings = ((RequestMapping) requestMappingAnnotation).value();
        if (mappings.length <= 0) {
            throw new MissingAnnotationException("Missing @RequestMapping value on class " + previousControllerClass.toString());
        }

        return new UriTemplate(mappings[0]).expand((Object[]) pathVars).toString();
    }
}
