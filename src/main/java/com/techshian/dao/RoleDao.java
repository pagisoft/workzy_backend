package com.techshian.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.techshian.model.Role;
import com.techshian.model.RoleName;
import com.techshian.model.User;

@Repository
public interface RoleDao extends JpaRepository<Role, Integer> {

	Optional<Role> findByName(RoleName roleUser);

	
}
