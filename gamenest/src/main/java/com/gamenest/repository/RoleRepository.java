package com.gamenest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamenest.model.Role;

/**
 * Repository interface for Role entity.
 * Provides CRUD operations and custom query methods through JpaRepository.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    Iterable<Role> findAllByNameIn(Iterable<String> names);

}
