package com.example.meetontest.api.security;

import com.example.meetontest.api.entities.*;
import com.example.meetontest.api.repositories.MeetingRepository;
import com.example.meetontest.api.repositories.RoleRepository;
import com.example.meetontest.api.repositories.TagRepository;
import com.example.meetontest.api.repositories.UserRepository;
import com.example.meetontest.api.security.jwt.AuthEntryPointJwt;
import com.example.meetontest.api.security.jwt.AuthTokenFilter;
import com.example.meetontest.api.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Date;
import java.util.HashSet;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    UserRepository userRepository;
    @Autowired
    MeetingRepository meetingRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TagRepository tagRepository;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if(roleRepository.count() == 0) {
            roleRepository.save(new Role(ERole.ROLE_USER));
            roleRepository.save(new Role(ERole.ROLE_MODERATOR));
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }

        if(tagRepository.count() == 0) {
            tagRepository.save(new Tag("Образование"));
            tagRepository.save(new Tag("Программирование"));
            tagRepository.save(new Tag("Java"));
            tagRepository.save(new Tag("Spring"));
        }

        if (!userRepository.existsByUsername("Zorgont")) {
            User vladlen = new User();
            vladlen.setUsername("Zorgont");
            vladlen.setFirstName("Vladlen");
            vladlen.setSecondName("Plakhotnyuk");
            vladlen.setEmail("zorgont@gmail.com");
            vladlen.setAbout("Hello, I'm Vladlen!");
            vladlen.setStatus("active");
            vladlen.setPassword(passwordEncoder().encode("123456"));
            vladlen.setRoles(new HashSet<>(roleRepository.findAll()));
            userRepository.save(vladlen);
        }

        if (meetingRepository.count() == 0) {
            Meeting meetingByVladlen = new Meeting();
            meetingByVladlen.setName("Meeting by Vladlen");
            meetingByVladlen.setAbout("Joins this wonderful meeting!");
            meetingByVladlen.setDate(new Date());
            meetingByVladlen.setParticipantAmount(100);
            meetingByVladlen.setPrivate(false);
            meetingByVladlen.setStatus("Planning");
            meetingByVladlen.setDetails("Secret Zoom link: ....");
            meetingByVladlen.setManager(userRepository.findByUsername("Zorgont").get());
            meetingByVladlen.setTags(new HashSet<>(tagRepository.findAll()));
            meetingRepository.save(meetingByVladlen);
        }

        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("/api/v1/users/**").permitAll()
                .antMatchers("/api/v1/test/**").authenticated()
                .antMatchers("/api/v1/meetings/**").authenticated()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}