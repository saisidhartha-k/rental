package com.cycle.rental.config;

import java.time.Duration;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig   {

    private final RsaKeyProperties rsaKeys;

    public SecurityConfig(RsaKeyProperties rsaKeys) {
        this.rsaKeys = rsaKeys;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorize -> authorize
           // .requestMatchers("/api/cycles/some-protected-endpoint").hasRole("ROLE_USER") // Requires ROLE_USER
            //  .requestMatchers("/api/cycles/restock").hasAuthority("ROLE_ADMIN")
            // .requestMatchers("/api/cycles/borrowed").hasAuthority("ROLE_ADMIN")

            .anyRequest().permitAll() 
        )
        .oauth2ResourceServer(server -> server
            .jwt(jwt -> jwt.decoder(jwtDecoder())));
        
    
    http
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .httpBasic(Customizer.withDefaults());

    return http.build();

    }



    @Bean
    public InMemoryUserDetailsManager users() {
    return new InMemoryUserDetailsManager(
            User.withUsername("s")
                    .password("{noop}d")
                    .authorities("ROLE_USER")
                    .build()
    );
}

// @Bean
// public InMemoryUserDetailsManager admin()
// {
//     return new InMemoryUserDetailsManager(
//             User.withUsername("admin")
//                     .password("{noop}admin")
//                     .authorities("ROLE_ADMIN")
//                     .build()
//     );
// }


@Bean
JwtDecoder jwtDecoder() {
    NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    decoder.setJwtValidator(new JwtTimestampValidator(Duration.ofSeconds(60))); 
    return decoder;
}

@Bean
JwtEncoder jwtEncoder() {
    JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
}



}