package uk.gov.companieshouse.web.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.gov.companieshouse.web.accounts.interceptor.LoggingInterceptor;
import uk.gov.companieshouse.web.accounts.interceptor.CompanyAccountsDataStateInterceptor;
import uk.gov.companieshouse.web.accounts.interceptor.UserDetailsInterceptor;

@SpringBootApplication
public class CompanyAccountsWebApplication implements WebMvcConfigurer {

    public static final String APPLICATION_NAME_SPACE = "company-accounts.web.ch.gov.uk";

    private UserDetailsInterceptor userDetailsInterceptor;
    private LoggingInterceptor loggingInterceptor;
    private CompanyAccountsDataStateInterceptor companyAccountsDataStateInterceptor;

    @Autowired
    public CompanyAccountsWebApplication(UserDetailsInterceptor userDetailsInterceptor,
                                         LoggingInterceptor loggingInterceptor,
                                         CompanyAccountsDataStateInterceptor companyAccountsDataStateInterceptor) {

        this.userDetailsInterceptor = userDetailsInterceptor;
        this.loggingInterceptor = loggingInterceptor;
        this.companyAccountsDataStateInterceptor = companyAccountsDataStateInterceptor;
    }

    public static void main(String[] args) {
        SpringApplication.run(CompanyAccountsWebApplication.class, args);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(loggingInterceptor);
        registry.addInterceptor(userDetailsInterceptor).excludePathPatterns("/accounts/*");
        registry.addInterceptor(companyAccountsDataStateInterceptor).excludePathPatterns("/accounts/*");;
    }
}
