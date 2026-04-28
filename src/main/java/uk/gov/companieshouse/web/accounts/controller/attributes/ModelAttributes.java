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
}
