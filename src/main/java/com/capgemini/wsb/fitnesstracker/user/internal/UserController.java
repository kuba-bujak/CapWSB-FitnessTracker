package com.capgemini.wsb.fitnesstracker.user.internal;

import com.capgemini.wsb.fitnesstracker.user.api.User;
import lombok.RequiredArgsConstructor;
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
    public List<BasicUserDto> getAllUsers() {
        return userService.findAllUsers()
                          .stream()
                          .map(basicUserMapper::toDto)
                          .toList();
    }

    @GetMapping("/user/{id}")
    public Optional<User> getUserById(@PathVariable("id") final Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto) {

        // Demonstracja how to use @RequestBody
        System.out.println("User with e-mail: " + userDto.email() + "passed to the request");

        User addedUser = userService.createUser(userMapper.toEntity(userDto));
        return ResponseEntity.ok().body(addedUser);
    }

}