package io.github.cwireset.tcc.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DiariaNaoPodeSerMenorqueUmException extends Exception {

    public DiariaNaoPodeSerMenorqueUmException() {
        super("Período inválido! O número mínimo de diárias precisa ser maior ou igual a 1.");
    }
}
