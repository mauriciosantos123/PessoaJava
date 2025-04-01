    package org.example.domain.repository;

    import org.example.domain.entity.Bairro;
    import org.example.domain.entity.Municipio;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    import java.util.Optional;

    public interface BairroRepository extends JpaRepository<Bairro, Integer> {
        Optional<Bairro> findByMunicipioCodigoMunicipioAndNomeIgnoreCase(Integer codigoMunicipio, String nome);

        @Query("SELECT b FROM Bairro b WHERE b.codigoBairro = :codigoBairro")
        Bairro obterBairroPorId(@Param("codigoBairro") Integer codigoBairro);
    }

