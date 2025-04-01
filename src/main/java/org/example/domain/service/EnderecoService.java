package org.example.domain.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.request.PessoaRequest;
import org.example.domain.dto.request.UFRequest;
import org.example.domain.dto.response.PessoaResponse;
import org.example.domain.entity.*;
import org.example.domain.exception.RegraNegocioException;
import org.example.domain.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final UFRepository ufRepository;
    private final MunicipioRepository municipioRepository;
    private  final PessoaRepository pessoaRepository;

    public void salvar(Endereco enderecoDTO) {
        Bairro bairro = enderecoDTO.getBairro();
        if (bairro != null && bairro.getMunicipio() != null && bairro.getMunicipio().getUf() != null) {
            Municipio municipio = bairro.getMunicipio();
            UF uf = municipio.getUf();


            municipio = municipioRepository.findById(municipio.getCodigoMunicipio())
                    .orElseThrow(() -> new RegraNegocioException("Municipio não encontrado"));
            uf = ufRepository.findById(uf.getCodigoUF())
                    .orElseThrow(() -> new RegraNegocioException("UF não encontrado"));

            Pessoa pessoa = enderecoDTO.getPessoa();
            if (pessoa == null || pessoa.getCodigoPessoa() == null) {
                throw new RegraNegocioException("Pessoa não informada no Endereco.");
            }
            pessoa = pessoaRepository.findById(pessoa.getCodigoPessoa())
                    .orElseThrow(() -> new RegraNegocioException("Pessoa não encontrada"));

            Endereco endereco = new Endereco(
                    enderecoDTO.getNumero(),
                    bairro,
                    enderecoDTO.getNomeRua(),
                    enderecoDTO.getCep(),
                    enderecoDTO.getComplemento(),
                    enderecoDTO.getPessoa()
            );
            validarCampos(endereco);
            enderecoRepository.save(endereco);
        } else {
            throw new RegraNegocioException("Bairro não está corretamente associado com Municipio e UF");
        }
    }

    public Endereco atualizarEndereco(Endereco enderecoDTO) {
        try {
            Endereco enderecoExistente = enderecoRepository.findById(enderecoDTO.getCodigoEndereco())
                    .orElseThrow(() -> new RegraNegocioException("Endereço não encontrado no banco de dados."));

            enderecoExistente.setNumero(enderecoDTO.getNumero());
            enderecoExistente.setBairro(enderecoDTO.getBairro());
            enderecoExistente.setNomeRua(enderecoDTO.getNomeRua());
            enderecoExistente.setCep(enderecoDTO.getCep());
            enderecoExistente.setComplemento(enderecoDTO.getComplemento());
            enderecoExistente.setPessoa(enderecoDTO.getPessoa());

            validarCampos(enderecoExistente);

            return enderecoRepository.save(enderecoExistente);
        } catch (Exception ex) {
            throw new RegraNegocioException("Não foi possível atualizar o endereço no banco de dados.");
        }
    }

    public void validarCampos(Endereco endereco) {
        if (endereco.getBairro().getCodigoBairro() == null || endereco.getBairro().getCodigoBairro() <= 0)
            throw new RegraNegocioException("O Campo codigo bairro deve ser preenchido e maior que zero");

        if (endereco.getNumero() == null)
            throw new RegraNegocioException("O Campo numero deve ser preenchido");

        if (endereco.getNomeRua() == null || endereco.getNomeRua().trim().isEmpty())
            throw new RegraNegocioException("O Campo nome rua deve ser preenchido");

        if (endereco.getComplemento() == null || endereco.getComplemento().trim().isEmpty())
            throw new RegraNegocioException("O Campo complemento deve ser preenchido");

        if (endereco.getCep() == null || endereco.getCep().trim().isEmpty())
            throw new RegraNegocioException("O Campo CEP deve ser preenchido.");


    }

}
