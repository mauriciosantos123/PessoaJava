package org.example.domain.dto.request;

import jakarta.persistence.Column;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EnderecoRequest {

    private Integer codigoEndereco;

    private Integer codigoBairro;

    private String nomeRua;

    private String numero;

    private String complemento;

    private String cep;
}
