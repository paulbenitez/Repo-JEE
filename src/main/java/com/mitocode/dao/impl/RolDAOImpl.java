package com.mitocode.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.mitocode.dao.IRolDAO;
import com.mitocode.model.Rol;
import com.mitocode.model.Usuario;
import com.mitocode.model.UsuarioRol;

@Stateless
public class RolDAOImpl implements Serializable, IRolDAO
{
	@PersistenceContext(unitName="blogPU")
	private EntityManager em;
	
	@Override
	public Integer registrar(Rol t) throws Exception {
		em.persist(t);
		return t.getId();
	}

	@Override
	public Integer modificar(Rol t) throws Exception {
		em.merge(t);
		return t.getId();
	}

	@Override
	public Integer eliminar(Rol t) throws Exception {
		/*Cuando se elimina, primero se necesita hacer un merge para hacer una transaccion
		 * no confirmada en la BD, JPA lo maneja de esta manera. Cuando se hace una eliminacion
		 * se tiene que eliminar algo que este insertado en la BD, por lo que el MERGE significa
		 * si no esta actualizado o insertado hazlo ahora mismo antes de eliminarlo.
		 * ES UNA BUENA PRACTICA realizar esto.*/
		em.remove(em.merge(t));
		return 1;
	}

	@Override
	public List<Rol> listar() throws Exception {
		/*JPQL Java Persistence Query Language, es una forma de
		 * trabajar los Querys con JPA
		 * JPQL esta orientado a OBJETOS y CLASES
		 * por lo que en la query se contruye con entidades y no con tablas de BD
		 * Para renombrar el nombre de la entidad se deberá setear:
		 * @Entity(name="abc") sobre la clase X*/
	
		Query q = em.createQuery("SELECT r FROM Rol r");
		List<Rol> lista = (List<Rol>) q.getResultList();
		return lista;
	}

	@Override
	public Rol listarPorId(Rol t) throws Exception {
		Query q = em.createQuery("FROM Rol r WHERE r.id = ?1");
		q.setParameter(1, t.getId());
		List<Rol> lista = (List<Rol>) q.getResultList();
		
		return lista != null && !lista.isEmpty() ? lista.get(0) : new Rol();
	}

	@Override
	public Integer asignar(Usuario usu, List<UsuarioRol> roles) 
	{
		/*Comportamiento NATIVO de SQL, donde se trabaja 
		 * con tablas de BD*/
		Query q = em.createNativeQuery("DELETE FROM usuario_rol UR WHERE UR.ID_USUARIO = ?1");
		q.setParameter(1, usu.getPersona().getIdPersona());
		q.executeUpdate();
		
		/* Nota: Al tener varia insert en un bucle con entityManager, esta
		 * se satura, por lo que se recomienda limpiar el EM por n
		 * ciclos de iteracion*
		 */
		
		/*Las expresiones lamba son funciones anonimas no tienen visibilidad
		 * a lo que hay hacia fuera de el si es una variable local de un 
		 * metodo. Salvo que sea atributo de clase.
		 * */
		int[] i = {0};
		roles.forEach(r -> {
			em.persist(r);
			if(i[0] % 100 == 0) {
				em.flush();
				em.clear();
			}
			i[0]++;
		});
		
		return i[0];
	}

}
