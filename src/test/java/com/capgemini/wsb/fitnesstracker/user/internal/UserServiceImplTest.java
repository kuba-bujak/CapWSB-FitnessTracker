package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.FitnessTracker;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Test;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Unit tests for the UserService implementation.
 * Tests the behavior of methods available in the user service.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FitnessTracker.class)
@Transactional
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserMapper userMapper;

    /**
     * Test to check creating a new user.
     * Creates a user using the user service and checks if the user is successfully added.
     */
    @Test
    @Transactional
    public void shouldCreateNewUser() {
        // given
        UserDto userDto = new UserDto(11L, "John", "Nowak", LocalDate.of(1999,12,12), "john.nowak@gmail.com");
        int usersListSize = userService.findAllUsers().size();
        // when
        User createdUser = userService.createUser(userMapper.toEntity(userDto));

        // then
        assertNotNull(createdUser.getId());
        assertEquals(usersListSize + 1, userService.findAllUsers().size());
        assertEquals(userDto.firstName(), createdUser.getFirstName());
        assertEquals(userDto.lastName(), createdUser.getLastName());
        assertEquals(userDto.birthdate(), createdUser.getBirthdate());
        assertEquals(userDto.email(), createdUser.getEmail());
    }

    /**
     * Test to check if an exception is thrown when creating a user with an existing email.
     */
    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    public void shouldThrowExceptionWhenCreateUserWithExistingEmail() {
        // given
        UserDto userDto = new UserDto(1L, "John", "Nowak", LocalDate.of(1999,12,12), "john.nowak@gmail.com");
        UserDto userDto2 = new UserDto(1L, "Anna", "Nowak", LocalDate.of(1999,12,12), "john.nowak@gmail.com");

        // when
        userService.createUser(userMapper.toEntity(userDto));
        userService.createUser(userMapper.toEntity(userDto2));
    }

    /**
     * Test to check user deletion.
     * Creates a user, deletes it using the user service, and checks if the user is successfully deleted.
     */
    @Test
    @Transactional
    public void shouldDeleteUser() {
        // given
        UserDto userDto = new UserDto(11L, "John", "Nowak", LocalDate.of(1999,12,12), "john.nowak@gmail.com");
        User createdUser = userService.createUser(userMapper.toEntity(userDto));

        int usersListSizeBeforeDelete = userService.findAllUsers().size();
        Optional<User> userToDelete = userService.getUser(createdUser.getId());

        // when
        assertTrue(userToDelete.isPresent());
        User deletedUser = userService.deleteUser(userToDelete.get());

        // then
        assertFalse(userService.getUser(createdUser.getId()).isPresent());
        assertEquals(createdUser.getId(), deletedUser.getId());
        assertEquals(usersListSizeBeforeDelete - 1, userService.findAllUsers().size());
    }

    /**
     * Test to check if an exception is thrown when trying to delete a non-existing user.
     */
    @Test(expected = IllegalArgumentException.class)
    @Transactional
    public void shouldNotDeleteNonExistingUser() {
        // given
        Long nonExistingUserId = 999L;
        int usersListSizeBeforeDelete = userService.findAllUsers().size();

        // when
        Optional<User> userToDelete = userService.getUser(nonExistingUserId);
        assertFalse(userToDelete.isPresent());

        User deletedUser = userService.deleteUser(userToDelete.orElse(null));
    }

    /**
     * Test to find users by email fragment.
     * Checks if users are correctly found based on the email fragment.
     */
    @Test
    @Transactional
    public void shouldFindUsersByEmailFragment() {
        // given
        UserDto userDto1 = new UserDto(11L, "John", "Nowak", LocalDate.of(1999,12,12), "john.nowak@gmail.com");
        UserDto userDto2 = new UserDto(12L, "Anna", "Nowak", LocalDate.of(1999,12,12), "anna.nowak@gmail.com");
        String emailFragment = "NoWaK";
        int initialUsersSize = userService.findAllUsers().size();
        int initialUsersContainsEmailFragmentSize = userService.findUserByEmailFragment(emailFragment).size();

        // when
        User createdUser1 = userService.createUser(userMapper.toEntity(userDto1));
        User createdUser2 = userService.createUser(userMapper.toEntity(userDto2));
        List<User> usersWithEmailFragment = userService.findUserByEmailFragment(emailFragment);

        // then
        assertEquals(initialUsersSize + 2, userService.findAllUsers().size());
        assertEquals(userDto1.firstName(), createdUser1.getFirstName());
        assertEquals(userDto2.firstName(), createdUser2.getFirstName());
        assertEquals(initialUsersContainsEmailFragmentSize + 2, userService.findUserByEmailFragment(emailFragment).size());
        assertEquals(createdUser1, usersWithEmailFragment.get(0));
        assertEquals(createdUser2, usersWithEmailFragment.get(1));
    }

    /**
     * Test to check if an empty list is returned when the email fragment is null.
     */
    @Test()
    @Transactional
    public void shouldReturnEmptyListWhenEmailFragmentIsNull() {
        // given
        String emailFragment = null;

        // when
        List<User> users = userService.findUserByEmailFragment(emailFragment);

        // then
        assertTrue(users.isEmpty());
    }

    /**
     * Test to check if an empty list is returned when the email fragment is empty.
     */
    @Test
    @Transactional
    public void shouldReturnEmptyListWhenEmailFragmentIsEmpty() {
        // given
        String emailFragment = "";

        // when
        List<User> users = userService.findUserByEmailFragment(emailFragment);

        // then
        assertTrue(users.isEmpty());
    }

    /**
     * Test to find users older than a specified age.
     * Checks if users are correctly found based on age.
     */
    @Test
    @Transactional
    public void shouldFindUsersOlderThan() {
        // given
        UserDto userDto1 = new UserDto(11L, "John", "Nowak", LocalDate.of(1950,12,12), "john.nowak@gmail.com");
        UserDto userDto2 = new UserDto(12L, "Anna", "Nowak", LocalDate.of(1950,12,12), "anna.nowak@gmail.com");
        int age = 50;
        List<User> initialUsersOlderThan = userService.findUsersOlderThan(age);

        // when
        User createdUser1 = userService.createUser(userMapper.toEntity(userDto1));
        User createdUser2 = userService.createUser(userMapper.toEntity(userDto2));
        List<User> usersOlderThan = userService.findUsersOlderThan(age);

        // then
        assertEquals(initialUsersOlderThan.size() + 2, usersOlderThan.size());
        assertEquals(createdUser2, usersOlderThan.get(3));
    }

    /**
     * Test to check if an empty list is returned when no users are older than the given age.
     */
    @Test
    @Transactional
    public void shouldReturnEmptyListWhenNoUsersOlderThanGivenAge() {
        // given
        int age = 150;

        // when
        List<User> users = userService.findUsersOlderThan(age);

        // then
        assertTrue(users.isEmpty());
    }

    /**
     * Test to check if a full list of users is returned when age is set to 0.
     */
    @Test
    @Transactional
    public void shouldReturnFullListOfUsersWhenAgeIsSetTo0() {
        // given
        int age = 0;

        // when
        List<User> users = userService.findUsersOlderThan(age);
        List<User> allUsers = userService.findAllUsers();

        // then
        assertFalse(users.isEmpty());
        assertEquals(allUsers.size(), users.size());
    }

