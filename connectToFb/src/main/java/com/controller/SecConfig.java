package com.controller;

import com.services.ConnSignUp;
import com.services.FacebookSignInAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.mem.InMemoryUsersConnectionRepository;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.social.connect.web.ProviderSignInController;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"com.services"})
public class SecConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    private ConnSignUp connSignUp;

    @Autowired
    ConnectionFactoryLocator connectionFactoryLocator;

    @Autowired
    UsersConnectionRepository usersConnectionRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(userDetailsService);
    }


    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/login*", "/signin/**", "/signup/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin().loginPage("/login").permitAll()
        .and()
        .logout();
    }

    @Bean
    // @Primary
    public ProviderSignInController providerSignInController() {
        ((InMemoryUsersConnectionRepository) usersConnectionRepository).setConnectionSignUp(connSignUp);
        return new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, new FacebookSignInAdapter());
    }


}
