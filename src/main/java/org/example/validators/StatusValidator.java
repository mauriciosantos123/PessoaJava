package org.example.validators;

import org.example.domain.exception.RegraNegocioException;

public class StatusValidator {
    public static void validarStatus(Integer status) {
        if (status != null) {
            if (status != 1 && status != 2) {
                throw new RegraNegocioException("Status só pode ser 1 - ATIVADO ou 2 - DESATIVADO");
            }
        }
    }
}
