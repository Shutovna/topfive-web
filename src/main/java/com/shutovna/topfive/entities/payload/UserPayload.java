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
    @NotNull(message = "{ru.shutovna.msg.username.not_null}")
    @Size(min = 4, max = 50, message = "{ru.shutovna.msg.username.size}")
    private String username;
    @NotNull(message = "{ru.shutovna.msg.password.not_null}")
    //@Size(min = 3, max = 50, message = "{ru.shutovna.msg.password.size}")
    private String password;
    @NotNull(message = "{ru.shutovna.msg.password_repeat.not_null}")
    private String passwordRepeat;

    @AssertTrue(message = "{ru.shutovna.msg.password_repeat}")
    public boolean isPasswordEquals() {
        return password != null && password.equals(passwordRepeat);
    }
}
