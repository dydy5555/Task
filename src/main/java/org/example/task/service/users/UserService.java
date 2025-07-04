package org.example.task.service.users;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.example.task.common.Provider;
import org.example.task.common.Role;
import org.example.task.config.jwt.JwtUtil;
import org.example.task.model.request.UserLogin;
import org.example.task.model.request.UserRegister;
import org.example.task.model.response.UserLoginResponse;
import org.example.task.model.user.Users;
import org.example.task.repository.users.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil utils;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, @Lazy JwtUtil utils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.utils = utils;
    }


    public Users getUserDetails(String phoneNumber, String provider) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            throw new RuntimeException("Phone number cannot be null or empty");
        }
        Optional<Users> userOptional = userRepository.findByPhoneNumberAndProvider(phoneNumber, Provider.valueOf(provider));
        return userOptional.orElseThrow(() -> new RuntimeException("User not found with the provided Gmail and phone number"));
    }

    @Transactional
    public UserLoginResponse registerUser(UserRegister request) {
//        if(request.getProvider().equals(Provider.CREDENTIAL)){
//            validateUserRegistration(request);
//        }
        Users saveUser = setUserDetails(request);
        request.setToken(utils.generateToken(saveUser));
        request.setStatus(true);
        return buildUserRegisterResponse(saveUser);
    }

    public UserLoginResponse loginUser(UserLogin request) {
        String phoneNumber = request.getPhoneNumber().startsWith("0") ?
                request.getPhoneNumber() : "0" + request.getPhoneNumber();
        Users user = userRepository.findByPhoneNumberAndProvider(phoneNumber, Provider.CREDENTIAL)
                .orElseThrow(() -> new RuntimeException("Invalid phone number or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid phone number or password");
        }
        if(!user.getStatus()){
            throw new RuntimeException("Invalid phone number or password");
        }
        return buildUserRegisterResponse(user);
    }

    private Users setUserDetails(UserRegister request) {
        Users user = new Users();
        user.setFullName(request.getFullName());
        user.setRole(Role.USER);
        user.setProvider(request.getProvider());
        user.setCreatedDate(ZonedDateTime.now());
        user.setModifiedDate(ZonedDateTime.now());
        user.setStatus(true);
        if (request.getProvider().equals(Provider.CREDENTIAL)) {
            return setCredentialUserDetails(user, request);}
        return new Users();
    }

    private Users setCredentialUserDetails(Users user, UserRegister request) {
//        OTPBot botRecord = telegramBotRepository.findByOtpCodeAndPhoneNumber(request.getOtpCode(),request.getPhoneNumber())
//                .orElseThrow(() -> new NotFoundExceptionClass("Otp Code is invalid"));
        user.setGmail(request.getGmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);
        return user;
    }

    private UserLoginResponse buildUserRegisterResponse(Users user) {
        UserLoginResponse response = new UserLoginResponse();
//        response.setTelegramVerify(telegramBotRepository.findByChatId(user.getChatId()).isPresent());
        response.setId(user.getId());
//        response.setProfileImage(Optional.ofNullable(user.getProfileImage()).orElse(""));
        response.setFullName(user.getFullName());
        response.setGmail(user.getGmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole());
        response.setChatId(user.getChatId());
        response.setProvider(user.getProvider());
        response.setToken(utils.generateToken(user));
        response.setStatus(true);
        response.setCreatedDate(user.getCreatedDate());
        response.setModifiedDate(user.getModifiedDate());
        response.setLoginTime(ZonedDateTime.now());
        return response;
    }
}
