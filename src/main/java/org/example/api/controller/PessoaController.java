package org.example.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.request.MunicipioRequest;
import org.example.domain.dto.request.PessoaRequest;
import org.example.domain.dto.response.PessoaResponse;
import org.example.domain.service.PessoaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pessoa")
@RequiredArgsConstructor
public class PessoaController {
    private final PessoaService pessoaService;

    @GetMapping
    public ResponseEntity<?> buscarPessoa(@RequestParam(required = false) Integer codigoPessoa,
                                             @RequestParam(required = false) String login,
                                             @RequestParam(required = false) Integer status) {

        Object result = pessoaService.buscarPessoa(codigoPessoa, login, status);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public List<PessoaResponse> salvarPessoa(@RequestBody PessoaRequest pessoaRequest){
        return ResponseEntity.ok(pessoaService.salvarPessoa(pessoaRequest)).getBody();
    }

    @PutMapping
    public List<PessoaResponse> atualizarPessoa(@RequestBody PessoaRequest pessoaRequest){
        return ResponseEntity.ok(pessoaService.atualizarPessoa(pessoaRequest)).getBody();
    }
}
