package com.urjc.daw.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@Order(1)
public class SecurityApiRest extends WebSecurityConfigurerAdapter {

	@Autowired
	public AuthenticationProviderUser authenticationProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/api/**");

		// here urls need authentication
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user/{name}");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/login").authenticated();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/register");

		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/question/{id}").hasAnyRole("TEACHER","STUDENT");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/question/{id}");


		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/concept/{id}").hasAnyRole("TEACHER");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/concept/{id}").hasAnyRole("TEACHER","STUDENT");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/concept/lesson/{idLesson}").hasAnyRole("TEACHER","STUDENT");
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/concept/{id}").hasAnyRole("TEACHER");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/concept/{id}").hasAnyRole("TEACHER");
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/concept/lesson/{idLesson}").hasAnyRole("TEACHER");
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/concept/image/{id}").hasAnyRole("TEACHER");

		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/lesson/{id}").permitAll();
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/lesson/").hasAnyRole("TEACHER");



		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/items/{id}").hasAnyRole("TEACHER","STUDENT");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/items/concept/{idConcept}").hasAnyRole("TEACHER");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/items/concept/{idConcept}").hasAnyRole("TEACHER","STUDENT");
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/items/user/{idUser}").hasAnyRole("TEACHER");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE, "/api/items/{id}").hasAnyRole("TEACHER");

		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/answer/{id}").hasAnyRole("TEACHER", "STUDENT");
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/answer/{idQuestion}").hasAnyRole("STUDENT");
		http.authorizeRequests().antMatchers(HttpMethod.PUT, "/api/answer/{id}").hasAnyRole("TEACHER");
		http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/answer/{idQuestion}/options/{ret}/{total}").hasAnyRole("STUDENT");

		http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/question/concept/{idConcept}").hasAnyRole("STUDENT");
		http.authorizeRequests().antMatchers(HttpMethod.DELETE,"/api/question/{id}").hasAnyRole("TEACHER");



		
		// urls not need authentication
		http.authorizeRequests().anyRequest().permitAll();

		// disable csrf
		http.csrf().disable();

		http.httpBasic();
		
		//do not redirect when logout
		http.logout().logoutSuccessHandler((rq, rs, a) -> {
		});

	}

	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManager) throws Exception {
		authenticationManager.authenticationProvider(authenticationProvider);
	}

}
