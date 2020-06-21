package com.mitocode.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.mindrot.jbcrypt.BCrypt;

import com.mitocode.dao.IUsuarioDAO;
import com.mitocode.model.Usuario;
import com.mitocode.service.IUsuarioService;

public class UsuarioServiceImpl implements IUsuarioService, Serializable
{
	@EJB
	private IUsuarioDAO dao;
	
	@Override
	public Usuario login(Usuario us) 
	{
		String clave = us.getContrasena();
		String ClaveHash = dao.traerPassHashed(us.getUsuario());
		
		if(BCrypt.checkpw(clave, ClaveHash)) 
		{
			return dao.leerPorNombreUsuario(us.getUsuario());
		}
		
		return new Usuario();
	}

	@Override
	public Integer registrar(Usuario t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer modificar(Usuario t) throws Exception {
		return dao.modificar(t);
	}

	@Override
	public Integer eliminar(Usuario t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Usuario> listar() throws Exception {
		return dao.listar();
	}

	@Override
	public Usuario listarPorId(Usuario t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String verificarConstrasena(String clave, String Pass) {
		String verificacion = "";
		
		if(BCrypt.checkpw(clave, Pass)) {
			verificacion = "ok";
		}
		return verificacion;
	}

	@Override
	public List<Usuario> listarPorNombreUsuario(String us) 
	{
		List<Usuario> lista = new ArrayList<>();
		try {
			if(us.trim().equals("")) {
				lista = dao.listar();
			}else {
				lista = dao.listarPorNombreUsuario(us);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return lista;
	}
}
