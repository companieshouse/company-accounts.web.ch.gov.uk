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
import uk.gov.companieshouse.csrf.config.ChsCsrfMitigationHttpSecurityBuilder;
import uk.gov.companieshouse.session.handler.SessionHandler;

@EnableWebSecurity
@Configuration
public class WebSecurityConfigurer {

    @Order(1)
    @Bean
    public SecurityFilterChain govUkAccountsSecurityFilterChain(HttpSecurity http)
            throws Exception {

        return ChsCsrfMitigationHttpSecurityBuilder.configureWebCsrfMitigations(
                        http.securityMatcher("/accounts/**")
                                .addFilterBefore(new SessionHandler(), BasicAuthenticationFilter.class)
                                .addFilterBefore(new HijackFilter(), BasicAuthenticationFilter.class))
                .build();
    }

    @Order(2)
    @Bean
    public SecurityFilterChain healthCheckSecurityFilterChain(HttpSecurity http) throws Exception {
        return ChsCsrfMitigationHttpSecurityBuilder.configureWebCsrfMitigations(
                        http.securityMatcher("/company-accounts-web/healthcheck").authorizeHttpRequests(
                                auth -> auth.requestMatchers("/company-accounts-web/healthcheck")
                                        .permitAll()))
                .build();
    }


    @Bean
    public SecurityFilterChain companyAccountsSecurityFilterChain(HttpSecurity http)
            throws Exception {
        return ChsCsrfMitigationHttpSecurityBuilder.configureWebCsrfMitigations(
                        http.addFilterBefore(new SessionHandler(), BasicAuthenticationFilter.class)
                                .addFilterBefore(new HijackFilter(), BasicAuthenticationFilter.class)
                                .addFilterBefore(new CompanyAuthFilter(), BasicAuthenticationFilter.class))
                .build();
    }
}
