package org.example.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@Table(name = "TB_ENDERECO")
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceEnderecoGenerator")
    @SequenceGenerator(name = "sequenceEnderecoGenerator", sequenceName = "SEQUENCE_ENDERECO", allocationSize = 1)
    @Column(name = "CODIGO_ENDERECO")
    private Integer codigoEndereco;

    @Column(name = "NOME_RUA")
    private String nomeRua;

    @Column(name = "NUMERO")
    private String numero;

    @Column(name = "COMPLEMENTO")
    private String complemento;

    @Column(name = "CEP")
    private String cep;

    @ManyToOne
    @JoinColumn(name = "CODIGO_PESSOA", referencedColumnName = "CODIGO_PESSOA")
    private Pessoa pessoa;

    @ManyToOne
    @JoinColumn(name = "CODIGO_BAIRRO", referencedColumnName = "CODIGO_BAIRRO")
    private Bairro bairro;

    public Endereco(String numero, Bairro bairro, String nomeRua, String cep, String complemento) {
        this.numero = numero;
        this.cep = cep ;
        this.bairro = bairro;
        this.complemento = complemento;
        this.nomeRua = nomeRua;
    }

    public Endereco(String numero, Bairro bairro, String nomeRua, String cep, String complemento, Pessoa pessoa) {
        this.numero = numero;
        this.cep = cep ;
        this.bairro = bairro;
        this.complemento = complemento;
        this.nomeRua = nomeRua;
        this.pessoa = pessoa;
    }
    public Endereco(Integer codigoEndereco, String numero, Bairro bairro, String nomeRua, String cep, String complemento, Pessoa pessoa) {
        this.codigoEndereco = codigoEndereco;
        this.numero = numero;
        this.cep = cep ;
        this.bairro = bairro;
        this.complemento = complemento;
        this.nomeRua = nomeRua;
        this.pessoa = pessoa;
    }
}
