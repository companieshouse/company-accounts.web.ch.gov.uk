package uk.gov.companieshouse.web.accounts.util;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import uk.gov.companieshouse.api.error.ApiError;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.web.accounts.enumeration.ValidationMessage;
import uk.gov.companieshouse.web.accounts.exception.MissingValidationMappingException;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.ValidationParentMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static uk.gov.companieshouse.web.accounts.CompanyAccountsWebApplication.APPLICATION_NAME_SPACE;

/**
 * The {@code ValidationContext} class provides support methods for handling
 * API validation errors and makes use of the {@link ValidationModel} and
 * {@link ValidationMapping} annotations.
 * <p>
 * Top-level models should be annotated with {@code ValidationModel}.
 * Model fields associated with specific API validation errors should be
 * annotated with {@code ValidationMapping} and include the full JSON error
 * path that will be returned by the API.
 * <p>
 * The {@code scanPackageForValidationMappings} method must be called at
 * startup to initialise the list of mappings between the API JSON error
 * paths and model fields that other methods of this class depend on.
 *
 * @see ValidationModel
 * @see ValidationMapping
 * @see ValidationMessage
 */
public class ValidationContext {
    private static final int MAX_DEPTH = 5;

    private static final String PATH_DELIMITER = ".";

    private static final String ERROR_PREFIX = "$";

    private static final Logger LOGGER = LoggerFactory.getLogger(APPLICATION_NAME_SPACE);

    private int depth = 0;

    private HashMap<String, String> mappings;

    private Set<Class<?>> primitivesSet = createPrimitivesSet();

    private ClassPathScanningCandidateComponentProvider scanner;

    /**
     * Constructs a {@code ValidationContext} instance with the specified
     * scanner and base package.
     *
     * @param scanner  the scanner instance to use
     * @param basePath the base package to begin scanning for validation
     *                 annotations
     */
    public ValidationContext(ClassPathScanningCandidateComponentProvider scanner, String basePath) {
        this.scanner = scanner;
        this.mappings = new HashMap<>();

        scanPackageForValidationMappings(basePath);
    }

    /**
     * Scans classes belonging to the specified package for any candidate
     * classes annotated with the {@code ValidationModel} annotation, and
     * generates a collection of mappings of JSON error paths to model object
     * fields to support the handling of API validation errors.
     *
     * @param basePackage       the base package from which to begin scanning
     * @see   ValidationModel
     * @see   ValidationMapping
     */
    public void scanPackageForValidationMappings(String basePackage) {
        scanner.addIncludeFilter(new AnnotationTypeFilter(ValidationModel.class));

        depth = 0;

        scanner.findCandidateComponents(basePackage).forEach(beanDefinition ->  {
            try {
                scanClassValidationMappings(Class.forName(beanDefinition.getBeanClassName()), "");
            } catch (ClassNotFoundException e) {
                LOGGER.error(e);
                throw new IllegalStateException("Unable to get class for bean: '" + beanDefinition.getBeanClassName() + "'");
            }
        });

        if (mappings.size() == 0) {
            throw new IllegalStateException("No validation mappings found in package " + basePackage);
        }
    }

    /**
     * Returns a list of validation errors derived from a list of provided {@link ApiError}'s
     *
     * @param  apiErrors a list of API errors
     * @return           a list of web-readable validation errors
     */
    public List<ValidationError> getValidationErrors(List<ApiError> apiErrors) {
        return apiErrors.stream().map(apiError -> {
            ValidationError validationError = new ValidationError();
            validationError.setFieldPath(getModelPathForErrorPath(apiError.getLocation()));

            ValidationMessage validationMessage = ValidationMessage.getMessageForApiError(apiError.getError());
            String messageKey = validationMessage.getMessageKey();
            if (!validationMessage.isGenericError()) {
                messageKey += apiError.getLocation().replace(ERROR_PREFIX, "");
            }
            validationError.setMessageKey(messageKey);

            validationError.setMessageArguments(apiError.getErrorValues());
            return validationError;
        }).collect(Collectors.toList());
    }

    /**
     * Returns a {@code String} object representation of the field mapped
     * to the specified JSON error path using a @ValidationMapping annotation;
     * nested fields are delimited by the dot separator, for example:
     * {@code parent.child.field}.
     *
     * @param path the JSON path error string
     * @return     the model object field path for the JSON path error string
     */
    private String getModelPathForErrorPath(String path) {
        String modelPath = mappings.get(path);
        if (StringUtils.isBlank(modelPath)) {
            throw new MissingValidationMappingException("No validation mapping found for API JSON error path: " + path);
        }

        return modelPath;
    }

    /**
     * Recursively scans the fields of a class hierarchy for field-level
     * {@code ValidationMapping} annotations.
     *
     * @param clazz    the class to be scanned
     * @param basePath the base path to be prepended to any generated field
     *                 path
     */
    private void scanClassValidationMappings(Class<?> clazz, String basePath) {
        if (depth >= MAX_DEPTH) {
            throw new IllegalStateException("Maximum recursion depth reached when performing validation scan of class " + clazz.toString());
        }

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isSynthetic()) {
                continue;
            }

            String webPath = basePath.isEmpty() ? field.getName() : basePath + PATH_DELIMITER + field.getName();

            Annotation validationMappingAnnotation = field.getAnnotation(ValidationMapping.class);
            if (validationMappingAnnotation != null) {
                String apiPath = ((ValidationMapping) validationMappingAnnotation).value();
                mappings.put(apiPath, webPath);
            }

            Annotation validationParentMappingAnnotation = field.getAnnotation(ValidationParentMapping.class);
            if (validationParentMappingAnnotation != null) {
                String apiPath = ((ValidationParentMapping) validationParentMappingAnnotation).value();
                mappings.put(apiPath, webPath);
            }

            if (isCandidateModelClass(field.getType())) {
                depth = depth + 1;
                scanClassValidationMappings(field.getType(), webPath);
            }
        }

        depth = depth - 1;
    }

    /**
     * Returns {@code true} if the {@code Class} object is not a primitive,
     * boxed primitive, collection or {@code String} or {@code LocalDate}, 
     * and therefore likely to be a model class.
     *
     * @param  clazz the {@code Class} object to test
     * @return       {@code true} if the class is not a primitive, boxed
     *               primitive, collection or {@code String}, otherwise
     *               {@code false}
     */
    private boolean isCandidateModelClass(Class<?> clazz) {
        return !(clazz.isPrimitive()
                || primitivesSet.contains(clazz)
                || Collection.class.isAssignableFrom(clazz)
                || Map.class.isAssignableFrom(clazz)
                || clazz.isEnum());
    }

    /**
     * Returns a set comprising the class objects representing the primitive
     * types and String class.
     *
     * @return a set of class objects
     */
    private Set<Class<?>> createPrimitivesSet() {
        Set<Class<?>> types = new HashSet<>();
        types.add(Byte.class);
        types.add(Short.class);
        types.add(Integer.class);
        types.add(Long.class);
        types.add(Float.class);
        types.add(Double.class);
        types.add(Boolean.class);
        types.add(Character.class);
        types.add(String.class);
        types.add(LocalDate.class);
        return types;
    }
}
