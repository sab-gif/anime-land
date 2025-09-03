package com.explore.anime_land.users;

import com.explore.anime_land.exceptions.EntityAlreadyExistsException;
import com.explore.anime_land.security.CustomUserDetail;
import com.explore.anime_land.users.dto.UserMapper;
import com.explore.anime_land.users.dto.UserRegisterRequest;
import com.explore.anime_land.users.dto.UserResponse;
import com.explore.anime_land.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
    return userRepository.findAll()
            .stream()
            .map(UserMapper::toDto)
            .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserByIdAdmin(Long id) {
        return getUserById(id);
    }

    @PreAuthorize("isAuthenticated()")
    public UserResponse getOwnUser(Long id){
        return getUserById(id);
    }

    private UserResponse getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), "id", id.toString()));
        return UserMapper.toDto(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), "username", username));
    }

    public UserResponse addUser(UserRegisterRequest request) {
        return addUserByRole(request, Role.USER);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse addAmin(UserRegisterRequest request) {
        return addUserByRole(request, Role.ADMIN);
    }

    private UserResponse addUserByRole(UserRegisterRequest request, Role role) {
        if (userRepository.existsByUsername(request.username())){
            throw new EntityAlreadyExistsException(User.class.getSimpleName(), "username", request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new EntityAlreadyExistsException(User.class.getSimpleName(), "email", request.email());
        }
        String encodedPassword = passwordEncoder.encode(request.password());
        User user = UserMapper.toEntity(request, role);
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return UserMapper.toDto(user);
    }

    @PreAuthorize("isAuthenticated()")
    public UserResponse updateOwnUser(Long id, UserRegisterRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), "id", id.toString()));
        if(!existingUser.getUsername().equals(request.username())) {
            if (userRepository.findByUsername(request.username()).isPresent()) {
                throw new EntityAlreadyExistsException(User.class.getSimpleName(), "username", request.username());
            }
        }
        if (!existingUser.getEmail().equals(request.email())) {
            if (userRepository.findByEmail(request.email()).isPresent()) {
                throw new EntityAlreadyExistsException(User.class.getSimpleName(), "email", request.email());
            }
        }
        existingUser.setUsername(request.username());
        existingUser.setEmail(request.email());
        existingUser.setPassword(passwordEncoder.encode(request.password()));
        User updatedUser = userRepository.save(existingUser);
        return UserMapper.toDto(updatedUser);
    }

    @PreAuthorize("isAuthenticated()")
    public String deleteOwnUser(Long id) {
        return deleteUserById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUserByIdAdmin(Long id) {
        return deleteUserById(id);
    }

    private String deleteUserById(Long id) {
        if(!userRepository.existsById(id)) {
            throw  new EntityNotFoundException(User.class.getSimpleName(), "id", id.toString());
        }
        userRepository.deleteById(id);
        return "User with id " + id + " deleted successfully";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new CustomUserDetail(userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), "username", username)));
    }


}
