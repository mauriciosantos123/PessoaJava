package org.example.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.entity.Endereco;

@Data
@AllArgsConstructor
@Setter
@Getter
public class EnderecoBairroResponse {

    private Integer codigoBairro;

    private Integer codigoMunicipio;

    private String nome;

    private Integer status;

    private EnderecoMunicipioResponse municipio;
}
