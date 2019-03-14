package com.urjc.daw.security;

import com.urjc.daw.models.user.User;
import com.urjc.daw.models.user.UserComponent;
import com.urjc.daw.models.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class AuthenticationProviderUser implements AuthenticationProvider {

    @Autowired
    UserRepository repository;

    @Autowired
    UserComponent userComponent;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = (String) authentication.getCredentials();

        User user = repository.findByName(name);

        if (user == null) {
            throw new BadCredentialsException("user not found");
        }

        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        userComponent.setLoggedUser(user);

        List<GrantedAuthority> rol = new ArrayList<>();
        rol.add(new SimpleGrantedAuthority(user.getUserType()));
        return new UsernamePasswordAuthenticationToken(user.getName(), password, rol);
    }

    @Override
    public boolean supports(Class<?> authenticationObject) {
        return true;
    }
}
