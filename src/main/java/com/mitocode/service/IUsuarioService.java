package com.mitocode.service;

import java.util.List;

import com.mitocode.model.Usuario;

public interface IUsuarioService extends IService<Usuario> 
{
	Usuario login(Usuario us);
	String verificarConstrasena(String clave, String Pass);
	List<Usuario> listarPorNombreUsuario(String us);
}