//    @Test
//    @Transactional
//    public void shouldUpdateUser() {
//        // given
//        UserDto userToCreate = new UserDto(11L, "John", "Nowak", LocalDate.of(1950,12,12), "john.nowak@gmail.com");
//        UserDto userToUpdate = new UserDto(11L, "John", "Nowak", LocalDate.of(1950,12,12), "anna.nowak@gmail.com");
//        int usersListSize = userService.findAllUsers().size();
//
//        // when
//        User createdUser = userService.createUser(userMapper.toEntity(userToCreate));
//
//        assertNotNull(createdUser.getId());
//        assertEquals(usersListSize + 1, userService.findAllUsers().size());
//        assertEquals(userToCreate.firstName(), createdUser.getFirstName());
//        assertEquals(userToCreate.lastName(), createdUser.getLastName());
//        assertEquals(userToCreate.birthdate(), createdUser.getBirthdate());
//        assertEquals(userToCreate.email(), createdUser.getEmail());
//
//        User updatedUser = userService.updateUser(userMapper.toEntity(userToUpdate));
//
//        // then
//
//        assertEquals(userToUpdate.Id(), updatedUser.getId());
//        assertEquals(userToUpdate.firstName(), updatedUser.getFirstName());
//        assertEquals(userToUpdate.lastName(), updatedUser.getLastName());
//        assertEquals(userToUpdate.birthdate(), updatedUser.getBirthdate());
//        assertEquals(userToUpdate.email(), updatedUser.getEmail());
//
//    }

    /**
     * Test to find a user by ID.
     * Checks if a user is correctly found based on the ID.
     */
    @Test
    @Transactional
    public void shouldFindUserById() {
        // given
        Long userId = 11L;
        UserDto userToCreate = new UserDto(userId, "John", "Nowak", LocalDate.of(1950,12,12), "john.nowak@gmail.com");

        // when
        User createdUser = userService.createUser(userMapper.toEntity(userToCreate));
        Optional<User> foundUser = userService.getUser(createdUser.getId());

        // then
        assertNotNull(foundUser.isPresent());
        assertEquals(createdUser.getFirstName(), foundUser.get().getFirstName());
        assertEquals(createdUser.getLastName(), foundUser.get().getLastName());
        assertEquals(createdUser.getBirthdate(), foundUser.get().getBirthdate());
        assertEquals(createdUser.getEmail(), foundUser.get().getEmail());

    }

    /**
     * Test to check if an exception is thrown when trying to find a user with an incorrect ID.
     */
    @Test(expected = UserNotFoundException.class)
    @Transactional
    public void shouldNotFindUserWithWrongId() {
        // given
        Long userId = 36276387216L;

        // when
        Optional<User> foundUser = userService.getUser(userId);

    }
}