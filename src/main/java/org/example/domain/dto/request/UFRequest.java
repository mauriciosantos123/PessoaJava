package org.example.domain.dto.request;

import lombok.*;
import org.example.domain.enums.StatusEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UFRequest {

    private String sigla;

    private String nome;

    private Integer status;
}
