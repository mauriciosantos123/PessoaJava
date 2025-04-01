package org.example.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.domain.exception.RegraNegocioException;

import java.util.List;
import java.util.Set;

import static org.example.validators.StatusValidator.validarStatus;


@Entity
@Table(name = "TB_UF")
@Data
@Getter
@Setter
@ToString
public class UF {

    @Id
    @Column(name = "CODIGO_UF")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceUfGenerator")
    @SequenceGenerator(name = "sequenceUfGenerator", sequenceName = "SEQUENCE_UF", allocationSize = 1)
    private Integer codigoUF;

    @Column(name = "SIGLA", length = 2)
    private String sigla;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "STATUS")
    private Integer status;

    @JsonIgnore
    @OneToMany(mappedBy = "uf")
    private List<Municipio> municipios;

    public boolean hasAnyFieldSet() {
        return (nome != null && !nome.isEmpty()) ||
                (sigla != null && !sigla.isEmpty()) ||
                (codigoUF != null);
    }

    public void setStatus(Integer status) {
        validarStatus(status);
        this.status = status;
    }
    @Override
    public String toString() {
        return "UF{" +
                "codigoUf=" + codigoUF + ", " +
                "nome=" + nome +
//                // Evite imprimir a lista completa de municipios
//                "municipiosCount=" + (municipios != null ? municipios.size() : 0) +
                '}';
    }

}
