package lazizbek.uz.app_news.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginDto {
    @NotNull(message = "Username bo'sh bo'lmasin")
    private String username;

    @NotNull(message = "Parol bo'sh bo'lmasin")
    private String password;
}
