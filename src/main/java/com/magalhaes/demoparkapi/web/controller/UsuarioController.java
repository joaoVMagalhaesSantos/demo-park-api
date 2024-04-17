package com.magalhaes.demoparkapi.web.controller;

import com.magalhaes.demoparkapi.entity.exception.ErrorMessage;
import com.magalhaes.demoparkapi.web.dto.UsuarioCreateDTO;
import com.magalhaes.demoparkapi.web.dto.UsuarioResponseDTO;
import com.magalhaes.demoparkapi.web.dto.UsuarioMapper;
import com.magalhaes.demoparkapi.web.dto.UsuarioSenhaDTO;
import com.magalhaes.demoparkapi.entity.Usuario;
import com.magalhaes.demoparkapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name="Usuarios", description = "Contem todas as operações relativas aos recursos para cadastro, edição e leitura de um usuário.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Criar novo usuario", description = "Recurso para criar um novo usuario",
        responses = {
            @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Usuario já cadastrado anteriormente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada invalidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
        })
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody UsuarioCreateDTO usuarioDTO){
        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(usuarioDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDTO(user));
    }

    @Operation(summary = "Consultar Usuario Pelo ID", description = "Recurso para consultar um usuario",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso consultado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorID(@PathVariable long id){
        Usuario user = usuarioService.getByID(id);
        return ResponseEntity.ok().body(UsuarioMapper.toDTO(user));
    }

    @Operation(summary = "Listar todos os usuarios", description = "Listar todos os usuarios cadastrados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista com todos os Usuarios",
                            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class)))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400", description = "Senha não confere",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/selecionarTodos")
    public ResponseEntity<List<UsuarioResponseDTO>> buscarTodos(){
        List<Usuario> user = usuarioService.getAll();
        return ResponseEntity.ok().body(UsuarioMapper.toListDTO(user));
    }
    @PutMapping("{id}")
    public ResponseEntity<Usuario> atualizarUser(@RequestBody Usuario usuario, @PathVariable Long id){
        Usuario user = usuarioService.updateUser(usuario, id);

        return ResponseEntity.ok().body(user);
    }

    @Operation(summary = "Atualizar senha", description = "Recurso para atualizar a senha de um usuario",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400", description = "Senha não confere",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PatchMapping("{id}")
    public ResponseEntity<Void> atualizarSenha(@RequestBody UsuarioSenhaDTO dto, @PathVariable Long id){
        Usuario user = usuarioService.updatePassword(id,dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }



}
