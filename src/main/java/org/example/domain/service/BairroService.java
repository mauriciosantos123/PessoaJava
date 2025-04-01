package org.example.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.request.BairroRequest;
import org.example.domain.dto.request.MunicipioRequest;
import org.example.domain.dto.response.BairroResponse;
import org.example.domain.dto.response.MunicipioResponse;
import org.example.domain.entity.Bairro;
import org.example.domain.entity.Municipio;
import org.example.domain.exception.RegraNegocioException;
import org.example.domain.repository.BairroRepository;
import org.example.domain.repository.MunicipioRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BairroService {
    private final BairroRepository bairroRepository;
    private final MunicipioRepository municipioRepository;

    public Object buscarBairro(Integer codigoBairro,
                               Integer codigoMunicipio,
                               String nome,
                               Integer status) {
        try {
            var filtro = new Bairro();
            filtro.setNome(nome);
            filtro.setMunicipio(codigoMunicipio != null ? obterMunicipio(codigoMunicipio) : null);
            filtro.setCodigoBairro(codigoBairro);
            filtro.setStatus(status);
            Sort sort = Sort.by(Sort.Direction.ASC, "nome");
            Example<Bairro> example = getExample(filtro, sort);


            if (filtro.getCodigoBairro() != null) {
                var result = bairroRepository.findOne(example);
                return result.map(this::convertToResponse);
            } else {
                return bairroRepository.findAll(example).stream()
                        .map(this::convertToResponse)
                        .collect(Collectors.toList());
            }

        } catch (Exception ex) {
            throw new RegraNegocioException("Não foi possível consultar bairro no banco de dados.");
        }
    }


    public List<BairroResponse> salvarBairro(BairroRequest bairroDTO) {
        try {
            Municipio municipio = obterMunicipio(bairroDTO.getCodigoMunicipio());

            Bairro bairro = new Bairro(municipio, bairroDTO.getNome(), bairroDTO.getStatus());
            validarBairro(bairro);
            bairroRepository.save(bairro);
            return bairroRepository.findAll().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RegraNegocioException("Não foi possível consultar bairro no banco de dados.");
        }
    }


    public List<BairroResponse> atualizarBairro(BairroRequest bairroDTO) {
        Bairro bairro = new Bairro(bairroDTO.getCodigoBairro(),
                obterMunicipio(bairroDTO.getCodigoMunicipio()),
                bairroDTO.getNome(),
                bairroDTO.getStatus());
        validarBairro(bairro);
        bairroRepository.findById(bairro.getCodigoBairro())
                .map(bairroExistente -> {
                    bairro.setCodigoBairro(bairroExistente.getCodigoBairro());
                    bairroRepository.save(bairro);
                    return bairroExistente;
                }).orElseThrow(() -> new RegraNegocioException("Não foi possível alterar Municipio no banco de dados."));

        return bairroRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }


    private static Example<Bairro> getExample(Bairro filtro, Sort sort) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Bairro> example = Example.of(filtro, matcher);
        return example;
    }

    private static void responseException(String reason) {
        throw new RegraNegocioException(reason);
    }

    private void validarBairro(Bairro bairro) {
        if (bairro.getNome() == null || bairro.getNome().trim().isEmpty())
            throw new RegraNegocioException("O Campo Nome deve ser preenchido");

        if (bairro.getStatus() == null)
            throw new RegraNegocioException("O Campo Status deve ser preenchido");

        validarBairroExiste(bairro);
    }

    private void validarBairroExiste(Bairro bairro) {
        Optional<Bairro> municipioExistente = bairroRepository
                .findByMunicipioCodigoMunicipioAndNomeIgnoreCase(bairro.getMunicipio().getCodigoMunicipio(), bairro.getNome());
        Integer codigoMunicipio = bairro.getMunicipio().getCodigoMunicipio();
        if (codigoMunicipio == null) {
            if (municipioExistente.isPresent()) {
                throw new RegraNegocioException("Já existe um bairro com o nome \"" + bairro.getNome() +
                        "\" no Municipio" + codigoMunicipio + " especificada.");
            }
        } else {
            if (municipioExistente.isPresent() &&
                    !municipioExistente.get().getMunicipio().getCodigoMunicipio().equals(codigoMunicipio)) {
                throw new RegraNegocioException("Já existe um bairro com o nome \"" + bairro.getNome() +
                        "\" no municipio especificada.");
            }

        }
    }

    private BairroResponse convertToResponse(Bairro bairro) {
        return new BairroResponse(
                bairro.getCodigoBairro(),
                bairro.getMunicipio().getCodigoMunicipio(),
                bairro.getNome(),
                bairro.getStatus()
        );
    }

    private Municipio obterMunicipio(Integer id) {
        Municipio municipio = municipioRepository.obterMunicioPorId(id);
        if (municipio != null) {
            return municipio;
        } else {
            throw new RegraNegocioException("Não existe o municipio informado");
        }
    }


}
