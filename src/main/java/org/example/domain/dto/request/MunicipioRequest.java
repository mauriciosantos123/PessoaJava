package org.example.domain.dto.request;

import lombok.*;
import org.example.domain.enums.StatusEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MunicipioRequest {
    private Integer codigoMunicipio;

    private Integer codigoUF;

    private String nome;

    private Integer status;
}
