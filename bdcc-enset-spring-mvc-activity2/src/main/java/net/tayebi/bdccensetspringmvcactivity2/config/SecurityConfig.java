package net.tayebi.bdccensetspringmvcactivity2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();//hasher mdp
    }

    //apres on va lutiliser pas ms on le securise avec json web token et au lieu de ca on utlise user web details
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        //specifi les users ayant droi t dacces app
        // mdp stokces en bd de maniere hasshe

        PasswordEncoder passwordEncoder = passwordEncoder();
        return new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder().encode("1234")).roles("USER").build(),
                User.withUsername("user2").password(passwordEncoder().encode("1234")).roles("USER").build(),
                User.withUsername("admin").password(passwordEncoder().encode("1234")).roles("USER","ADMIN").build()
        );
    }
    // bean qui rtrn un obj sec filter chaune et recoit un objt http sec
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //si user pas authentf il affiche le sien sinn fl -> login etc et tt req necessite une authentif
        // tt req de index /  doivent etre des users
        return http
//                .formLogin(Customizer.withDefaults()) form de login de spring
                .formLogin(fl->fl.loginPage("/login").permitAll())
                .csrf(Customizer.withDefaults())
//                .csrf(csrf-> csrf.disable())
//                .authorizeHttpRequests(ar->ar.requestMatchers("/index/**").hasRole("USER"))
//                .authorizeHttpRequests(ar->ar.requestMatchers("/save**/**","/delete/**").hasRole("ADMIN"))
                //tt necessite auth avec lu role seul /public grace a permit all cad autorise sans authenitf
                .authorizeHttpRequests(ar->ar.requestMatchers("/user/**").hasRole("USER"))
                .authorizeHttpRequests(ar->ar.requestMatchers("/admin/**").hasRole("ADMIN"))
                .authorizeHttpRequests(ar->ar.requestMatchers("/public/**","/webjars/**").permitAll())

                .authorizeHttpRequests(ar->ar.anyRequest().authenticated())
                .exceptionHandling(eh->eh.accessDeniedPage("/notAuthorized"))
                .build();
    }
}
