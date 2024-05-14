package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
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

    @GetMapping
    public ResponseEntity<List<BasicUserDto>> getAllUsers() {
        List<BasicUserDto> users = userService.findAllUsers().stream()
                .map(basicUserMapper::toDto)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") final Long id) {
        Optional<User> user = userService.getUser(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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

}