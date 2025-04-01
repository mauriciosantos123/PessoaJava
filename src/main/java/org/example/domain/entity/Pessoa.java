package org.example.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

import static org.example.validators.StatusValidator.validarStatus;

@Entity
@Data
@Table(name = "TB_PESSOA")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequencePessoaGenerator")
    @SequenceGenerator(name = "sequencePessoaGenerator", sequenceName = "SEQUENCE_PESSOA", allocationSize = 1)
    @Column(name = "CODIGO_PESSOA")
    private Integer codigoPessoa;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "SOBRENOME")
    private String sobrenome;

    @Column(name = "IDADE")
    private Integer idade;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "SENHA")
    private String senha;

    @Column(name = "STATUS")
    private Integer status;

    @OneToMany(mappedBy = "pessoa")
    private List<Endereco> enderecos;

    public Pessoa(String nome, String sobrenome, Integer idade, String login, String senha, Integer status, List<Endereco> enderecos) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.idade = idade;
        this.status = status;
        this.login = login;
        this.senha = senha;
        this.enderecos = enderecos;
    }



    public void setStatus(Integer status) {
        validarStatus(status);
        this.status = status;
    }

    @Override
    public String toString(){
        return "Pessoa{" +
            "codigoPessoa=" + codigoPessoa + ", " +
            "nome=" + nome +
            '}';
    }
}
