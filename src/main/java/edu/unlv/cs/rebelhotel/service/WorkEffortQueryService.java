package edu.unlv.cs.rebelhotel.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Service;

import edu.unlv.cs.rebelhotel.domain.Student;
import edu.unlv.cs.rebelhotel.domain.WorkEffort;
import edu.unlv.cs.rebelhotel.form.FormWorkEffortQuery;

@Service
public class WorkEffortQueryService {

	public List<WorkEffort> queryWorkEfforts(FormWorkEffortQuery fweq)
			throws Exception {

		DetachedCriteria search = DetachedCriteria.forClass(WorkEffort.class);
		search.createAlias("student", "student");
		// look for non-empty fields here

		if (!fweq.getUserId().isEmpty()) {
			search.add(Restrictions.like("student.userId",
					"%" + fweq.getUserId() + "%"));
		}
		if (!fweq.getStudentFirstName().isEmpty()) {

			search.add(Restrictions.like("student.firstName",
					"%" + fweq.getStudentFirstName() + "%"));
		}
		if (!fweq.getStudentMiddleName().isEmpty()) {
			search.add(Restrictions.like("student.middleName",
					"%" + fweq.getStudentMiddleName() + "%"));

		}
		if (!fweq.getStudentLastName().isEmpty()) {
			search.add(Restrictions.like("student.lastName",
					"%" + fweq.getStudentLastName() + "%"));

		}
		if (!fweq.getEmployerName().isEmpty()) {
			search.add(Restrictions.like("employer.name",
					"%" + fweq.getEmployerName() + "%"));
		}
		if (!fweq.getEmployerLocation().isEmpty()) {
			search.add(Restrictions.like("employer.location",
					"%" + fweq.getEmployerLocation() + "%"));
		}

		if (fweq.getStartDate() != (null) && fweq.getEndDate() == null) {
			search.add(Restrictions.ge("duration.startDate",
					fweq.getStartDate()));
		}
		if (fweq.getEndDate() != (null) && fweq.getEndDate() == null) {
			search.add(Restrictions.le("duration.endDate", fweq.getEndDate()));
		}

		if (fweq.getEndDate() != (null) && fweq.getEndDate() != null) {

			search.add(Restrictions.between("duration.startDate",
					fweq.getStartDate(), fweq.getEndDate()));
			search.add(Restrictions.between("duration.endDate",
					fweq.getStartDate(), fweq.getEndDate()));

		}
		search.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.alias(Projections.property("id"), "id"))));

		DetachedCriteria rootQuery = DetachedCriteria
				.forClass(WorkEffort.class);

		rootQuery.add(Subqueries.propertyIn("id", search));
		rootQuery.createAlias("student", "student");

		switch (fweq.getSortOptions()) {

		case Student:
			rootQuery.addOrder(Order.asc("student.lastName"));
			rootQuery.addOrder(Order.asc("student.middleName"));
			rootQuery.addOrder(Order.asc("student.firstName"));
			rootQuery.addOrder(Order.asc("employer.name"));
			rootQuery.addOrder(Order.asc("employer.location"));
			rootQuery.addOrder(Order.desc("duration.startDate"));
			rootQuery.addOrder(Order.desc("duration.endDate"));
			break;

		case Date:
			rootQuery.addOrder(Order.desc("duration.startDate"));
			rootQuery.addOrder(Order.desc("duration.endDate"));
			rootQuery.addOrder(Order.asc("student.lastName"));
			rootQuery.addOrder(Order.asc("student.middleName"));
			rootQuery.addOrder(Order.asc("student.firstName"));
			rootQuery.addOrder(Order.asc("employer.name"));
			rootQuery.addOrder(Order.asc("employer.location"));

			break;

		case Employer:
			rootQuery.addOrder(Order.asc("employer.name"));
			rootQuery.addOrder(Order.asc("employer.location"));
			rootQuery.addOrder(Order.asc("student.lastName"));
			rootQuery.addOrder(Order.asc("student.middleName"));
			rootQuery.addOrder(Order.asc("student.firstName"));
			rootQuery.addOrder(Order.desc("duration.startDate"));
			rootQuery.addOrder(Order.desc("duration.endDate"));
			break;
		}

		Session session = (Session) Student.entityManager().unwrap(Session.class);
		Transaction transaction = null;
		List workefforts;
		try {
			transaction = session.beginTransaction();
			workefforts = rootQuery.getExecutableCriteria(session).list();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;

		} finally {
			session.close();
		}

		return workefforts;

	}

}
