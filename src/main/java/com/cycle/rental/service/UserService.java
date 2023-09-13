// package com.cycle.rental.service;



// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
// import org.springframework.stereotype.Service;

// import com.cycle.rental.repository.UserRepository;


// @Service
// public class UserService {

//     @Autowired
//     private UserRepository userRepository;

//     public Optional<User> authenticate(String username, String password) {
//         Optional<User> optUser = userRepository.findByName(username);
//         if (optUser.isEmpty()) {
//             try {
//                 throw new Exception("User not found");
//             } catch (Exception e) {
//                 // TODO Auto-generated catch block
//                 e.printStackTrace();
//             }
//         }
//         if (!optUser.get().getPassword().equals(password)) {
//             return Optional.empty();
//         }
//         return optUser;
//     }

//     public User create(User user) {
//         return userRepository.save(user);
//     }
    
// }
