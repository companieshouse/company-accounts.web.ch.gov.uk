package uk.gov.companieshouse.web.accounts.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import uk.gov.companieshouse.auth.filter.CompanyAuthFilter;
import uk.gov.companieshouse.auth.filter.HijackFilter;
import uk.gov.companieshouse.session.handler.SessionHandler;

@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Configuration
    @Order(1)
    public static class CompanyAccountsSecurityFilterConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http)
            throws Exception {

            http.authorizeRequests()
                .antMatchers("/accounts/*").permitAll()
                .and()
                    .addFilterBefore(new SessionHandler(), BasicAuthenticationFilter.class)
                    .addFilterBefore(new HijackFilter(), BasicAuthenticationFilter.class)
                    .addFilterBefore(new CompanyAuthFilter(), BasicAuthenticationFilter.class);
        }

//        @Override
//        public void configure(org.springframework.security.config.annotation.web.builders.WebSecurityConfigurer web) throws Exception {
//            web.ignoring().antMatchers("/accounts/*");
//        }


   }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/accounts/corporation-tax");
    }
}
