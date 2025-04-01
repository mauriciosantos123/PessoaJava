package org.example.api.controller;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.request.UFRequest;
import org.example.domain.entity.UF;
import org.example.domain.service.UFService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/uf")
@RequiredArgsConstructor
public class UFController {

    private final UFService ufService;

    @PostMapping
    public ResponseEntity<?> salvarUF(@RequestBody UFRequest ufDto){
            return ResponseEntity.ok(ufService.salvar(ufDto));
    }


    @GetMapping
    public  ResponseEntity<?> buscarUF(@RequestParam(required = false) String sigla,
                                         @RequestParam(required = false) String nome,
                                         @RequestParam(required = false) Integer codigoUF,
                                         @RequestParam(required = false) Integer status) {

        Object result = ufService.buscarUf(sigla, nome, codigoUF, status);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<List<UF>> atualizarUF(@RequestBody UF uf){
        return ResponseEntity.ok(ufService.atualizar(uf));
    }

//    @DeleteMapping
//    public ResponseEntity deletarUF(@RequestParam Integer id){
//        return ResponseEntity.ok()
//    }
}
