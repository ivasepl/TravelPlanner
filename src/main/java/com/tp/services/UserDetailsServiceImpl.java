package com.tp.services;

import com.tp.jpa.UsersEntity;
import com.tp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService
{
    public static final String ROLE_USER = "ROLE_USER";

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username)
                                            throws UsernameNotFoundException
    {

        if(username.isEmpty()){
            throw new UsernameNotFoundException("Username cannot be empty!");
        }
        final UsersEntity user = userService.findByUsername(username);
        final List<GrantedAuthority> grantedAuths = new ArrayList<>();
        if (user == null)
        {
            throw new UsernameNotFoundException("Invalid username or password!");
        }
        else
        {
            grantedAuths.add(new SimpleGrantedAuthority(ROLE_USER));
            return new User(user.getUsername(), user.getPassword(), grantedAuths);
        }

    }

}