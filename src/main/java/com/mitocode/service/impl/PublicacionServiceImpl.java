package com.mitocode.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import com.mitocode.dao.IPublicacionDAO;
import com.mitocode.model.Mencion;
import com.mitocode.model.Persona;
import com.mitocode.model.Publicacion;
import com.mitocode.model.Tag;
import com.mitocode.service.IPublicacionService;

@Named
public class PublicacionServiceImpl implements IPublicacionService, Serializable {
	
	@EJB
	private IPublicacionDAO dao;
	
	@Override
	public Integer registrar(Publicacion publicacion) throws Exception {
		/*
		List<Tag> tags = new ArrayList<>();
		List<Mencion> menciones = new ArrayList<>();

		String texto = publicacion.getCuerpo();
		texto = texto.replaceAll(",", "");
		String[] arreglo = texto.split(" ");
		for (String elemento : arreglo) {
			if (elemento.startsWith("@")) {
				elemento = elemento.substring(1, elemento.length());
				menciones.add(new Mencion(publicacion, elemento));
			}

			if (elemento.startsWith("#")) {
				elemento = elemento.substring(1, elemento.length());
				tags.add(new Tag(publicacion, elemento));
			}
		}
		publicacion.setTags(tags);
		publicacion.setMenciones(menciones);

		// "Hola como estas @mitocode #premium #2018"
		int rpta = dao.registrar(publicacion);
		return rpta > 0 ? 1 : 0;
		*/
		
		
		List<Tag> tags = new ArrayList<>();
		List<Mencion> menciones = new ArrayList<>();
		
		String texto = publicacion.getCuerpo();
		String[] _tags = obtenerArregloPorSeparador(texto, "#");
		String[] _menciones = obtenerArregloPorSeparador(texto, "@");
		
		if(_tags != null) {
			for(String e : _tags) {
				tags.add(new Tag(publicacion, e));
			}
			publicacion.setTags(tags);
		}
		
		if(_menciones != null) {
			for(String e : _menciones) {
				menciones.add(new Mencion(publicacion, e));
			}
			publicacion.setMenciones(menciones);
		}
		
		int rpta = dao.registrar(publicacion);
		return rpta > 0 ? 1 : 0;
	}
	
	public String[] obtenerArregloPorSeparador(String texto, String separador) 
	{
		String[] retornar = null;
		String[] arreglo = limpiarTexto(texto, separador).split(" ");
		
		int ind = 0;
		String cadena = "";
		String parcial = "";
		for(String e : arreglo) 
		{
			if(e.contains(separador)) 
			{
				for(int i = 0; i < e.length(); i++) 
				{
					if(ind == 1) {
						if(!e.substring(i, i + 1).equals(separador))
							parcial = parcial + e.substring(i, i + 1);
						
						if(parcial.length() > 0 && ((i == e.length() - 1) || (e.substring(i, i + 1).equals(separador)))) {
							if(!((e.substring(i, i + 1).equals(separador)) && (e.substring(i - 1, i).equals(separador))))
								parcial = parcial + " ";
						}
					}
					if(e.substring(i, i + 1).equals(separador) && ind == 0)
						ind = 1;
				}
				cadena = cadena + parcial;
				parcial = "";
				ind = 0;
			}
		}
		
		cadena = cadena.trim();
		if(cadena.length() > 0) {
			retornar = cadena.split(" ");
		}
		return retornar;
	}
	
	public String limpiarTexto(String texto, String separador) 
	{
		StringBuilder retorna = new StringBuilder();
		int codCaracter = (int)(separador.charAt(0));
		
		for(int i = 0; i < texto.length(); i++) 
		{
			char valor = texto.substring(i, i+1).charAt(0);
			int codAscii = (int)valor;
			
			if((codAscii >= 48 && codAscii <= 57)   || (codAscii >= 65 && codAscii <= 90)   || 
			   (codAscii >= 97 && codAscii <= 122)  || (codAscii >= 164 && codAscii <= 165) || 
			   (codAscii == codCaracter))
			{
				retorna = retorna.append(texto.substring(i, i+1));
			}else {
				retorna = retorna.append(" ");
			}
		}
		return retorna.toString().trim();
	}

	@Override
	public Integer modificar(Publicacion t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer eliminar(Publicacion t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Publicacion> listar() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Publicacion listarPorId(Publicacion t) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Publicacion> listarPublicacionesPorPublicador(Persona publicador) throws Exception {
		return dao.listarPublicacionesPorPublicador(publicador);
	}

	@Override
	public List<Publicacion> listarPublicacionesDeSeguidores(Persona per) {
		return dao.listarPublicacionesDeSeguidores(per);
	}
}