package guru.springframework.spring6authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        // Deprecato e non più necessario, SpringBootAutoConfiguration si occupa già di applicare la security predefinita per il server di autorizzazione se trova le dipendenze corrette nel classpath
        //OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(withDefaults()); // Enable OpenID Connect

        http
                .exceptionHandling(exception -> exception.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))) // Redirect al login quando non autenticato
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults())); // Posso sostituire withDefaults() con jwt -> {} ed impostare personalizzazioni

        return http.build();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .formLogin(withDefaults()); // Gestisce il redirect al login dalla filterChain dell`authServer

        return http.build();
    }

}
