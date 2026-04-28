package uk.gov.companieshouse.web.accounts.controller.attributes;

import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ModelAttributesTest {

    @Test
    void getFeedbackUrlReturnsPropertyFromEnvironment() {
        Environment env = mock(Environment.class);
        when(env.getProperty("feedback.url", "")).thenReturn("https://example.com/feedback");

        ModelAttributes attributes = new ModelAttributes(env);

        String feedbackUrl = attributes.getFeedbackUrl();

        assertEquals("https://example.com/feedback", feedbackUrl);
    }

    @Test
    void getFeedbackUrlReturnsEmptyStringWhenPropertyNotSet() {
        Environment env = mock(Environment.class);
        when(env.getProperty("feedback.url", "")).thenReturn("");
        ModelAttributes attributes = new ModelAttributes(env);
        String feedbackUrl = attributes.getFeedbackUrl();
        assertEquals("", feedbackUrl);
    }
}
