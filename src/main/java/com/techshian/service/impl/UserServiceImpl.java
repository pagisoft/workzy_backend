package com.techshian.service.impl;

import com.techshian.dao.RoleDao;
import com.techshian.dao.UserDao;
import com.techshian.exception.AppException;
import com.techshian.model.Role;
import com.techshian.model.RoleName;
import com.techshian.model.User;
import com.techshian.model.UserDto;
import com.techshian.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {
	
	@Autowired
	private UserDao userDao;

	@Autowired
	RoleDao roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByEmail(username);
		if(user == null){
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
	}

	private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		user.getRoles().forEach(role -> {
			//authorities.add(new SimpleGrantedAuthority(role.getName()));
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		});
		return authorities;
		//return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}

	public List<User> findAll() {
		List<User> list = new ArrayList<>();
		userDao.findAll().iterator().forEachRemaining(list::add);
		return list;
	}

	@Override
	public void delete(long id) {
		userDao.deleteById(id);
	}

	@Override
	public User findOne(String username) {
		return userDao.findByUsername(username);
	}

	@Override
	public User findById(Long id) {
		return userDao.findById(id).get();
	}

	@Override
    public ResponseEntity<?> save(UserDto user) {
		 
		if(userDao.existsByEmail(user.getEmail())) {
	            return new ResponseEntity(new com.techshian.payload.ApiResponse(false, "Email Address already in use!"),HttpStatus.BAD_REQUEST);
	    }
		
	    User newUser = new User();
	    newUser.setUsername(user.getUsername());
	    newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setEmail(user.getEmail());
		Role userRole = roleRepository.findByName(RoleName.ROLE_EMPLOYER)
	                .orElseThrow(() -> new AppException("User Role not set."));
		newUser.setRoles(Collections.singleton(userRole));
        return new ResponseEntity<>(userDao.save(newUser),HttpStatus.BAD_GATEWAY);
    }
	
	@Override
    public ResponseEntity<?> saveTalent(UserDto user) {
		
		  if(userDao.existsByEmail(user.getEmail())) {
	            return new ResponseEntity(new com.techshian.payload.ApiResponse(false, "Email Address already in use!"),HttpStatus.BAD_REQUEST);
	        }
	        
	    User newUser = new User();
	    newUser.setUsername(user.getUsername());
	    newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setEmail(user.getEmail());
		Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
	                .orElseThrow(() -> new AppException("User Role not set."));
		newUser.setRoles(Collections.singleton(userRole));
        return new ResponseEntity<>(userDao.save(newUser),HttpStatus.BAD_GATEWAY);

    }
}
