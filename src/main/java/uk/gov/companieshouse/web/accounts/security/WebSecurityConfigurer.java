package uk.gov.companieshouse.web.accounts.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import uk.gov.companieshouse.auth.filter.CompanyAuthFilter;
import uk.gov.companieshouse.auth.filter.HijackFilter;
import uk.gov.companieshouse.session.handler.SessionHandler;

@EnableWebSecurity
public class WebSecurityConfigurer {

    @Configuration
    @Order(1)
    public static class GovUkAccountsSecurityFilterConfig {

        @Bean
        public SecurityFilterChain govUkAccountsSecurityFilterChain(HttpSecurity http)
                throws Exception {

            return http.securityMatcher("/accounts/**")
                    .addFilterBefore(new SessionHandler(), BasicAuthenticationFilter.class)
                    .addFilterBefore(new HijackFilter(), BasicAuthenticationFilter.class).build();
        }
    }

    @Configuration
    public static class CompanyAccountsSecurityFilterConfig {

        @Bean
        public SecurityFilterChain companyAccountsSecurityFilterChain(HttpSecurity http)
                throws Exception {
            http
                    .addFilterBefore(new SessionHandler(), BasicAuthenticationFilter.class)
                    .addFilterBefore(new HijackFilter(), BasicAuthenticationFilter.class)
                    .addFilterBefore(new CompanyAuthFilter(), BasicAuthenticationFilter.class);

            return http.build();
        }
    }
}
