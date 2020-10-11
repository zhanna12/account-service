package kz.iitu.pharm.accountservice.config;

import kz.iitu.pharm.accountservice.Service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    String[] resources = new String[]{
            "/include/**", "/static/css/**","/icons/**", "/static/img/**","/js/**","/layer/**",  "/index", "/img/**"
            , "/css/**"
    };

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
             //   .csrf().disable()
                .authorizeRequests()
                .antMatchers("/","/index","/signup", "/basket").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers(resources).permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/users/**","/basket/**/**", "/drugs/**", "/register/**").permitAll()
                .antMatchers("/users/create").hasAuthority("ADMIN")
                .antMatchers("/users/update/**").hasAuthority("ADMIN")
                .antMatchers("/drugs/add/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .defaultSuccessUrl("/index")
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                // What's the authenticationManager()?
                // An object provided by WebSecurityConfigurerAdapter, used to authenticate the user passing user's credentials
                // The filter needs this auth manager to authenticate the user.
                .addFilter(new JwtTokenGeneratorFilter(authenticationManager()))

                // Add a filter to validate the tokens with every request
                .addFilterAfter(new JwtTokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceImpl)
                .passwordEncoder(passwordEncoder());
    }
}
