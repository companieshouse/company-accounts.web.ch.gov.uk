package uk.gov.companieshouse.web.accounts.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import uk.gov.companieshouse.api.error.ApiError;
import uk.gov.companieshouse.web.accounts.exception.MissingValidationMappingException;
import uk.gov.companieshouse.web.accounts.validation.ValidationError;
import uk.gov.companieshouse.web.accounts.validation.ValidationMapping;
import uk.gov.companieshouse.web.accounts.validation.ValidationModel;
import uk.gov.companieshouse.web.accounts.validation.ValidationParentMapping;

@ExtendWith(MockitoExtension.class)
public class ValidationContextTest {
    
    private static final String BEAN_CLASS_NAME = MockValidationModel.class.getName();
    private static final String NESTED_BEAN_NAME = MockNestedValidationModel.class.getName();
    private static final String MAP_BEAN_NAME = MockMapValidationModel.class.getName();
    private static final String COLLECTION_BEAN_NAME = MockCollectionValidationModel.class.getName();
    private static final String RECURSIVE_BEAN_NAME = MockRecursiveValidationModel.class.getName();
    private static final String LOCALDATE_BEAN_NAME = MockValidationModelWithLocalDate.class.getName();
    private static final String PRIMITIVES_ONLY_BEAN_NAME = MockPrimitivesOnlyValidationModel.class.getName();
    private static final String INVALID_BEAN_CLASS_NAME = MockValidationModel.class.getName() + "invalid";
    private static final String PARENT_MAPPING_BEAN_NAME = MockValidationModelWithParentMapping.class.getName();
    
    private static final String BASE_PATH = "test";
    private static final String JSON_PATH = "json.path";
    private static final String FIELD_PATH = "mockString";
    private static final String NESTED_FIELD_PATH = "mockValidationModel.mockString";
    private static final String MESSAGE_KEY = "value_outside_range";
    private static final String WEB_ERROR_MESSAGE = "validation.range.outside";
    private static final String ERROR = "error";
    private static final Map<String, String> MESSAGE_ARGUMENTS = new HashMap<>();

    @Mock
    private ClassPathScanningCandidateComponentProvider mockProvider;

    @BeforeAll
    public static void setUpData() {
        MESSAGE_ARGUMENTS.put(MESSAGE_KEY, ERROR);
    }
    
    @Test
    public void testSuccessfulScan() {
        mockComponentScanning(BEAN_CLASS_NAME);

        Assertions.assertAll(() ->
                new ValidationContext(mockProvider, BASE_PATH));
    }

    @Test
    public void testSuccessfulScanWithMapFieldPresent() {
        mockComponentScanning(MAP_BEAN_NAME);

        Assertions.assertAll(() ->
                new ValidationContext(mockProvider, BASE_PATH));
    }

    @Test
    public void testSuccessfulScanWithCollectionFieldPresent() {
        mockComponentScanning(COLLECTION_BEAN_NAME);

        Assertions.assertAll(() ->
                new ValidationContext(mockProvider, BASE_PATH));
    }
    
    @Test
    public void testSuccessfulScanWithLocalDateFieldPresent() {
        mockComponentScanning(LOCALDATE_BEAN_NAME);

        Assertions.assertAll(() ->
                new ValidationContext(mockProvider, BASE_PATH));
    }

    @Test
    public void testSuccessfulScanWithParentFieldMapping() {
        mockComponentScanning(PARENT_MAPPING_BEAN_NAME);

        Assertions.assertAll(() ->
                new ValidationContext(mockProvider, BASE_PATH));
    }
    
    @Test
    public void testNoMappingsFound() {
        assertThrows(IllegalStateException.class, () -> new ValidationContext(mockProvider, BASE_PATH));
    }
    
    @Test
    public void testValidationMappingBeanNotFound() {
        mockComponentScanning(INVALID_BEAN_CLASS_NAME);
        assertThrows(IllegalStateException.class, () -> new ValidationContext(mockProvider, BASE_PATH));
    }
    
    @Test
    public void testNoAnnotationsInValidationModel() {
        mockComponentScanning(PRIMITIVES_ONLY_BEAN_NAME);
        assertThrows(IllegalStateException.class, () -> new ValidationContext(mockProvider, BASE_PATH));
    }
    
    @Test
    public void testScanMaxDepthReached() {
        mockComponentScanning(RECURSIVE_BEAN_NAME);
        assertThrows(IllegalStateException.class, () -> new ValidationContext(mockProvider, BASE_PATH));
    }

    @Test
    public void testGetValidationError() {
        mockComponentScanning(BEAN_CLASS_NAME);

        ValidationContext context = new ValidationContext(mockProvider, BASE_PATH);
        
        List<ValidationError> validationErrors = context.getValidationErrors(createErrors(1));
        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());
        
