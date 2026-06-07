package com.nullcanvas.blogapp.auth.repository;

import com.nullcanvas.blogapp.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data repository for {@link User} entities.
 *
 * <p>
 * Extends {@link JpaRepository} for standard CRUD and adds email-based lookups used by
 * login and registration.
 * </p>
 *
 * @author Anirudh
 * @since 26/05/26
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Finds a user by email.
	 * @param email the email to look up
	 * @return the matching user, or empty if none exists
	 */
	Optional<User> findByEmail(String email);

	/**
	 * Checks whether an account already exists for the given email.
	 * @param email the email to test
	 * @return {@code true} if a user with this email exists
	 */
	boolean existsByEmail(String email);

}
