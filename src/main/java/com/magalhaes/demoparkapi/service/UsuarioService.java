package com.magalhaes.demoparkapi.service;

import com.magalhaes.demoparkapi.entity.Usuario;
import com.magalhaes.demoparkapi.entity.exception.EntityNotFoundException;
import com.magalhaes.demoparkapi.entity.exception.PasswordInvalidException;
import com.magalhaes.demoparkapi.entity.exception.UsernameUniqueViolationException;
import com.magalhaes.demoparkapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario usuario) {
        try{
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException ex){
            throw new UsernameUniqueViolationException(String.format("Username {%s} já cadastrado",usuario.getUsername()));
        }

    }

    @Transactional(readOnly = true)
    public Usuario getByID(long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Usuario id=%s não encontrado", id))
        );
    }
    @Transactional(readOnly = true)
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public Usuario updateUser(Usuario usuario, Long id) {
        Usuario user = getByID(id);
        return usuarioRepository.save(user);
    }

    @Transactional
    public Usuario updatePassword(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {
        if(!novaSenha.equals(confirmaSenha)){
            throw new PasswordInvalidException("Nova senha não confere com confirmação de senha.");
        }

        Usuario user = getByID(id);
        if(!user.getPassword().equals(senhaAtual)){
            throw new PasswordInvalidException("Sua senha não confere");
        }

        user.setPassword(novaSenha);
        return user;
    }
}
