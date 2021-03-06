package ca.bcit.infosys.controllers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ca.bcit.infosys.managers.CredentialManager;
import ca.bcit.infosys.managers.EmployeeManager;
import ca.bcit.infosys.models.Employee;

@Named("login")
@ConversationScoped
public class Login implements Serializable {
	private static final long serialVersionUID = 1L;
	@PersistenceContext(unitName = "BluehostTesty")
	static EntityManager em;
	private String username;
	private String password;
	private Employee currentUser;
	private boolean hr = false;
	@Inject
	private CredentialManager crmgr;
	@Inject
	private Conversation conversation;
	@Inject
	EmployeeManager emgr;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Employee getCurrentUser() {
		if (currentUser == null) {
			Employee e = emgr.getLoginEmployee(crmgr.getID(username));
			setCurrentUser(e);
		}
		return currentUser;
	}

	public void setCurrentUser(Employee currentUser) {
		this.currentUser = currentUser;
	}

	public boolean isHr() {
		return hr;
	}

	public void setHr(boolean hr) {
		this.hr = hr;
	}

	static Map<String, Integer> map2 = new HashMap<String, Integer>();
	static Map<String, String> map = new HashMap<String, String>();
	static Map<Integer, Boolean> activeMap = new HashMap<Integer, Boolean>();
	static Map<Integer, String> nameMap = new HashMap<Integer, String>();
	static Map<Integer, Integer> roleMap = new HashMap<Integer, Integer>();

	@SuppressWarnings("unchecked")
	public static void setMaps() {
		String jpaQuery = "select c.username, c.password, c.employeeID from Credential c";
		String eQuery = "select e.employeeID, e.isActive from Employee e";
		String nameQuery = "select e.employeeID, e.firstName, e.lastName from Employee e";
		String roleQuery = "select e.employeeID, e.roleID from Employee e";
		List<Object[]> resultList = em.createQuery(jpaQuery).getResultList();
		List<Object[]> resultList2 = em.createQuery(eQuery).getResultList();
		List<Object[]> resultList3 = em.createQuery(nameQuery).getResultList();
		List<Object[]> resultList4 = em.createQuery(roleQuery).getResultList();
		for (Object[] object : resultList) {
			map.put((String) object[0], (String) object[1]);
		}

		for (Object[] object : resultList) {
			map2.put((String) object[0], (Integer) object[2]);
		}
		for (Object[] object : resultList2) {
			activeMap.put((Integer) object[0], (Boolean) object[1]);
		}
		for (Object[] object : resultList3) {
			nameMap.put((Integer) object[0], (String) (object[1] + " " + object[2]));
		}
		for (Object[] object : resultList4) {
			roleMap.put((Integer) object[0], (Integer) (object[1]));
		}
	}

	public String validate() {
		conversation.begin();
		int roleID = roleMap.get(getCurrentUser().getEmployeeID());
		if (roleID == 1 || roleID == 3 || roleID == 5)
			setHr(true);
		return "adminLanding";
	}

	public String logout() {
		System.out.println("logout");
		conversation.end();
		return "logout";
	}

	public void logout2() {
		conversation.end();
	}

	public String goHome() {
		return "home";
	}

	public String changePassword() {
		return "changePassword";
	}

}
