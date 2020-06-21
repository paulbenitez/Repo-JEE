package com.mitocode.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.mindrot.jbcrypt.BCrypt;

import com.mitocode.dao.IUsuarioDAO;
import com.mitocode.model.Persona;
import com.mitocode.model.Usuario;
import com.mitocode.util.ReporteSeguidor;

@Stateless
public class UsuarioDAOImpl implements IUsuarioDAO, Serializable
{
	@PersistenceContext(unitName ="blogPU")
	private EntityManager em;
	
	@Override
	public Integer registrar(Usuario t) throws Exception {
		em.persist(t);
		return t.getPersona().getIdPersona();
	}

	@Override
	public Integer modificar(Usuario t) throws Exception {
		em.merge(t);
		return t.getPersona().getIdPersona();
	}

	@Override
	public Integer eliminar(Usuario t) throws Exception {
		em.remove(em.merge(t));
		return t.getPersona().getIdPersona();
	}

	@Override
	public List<Usuario> listar() throws Exception {
		List<Usuario> lista = new ArrayList<>();
		
		try {
			Query q = em.createQuery("SELECT p FROM Usuario p");
			lista = (List<Usuario>)q.getResultList();
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		return lista;
	}

	@Override
	public Usuario listarPorId(Usuario t) throws Exception {
		Usuario us = new Usuario();
		List<Usuario> lista = new ArrayList<>();
		
		try {
			Query q = em.createQuery("FROM Usuario u WHERE u.id = ?1");
			q.setParameter(1, t.getPersona().getIdPersona());
			
			lista = (List<Usuario>)q.getResultList();
			if(lista != null && !lista.isEmpty()) {
				us = lista.get(0);
			}
			
		}catch(Exception e) {
			throw e;
		}
		
		return us;
	}

	@Override
	public String traerPassHashed(String us) {
		Usuario usuario = new Usuario();
		
		try {
			Query q = em.createQuery("FROM Usuario u WHERE u.usuario = ?1");
			q.setParameter(1, us);
			
			List<Usuario> lista = q.getResultList();
			if(!lista.isEmpty()) {
				usuario = lista.get(0);
			}
		}catch(Exception e) {
			throw e;
		}
		String x = BCrypt.hashpw("", BCrypt.gensalt());
		
		return usuario != null && usuario.getId() != null ? usuario.getContrasena() : x;
	}

	@Override
	public Usuario leerPorNombreUsuario(String us) {
		Usuario usuario = new Usuario();
		List<Usuario> lista = new ArrayList<>();
		
		try {
			Query q = em.createQuery("FROM Usuario u WHERE u.usuario = ?1");
			q.setParameter(1, us);
			
			lista = (List<Usuario>)q.getResultList();
			if(!lista.isEmpty() && lista != null) 
			{
				usuario = lista.get(0);
			}
		}catch(Exception e) {
			throw e;
		}
		
		return usuario;
	}

	@Override
	public List<Usuario> listarPorNombreUsuario(String us) {
		List<Usuario> lista = new ArrayList<>();
		
		try {
			Query q = em.createQuery("FROM Usuario u WHERE u.usuario LIKE ?1");
			//Query q = em.createNativeQuery("SELECT * FROM USUARIO WHERE usuario like %:1%");
			q.setParameter(1, "%" + us + "%");
			/*
			List<Object[]> data = q.getResultList();
			
			for(Object[] x : data) 
			{
				Usuario user = new Usuario();
				user.setId(Integer.parseInt(String.valueOf(x[0])));
				user.setContrasena(String.valueOf(x[1]));
				user.setEstado(String.valueOf(x[2]));
				user.setUsuario(String.valueOf(x[3]));
				
				Persona per = new Persona();
				per.setIdPersona(user.getId());
				user.setPersona(per);
				
				lista.add(user);
			}*/
			lista = (List<Usuario>)q.getResultList();
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return lista;
	}

}
