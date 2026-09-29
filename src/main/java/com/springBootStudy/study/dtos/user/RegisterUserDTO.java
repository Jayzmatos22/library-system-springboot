package com.springBootStudy.study.dtos.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserDTO(

        @NotBlank(message = "E-mail obrigatório!")
        @Email(message = "E-mail inválido")
        @Size(max = 150, message = "E-mail deve conter no máximo 150 caracteres!")
        String email,

        @NotBlank(message = "Nome obrigatório!")
        @Size(min = 3, max = 200, message = "Nome deve conter entre 3 e 200 caracteres")
        String name,

        @NotBlank(message = "Senha obrigatória!")
        @Size(min = 8, max = 72, message = "Senha deve conter entre 8 e 72 caracteres")
        String password

) {
}
