package com.example.examendi.utlity;

import com.example.examendi.domain.Cliente;
import lombok.Getter;
import lombok.Setter;

public class Session {
    @Getter
    @Setter
    private static Cliente currentCliente;

}
