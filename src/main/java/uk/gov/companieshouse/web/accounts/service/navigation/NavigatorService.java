package uk.gov.companieshouse.web.accounts.service.navigation;

import java.lang.annotation.Annotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.util.UriTemplate;
import uk.gov.companieshouse.web.accounts.annotation.NextController;
import uk.gov.companieshouse.web.accounts.annotation.PreviousController;
import uk.gov.companieshouse.web.accounts.controller.BranchController;
import uk.gov.companieshouse.web.accounts.controller.ConditionalController;
import uk.gov.companieshouse.web.accounts.exception.MissingAnnotationException;
import uk.gov.companieshouse.web.accounts.exception.NavigationException;
import uk.gov.companieshouse.web.accounts.exception.ServiceException;

/**
 * The {@code NavigatorService} class provides support methods for handling
 * navigation between controllers and for generating redirects or retrieving
 * controller @{link RequestMapping} paths.
 *
 * @see NextController
 * @see PreviousController
 */
@Service
public class NavigatorService {

    @Autowired
    private ApplicationContext applicationContext;

    private static final int EXPECTED_PATH_VAR_COUNT = 3;

    /**
     * Searches the controller chain for the next or previous controller in the
     * web journey. The controller search begins at the controller {@code clazz}
     * in the chain and the scan will be performed in the direction specified.
     *
     * @param  clazz     the controller class in the chain to begin the scan at
     * @param  direction the direction to follow when scanning the controller chain
     * @return the next or previous controller class in the chain dependent on {@code direction}
     */
    private Class getControllerClass(Class clazz, Direction direction, String ... pathVars) {

        Class controllerClass;

        if (direction == Direction.FORWARD) {
            controllerClass = getNextControllerClass(clazz, pathVars);
        } else {
            controllerClass = getPreviousControllerClass(clazz, pathVars);
        }

        return controllerClass;
    }

    /**
     * Returns the class of the next controller in the chain that follows
     * the controller class {@code clazz}.
     *
     * @param  clazz the controller class in the chain to begin the scan at
     * @return the next controller class in the chain
     */
    private Class getNextControllerClass(Class clazz, String ... pathVars) {
        Annotation nextControllerAnnotation = AnnotationUtils.findAnnotation(clazz, NextController.class);
        if (nextControllerAnnotation == null) {
            throw new MissingAnnotationException("Missing @NextController annotation on " + clazz.toString());
        }

        Class[] classList = ((NextController) nextControllerAnnotation).value();

        if (classList.length > 0) {

            if (classList.length == 1) {
                return ((NextController) nextControllerAnnotation).value()[0];
            } else {

                Class notImplementingBranch = null;

                for (int i = 0, j = 0; i < classList.length; i++) {

                    Class specificClass = classList[i];

                    if (!isBranchController(specificClass)) {
                        j++;
                        notImplementingBranch = specificClass;

                        if (j > 1) {
                            throw new NavigationException("getNextControllerClass: More than one default branch " + clazz.toString());
                        }
                    } else {
                        BranchController potential = (BranchController)applicationContext.getBean(specificClass);

                        if (potential.shouldBranch(pathVars)) {
                            return potential.getClass();
                        }
                    }
                }

                if (notImplementingBranch != null) {
                    return notImplementingBranch;
                } else {
                    throw new NavigationException("getNextControllerClass: No default branch and no branch is valid " + clazz.toString());
                }
            }
        } else {
            throw new NavigationException("getNextControllerClass: No next controller to navigate to " + clazz.toString());
        }
    }

    /**
     * Returns the class of the previous controller in the chain that preceedes
     * the controller class {@code clazz}.
     *
     * @param  clazz the controller class in the chain to begin the scan at
     * @return the previous controller class in the chain
     */
    private Class getPreviousControllerClass(Class clazz, String ... pathVars) {
        Annotation previousControllerAnnotation = AnnotationUtils.findAnnotation(clazz, PreviousController.class);
        if (previousControllerAnnotation == null) {
            throw new MissingAnnotationException("Missing @PreviousController annotation on " + clazz.toString());
        }

        Class[] classList = ((PreviousController) previousControllerAnnotation).value();

        if (classList.length > 0) {

            if (classList.length == 1) {
                return ((PreviousController) previousControllerAnnotation).value()[0];
            } else {

                Class notImplementingBranch = null;

                for (int i = 0, j = 0; i < classList.length; i++) {

                    Class specificClass = classList[i];

                    if (!isBranchController(specificClass)) {
                        j++;
                        notImplementingBranch = specificClass;

                        if (j > 1) {
                            throw new NavigationException("getPreviousControllerClass: More than one default branch " + clazz.toString());
                        }
                    } else {
                        BranchController potential = (BranchController)applicationContext.getBean(specificClass);

                        if (potential.shouldBranch(pathVars)) {
                            return potential.getClass();
                        }
                    }
                }

                if (notImplementingBranch != null) {
                    return notImplementingBranch;
                } else {
                    throw new NavigationException("getPreviousControllerClass: No default branch and no branch is valid " + clazz.toString());
                }
            }
        } else {
            throw new NavigationException("getPreviousControllerClass: No previous controller to navigate to " + clazz.toString());
        }
    }

