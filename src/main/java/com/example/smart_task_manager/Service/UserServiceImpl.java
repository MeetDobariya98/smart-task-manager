package com.example.smart_task_manager.Service;

import com.example.smart_task_manager.Dto.UserRequest;
import com.example.smart_task_manager.Dto.UserResponse;
import com.example.smart_task_manager.Entity.User;
import com.example.smart_task_manager.Exception.UserNotFoundException;
import com.example.smart_task_manager.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createUser(UserRequest request) {
        repository.save(request);
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

    private UserResponse convertToResponse(User user) {

        return new UserResponse(

                user.getId(),

                user.getName(),

                user.getEmail()
        );
    }

}