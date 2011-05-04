package edu.unlv.cs.rebelhotel.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Service;

import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.form.FormStudentQuickFind;

@Service
public class StudentQuickFindService {

	public List<Student> findStudents(FormStudentQuickFind form)
	throws Exception {
		
		DetachedCriteria search = DetachedCriteria.forClass(Student.class);
		
		if(!form.getUserId().isEmpty()){
			search.add(Restrictions.eq("userId", form.getUserId()));

		}
		
		else {
			
			if(!form.getFirstName().isEmpty() ){
				search.add(Restrictions.like("firstName",form.getFirstName() + "%"));
		}
			if(!form.getLastName().isEmpty()){
				search.add(Restrictions.like("lastName",form.getLastName() + "%"));
			}
		}
		
		
		search.setProjection(Projections.distinct(Projections.projectionList().add(Projections.alias(Projections.property("id"), "id"))));

		DetachedCriteria rootQuery = DetachedCriteria.forClass(Student.class);

		rootQuery.add(Subqueries.propertyIn("id", search));
		Session session = (Session) Student.entityManager().unwrap(Session.class);
		Transaction transaction = null;
		List<Student> students;
		try {
			transaction = session.beginTransaction();
			students = rootQuery.getExecutableCriteria(session).list();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;

		} finally {
			session.close();
		}

		return students;

	}

}
