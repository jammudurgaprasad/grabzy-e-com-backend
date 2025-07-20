package com.example.ecombackend.customer.service;

import com.example.ecombackend.customer.models.Users;
import com.example.ecombackend.customer.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder PasswordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }


    public Users saveUser(Users user){
        Optional <Users> optionalUser = userRepository.findByEmail(user.getEmail());
        if(optionalUser.isPresent()){
            throw new RuntimeException("User with this email id already exists");
        }
        String encodedPassword = PasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }


    public Users login(Users loginRequest){
        Optional<Users> optionalUser = userRepository.findByEmail(loginRequest.getEmail());

        if(optionalUser.isPresent()){
            Users user = optionalUser.get();
            boolean isPasswordMatch = PasswordEncoder.matches(loginRequest.getPassword(), user.getPassword());
            if(isPasswordMatch){
                return user;
            }
        }
        throw new RuntimeException("Invalid username or password");
    }


//    public Users saveUser(Users user){
//        return userRepository.save(user);
//    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public Users getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public Users updateUser(Long id, Users updatedUser) {
        Users existingUser = getUserById(id);
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<Users> saveAll(List<Users> users) {
        return userRepository.saveAll(users);
    }
}
