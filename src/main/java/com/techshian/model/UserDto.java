package com.techshian.model;

public class UserDto {

    private String username;
    private String password;
    private String email;
    private String link;
    
    

    public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

  

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

     
}
