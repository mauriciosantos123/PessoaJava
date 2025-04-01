package org.example.domain.repository;

import org.example.domain.entity.Municipio;
import org.example.domain.entity.UF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UFRepository extends JpaRepository<UF, Integer> {


    Optional<UF> findByNomeIgnoreCaseOrCodigoUFOrSiglaIgnoreCase(String nome, Integer codigoUF, String sigla);
    Optional<UF>  findByNomeIgnoreCase(String nome);
    Optional<UF>  findBySiglaIgnoreCase(String sigla);
    Optional<UF>  findByCodigoUF(Integer sigla);

    @Query("SELECT u FROM UF u WHERE u.codigoUF = :idUF")
    UF obterUFPorId(@Param("idUF") Integer idUF);
}