    /**
     * Searches the controller chain to determine which controller is next
     * in the journey, ignoring any conditional controllers that signal they
     * will not be rendered (i.e. any controller that returns boolean false
     * to {@code willRender}).
     * <p>
     * The controller search begins at the controller {@code clazz}
     * in the chain and the scan will be performed in the direction specified.
     *
     * @param  clazz the controller class in the chain to begin the scan at
     * @return the previous controller class in the chain
     */
    private Class findControllerClass(Class clazz, Direction direction, String... pathVars) {

        Class controllerClass = getControllerClass(clazz, direction, pathVars);
        if (!isConditionalController(controllerClass) || pathVars.length != EXPECTED_PATH_VAR_COUNT) {
            return controllerClass;
        }

        String companyNumber = pathVars[0];
        String transactionId = pathVars[1];
        String companyAccountsId = pathVars[2];

        boolean foundController = false;

        while (!foundController) {

            if (isConditionalController(controllerClass)) {
                ConditionalController conditionalController = (ConditionalController) applicationContext.getBean(controllerClass);

                try {
                    if (!conditionalController.willRender(companyNumber, transactionId, companyAccountsId)) {
                        controllerClass = getControllerClass(controllerClass, direction);
                        continue;
                    }
                } catch (ServiceException e) {

                    throw new NavigationException("Error when determining whether to render conditional controller " + conditionalController.getClass().toString(), e);
                }
            }

            foundController = true;
        }

        return controllerClass;
    }

    /**
     * Searches the controller chain for the next controller, taking into
     * consideration any conditional controllers that may not have data to
     * render, and returns a string comprising of the redirect prefix and
     * {@link RequestMapping} path of the next controller after populating
     * with the path variables specified.
     *
     * @param  clazz    the controller class in the chain to begin the scan at
     * @param  pathVars a variable number of strings representing any path variables
     * @return a string comprising redirect prefix and {@link RequestMapping}
     *         path of the next controller
     */
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

        for (String mapping : mappings) {
            UriTemplate mappingTemplate = new UriTemplate(mapping);

            if (pathVars.length == mappingTemplate.getVariableNames().size()) {
                return UrlBasedViewResolver.REDIRECT_URL_PREFIX + mappingTemplate.expand((Object[]) pathVars).toString();
            }
        }

        throw new NavigationException("No mapping found that matches the number of path variables provided");
    }

    /**
     * Searches the controller chain for the previous controller, taking into
     * consideration any conditional controllers that may not have data to
     * render, and returns a string comprising the {@link RequestMapping} path
     * of the previous controller populated with the path variables specified.
     *
     * @param  clazz    the controller class in the chain to begin the scan at
     * @param  pathVars a variable number of strings representing any path variables
     * @return a string comprising the {@link RequestMapping} path from the
     *         previous controller
     */
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

        for (String mapping : mappings) {
            UriTemplate mappingTemplate = new UriTemplate(mapping);

            if (pathVars.length == mappingTemplate.getVariableNames().size()) {
                return mappingTemplate.expand((Object[]) pathVars).toString();
            }
        }

        throw new NavigationException("No mapping found that matches the number of path variables provided");
    }

    /**
     * Returns true if {@code clazz} implements the {@link ConditionalController}
     * interface, otherwise returns false.
     *
     * @return true if {@code clazz} implements the {@link ConditionalController} interface
     */
    private boolean isConditionalController(Class clazz) {
        return ConditionalController.class.isAssignableFrom(clazz);
    }

    private boolean isBranchController(Class clazz) {
        return BranchController.class.isAssignableFrom(clazz);
    }

    private enum Direction {
        FORWARD,
        BACKWARD
    }
}
