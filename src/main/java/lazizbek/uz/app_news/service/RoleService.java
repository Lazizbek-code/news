package lazizbek.uz.app_news.service;

import lazizbek.uz.app_news.entity.Role;
import lazizbek.uz.app_news.payload.ApiResponse;
import lazizbek.uz.app_news.payload.RoleDto;
import lazizbek.uz.app_news.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;


    public ApiResponse addRole(RoleDto roleDto) {
        boolean name = roleRepository.existsByName(roleDto.getName());
        if (name) return new ApiResponse("Bunday nomdagi lavozim mavjud", false);

        Role role = new Role(
                roleDto.getName(),
                roleDto.getPermissionList(),
                roleDto.getDescription()
        );
        roleRepository.save(role);
        return new ApiResponse("Ma'lumot saqlandi", true);
    }

    public ApiResponse editRole(Long id, RoleDto roleDto) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        if (!optionalRole.isPresent()) {
            return new ApiResponse("Role topilmadi", false);
        }

        if (roleRepository.existsByNameAndIdNot(roleDto.getName(), id)) {
            return new ApiResponse("Role nomi allaqachon mavjud", false);
        }

        Role role = optionalRole.get();
        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());
        role.setPermissionList(roleDto.getPermissionList());
        roleRepository.save(role);
        return new ApiResponse("Role o'zgartirildi", true);
    }

    public ApiResponse deleteRole(Long id) {
        try {
            roleRepository.deleteById(id);
            return new ApiResponse("Ma'lumot o'chirildi", true);
        }catch (Exception e){
            return new ApiResponse("Xatolik", false);
        }
    }

    public List<Role> get() {
        return roleRepository.findAll();
    }

    public Role getById(Long id) {
        Optional<Role> optionalRole = roleRepository.findById(id);
        return optionalRole.orElse(null);
    }


}
