package org.example.domain.repository;

import org.example.domain.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Integer> {
    @Query("SELECT m FROM Pessoa m WHERE m.codigoPessoa = :codigoPessoa")
    Pessoa obterPessoaPorId(@Param("codigoPessoa") Integer codigoPessoa);

    @Query("SELECT p FROM Pessoa p WHERE p.login = :login OR p.login IS NULL")
    Optional<Pessoa> findByLoginIgnoreCaseOrNull(@Param("login") String login);

    Optional<Pessoa> findByLoginIgnoreCase(String login);
}
