package org.example.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class UFResponse {

    private Integer codigoUF;

    private String sigla;

    private String nome;

    private Integer status;
}
