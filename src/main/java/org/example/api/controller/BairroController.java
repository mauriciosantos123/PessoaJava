package org.example.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.request.BairroRequest;
import org.example.domain.dto.request.MunicipioRequest;
import org.example.domain.dto.response.BairroResponse;
import org.example.domain.dto.response.MunicipioResponse;
import org.example.domain.entity.Bairro;
import org.example.domain.entity.Municipio;
import org.example.domain.enums.StatusEnum;
import org.example.domain.service.BairroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bairro")
@RequiredArgsConstructor
public class BairroController {

    private final BairroService bairroService;

    @GetMapping
    public ResponseEntity<?> buscarBairro(@RequestParam(required = false) Integer codigoBairro,
                                             @RequestParam(required = false) Integer codigoMunicipio,
                                             @RequestParam(required = false) String nome,
                                             @RequestParam(required = false) Integer status) {
        Object result = bairroService.buscarBairro(codigoBairro, codigoMunicipio, nome, status);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<List<BairroResponse>> salvarMunicipio(@RequestBody BairroRequest bairroRequest) {
        return ResponseEntity.ok(bairroService.salvarBairro(bairroRequest));
    }

    @PutMapping
    public ResponseEntity<List<BairroResponse>> atualizarMunicipio (@RequestBody BairroRequest bairroDTO){
        return ResponseEntity.ok(bairroService.atualizarBairro(bairroDTO));
    }
}
