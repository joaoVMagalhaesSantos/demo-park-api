package com.magalhaes.demoparkapi.web.dto;

import com.magalhaes.demoparkapi.entity.Usuario;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {

    public static Usuario toUsuario(UsuarioCreateDTO createDTO){
        return  new ModelMapper().map(createDTO, Usuario.class);
    }
    public static UsuarioResponseDTO toDTO(Usuario usuario){
        String role = usuario.getRole().name().substring("Role_".length());
        PropertyMap<Usuario,UsuarioResponseDTO> props = new PropertyMap<Usuario, UsuarioResponseDTO>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };
        ModelMapper mapper =  new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(usuario, UsuarioResponseDTO.class);
    }

    public static List<UsuarioResponseDTO> toListDTO(List<Usuario> usuarios){
        return usuarios.stream().map(user -> toDTO(user)).collect(Collectors.toList());
    }
}
