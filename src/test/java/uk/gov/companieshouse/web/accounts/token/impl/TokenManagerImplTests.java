package uk.gov.companieshouse.web.accounts.token.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.security.SignatureException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.environment.EnvironmentReader;
import uk.gov.companieshouse.web.accounts.token.TokenManager;

@ExtendWith(MockitoExtension.class)
public class TokenManagerImplTests {

    private static final String CHS_JWT_SECRET_ENV_VAR = "CHS_JWT_SECRET";

    private static final String CHS_JWT_SECRET = "ZTljOTQ3MTdkN2Y0YmUwOTJjZDA0OTJmMTQ4OTMwODM=";

    @Mock
    private EnvironmentReader mockEnvironmentReader;

    @InjectMocks
    private TokenManager tokenManager = new TokenManagerImpl();

    @Test
    void tokenManagerEncodeDecode() throws JsonProcessingException, SignatureException {

        when(mockEnvironmentReader.getMandatoryString(CHS_JWT_SECRET_ENV_VAR)).thenReturn(CHS_JWT_SECRET);

        TestNestedClass testNestedClass = new TestNestedClass();
        testNestedClass.setTestNestedValue("testNestedValue");

        TestClass testClass = new TestClass();
        testClass.setTestValue("testValue");
        testClass.setTestNestedClass(testNestedClass);

        String token = tokenManager.createJWT(testClass);

        TestClass result = tokenManager.decodeJWT(token, TestClass.class);

        assertNotNull(result);
        assertEquals(testClass.getTestValue(), result.getTestValue());
        assertNotNull(result.getTestNestedClass());
        assertEquals(testClass.getTestNestedClass().getTestNestedValue(), result.getTestNestedClass().getTestNestedValue());
    }

    private static class TestClass {

        @JsonProperty("test_value")
        private String testValue;

        private TestNestedClass testNestedClass;

        public String getTestValue() {
            return testValue;
        }

        public void setTestValue(String testValue) {
            this.testValue = testValue;
        }

        public TestNestedClass getTestNestedClass() {
            return testNestedClass;
        }

        public void setTestNestedClass(
                TestNestedClass testNestedClass) {
            this.testNestedClass = testNestedClass;
        }
    }

    private static class TestNestedClass {

        private String testNestedValue;

        public String getTestNestedValue() {
            return testNestedValue;
        }

        public void setTestNestedValue(String testNestedValue) {
            this.testNestedValue = testNestedValue;
        }
    }
}
