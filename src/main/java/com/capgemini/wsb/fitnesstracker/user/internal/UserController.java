package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping
    public ResponseEntity<List<BasicUserDto>> getAllUsers() {
        List<BasicUserDto> users = userService.findAllUsers().stream()
                .map(basicUserMapper::toDto)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") final Long id) {
        Optional<User> user = userService.getUser(id);
        if (user.isPresent()) {
            return ResponseEntity.ok().body(userMapper.toDto(user.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto) {
        User addedUser = userService.createUser(userMapper.toEntity(userDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);
    }

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

    @GetMapping("/search/email")
    public ResponseEntity<List<UserEmailDto>> findUserByEmailFragment(@RequestParam String emailFragment){
        List<UserEmailDto> users = userService.findUserByEmailFragment(emailFragment).stream().map(userEmailMapper::toDto).toList();
        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/search/age")
    public ResponseEntity<List<UserDto>> findUsersOlderThan(@RequestParam int age) {
        List<UserDto> users = userService.findUsersOlderThan(age).stream().map(userMapper::toDto).toList();
        return ResponseEntity.ok().body(users);
    }

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

            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok().body(userMapper.toDto(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}