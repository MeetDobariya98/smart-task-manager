package com.example.smart_task_manager.Service;

import com.example.smart_task_manager.Dto.UserRequest;
import com.example.smart_task_manager.Dto.UserResponse;
import com.example.smart_task_manager.Dto.UserUpdateRequest;
import com.example.smart_task_manager.Entity.User;
import com.example.smart_task_manager.Exception.DuplicateEmailException;
import com.example.smart_task_manager.Exception.UserNotFoundException;
import com.example.smart_task_manager.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final BCryptPasswordEncoder passwordEncoder;


    @Override
    public void createUser(UserRequest request) {

        if (repository.existsByEmail(request.email())) {

            throw new DuplicateEmailException(
                    "Email already registered."
            );
        }

        String encryptedPassword =
                passwordEncoder.encode(request.password());

        UserRequest encryptedRequest =
                new UserRequest(
                        request.name(),
                        request.email(),
                        encryptedPassword
                );

        repository.save(encryptedRequest);
    }

    @Override
    public List<UserResponse> getUsers() {

        List<User> users = repository.findAll();

        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail()
                ))
                .toList();
    }

    @Override
    public UserResponse getUser(Long id) {

        User user = repository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with id " + id + " does not exist"));

        return convertToResponse(user);
    }

    @Override
    public void deleteUser(Long id) {

        repository.delete(id);
    }

    @Override
    public void updateUser(Long id,
                           UserUpdateRequest request) {

        User existingUser =
                repository.findById(id)
                        .orElseThrow(() ->
                                new UserNotFoundException(
                                        "User not found with id " + id));

        if (!existingUser.getEmail()
                .equals(request.email())
                && repository.existsByEmail(request.email())) {

            throw new DuplicateEmailException(
                    "Email already exists.");
        }

        String encryptedPassword =
                passwordEncoder.encode(
                        request.password());

        UserUpdateRequest updatedRequest =
                new UserUpdateRequest(

                        request.name(),

                        request.email(),

                        encryptedPassword
                );

        repository.update(id,
                updatedRequest);
    }

    private UserResponse convertToResponse(User user) {

        return new UserResponse(

                user.getId(),

                user.getName(),

                user.getEmail()
        );
    }

    public UserServiceImpl(UserRepository repository,
                           BCryptPasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

}