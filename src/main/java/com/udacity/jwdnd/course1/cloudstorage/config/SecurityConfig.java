package com.udacity.jwdnd.course1.cloudstorage.config;

import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
     private AuthenticationService authenticationService;

     @Override
     protected void configure(AuthenticationManagerBuilder auth){
         auth.authenticationProvider(authenticationService);
     }
     @Override
     public void configure(WebSecurity web){
         web.ignoring().antMatchers("/h2-console/**");
     }
     @Override
     protected void configure(HttpSecurity http) throws Exception {
         http.authorizeRequests().antMatchers("/signup","/css/**","/js/**").permitAll().anyRequest().authenticated();
         http.formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/home",true);
         http.logout().logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID")
                 .logoutSuccessUrl("/login?logout").permitAll();
     }
}
