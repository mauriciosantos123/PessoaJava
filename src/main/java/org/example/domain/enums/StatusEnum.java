package org.example.domain.enums;

public enum StatusEnum {
    ATIVADO(1),
    DESATIVADO(2);

    private final int codigo;

    StatusEnum(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

}
