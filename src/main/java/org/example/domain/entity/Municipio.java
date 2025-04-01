package org.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static org.example.validators.StatusValidator.validarStatus;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "TB_MUNICIPIO")
public class Municipio {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceMunicipioGenerator")
    @SequenceGenerator(name = "sequenceMunicipioGenerator", sequenceName = "SEQUENCE_MUNICIPIO", allocationSize = 1)
    @Column(name = "CODIGO_MUNICIPIO")
    private Integer codigoMunicipio;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "STATUS")
    private Integer status;


    @ManyToOne
    @JoinColumn(name = "CODIGO_UF", referencedColumnName = "CODIGO_UF")
    private UF uf;

    @OneToMany(mappedBy = "municipio")
    private List<Bairro> bairros;

    public void setStatus(Integer status) {
        validarStatus(status);
        this.status = status;
    }

    public Municipio(UF uf, String nome, Integer status) {
        this.uf = uf;
        this.nome = nome;
        this.status = status;
    }

    public Municipio(UF uf, String nome, Integer status, Integer codigoMunicipio) {
        this.uf = uf;
        this.nome = nome;
        this.status = status;
        this.codigoMunicipio = codigoMunicipio;
    }
    @Override
    public String toString() {
        return "Municipio{" +
                "codigoMunicipio=" + codigoMunicipio + ", " +
                "nome=" + nome +
                "uf=" + (uf != null ? uf.getCodigoUF() : "null") +  // Apenas inclui o código ou algum dado básico
                '}';
    }
}