        ValidationError validationError = validationErrors.get(0);
        assertEquals(FIELD_PATH, validationError.getFieldPath());
        assertEquals(WEB_ERROR_MESSAGE, validationError.getMessageKey());
    }

    @Test
    public void testGetValidationErrorForNestedModelField() {
        mockComponentScanning(NESTED_BEAN_NAME);

        ValidationContext context = new ValidationContext(mockProvider, BASE_PATH);

        List<ValidationError> validationErrors = context.getValidationErrors(createErrors(1));
        assertNotNull(validationErrors);
        assertEquals(1, validationErrors.size());

        ValidationError validationError = validationErrors.get(0);
        assertEquals(NESTED_FIELD_PATH, validationError.getFieldPath());
        assertEquals(WEB_ERROR_MESSAGE, validationError.getMessageKey());
    }
    
    @Test
    public void testGetMultipleValidationErrors() {
        mockComponentScanning(BEAN_CLASS_NAME);

        ValidationContext context = new ValidationContext(mockProvider, BASE_PATH);

        int errorCount = 2;
        
        List<ValidationError> validationErrors = context.getValidationErrors(createErrors(errorCount));
        assertNotNull(validationErrors);
        assertEquals(errorCount, validationErrors.size());

        validationErrors.stream().parallel().forEach(validationError -> {
            assertEquals(FIELD_PATH, validationError.getFieldPath());
            assertEquals(WEB_ERROR_MESSAGE, validationError.getMessageKey());
        });
    }
    
    @Test
    public void testMissingMappingKey() {
        mockComponentScanning(BEAN_CLASS_NAME);

        ValidationContext context = new ValidationContext(mockProvider, BASE_PATH);

        List<ApiError> errors = new ArrayList<>();
        ApiError error = new ApiError();
        error.setError(MESSAGE_KEY);
        error.setLocation("invalid");
        error.setErrorValues(MESSAGE_ARGUMENTS);
        errors.add(error);

        assertThrows(MissingValidationMappingException.class, () -> context.getValidationErrors(errors));
    }
    
    /**
     * Returns a list of API errors encapsulating dummy data.
     * 
     * @param count the number of dummy errors to create
     */
    private List<ApiError> createErrors(int count) {
        List<ApiError> errors = new ArrayList<>();
        
        for (int i = 0; i < count; i++) {
            ApiError error = new ApiError();
            error.setError(MESSAGE_KEY);
            error.setLocation(JSON_PATH);
            error.setErrorValues(MESSAGE_ARGUMENTS);
            errors.add(error);
        }
        
        return errors;
    }
    
    /**
     * Mock the scanning of model classes annotated with {@link ValidationModel}
     * to return the bean class name specified in the {@code beanName} parameter.
     *
     * @param beanName the bean class name to return
     */
    private void mockComponentScanning(String beanName) {
        BeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClassName(beanName);
        
        Set<BeanDefinition> definitions = new HashSet<>();
        definitions.add(definition);
        when(mockProvider.findCandidateComponents(BASE_PATH)).thenReturn(definitions);
    }
    
    /**
     * Mocked class containing a single field with validation annotation.
     */
    @ValidationModel
    private class MockValidationModel {
        @ValidationMapping(JSON_PATH)
        public String mockString;
    }

    /**
     * Mocked class containing a single validation mapping and a map field
     * without validation annotation.
     */
    @ValidationModel
    private class MockMapValidationModel {
        @SuppressWarnings("unused")
        @ValidationMapping(JSON_PATH)
        public String mockString;

        @SuppressWarnings("unused")
        public Map<String, String> mockMap;
    }

    /**
     * Mocked class containing a single validation mapping and a collection
     * field without validation annotation.
     */
    @ValidationModel
    private class MockCollectionValidationModel {
        @SuppressWarnings("unused")
        @ValidationMapping(JSON_PATH)
        public String mockString;

        @SuppressWarnings("unused")
        public ArrayList<String> mockArrayList;
    }

    /**
     * Mocked class containing a single field which itself is a model
     * object.
     */
    @ValidationModel
    private class MockNestedValidationModel {
        @SuppressWarnings("unused")
        public MockValidationModel mockValidationModel;
    }
    
    /**
     * Mocked class containing a single field of the same class type; creating
     * an infinitely recursive structure.
     */
    @ValidationModel
    private class MockRecursiveValidationModel {
        @SuppressWarnings("unused")
        public MockRecursiveValidationModel mockString;
    }
    
    /**
     * Mocked class containing a single field of the type LocalDate
     */
    @ValidationModel
    private class MockValidationModelWithLocalDate {
        @SuppressWarnings("unused")
        @ValidationMapping(JSON_PATH)
        public String mockString;
        
        @SuppressWarnings("unused")
        public LocalDate localDate;
    }
    
    /**
     * Mocked class containing multiple primitive fields with no validation
     * annotations.
     */
    @ValidationModel
    private class MockPrimitivesOnlyValidationModel {
        @SuppressWarnings("unused")
        public int mockString;
        
        @SuppressWarnings("unused")
        public Integer mockString2;
    }

    /**
     * Mocked class containing a single field with validation parent
     * mapping.
     */
    private class MockValidationModelWithParentMapping {
        @SuppressWarnings("unused")
        @ValidationParentMapping(JSON_PATH)
        public String mockString;
    }
}
