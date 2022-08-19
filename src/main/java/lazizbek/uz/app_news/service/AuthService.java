package lazizbek.uz.app_news.service;

import lazizbek.uz.app_news.entity.User;
import lazizbek.uz.app_news.exceptions.ResourceNotFoundException;
import lazizbek.uz.app_news.payload.ApiResponse;
import lazizbek.uz.app_news.payload.LoginDto;
import lazizbek.uz.app_news.payload.RegisterDto;
import lazizbek.uz.app_news.repository.RoleRepository;
import lazizbek.uz.app_news.repository.UserRepository;
import lazizbek.uz.app_news.security.JwtProvider;
import lazizbek.uz.app_news.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Lazy
    @Autowired
    PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    public ApiResponse registerUser(RegisterDto registerDto){
        if (!registerDto.getPassword().equals(registerDto.getPrePassword()))
            return new ApiResponse("Parollar mos emas", false);
        boolean username = userRepository.existsByUsername(registerDto.getUsername());
        if (username) return new ApiResponse("Bunday username avval ro'yhatdan o'tgan", false);
        User user = new User(
                registerDto.getFullName(),
                registerDto.getUsername(),
                passwordEncoder.encode(registerDto.getPassword()),
                roleRepository.findByName(AppConstants.USER).orElseThrow(()->new ResourceNotFoundException("Role","name",AppConstants.USER)),
                true
        );
        userRepository.save(user);
        return new ApiResponse("Muvaffaqiyatli ro'yhatdan o'tdingiz", true);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // login
    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getUsername(), loginDto.getPassword()));

            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(user.getUsername(), user.getRole());

            return new ApiResponse("Token", true, token);
        } catch (BadCredentialsException e) {
            return new ApiResponse("Parol yoki login xato", false);
        }
    }
}
