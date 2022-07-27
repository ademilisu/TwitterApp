package demo.twitter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import demo.twitter.entity.User;

@Service
public class MyUserDetailService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		} else {
			List<GrantedAuthority> authorities = new ArrayList<>();
			org.springframework.security.core.userdetails.User usr = new org.springframework.security.core.userdetails.User(
					user.getUsername(), user.getPassword(), authorities);
			return usr;
		}

	}

}
