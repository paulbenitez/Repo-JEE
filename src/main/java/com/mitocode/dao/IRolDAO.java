package com.mitocode.dao;

import java.util.List;

import javax.ejb.Local;

import com.mitocode.model.Rol;
import com.mitocode.model.Usuario;
import com.mitocode.model.UsuarioRol;

@Local
public interface IRolDAO extends ICRUD<Rol>
{
	Integer asignar(Usuario usu, List<UsuarioRol> roles);
}
