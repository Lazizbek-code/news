package lazizbek.uz.app_news.service;

import lazizbek.uz.app_news.entity.Role;
import lazizbek.uz.app_news.entity.User;
import lazizbek.uz.app_news.payload.ApiResponse;
import lazizbek.uz.app_news.payload.UserDto;
import lazizbek.uz.app_news.repository.RoleRepository;
import lazizbek.uz.app_news.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public ApiResponse add(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            return new ApiResponse("Bunday username mavhud", false);
        }

        Optional<Role> optionalRole = roleRepository.findById(userDto.getRoleId());
        if (!optionalRole.isPresent()) {
            return new ApiResponse("Role topilmadi", false);
        }
        Role role = optionalRole.get();

        User user = new User(
                userDto.getFullName(),
                userDto.getUsername(),
                userDto.getPassword(),
                role,
                true
        );
        User savedUser = userRepository.save(user);

        return new ApiResponse("Ma'lumot saqlandi",true);
    }

    public ApiResponse delete(Long id) {
        try {
            userRepository.deleteById(id);
            return new ApiResponse("O'chirildi", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik", false);
        }
    }


    public List<User> get() {
        return userRepository.findAll();
    }


    public User getById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }


    public ApiResponse edit(Long id, UserDto userDto) {
        if (userRepository.existsByUsernameAndIdNot(userDto.getUsername(), id)) {
            return new ApiResponse("Bunday username mavjud", false);
        }

        Optional<User> optionalUser = userRepository.findById(id);
        if (!optionalUser.isPresent()) {
            return new ApiResponse("User topilmadi", false);
        }

        Optional<Role> optionalRole = roleRepository.findById(userDto.getRoleId());
        if (!optionalRole.isPresent()) {
            return new ApiResponse("Role topilmadi", false);
        }
        Role role = optionalRole.get();

        User user = optionalUser.get();
        user.setFullName(userDto.getFullName());
        user.setPassword(userDto.getPassword());
        user.setRole(role);
        userRepository.save(user);
        return new ApiResponse("Ma'lumot o'zgartirildi", true);
    }
}
