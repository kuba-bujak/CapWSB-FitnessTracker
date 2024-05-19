package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.BasicUserDto;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserDto;
import com.capgemini.wsb.fitnesstracker.user.api.UserEmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
class UserController {

    private final UserServiceImpl userService;

    private final UserMapper userMapper;

    private final BasicUserMapper basicUserMapper;

    private final UserEmailMapper userEmailMapper;

    /**
     * Retrieves all users.
     *
     * @return ResponseEntity containing a list of BasicUserDto objects.
     */
    @GetMapping
    public ResponseEntity<List<BasicUserDto>> getAllUsers() {
        List<BasicUserDto> users = userService.findAllUsers().stream()
                .map(basicUserMapper::toDto)
                .toList();
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return ResponseEntity containing a UserDto if the user is found, or ResponseEntity.notFound() if not found.
     */
    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") final Long id) {
        Optional<User> user = userService.getUser(id);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(userMapper.toDto(user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Adds a new user.
     *
     * @param userDto The DTO representing the user to be added.
     * @return ResponseEntity containing the added user and status code 201 (Created).
     */
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto) {
        User addedUser = userService.createUser(userMapper.toEntity(userDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id The ID of the user to delete.
     * @return ResponseEntity containing the status code 204 (No Content) if successful, or ResponseEntity.notFound() if the user is not found.
     */
    @DeleteMapping("/user/{id}")
    public ResponseEntity<Optional<User>> deleteUser(@PathVariable("id") final Long id) {
        Optional<User> userToDelete = userService.getUser(id);
        if (userToDelete.isPresent()) {
            userService.deleteUser(userToDelete.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Finds users by email fragment.
     *
     * @param emailFragment The fragment of the email address to search for.
     * @return ResponseEntity containing a list of UserEmailDto objects.
     */
    @GetMapping("/search/email")
    public ResponseEntity<List<UserEmailDto>> findUserByEmailFragment(@RequestParam String emailFragment){
        List<UserEmailDto> users = userService.findUserByEmailFragment(emailFragment).stream().map(userEmailMapper::toDto).toList();
        return ResponseEntity.ok().body(users);
    }

    /**
     * Finds users older than a specified age.
     *
     * @param age The age threshold.
     * @return ResponseEntity containing a list of UserDto objects.
     */
    @GetMapping("/search/age")
    public ResponseEntity<List<UserDto>> findUsersOlderThan(@RequestParam int age) {
        List<UserDto> users = userService.findUsersOlderThan(age).stream().map(userMapper::toDto).toList();
        return ResponseEntity.ok().body(users);
    }

    /**
     * Updates a user's information.
     *
     * @param id         The ID of the user to update.
     * @param fieldName  The name of the field to update.
     * @param fieldValue The new value of the field.
     * @return ResponseEntity containing the updated UserDto if successful, or ResponseEntity.notFound() if the user is not found.
     */
    @PatchMapping("/user/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestParam String fieldName,
            @RequestParam String fieldValue
    ) {
        Optional<User> optionalUser = userService.getUser(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            switch (fieldName) {
                case "firstName":
                    user.setFirstName(fieldValue);
                    break;
                case "lastName":
                    user.setLastName(fieldValue);
                    break;
                case "email":
                    user.setEmail(fieldValue);
                    break;
                default:
                    return ResponseEntity.badRequest().build();
            }

            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok().body(userMapper.toDto(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}