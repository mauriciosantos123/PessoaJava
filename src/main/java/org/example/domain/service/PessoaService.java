package org.example.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.dto.request.MunicipioRequest;
import org.example.domain.dto.request.PessoaRequest;
import org.example.domain.dto.response.*;
import org.example.domain.entity.*;
import org.example.domain.exception.RegraNegocioException;
import org.example.domain.repository.BairroRepository;
import org.example.domain.repository.EnderecoRepository;
import org.example.domain.repository.MunicipioRepository;
import org.example.domain.repository.PessoaRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PessoaService {
    private final PessoaRepository pessoaRepository;
    private final BairroRepository bairroRepository;
    private final EnderecoRepository enderecoRepository;
    private final EnderecoService enderecoService;
    private final MunicipioRepository municipioRepository;

    public Object buscarPessoa(Integer codigoPessoa,
                               String login,
                               Integer status) {

        var filtro = new Pessoa();
        filtro.setLogin(login);
        filtro.setCodigoPessoa(codigoPessoa);
        filtro.setStatus(status);
        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        Example<Pessoa> example = getExample(filtro, sort);

        if (filtro.getCodigoPessoa() != null) {
            var result = pessoaRepository.findOne(example);
            if (result.isPresent()) {
                return result.map(pessoa -> convertToResponse(pessoa, codigoPessoa != null || codigoPessoa > 0));
            } else {
                return new ArrayList<>();
            }
        } else {

            return pessoaRepository.findAll(example).stream()
                    .map(pessoa -> convertToResponse(pessoa, false))
                    .collect(Collectors.toList());
        }
    }


    public List<PessoaResponse> salvarPessoa(PessoaRequest pessoaDTO) {
        try {

            Pessoa pessoa = new Pessoa(pessoaDTO.getNome(),
                    pessoaDTO.getSobrenome(),
                    pessoaDTO.getIdade(),
                    pessoaDTO.getLogin(),
                    pessoaDTO.getSenha(),
                    pessoaDTO.getStatus(),
                    listaEnderecos(pessoaDTO)
            );
            validarCampos(pessoa);
            validarSeExiste(pessoa);
            Pessoa pessoaSalva = pessoaRepository.save(pessoa);
            for (var enderecos : pessoa.getEnderecos()) {
                enderecos.setBairro(bairroRepository.obterBairroPorId(enderecos.getBairro().getCodigoBairro()));
                enderecos.getBairro().setMunicipio(municipioRepository.obterMunicioPorId(enderecos.getBairro().getMunicipio().getCodigoMunicipio()));
                enderecos.setPessoa(pessoaSalva);
                enderecoService.salvar(enderecos);
            }
            return pessoaRepository.findAll().stream()
                    .map(pessoaEx -> convertToResponse(pessoaEx, false))
                    .collect(Collectors.toList());
        } catch (RegraNegocioException ex) {
            throw new RegraNegocioException(ex.getMessage());
        } catch (Exception ex) {
            throw new RegraNegocioException("Erro inesperado: " + ex.getMessage());
        }
    }

    private List<Endereco> listaEnderecos(PessoaRequest pessoaDTO) {
        if (pessoaDTO.getEnderecos() != null) {
            List<Endereco> lista = new ArrayList<>();

            for (var enderecoDTO : pessoaDTO.getEnderecos()) {
                Bairro b = bairroRepository.obterBairroPorId(enderecoDTO.getCodigoBairro());
                if (b == null) {
                    throw new RegraNegocioException("Não existe um bairro com o ID informado ! ");
                } else {

                    Endereco endereco = new Endereco(enderecoDTO.getNumero(), b, enderecoDTO.getNomeRua(), enderecoDTO.getCep(), enderecoDTO.getComplemento());
                    if (enderecoDTO.getCodigoBairro() != null)
                        endereco.setCodigoEndereco(enderecoDTO.getCodigoEndereco());
                    lista.add(endereco);
                }
            }
            return lista;
        }
        return new ArrayList<>();
    }


    public List<PessoaResponse> atualizarPessoa(PessoaRequest pessoaDTO) {
        try {
            Pessoa pessoa = new Pessoa(
                    pessoaDTO.getCodigoPessoa()
                    , pessoaDTO.getNome()
                    , pessoaDTO.getSobrenome()
                    , pessoaDTO.getIdade()
                    , pessoaDTO.getLogin()
                    , pessoaDTO.getSenha()
                    , pessoaDTO.getStatus()
                    , listaEnderecos(pessoaDTO));

            Pessoa pessoaExistente = pessoaRepository.findById(pessoa.getCodigoPessoa())
                    .orElseThrow(() -> new RegraNegocioException("Pessoa não encontrada no banco de dados."));

            pessoaExistente.setNome(pessoa.getNome());
            pessoaExistente.setSobrenome(pessoa.getSobrenome());
            pessoaExistente.setIdade(pessoa.getIdade());
            pessoaExistente.setLogin(pessoa.getLogin());
            pessoaExistente.setSenha(pessoa.getSenha());
            pessoaExistente.setStatus(pessoa.getStatus());
            atualizarEnderecos(pessoaExistente, pessoa.getEnderecos());

            pessoaRepository.save(pessoaExistente);
            return pessoaRepository.findAll().stream()
                    .map(p -> convertToResponse(p, false))
                    .collect(Collectors.toList());
        } catch (RegraNegocioException ex) {
            throw new RegraNegocioException(ex.getMessage());
        } catch (Exception ex) {
            throw new RegraNegocioException("Erro inesperado: " + ex.getMessage());
        }
    }

    @Transactional
    private void atualizarEnderecos(Pessoa pessoaExistente, List<Endereco> novosEnderecos) {
        try {
            List<Endereco> enderecosAtuais = pessoaExistente.getEnderecos();

            List<Endereco> enderecosParaRemover = enderecosAtuais.stream()
                    .filter(enderecoAtual ->
                            novosEnderecos.stream()
                                    .noneMatch(novo ->
                                            novo.getCodigoEndereco() != null &&
                                                    novo.getCodigoEndereco().equals(enderecoAtual.getCodigoEndereco())))
                    .toList();

            enderecosParaRemover.forEach(endereco -> {
                if (enderecoRepository.existsById(endereco.getCodigoEndereco())) {
                    enderecoRepository.delete(endereco);
                    pessoaExistente.getEnderecos().removeAll(enderecosParaRemover);
                } else {
                    throw new RegraNegocioException("Endereço não encontrado no banco de dados para remoção: " + endereco.getCodigoEndereco());
                }
            });


            for (Endereco novoEndereco : novosEnderecos) {
                enderecoService.validarCampos(novoEndereco);

                Endereco enderecoExistente = enderecosAtuais.stream()
                        .filter(e -> e.getCodigoEndereco().equals(novoEndereco.getCodigoEndereco()))
                        .findFirst()
                        .orElse(null);

                if (enderecoExistente != null) {

                    enderecoExistente.setNumero(novoEndereco.getNumero());
                    enderecoExistente.setNomeRua(novoEndereco.getNomeRua());
                    enderecoExistente.setCep(novoEndereco.getCep());
                    enderecoExistente.setComplemento(novoEndereco.getComplemento());
                    enderecoExistente.setBairro(novoEndereco.getBairro());
                    enderecoRepository.save(enderecoExistente); // Salva no banco
                } else {

                    novoEndereco.setPessoa(pessoaExistente);
                    enderecoRepository.save(novoEndereco);
                }
            }
        } catch (Exception ex) {
            throw new RegraNegocioException("Não foi possível atualizar pessoa no banco de dados.");
        }
    }

    private void validarCampos(Pessoa pessoa) {
        if (pessoa.getNome() == null || pessoa.getNome().trim().isEmpty())
            throw new RegraNegocioException("O Campo Nome deve ser preenchido");

        if (pessoa.getSobrenome() == null || pessoa.getSobrenome().trim().isEmpty())
            throw new RegraNegocioException("O Campo sobrenome deve ser preenchido");

        if (pessoa.getLogin() == null || pessoa.getLogin().trim().isEmpty())
            throw new RegraNegocioException("O Campo login deve ser preenchido");

        if (pessoa.getSenha() == null || pessoa.getSenha().trim().isEmpty())
            throw new RegraNegocioException("O Campo senha deve ser preenchido");

        if (pessoa.getIdade() == null || pessoa.getIdade() <= 0)
            throw new RegraNegocioException("O Campo idade deve ser preenchido e maior q zero");

        if (pessoa.getStatus() == null)
            throw new RegraNegocioException("O Campo Status deve ser preenchido");

        if (pessoa.getEnderecos() == null || pessoa.getEnderecos().isEmpty()) {
            throw new RegraNegocioException("Uma pessoa deve ter pelo menos um endereço.");
        }
    }

    private void validarSeExiste(Pessoa pessoa) {
        Optional<Pessoa> pessoaExistente = pessoaRepository
                .findByLoginIgnoreCaseOrNull(pessoa.getLogin());

        if (pessoa.getCodigoPessoa() == null) {
            if (pessoaExistente.isPresent()) {
                throw new RegraNegocioException("Já existe login \"" + pessoa.getLogin());
            }
        } else {
            if (pessoaExistente.isPresent() &&
                    !pessoaExistente.get().getCodigoPessoa().equals(pessoa.getCodigoPessoa())) {
                throw new RegraNegocioException("Já existe um login com o nome \"" + pessoa.getLogin());
            }

        }
    }

    private static Example<Pessoa> getExample(Pessoa filtro, Sort sort) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        return Example.of(filtro, matcher);
    }

    private PessoaResponse convertToResponse(Pessoa pessoa, boolean isCodigo) {
        PessoaResponse pessoaResponse = new PessoaResponse();
        pessoaResponse.setCodigoPessoa(pessoa.getCodigoPessoa());
        pessoaResponse.setNome(pessoa.getNome());
        pessoaResponse.setIdade(pessoa.getIdade());
        pessoaResponse.setLogin(pessoa.getLogin());
        pessoaResponse.setSenha(pessoa.getSenha());
        pessoaResponse.setStatus(pessoa.getStatus());
        pessoaResponse.setSobrenome(pessoa.getSobrenome());
        List<EnderecoResponse> listaEnderecos = new ArrayList<>();

        if (isCodigo) {
            for (var endereco : pessoa.getEnderecos()) {
                listaEnderecos.add(convertEnderecoToResponse(endereco));
            }
        }
        pessoaResponse.setEnderecos(listaEnderecos);
        return pessoaResponse;
    }

    private EnderecoResponse convertEnderecoToResponse(Endereco endereco) {
        EnderecoResponse enderecoResponse = new EnderecoResponse();
        enderecoResponse.setCodigoEndereco(endereco.getCodigoEndereco());
        enderecoResponse.setCodigoPessoa(endereco.getPessoa().getCodigoPessoa());
        enderecoResponse.setCodigoBairro(endereco.getBairro().getCodigoBairro());
        enderecoResponse.setNomeRua(endereco.getNomeRua());
        enderecoResponse.setNumero(endereco.getNumero());
        enderecoResponse.setComplemento(endereco.getComplemento());
        enderecoResponse.setBairro(convertBairroToResponse(endereco.getBairro()));
        return enderecoResponse;
    }

    private EnderecoMunicipioResponse convertMunicipioToResponse(Municipio municipio) {
        return new EnderecoMunicipioResponse(
                municipio.getCodigoMunicipio(),
                municipio.getUf().getCodigoUF(),
                municipio.getNome(),
                municipio.getStatus(),
                convertUFToResponse(municipio.getUf())

        );
    }

    private EnderecoBairroResponse convertBairroToResponse(Bairro bairro) {
        return new EnderecoBairroResponse(
                bairro.getCodigoBairro(),
                bairro.getMunicipio().getCodigoMunicipio(),
                bairro.getNome(),
                bairro.getStatus(),
                convertMunicipioToResponse(bairro.getMunicipio())
        );
    }

    private UFResponse convertUFToResponse(UF uf) {
        return new UFResponse(uf.getCodigoUF(), uf.getSigla(), uf.getNome(), uf.getStatus());
    }
}

