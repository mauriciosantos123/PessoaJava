package org.example.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.entity.Endereco;

import java.util.List;

@Data
@AllArgsConstructor
@Setter
@Getter
public class PessoaResponse {

    private Integer codigoPessoa;

    private String nome;

    private String sobrenome;

    private Integer idade;

    private String login;

    private String senha;

    private Integer status;

    private List<EnderecoResponse> enderecos;

    public PessoaResponse() {

    }
}
