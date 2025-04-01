package org.example.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
@Getter
public class BairroResponse {

    private Integer codigoBairro;

    private Integer codigoMunicipio;

    private String nome;

    private Integer status;
}
