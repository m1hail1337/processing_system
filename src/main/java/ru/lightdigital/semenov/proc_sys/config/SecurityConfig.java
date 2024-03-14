package ru.lightdigital.semenov.proc_sys.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.lightdigital.semenov.proc_sys.service.security.AuthenticationService;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalAuthentication
@Configuration
@SecurityScheme(name = "bearer", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class SecurityConfig {
    private final AuthenticationService authenticationService;
    private final JwtRequestFilter jwtRequestFilter;

    private static final String[] AUTH_WHITELIST = {
            // Auth pages
            "/login",
            "/logout",
            // -- Swagger UI v3 (OpenAPI)
            "/v2/API-docs",
            "/v3/API-docs",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui/**",
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeHttpRequests) -> {
                            authorizeHttpRequests.requestMatchers(AUTH_WHITELIST).permitAll();
                            authorizeHttpRequests.requestMatchers("/user/**").hasRole("USER");
                            authorizeHttpRequests.requestMatchers("/operator/**").hasRole("OPERATOR");
                            authorizeHttpRequests.requestMatchers("/admin/**").hasRole("ADMIN");
                            authorizeHttpRequests.anyRequest().authenticated();
                        }
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .logout((httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.logoutUrl("/logout")));
//                .exceptionHandling((exceptionHandling) -> {
//                    exceptionHandling.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
//                });
        return httpSecurity.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(authenticationService);
        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
