package org.example.domain.repository;

import org.example.domain.entity.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {
    Optional<Municipio> findByUfCodigoUFAndNomeIgnoreCase(Integer codigoUF, String nome);

    @Query("SELECT m FROM Municipio m WHERE m.codigoMunicipio = :codigoMunicipio")
    Municipio obterMunicioPorId(@Param("codigoMunicipio") Integer codigoMunicipio);
}
