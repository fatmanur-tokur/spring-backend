package com.fatmanurtokur.shopping.service.impl;

import com.fatmanurtokur.shopping.config.JwtTokenProvider;
import com.fatmanurtokur.shopping.dto.PasswordChangeDto;
import com.fatmanurtokur.shopping.dto.PasswordResetToken;
import com.fatmanurtokur.shopping.dto.UserDto;
import com.fatmanurtokur.shopping.entity.Roles;
import com.fatmanurtokur.shopping.entity.UserRoles;
import com.fatmanurtokur.shopping.entity.Users;
import com.fatmanurtokur.shopping.mapper.UserMapper;
import com.fatmanurtokur.shopping.repository.RolesRepository;
import com.fatmanurtokur.shopping.repository.UserRepository;
import com.fatmanurtokur.shopping.repository.UserRolesRepository;
import com.fatmanurtokur.shopping.service.IUserService;
import com.fatmanurtokur.shopping.validate.UserLoginValidate;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    private UserRepository userRepository;
    private RolesRepository rolesRepository;
    private UserRolesRepository userRolesRepository;
    private PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final Map<String, PasswordResetToken> tokenStore = new ConcurrentHashMap<>();


    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername()) || userRepository.existsByEmail(userDto.getEmail())) {
            return null;
        }
        Users users = UserMapper.mapToUsers(userDto);
        users.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Users savedUsers = userRepository.save(users);

        Roles role = rolesRepository.findByRoleName("USER");
        if(role!=null){
            UserRoles userRoles = new UserRoles();
            userRoles.setUser(savedUsers);
            userRoles.setRole(role);
            userRolesRepository.save(userRoles);
        }
        Set<Roles> roles = userRolesRepository.findAllByUser_UserId(savedUsers.getUserId()).stream()
                .map(UserRoles::getRole)
                .collect(Collectors.toSet());
        return UserMapper.mapToUserDto(savedUsers);

    }

    @Override
    public String login(UserLoginValidate loginValidate){

        Users user = userRepository.findByEmail(loginValidate.getEmail());

        if (user == null) {
            return null;
        }
        if(!passwordEncoder.matches(loginValidate.getPassword(), user.getPassword())){
            return null;
        }
        UserDetails userDetails = new User(user.getUsername(), user.getPassword(), Collections.emptyList());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
        return jwtTokenProvider.generateToken(authentication);

    }

    @Override
    public UserDto getUserByUsername(String username){
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            return null;        }
        return UserMapper.mapToUserDto(user);

    }

    @Override
    public UserDto getUserByEmail(String email){
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            return null;        }
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }

    @Override
    public boolean changePassword(String username, PasswordChangeDto passwordChangeDto) {
        Users user = userRepository.findByUsername(username);
        if (user==null){
            return false;
        }
        if(!passwordEncoder.matches(passwordChangeDto.getOldPassword(), user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    @Override
    public String generatePasswordResetCode(String email){
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        String code = String.valueOf(new Random().nextInt(9000) + 1000);
        tokenStore.put(email, new PasswordResetToken(code, System.currentTimeMillis()));
        Executors.newSingleThreadScheduledExecutor().schedule(() -> tokenStore.remove(email), 1, TimeUnit.MINUTES);
        return code;
    }

    @Override
    public boolean resetPassword(String email, String code, String newPassword){
        PasswordResetToken resetToken = tokenStore.get(email);
        if (resetToken == null || !resetToken.getCode().equals(code) || System.currentTimeMillis() - resetToken.getTimestamp() > 60000) {
            return false;
        }
        Users user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenStore.remove(email);
        return true;
    }
}
