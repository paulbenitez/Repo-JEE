package com.mitocode.dao;

import java.util.List;

import javax.ejb.Local;

import com.mitocode.model.Persona;

@Local /*Significa que el contexto de ejecucion es la misma JVM de la maquina (del servidor). 
         En caso se usara @Remote significa que si por alguna razon necesitas acceder a un recurso que esta en otra JVM, 
         si necesitas comunicarte con EJB necesitas activar el @Remote 
         Pero es mejor es usar servicios web en vez de @Remote
         
         El @Local simplemente es cuando se despliegue esta aplicacion los metodos van estar en la misma
         jdk donde van a ser consumidos.
         */
public interface IPersonaDAO extends ICRUD<Persona>
{
	
}
