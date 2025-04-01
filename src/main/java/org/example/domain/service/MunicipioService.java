package org.example.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.request.MunicipioRequest;
import org.example.domain.dto.response.MunicipioResponse;
import org.example.domain.entity.Municipio;
import org.example.domain.entity.UF;
import org.example.domain.enums.StatusEnum;
import org.example.domain.exception.RegraNegocioException;
import org.example.domain.repository.MunicipioRepository;
import org.example.domain.repository.UFRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class MunicipioService {
    private final MunicipioRepository municipioRepository;
    private final UFRepository ufRepository;

    public Object buscarMunicipio(Integer codigoMunicipio,
                                  Integer codigoUF,
                                  String nome,
                                  Integer status) {
        try {
            var filtro = new Municipio();
            filtro.setNome(nome);
            filtro.setCodigoMunicipio(codigoMunicipio);
            if (codigoUF != null) {
                filtro.setUf(obterUF(codigoUF));
            }
            filtro.setStatus(status);
            Sort sort = Sort.by(Sort.Direction.ASC, "nome");
            Example<Municipio> example = getExample(filtro, sort);

            if (codigoMunicipio != null) {
                var result = municipioRepository.findOne(example);
                return result.map(this::convertToResponse)
                        .orElse(null);
            } else {
                List<Municipio> municipios = municipioRepository.findAll(example);
                if (municipios.isEmpty()) {
                    return null;
                }
                return municipios.stream()
                        .map(this::convertToResponse)
                        .collect(Collectors.toList());
            }
        } catch (Exception ex) {
            throw new RegraNegocioException("Não foi possível consultar Municipio no banco de dados.");
        }
    }

    public List<MunicipioResponse> salvarMunicipio(MunicipioRequest municipioDTO) {
        try {
            Municipio municipio = new Municipio(
                    obterUF(municipioDTO.getCodigoUF()),
                    municipioDTO.getNome(),
                    municipioDTO.getStatus());
            validarMunicipio(municipio);
            municipioRepository.save(municipio);
            return municipioRepository.findAll().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RegraNegocioException("Não foi possível salvar Municipio no banco de dados.");
        }
    }


    public List<MunicipioResponse> atualizarMunicipio(MunicipioRequest municipioDTO) {
        try {
            Municipio municipio = new Municipio(
                    obterUF(municipioDTO.getCodigoUF()),
                    municipioDTO.getNome(),
                    municipioDTO.getStatus(),
                    municipioDTO.getCodigoMunicipio());
            validarMunicipio(municipio);
            municipioRepository.findById(municipio.getCodigoMunicipio())
                    .map(municipioExistente -> {
                        municipio.setCodigoMunicipio(municipioExistente.getCodigoMunicipio());
                        municipioRepository.save(municipio);
                        return municipioExistente;
                    }).orElseThrow(() -> new RegraNegocioException("Não foi possível alterar Municipio no banco de dados."));

            return municipioRepository.findAll().stream()
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RegraNegocioException("Não foi possível atualizar Municipio no banco de dados.");
        }
    }

    private void validarMunicipio(Municipio municipio) {
        if (municipio.getNome() == null || municipio.getNome().trim().isEmpty())
            throw new RegraNegocioException("O Campo Nome deve ser preenchido");

        if (municipio.getStatus() == null)
            throw new RegraNegocioException("O Campo Status deve ser preenchido");

        validarMunicipioExiste(municipio);
    }

    private void validarMunicipioExiste(Municipio municipio) {
        Optional<Municipio> municipioExistente = municipioRepository
                .findByUfCodigoUFAndNomeIgnoreCase(municipio.getUf().getCodigoUF(), municipio.getNome());

        if (municipio.getCodigoMunicipio() == null) {
            if (municipioExistente.isPresent()) {
                throw new RegraNegocioException("Já existe um município com o nome \"" + municipio.getNome() +
                        "\" na UF " + municipio.getUf().getCodigoUF() + " especificada.");
            }
        } else {
            if (municipioExistente.isPresent()) {
                if (!municipioExistente.get().getCodigoMunicipio().equals(municipio.getCodigoMunicipio())) {
                    throw new RegraNegocioException("Já existe um município com o nome \"" + municipio.getNome() +
                            "\" na UF " + municipio.getUf().getCodigoUF() + " especificada.");
                }
            }
        }
    }


    private static Example<Municipio> getExample(Municipio filtro, Sort sort) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Municipio> example = Example.of(filtro, matcher);
        return example;
    }

    private static void responseException(String reason) {
        throw new RegraNegocioException(reason);
    }

    private Integer getStatus(StatusEnum status) {
        return status.getCodigo();
    }

    private MunicipioResponse convertToResponse(Municipio municipio) {
        return new MunicipioResponse(
                municipio.getCodigoMunicipio(),
                municipio.getUf().getCodigoUF(),
                municipio.getNome(),
                municipio.getStatus()
        );
    }

    private UF obterUF(Integer idUF) {
        UF uf = ufRepository.obterUFPorId(idUF);
        if (uf != null) {
            return uf;
        } else {
            throw new RegraNegocioException("Não existe a UF informada");
        }
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
