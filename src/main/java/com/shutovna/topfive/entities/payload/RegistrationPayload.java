package com.shutovna.topfive.entities.payload;

import com.shutovna.topfive.entities.User;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationPayload {

    @NotBlank(message = "{ru.shutovna.msg.user.username.not_null}")
    @Size(min = 3, max = 20, message = "{ru.shutovna.msg.user.username.size}")
    private String username;
    @NotBlank(message = "{ru.shutovna.msg.user.password.not_null}")
    @Size(min = 5, max = 20, message = "{ru.shutovna.msg.user.password.size}")
    private String password;
    @NotBlank(message = "{ru.shutovna.msg.user.password.confirm}")
    private String confirmPassword;


    @AssertTrue(message = "{ru.shutovna.msg.user.password.not_equal}")
    public boolean isPasswordsEqual() {
        return password.equals(confirmPassword);
    }

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(null, username, passwordEncoder.encode(password));
    }

}
