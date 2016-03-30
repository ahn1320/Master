/**
 * 
 */
package ca.bcit.infosys.managers;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import ca.bcit.infosys.models.PayLevel;

/**
 * @author Brendan Voon
 *
 */
@Dependent
@Stateless
public class PayLevelManager {

	@PersistenceContext(unitName = "BluehostTesty")
	EntityManager em;
	
	public PayLevel find(int id) {
		return em.find(PayLevel.class, id);
	}
	
	public void persist(PayLevel payLevel) {
		em.persist(payLevel);
	}
	
	public void remove(PayLevel payLevel) {
		payLevel = find(payLevel.getPayLevelID());
		em.remove(payLevel);
	}
	
	public PayLevel[] getAll() {
		TypedQuery<PayLevel> query = em.createQuery("select p from PayLevel p", PayLevel.class);
		java.util.List<PayLevel> categories = query.getResultList();
		
		PayLevel[] payLevels = new PayLevel[categories.size()];
		for(int i = 0; i < payLevels.length; i++) {
			payLevels[i] = categories.get(i);
		}
		
		return payLevels;
	}
}