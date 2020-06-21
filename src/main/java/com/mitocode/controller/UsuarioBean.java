package com.mitocode.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.mindrot.jbcrypt.BCrypt;

import com.mitocode.model.Usuario;
import com.mitocode.service.IUsuarioService;

@Named
@ViewScoped
public class UsuarioBean implements Serializable
{
	@Inject
	private IUsuarioService service;
	
	private String nombre;
	private List<Usuario> lista;
	private Usuario user;
	private String claveVerificacion;
	private String verificado;
	private String claveNueva;
	
	@PostConstruct
	public void init() {
		this.user = new Usuario();
		listar();
	}
	
	private void listar() {
		try {
			this.lista = this.service.listar();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Transactional 
	public void actualizar()
	{
		try
		{
			String claveHash = BCrypt.hashpw(this.claveNueva, BCrypt.gensalt());
			this.user.setContrasena(claveHash);
			this.service.modificar(this.user);
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Actualización exitosa"));
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	public void verificar()
	{		
		this.verificado = this.service.verificarConstrasena(this.claveVerificacion, this.user.getContrasena());
		
		if(this.verificado.equalsIgnoreCase("ok")) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Aviso", "Clave correcta"));
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Clave incorrecta"));
		}
	}
	
	public void buscar() {
		try {
			this.lista = this.service.listarPorNombreUsuario(this.nombre);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mostrarData(Usuario us) {
		this.user = us;
		this.verificado = "";
		this.claveVerificacion = "";
	}

	public Usuario getUser() {
		return user;
	}

	public void setUser(Usuario user) {
		this.user = user;
	}

	public List<Usuario> getLista() {
		return lista;
	}

	public void setLista(List<Usuario> lista) {
		this.lista = lista;
	}

	public String getClaveVerificacion() {
		return claveVerificacion;
	}

	public void setClaveVerificacion(String claveVerificacion) {
		this.claveVerificacion = claveVerificacion;
	}

	public String getVerificado() {
		return verificado;
	}

	public void setVerificado(String verificado) {
		this.verificado = verificado;
	}

	public String getClaveNueva() {
		return claveNueva;
	}

	public void setClaveNueva(String claveNueva) {
		this.claveNueva = claveNueva;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
