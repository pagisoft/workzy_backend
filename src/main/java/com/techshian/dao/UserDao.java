package com.techshian.dao;

import com.techshian.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
    User findByUsername(String username);

	User findByEmail(String username);

	boolean existsByEmail(String email);
}
