package com.mitocode.controller;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.mitocode.model.Persona;
import com.mitocode.service.IPersonaService;

@Named
@ViewScoped
public class PersonaBean implements Serializable
{
	@Inject
	private IPersonaService service;
	private Persona persona;
	private List<Persona> lista;
	private String tipoDialog;
	private StreamedContent graphicText;
	//private UploadedFile file; 
	/*
	public PersonaBean() {
		this.persona = new Persona();
		//this.service = new PersonaServiceImpl();
		//this.listar();
	} */
	
	@PostConstruct
	public void init() {
		this.persona = new Persona();
		this.listar();
		this.tipoDialog = "Nuevo";
	}
	
	public void limpiarData() {
		this.persona = new Persona();
		this.tipoDialog = "Nuevo";
	}
	
	public void subir(FileUploadEvent event) {
		try {
			if(event.getFile()!=null) {
				this.persona.setFoto(event.getFile().getContents());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void mostrarData(Persona p) {
		this.persona = p;
		this.tipoDialog = "Modificar";
		
		if(this.persona.getFoto() != null)
		{
			if(this.persona.getFoto().length > 0)
			{
				try 
				{/*
					ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
					InputStream input = externalContext.getResourceAsStream("/resources/images/edit.jpg");
					*/
					InputStream input = new ByteArrayInputStream(p.getFoto());
					this.graphicText = new DefaultStreamedContent(input, "image/jpg");
		            
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void operar(String accion) {
		try
		{	/*
			if(this.file != null)
				this.persona.setFoto(this.file.getContents());
			*/
			if(accion.equalsIgnoreCase("R")) {
				this.service.registrar(this.persona);
			}
			else if(accion.equalsIgnoreCase("M")) {
				this.service.modificar(this.persona);
			}
			
			this.listar();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void listar() {
		try {
			this.lista = this.service.listar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public List<Persona> getLista() {
		return lista;
	}

	public void setLista(List<Persona> lista) {
		this.lista = lista;
	}

	public String getTipoDialog() {
		return tipoDialog;
	}

	public void setTipoDialog(String tipoDialog) {
		this.tipoDialog = tipoDialog;
	}

	public StreamedContent getGraphicText() {
		return graphicText;
	}

	public void setGraphicText(StreamedContent graphicText) {
		this.graphicText = graphicText;
	}
/*
	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}*/
}
