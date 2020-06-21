package com.mitocode.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.mitocode.model.Persona;
import com.mitocode.model.PublicadorSeguidor;
import com.mitocode.model.Usuario;
import com.mitocode.service.IPersonaService;
import com.mitocode.service.ISeguidorService;
import com.mitocode.util.MensajeManager;

@Named
@ViewScoped
public class SeguirBean implements Serializable
{
	@Inject
	private IPersonaService personaService;
	
	@Inject
	private ISeguidorService seguidorService;
	
	@Inject
	private MensajeManager mensajeManager;
	
	private Usuario us;
	private List<Persona> personas;
	private List<PublicadorSeguidor> seguidos;
	
	@PostConstruct
	public void init() {
		this.us = (Usuario)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		
		this.listarSeguidos();
		this.listarPersonas();
	}
	
	public void listarPersonas() {
		try {
			this.personas = this.personaService.listar();
			
			this.personas.remove(us.getPersona());
			
			this.personas.forEach(p -> {
				this.seguidos.forEach(s -> {
					if(s.getPublicador().getIdPersona().equals(p.getIdPersona())) {
						p.setEsSeguido(true);
					}
				});
			});
		}catch(Exception e) {
			//
		}
	}

	public void listarSeguidos() {
		try {
			this.seguidos = this.seguidorService.listarSeguidos(this.us.getPersona());
		}catch(Exception e) {
			//
		}
	}
	
	public void seguir(Persona aseguir) 
	{
		try {
			List<Persona> seguidores = new ArrayList<>();
			List<Persona> publicadores = new ArrayList<>();
			
			seguidores.add(this.us.getPersona());
			publicadores.add(aseguir);
			
			this.seguidorService.registrarPublicadoresSeguidores(seguidores, publicadores);
			mensajeManager.mostrarMensaje("Aviso", "!Ahora sigues a " + aseguir.getNombres() + "¡", "INFO");
		}catch(Exception e) {
			mensajeManager.mostrarMensaje("Aviso", "Error al seguir", "ERROR");
		}
		finally {
			this.listarSeguidos();
			this.listarPersonas();
		}
	}
	
	public void dejar(Persona aDejar) {
		try {
			List<Persona> seguidores = new ArrayList<>();
			List<Persona> publicadores = new ArrayList<>();
			
			seguidores.add(this.us.getPersona());
			publicadores.add(aDejar);
			
			this.seguidorService.dejarSeguir(seguidores, publicadores);
			mensajeManager.mostrarMensaje("Aviso", "!Dejaste de seguir a " + aDejar.getNombres() + "¡", "INFO");
		}catch(Exception e) {
			mensajeManager.mostrarMensaje("Aviso", "Error al seguir", "ERROR");
		}
		finally {
			this.listarSeguidos();
			this.listarPersonas();
		}
	}

	public List<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(List<Persona> personas) {
		this.personas = personas;
	}
	
	
}
