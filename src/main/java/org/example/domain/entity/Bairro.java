package org.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.example.domain.enums.StatusEnum;

import java.util.List;

import static org.example.validators.StatusValidator.validarStatus;

@Entity
@Data
@Table(name = "TB_BAIRRO")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Bairro {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceBairroGenerator")
    @SequenceGenerator(name = "sequenceBairroGenerator", sequenceName = "SEQUENCE_BAIRRO", allocationSize = 1)
    @Column(name = "CODIGO_BAIRRO")
    private Integer codigoBairro;


    @ManyToOne
    @JoinColumn(name = "CODIGO_MUNICIPIO", referencedColumnName = "CODIGO_MUNICIPIO")
    private Municipio municipio;

    //private Integer codigoMunicipio;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "STATUS")
    private Integer status;


    @OneToMany(mappedBy = "bairro")
    @JsonIgnore
    private List<Endereco> enderecos;


    public Bairro(Municipio codigoMunicipio, String nome, Integer status) {

        this.municipio = codigoMunicipio;
        this.nome = nome;
        this.status = status;

    }

    public Bairro(Integer codigoBairro, Municipio codigoMunicipio, String nome, Integer status) {

        this.codigoBairro = codigoBairro;
        this.municipio = codigoMunicipio;
        this.nome = nome;
        this.status = status;

    }

    public void setStatus(Integer status) {
        validarStatus(status);
        this.status = status;
    }

    @Override
    public String toString() {
        return "Bairro {nome=" + nome +
                ", codigoBairro=" + codigoBairro + "}";
    }
}
