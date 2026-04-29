package uk.gov.companieshouse.web.accounts.controller.attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ModelAttributes {

    private final Environment environment;
    
    @Autowired
    ModelAttributes(Environment environment) {
        this.environment = environment;
    }

    @ModelAttribute("feedbackUrl")
    protected String getFeedbackUrl() {
        return environment.getProperty("feedback.url", "");
    }

    @ModelAttribute("envChsUrl")
    protected String getEnvChsUrl() {
        return environment.getProperty("chs.url", "");
    }

    @ModelAttribute("cdnUrl")
    protected String getCdnUrl() {
        return environment.getProperty("cdn.url", "");
    }

    @ModelAttribute("developerUrl")
    protected String getDeveloperUrl() {
        return environment.getProperty("developer.url", "");
    }

    @ModelAttribute("enquiriesUrl")
    protected String getEnquiriesUrl() {
        return environment.getProperty("enquiries", "");
    }

    @ModelAttribute("piwikUrl")
    protected String getPiwikUrl() {
        return environment.getProperty("piwik.url", "");
    }

    @ModelAttribute("piwikSiteId")
    protected String getPiwikSiteId() {
        return environment.getProperty("piwik.siteId", "");
    }

    @ModelAttribute("accountUrl")
    protected String getAccountUrl() {
        return environment.getProperty("account.url", "");
    }
}
