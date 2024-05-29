package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.api.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserProvider {

    private final UserRepository userRepository;

    /**
     * Creates a new user.
     *
     * @param user The user to create.
     * @return The created user.
     * @throws IllegalArgumentException If the user ID is not null.
     */
    @Override
    public User createUser(final User user) {
        log.info("Creating User {}", user);
        if (user.getId() != null) {
            throw new IllegalArgumentException("User has already DB ID, update is not permitted!");
        }
        return userRepository.save(user);
    }

    /**
     * Deletes an existing user.
     *
     * @param user The user to delete.
     * @return The deleted user.
     * @throws IllegalArgumentException If the user is null.
     */
    @Override
    public User deleteUser(final User user) {
        if (user != null) {
            log.info("Deleting user {}", user);
            userRepository.delete(user);
            return user;
        } else {
            throw new IllegalArgumentException("User cannot be null");
        }
    }

    /**
     * Finds users by email fragment.
     *
     * @param emailFragment The fragment of the email address to search for.
     * @return The list of users matching the email fragment.
     */
    @Override
    public List<User> findUserByEmailFragment(String emailFragment) {
        log.info("Looking for user with email fragment of {}", emailFragment);
        return userRepository.findUserIdsAndEmailsByEmailFragment(emailFragment);
    }

    /**
     * Finds users older than a specified age.
     *
     * @param age The age threshold.
     * @return The list of users older than the specified age.
     */
    @Override
    public List<User> findUsersOlderThan(int age) {
        log.info("Looking for users older than {}", age);
        LocalDate maxBirthdate = LocalDate.now().minusYears(age);
        return userRepository.findUsersOlderThan(maxBirthdate);
    }

    /**
     * Updates an existing user.
     *
     * @param user The user with updated information.
     * @return The updated user.
     * @throws IllegalArgumentException If the user does not exist.
     */
    @Override
    public User updateUser(User user) {
        User userToUpdate = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + user.getId()));

        // Update the user fields
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setBirthdate(user.getBirthdate());
        userToUpdate.setEmail(user.getEmail());

        return userRepository.save(userToUpdate);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return An Optional containing the user, if found.
     * @throws UserNotFoundException If no user is found with the specified ID.
     */
    @Override
    public Optional<User> getUser(final Long userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            throw new UserNotFoundException(userId);
        }
        return foundUser;
    }

    /**
     * Retrieves a user by email address.
     *
     * @param email The email address of the user to retrieve.
     * @return An Optional containing the user, if found.
     */
    @Override
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves all users.
     *
     * @return The list of all users.
     */
    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}