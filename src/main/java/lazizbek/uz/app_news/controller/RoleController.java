package lazizbek.uz.app_news.controller;

import lazizbek.uz.app_news.aop.CheckPermission;
import lazizbek.uz.app_news.entity.Role;
import lazizbek.uz.app_news.payload.ApiResponse;
import lazizbek.uz.app_news.payload.RoleDto;
import lazizbek.uz.app_news.payload.UserDto;
import lazizbek.uz.app_news.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    RoleService roleService;

    @CheckPermission(permission = "ADD_ROLE")
    @PostMapping
    public HttpEntity<?> addRole(@Valid @RequestBody RoleDto roleDto){
        ApiResponse apiResponse = roleService.addRole(roleDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @CheckPermission(permission = "EDIT_ROLE")
    @PutMapping("/{id}")
    public HttpEntity<?> editRole(@PathVariable Long id, @Valid @RequestBody RoleDto roleDto) {
        ApiResponse apiResponse = roleService.editRole(id, roleDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

//    @CheckPermission(permission = "DELETE_ROLE")
//    @DeleteMapping("{/id}")
//    public HttpEntity<?> deleteRole(@PathVariable Long id){
//        ApiResponse apiResponse = roleService.deleteRole(id);
//        return  ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
//    }

    @CheckPermission(permission = "VIEW_ROLE")
    @GetMapping
    public ResponseEntity<?> get() {
        List<Role> roles = roleService.get();
        return ResponseEntity.status(roles.size() != 0 ? 200 : 409).body(roles);
    }


    @CheckPermission(permission = "VIEW_ROLE")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Role role = roleService.getById(id);
        return ResponseEntity.status(role != null ? 200 : 409).body(role);
    }

}
