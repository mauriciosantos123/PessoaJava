package org.example.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
@Getter
public class EnderecoMunicipioResponse {

    private Integer codigoMunicipio;

    private Integer codigoUF;

    private String nome;

    private Integer status;

    private UFResponse uf;
}
