package uk.gov.companieshouse.web.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.web.accounts.interceptor.LoggingInterceptor;
import uk.gov.companieshouse.web.accounts.interceptor.UserDetailsInterceptor;
import uk.gov.companieshouse.web.accounts.util.ValidationHelper;

@SpringBootApplication
public class CompanyAccountsWebApplication implements WebMvcConfigurer {

    public static final String APPLICATION_NAME_SPACE = "company-accounts.web.ch.gov.uk";
    public static final String APPLICATION_MODEL_PACKAGE = "uk.gov.companieshouse.web.accounts.model";

    private static final Logger LOGGER = LoggerFactory.getLogger(APPLICATION_NAME_SPACE);

    private UserDetailsInterceptor userDetailsInterceptor;
    private LoggingInterceptor loggingInterceptor;

    @Autowired
    public CompanyAccountsWebApplication(UserDetailsInterceptor userDetailsInterceptor,
                                         LoggingInterceptor loggingInterceptor) {

        this.userDetailsInterceptor = userDetailsInterceptor;
        this.loggingInterceptor = loggingInterceptor;
    }

    public static void main(String[] args) {

        try {
            ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(true);
            ValidationHelper.scanPackageForValidationMappings(provider, APPLICATION_MODEL_PACKAGE);
        } catch (RuntimeException e) {
            LOGGER.error(e.getMessage());
            System.exit(1);
        }

        SpringApplication.run(CompanyAccountsWebApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loggingInterceptor);
        registry.addInterceptor(userDetailsInterceptor);
    }
}
