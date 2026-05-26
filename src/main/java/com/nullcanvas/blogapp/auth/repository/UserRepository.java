package com.nullcanvas.blogapp.auth.repository;

import com.nullcanvas.blogapp.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Anirudh
 * @since 26/05/26
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
