package org.example.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.request.UFRequest;
import org.example.domain.entity.UF;
import org.example.domain.exception.RegraNegocioException;
import org.example.domain.repository.UFRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UFService {
    private final UFRepository ufRepository;


    public List<UF> salvar(UFRequest UFRequest) {
        UF uf = mapperUF(UFRequest);
        validarCampos(uf);
        salvarUF(uf);
        return ufRepository.findAll();
    }


    public Object buscarUf(String sigla,
                           String nome,
                           Integer codigoUF,
                           Integer status) {
        try {
            var filtro = new UF();
            filtro.setNome(nome);
            filtro.setSigla(sigla);
            filtro.setCodigoUF(codigoUF);
            filtro.setStatus(status);
            Sort sort = Sort.by(Sort.Direction.ASC, "codigoUF");
            Example<UF> example = getExample(filtro, sort);

            if (filtro.hasAnyFieldSet()) {
                var result = ufRepository.findOne(example);

                return result.isPresent() ? result : result.map(List::of).orElseGet(List::of);
            } else {
                return ufRepository.findAll(example);
            }
        } catch (Exception ex) {
            throw new RegraNegocioException("Não foi possível consultar UF no banco de dados.");
        }
    }


    public List<UF> atualizar(UF uf) {
        validarCampos(uf);
        ufRepository.findById(uf.getCodigoUF())
                .map(ufExistente -> {
                    uf.setCodigoUF(ufExistente.getCodigoUF());
                    ufRepository.save(uf);
                    return ufExistente;
                }).orElseThrow(() -> new RegraNegocioException("Não foi possível alterar UF no banco de dados."));

        return ufRepository.findAll();
    }


    //Privates
    private static Example<UF> getExample(UF filtro, Sort sort) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<UF> example = Example.of(filtro, matcher);
        return example;
    }

    private UF mapperUF(UFRequest UFRequest) {
        UF uf = new UF();
        uf.setStatus(UFRequest.getStatus());
        uf.setNome(UFRequest.getNome());
        uf.setSigla(UFRequest.getSigla());
        return uf;
    }

    private void validarCampos(UF uf) {
        if (uf.getNome() == null || uf.getNome().trim().isEmpty())
            throw new RegraNegocioException("O Campo Nome deve ser preenchido");

        if (uf.getSigla() == null || uf.getSigla().isEmpty()) {
            throw new RegraNegocioException("O Campo Sigla deve ser preenchido");
        } else if (uf.getSigla().length() != 2) {
            throw new RegraNegocioException("O Campo Sigla deve ter apenas 2 Caracteres");
        } else if (!uf.getSigla().matches("[A-Za-z]{2}")) {
            throw new RegraNegocioException("O Campo Sigla deve conter apenas letras e não deve ter números");
        }
        if (uf.getStatus() == null)
            throw new RegraNegocioException("O Campo Status deve ser preenchido");

        validarUFExiste(uf);

    }

    private void validarUFExiste(UF uf) {

        Optional<UF> resultByNome = ufRepository.findByNomeIgnoreCase(uf.getNome());
        if (resultByNome.isPresent()) {
            if (!resultByNome.get().getCodigoUF().equals(uf.getCodigoUF())) {
                throw new RegraNegocioException("Já existe uma UF com o nome \"" + uf.getNome() + "\"");
            }
        }

        Optional<UF> resultBySigla = ufRepository.findBySiglaIgnoreCase(uf.getSigla());
        if (resultBySigla.isPresent()) {
            if (!resultBySigla.get().getCodigoUF().equals(uf.getCodigoUF())) {
                throw new RegraNegocioException("Já existe uma UF com a sigla \"" + uf.getSigla() + "\"");
            }
        }

        Optional<UF> resultByCodigoUF = ufRepository.findByCodigoUF(uf.getCodigoUF());
        if (resultByCodigoUF.isPresent()) {
            if (!resultByCodigoUF.get().getCodigoUF().equals(uf.getCodigoUF())) {
                throw new RegraNegocioException("Já existe uma UF com o código \"" + uf.getCodigoUF() + "\"");
            }
        }
    }

    private static void responseException(String reason) {
        throw new RegraNegocioException(reason);
    }

    private void salvarUF(UF uf) {
        try {
            uf.setSigla(uf.getSigla().toUpperCase());
            ufRepository.save(uf);
        } catch (Exception ex) {
           throw new RegraNegocioException("não foi possivel salvar uf");
        }
    }

}
