package uk.gov.companieshouse.web.accounts;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import uk.gov.companieshouse.accountsdates.AccountsDatesHelper;
import uk.gov.companieshouse.accountsdates.impl.AccountsDatesHelperImpl;
import uk.gov.companieshouse.environment.EnvironmentReader;
import uk.gov.companieshouse.environment.impl.EnvironmentReaderImpl;
import uk.gov.companieshouse.web.accounts.util.ValidationContext;

@Configuration
public class CompanyAccountsWebApplicationConfig {

    public static final String APPLICATION_MODEL_PACKAGE = "uk.gov.companieshouse.web.accounts.model";

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public ValidationContext createValidationContext() {

        return new ValidationContext(new ClassPathScanningCandidateComponentProvider(false),
                APPLICATION_MODEL_PACKAGE);
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public EnvironmentReader createEnvironmentReader() {

        return new EnvironmentReaderImpl();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public AccountsDatesHelper createAccountsDatesHelper() {

        return new AccountsDatesHelperImpl();
    }
}
