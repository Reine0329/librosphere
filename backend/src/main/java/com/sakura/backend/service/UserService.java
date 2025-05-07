package com.sakura.backend.service;

import com.sakura.backend.model.User;
import com.sakura.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByName(String name) {
        return userRepository.findByName(name);
    }

    public User createUser(User user) {
        if (userRepository.existsByAccount(user.getAccount())) {
            throw new RuntimeException("账号已存在");
        }
        user.setAuthority("2");
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("用户未找到"));

        if (!user.getAccount().equals(userDetails.getAccount()) &&
                userRepository.existsByAccount(userDetails.getAccount())) {
            throw new RuntimeException("账号已存在");
        }

        user.setAccount(userDetails.getAccount());
        user.setPassword(userDetails.getPassword());
        user.setName(userDetails.getName());

        if (!"1".equals(user.getAuthority())) {
            user.setAuthority("2");
        }

        user.setPhone(userDetails.getPhone());

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("用户未找到"));
        userRepository.delete(user);
    }

    public User updatePassword(Long id, String newPassword) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("用户未找到"));
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
