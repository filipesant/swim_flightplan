package org.icea.swim.ws.config;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class JPAConfiguration {

	private static EntityManagerFactory emf = null;

	private static EntityManager em = null;

	public static EntityManager getEntityManager(String persistenceUnity){

		if (emf == null && em == null){

			emf = Persistence.createEntityManagerFactory(persistenceUnity);
			
			em = emf.createEntityManager();
		}
		
		return em;
	}

	public static void closeEntityManager(){

		em.close();
		
		emf.close();
	}
}