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

    @Test
    void getEnvChsUrlReturnsPropertyFromEnvironment() {
        Environment env = mock(Environment.class);
        when(env.getProperty("chs.url", "")).thenReturn("http://chs");

        ModelAttributes attributes = new ModelAttributes(env);

        assertEquals("http://chs", attributes.getEnvChsUrl());
    }

    @Test
    void getCdnUrlReturnsPropertyFromEnvironment() {
        Environment env = mock(Environment.class);
        when(env.getProperty("cdn.url", "")).thenReturn("http://cdn");

        ModelAttributes attributes = new ModelAttributes(env);

        assertEquals("http://cdn", attributes.getCdnUrl());
    }

    @Test
    void getDeveloperUrlReturnsPropertyFromEnvironment() {
        Environment env = mock(Environment.class);
        when(env.getProperty("developer.url", "")).thenReturn("http://developer");

        ModelAttributes attributes = new ModelAttributes(env);

        assertEquals("http://developer", attributes.getDeveloperUrl());
    }

    @Test
    void getEnquiriesUrlReturnsPropertyFromEnvironment() {
        Environment env = mock(Environment.class);
        when(env.getProperty("enquiries", "")).thenReturn("http://enquiries");

        ModelAttributes attributes = new ModelAttributes(env);

        assertEquals("http://enquiries", attributes.getEnquiriesUrl());
    }

    @Test
    void getPiwikUrlReturnsPropertyFromEnvironment() {
        Environment env = mock(Environment.class);
        when(env.getProperty("piwik.url", "")).thenReturn("http://piwik");

        ModelAttributes attributes = new ModelAttributes(env);

        assertEquals("http://piwik", attributes.getPiwikUrl());
    }

    @Test
    void getPiwikSiteIdReturnsPropertyFromEnvironment() {
        Environment env = mock(Environment.class);
        when(env.getProperty("piwik.siteId", "")).thenReturn("42");

        ModelAttributes attributes = new ModelAttributes(env);

        assertEquals("42", attributes.getPiwikSiteId());
    }

    @Test
    void getAccountUrlReturnsPropertyFromEnvironment() {
        Environment env = mock(Environment.class);
        when(env.getProperty("account.url", "")).thenReturn("http://account");

        ModelAttributes attributes = new ModelAttributes(env);

        assertEquals("http://account", attributes.getAccountUrl());
    }

    @Test
    void getEnvChsUrlReturnsEmptyStringWhenPropertyNotSet() {
        Environment env = mock(Environment.class);
        when(env.getProperty("chs.url", "")).thenReturn("");
        ModelAttributes attributes = new ModelAttributes(env);
        assertEquals("", attributes.getEnvChsUrl());
    }

    @Test
    void getCdnUrlReturnsEmptyStringWhenPropertyNotSet() {
        Environment env = mock(Environment.class);
        when(env.getProperty("cdn.url", "")).thenReturn("");
        ModelAttributes attributes = new ModelAttributes(env);
        assertEquals("", attributes.getCdnUrl());
    }

    @Test
    void getDeveloperUrlReturnsEmptyStringWhenPropertyNotSet() {
        Environment env = mock(Environment.class);
        when(env.getProperty("developer.url", "")).thenReturn("");
        ModelAttributes attributes = new ModelAttributes(env);
        assertEquals("", attributes.getDeveloperUrl());
    }

    @Test
    void getEnquiriesUrlReturnsEmptyStringWhenPropertyNotSet() {
        Environment env = mock(Environment.class);
        when(env.getProperty("enquiries", "")).thenReturn("");
        ModelAttributes attributes = new ModelAttributes(env);
        assertEquals("", attributes.getEnquiriesUrl());
    }

    @Test
    void getPiwikUrlReturnsEmptyStringWhenPropertyNotSet() {
        Environment env = mock(Environment.class);
        when(env.getProperty("piwik.url", "")).thenReturn("");
        ModelAttributes attributes = new ModelAttributes(env);
        assertEquals("", attributes.getPiwikUrl());
    }

    @Test
    void getPiwikSiteIdReturnsEmptyStringWhenPropertyNotSet() {
        Environment env = mock(Environment.class);
        when(env.getProperty("piwik.siteId", "")).thenReturn("");
        ModelAttributes attributes = new ModelAttributes(env);
        assertEquals("", attributes.getPiwikSiteId());
    }

    @Test
    void getAccountUrlReturnsEmptyStringWhenPropertyNotSet() {
        Environment env = mock(Environment.class);
        when(env.getProperty("account.url", "")).thenReturn("");
        ModelAttributes attributes = new ModelAttributes(env);
        assertEquals("", attributes.getAccountUrl());
    }

}
