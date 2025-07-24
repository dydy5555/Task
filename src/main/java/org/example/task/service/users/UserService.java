package org.example.task.service.users;

import jakarta.transaction.Transactional;
import org.example.task.exception.IllegalPasswordException;
import org.example.task.exception.NotFoundExceptionClass;
import org.example.task.exception.UserAlreadyExistsException;
import org.example.task.payload.user.ChangePassword;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.example.task.common.Provider;
import org.example.task.common.Role;
import org.example.task.config.jwt.JwtUtil;
import org.example.task.payload.user.UserLogin;
import org.example.task.payload.user.UserRegister;
import org.example.task.payload.user.UserLoginResponse;
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
        Optional<Users> userOptional = userRepository.findByPhoneNumberOrGmailAndProvider(phoneNumber,phoneNumber, Provider.valueOf(provider));
        return userOptional.orElseThrow(() -> new NotFoundExceptionClass("User not found with the provided Gmail and phone number"));
    }

    @Transactional
    public UserLoginResponse registerUser(UserRegister request) {
        if(request.getProvider().equals(Provider.CREDENTIAL)){
            validateUserRegistration(request);
        }
        Users saveUser = setUserDetails(request);
        request.setToken(utils.generateToken(saveUser));
        request.setStatus(true);
        return buildUserRegisterResponse(saveUser);
    }

    public UserLoginResponse loginUser(UserLogin request) {
        String phoneOrEmail = request.getPhoneOrGmail();

        Users user = userRepository.findByPhoneNumberOrGmailAndProvider(
                        phoneOrEmail, phoneOrEmail, Provider.CREDENTIAL)
                .orElseThrow(() -> new NotFoundExceptionClass("Invalid phone number or email"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new NotFoundExceptionClass("Invalid password");
        }
        if(!user.getStatus()){
            throw new NotFoundExceptionClass("Invalid phone number or password");
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

    private void validateUserRegistration(UserRegister request) {
        if (userRepository.existsByGmailAndProvider(request.getGmail(), Provider.CREDENTIAL)) {
            throw new UserAlreadyExistsException("A user with this email already exists.");
        }

        if (userRepository.existsByPhoneNumberAndProvider(request.getPhoneNumber(), Provider.CREDENTIAL)) {
            throw new UserAlreadyExistsException("A user with this phone number already exists.");
        }
    }

    public Boolean changePassword (ChangePassword changePassword){
        Optional<Users> userOptional = userRepository.findByPhoneNumberOrGmailAndProvider(changePassword.getPhoneNumber(),changePassword.getPhoneNumber(), Provider.CREDENTIAL);
        if (userOptional.isEmpty()) {
            throw new IllegalPasswordException("User not found");
        }
        Users user = userOptional.get();
        if (!passwordEncoder.matches(changePassword.getCurrentPassword(), user.getPassword())) {
            throw new IllegalPasswordException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        userRepository.save(user);
        return true;
    }
}
