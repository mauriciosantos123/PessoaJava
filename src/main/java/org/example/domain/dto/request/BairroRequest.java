package org.example.domain.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class BairroRequest {

    private Integer codigoBairro;

    private Integer codigoMunicipio;

    private String nome;

    private Integer status;

}
