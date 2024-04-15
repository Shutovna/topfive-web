package com.shutovna.topfive.entities.payload;


import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPayload {
    @NotNull(message = "{ru.nikitos.msg.username.not_null}")
    @Size(min = 4, max = 50, message = "{ru.nikitos.msg.username.size}")
    private String username;
    @NotNull(message = "{ru.nikitos.msg.password.not_null}")
    //@Size(min = 3, max = 50, message = "{ru.nikitos.msg.password.size}")
    private String password;
    @NotNull(message = "{ru.nikitos.msg.password_repeat.not_null}")
    private String passwordRepeat;

    @AssertTrue(message = "{ru.nikitos.msg.password_repeat}")
    public boolean isPasswordEquals() {
        return password != null && password.equals(passwordRepeat);
    }
}
