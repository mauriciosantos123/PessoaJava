package org.example.domain.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EnderecoResponse {

    private Integer codigoEndereco;

    private Integer codigoPessoa;

    private Integer codigoBairro;

    private String nomeRua;

    private String numero;

    private String complemento;

    private String cep;

    private EnderecoBairroResponse bairro;

}
