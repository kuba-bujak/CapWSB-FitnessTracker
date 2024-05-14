package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.FitnessTracker;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest(classes = FitnessTracker.class)
@Transactional
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserMapper userMapper;

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

    @Test
    @Transactional
    public void shouldFindUserById() {
        // given
        Long userId = 11L;
        UserDto userToCreate = new UserDto(userId, "John", "Nowak", LocalDate.of(1950,12,12), "john.nowak@gmail.com");

        // when
        User createdUser = userService.createUser(userMapper.toEntity(userToCreate));
        Optional<User> foundUser = userService.getUser(userId);

        // then
        assertNotNull(foundUser.isPresent());
        assertEquals(createdUser.getFirstName(), foundUser.get().getFirstName());
        assertEquals(createdUser.getLastName(), foundUser.get().getLastName());
        assertEquals(createdUser.getBirthdate(), foundUser.get().getBirthdate());
        assertEquals(createdUser.getEmail(), foundUser.get().getEmail());

    }
}
