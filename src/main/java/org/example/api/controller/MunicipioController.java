package org.example.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.request.MunicipioRequest;
import org.example.domain.dto.response.MunicipioResponse;
import org.example.domain.entity.Municipio;
import org.example.domain.enums.StatusEnum;
import org.example.domain.service.MunicipioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/municipio")
@RequiredArgsConstructor
public class MunicipioController {

    private final MunicipioService municipioService;

    @GetMapping
    public ResponseEntity<?> buscarMunicipio(@RequestParam(required = false) Integer codigoMunicipio,
                                             @RequestParam(required = false) Integer codigoUF,
                                             @RequestParam(required = false) String nome,
                                             @RequestParam(required = false) Integer status) {

        Object result = municipioService.buscarMunicipio(codigoMunicipio, codigoUF, nome, status);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<List<MunicipioResponse>> salvarMunicipio(@RequestBody MunicipioRequest municipioRequest) {
        return ResponseEntity.ok(municipioService.salvarMunicipio(municipioRequest));
    }

    @PutMapping
    public ResponseEntity<List<MunicipioResponse>> atualizarMunicipio (@RequestBody MunicipioRequest municipio){
    return ResponseEntity.ok(municipioService.atualizarMunicipio(municipio));
    }
}
