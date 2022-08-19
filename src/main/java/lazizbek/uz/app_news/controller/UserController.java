package lazizbek.uz.app_news.controller;

import lazizbek.uz.app_news.aop.CheckPermission;
import lazizbek.uz.app_news.entity.User;
import lazizbek.uz.app_news.payload.ApiResponse;
import lazizbek.uz.app_news.payload.UserDto;
import lazizbek.uz.app_news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserService userService;

    @CheckPermission(permission = "ADD_USER")
    @PostMapping
    public HttpEntity<?> add(@Valid @RequestBody UserDto userDto) {
        ApiResponse apiResponse = userService.add(userDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission(permission = "DELETE_USER")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        ApiResponse apiResponse = userService.delete(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }


    @CheckPermission(permission = "VIEW_USERS")
    @GetMapping
    public HttpEntity<?> get() {
        List<User> users = userService.get();
        return ResponseEntity.status(users.size() != 0 ? 200 : 409).body(users);
    }


    @CheckPermission(permission = "VIEW_USERS")
    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return ResponseEntity.status(user != null ? 200 : 409).body(user);
    }

    @CheckPermission(permission = "EDIT_USER")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        ApiResponse apiResponse = userService.edit(id, userDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
