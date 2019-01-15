package uk.gov.companieshouse.web.accounts.util;

import java.lang.annotation.Annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.MissingAnnotationException;

/**
 * The {@code Navigator} class provides support methods for handling
 * navigation between controllers.
 *
 * @see NextController
 * @see PreviousController
 */
@Component
public class Navigator {

    @Autowired
    private ApplicationContext applicationContext;

    private static final int EXPECTED_PATH_VAR_COUNT = 3;

    /**
     * Searches the controller chain for a controller of the specified class in the
     * desired direction.
     *
     * @param clazz     the desired controller class to find
     * @param direction the direction in the controller chain to follow when searching
     * @return
     */
    private Class getControllerClass(Class clazz, Direction direction) {

        Class controllerClass;

        if (direction == Direction.FORWARD) {
            controllerClass = getNextControllerClass(clazz);
        } else {
            controllerClass = getPreviousControllerClass(clazz);
        }

        return controllerClass;
    }

    private Class getNextControllerClass(Class clazz) {
        Annotation nextControllerAnnotation = AnnotationUtils.findAnnotation(clazz, NextController.class);
        if (nextControllerAnnotation == null) {
            throw new MissingAnnotationException("Missing @NextController annotation on " + clazz.toString());
        }

        return ((NextController) nextControllerAnnotation).value();
    }

    private Class getPreviousControllerClass(Class clazz) {
        Annotation previousControllerAnnotation = AnnotationUtils.findAnnotation(clazz, PreviousController.class);
        if (previousControllerAnnotation == null) {
            throw new MissingAnnotationException("Missing @PreviousController annotation on " + clazz.toString());
        }

        return ((PreviousController) previousControllerAnnotation).value();
    }

    /**
     * Returns true if {@code clazz} implements the {@code ConditionalController}
     * interface, otherwise returns false.
     *
     * @return true if {@code clazz} implements the {@code ConditionalController} interface
     */
    private boolean isConditionalController(Class clazz) {
        return ConditionalController.class.isAssignableFrom(clazz);
    }

    private Class findControllerClass(Class clazz, Direction direction, String... pathVars) {

        Class controllerClass = getControllerClass(clazz, direction);
        if (!isConditionalController(controllerClass) || pathVars.length != EXPECTED_PATH_VAR_COUNT) {
            return controllerClass;
        }

        String companyNumber = pathVars[0];
        String transactionId = pathVars[1];
        String companyAccountsId = pathVars[2];

        boolean foundController = false;

        while (foundController == false) {

            if (isConditionalController(controllerClass)) {
                ConditionalController conditionalController = (ConditionalController) applicationContext.getBean(controllerClass);
                if (conditionalController.willRender(companyNumber, transactionId, companyAccountsId) == false) {
                    controllerClass = getControllerClass(controllerClass, direction);
                    continue;
                }
            }

            foundController = true;
        }

        return controllerClass;
    }

    public String getNextControllerRedirect(Class clazz, String... pathVars) {

        Class nextControllerClass = findControllerClass(clazz, Direction.FORWARD, pathVars);

        Annotation requestMappingAnnotation = AnnotationUtils.findAnnotation(nextControllerClass, RequestMapping.class);
        if (requestMappingAnnotation == null) {
            throw new MissingAnnotationException("Missing @RequestMapping annotation on " + nextControllerClass.toString());
        }

        String[] mappings = ((RequestMapping) requestMappingAnnotation).value();
        if (mappings.length <= 0) {
            throw new MissingAnnotationException("Missing @RequestMapping value on " + nextControllerClass.toString());
        }

        return UrlBasedViewResolver.REDIRECT_URL_PREFIX + new UriTemplate(mappings[0]).expand((Object[]) pathVars);
    }

    public String getPreviousControllerPath(Class clazz, String... pathVars) {

        Class previousControllerClass = findControllerClass(clazz, Direction.BACKWARD, pathVars);

        Annotation requestMappingAnnotation = AnnotationUtils.findAnnotation(previousControllerClass, RequestMapping.class);
        if (requestMappingAnnotation == null) {
            throw new MissingAnnotationException("Missing @RequestMapping annotation on " + previousControllerClass.toString());
        }

        String[] mappings = ((RequestMapping) requestMappingAnnotation).value();
        if (mappings.length <= 0) {
            throw new MissingAnnotationException("Missing @RequestMapping value on " + previousControllerClass.toString());
        }

        return new UriTemplate(mappings[0]).expand((Object[]) pathVars).toString();
    }

    private enum Direction {
        FORWARD,
        BACKWARD
    }
}
