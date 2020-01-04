package com.techshian.controller;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techshian.config.TokenProvider;
import com.techshian.email.EmailService;
import com.techshian.model.AuthToken;
import com.techshian.model.LoginUser;
import com.techshian.model.User;
import com.techshian.model.UserDto;
import com.techshian.payload.AES;
import com.techshian.payload.ApiResponse;
import com.techshian.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/signin", method = RequestMethod.POST)
    public ResponseEntity<?> signin(@RequestBody LoginUser loginUser) throws AuthenticationException {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getEmail(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return ResponseEntity.ok(new AuthToken(token));
    }
    

    @RequestMapping(value="/signupEmployer", method = RequestMethod.POST)
    public ResponseEntity<?> signupEmployer(@RequestBody UserDto user){
        return userService.save(user);
    }
    
    @RequestMapping(value="/signupTalent", method = RequestMethod.POST)
    public ResponseEntity<?> signupTalent(@RequestBody UserDto user){
        return userService.saveTalent(user);
    }
    
    
  //Forgot pasword A Talent
	@RequestMapping(value="/forgot",method=RequestMethod.POST)
	public ResponseEntity<?> forgot(@RequestBody UserDto loginRequest){
		try {
			
			 
			
			if(userService.loadUserByEmail(loginRequest.getEmail())!=null) {
					

			        SimpleMailMessage msg = new SimpleMailMessage();
			        msg.setFrom("verify-email@workzy.com");
			        msg.setTo(loginRequest.getEmail());
			        msg.setText("Dear Customer, Looks like you have forgot your password ,Please  click the reset password link below to reset password: http://qa.workzy.eu/#/panel/change-password/"+ URLEncoder.encode(AES.encrypt(loginRequest.getEmail()+"#"+new Date(),"techshian"), "UTF-8"));
			        msg.setSubject("workzy.com - Reset Password !");
			       
			        try {
			           emailService.sendEmail(msg);
			        }catch(Exception ex) {
			        	ex.printStackTrace();
			        	System.out.println(ex.getCause());
			        }
			        return new ResponseEntity<>(new ApiResponse(true,"Reset password link sent successfully!"),HttpStatus.OK);
			
			}else {
				return new ResponseEntity<>(new ApiResponse(false,"Please try again , referral link not found  !"),HttpStatus.BAD_GATEWAY);
			}  
			
		 } catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<>(new ApiResponse(false,"Please try again , referral link not found  !"),HttpStatus.BAD_GATEWAY);
		}
		
	}
	@RequestMapping(value="/reset",method=RequestMethod.POST)
	private ResponseEntity<?> resetPassword(@RequestBody UserDto talentAuthRequest ) throws Exception {
		
		
	    
		try {
			
			String check = AES.decrypt(talentAuthRequest.getLink(),  "techshian");
			String[] codes= check.split("#");
			String id = codes[0];
			
			
			if(!id.isEmpty() && userService.loadUserByEmail(id) != null) {
				User user =  userService.loadUserByEmail(id) ;
				
				user.setPassword(passwordEncoder.encode(talentAuthRequest.getPassword()));
				userService.update(user);
				
				return new ResponseEntity<>(new  ApiResponse(true,"Password Reset Successfuly !"),HttpStatus.OK);
			}else {
				
				return new ResponseEntity<>(new  ApiResponse(true,"Please try later !"),HttpStatus.BAD_REQUEST);
			}
		}catch(Exception ex){
			
			return new ResponseEntity<>(new  ApiResponse(true,"Please try later !"),HttpStatus.BAD_REQUEST);
		}
		
		
		
		
		
	}




}
