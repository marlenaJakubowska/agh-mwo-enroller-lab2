package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("meetingService")
public class MeetingService {

	Session session;
	DatabaseConnector connector;


	public MeetingService() {
		connector = DatabaseConnector.getInstance();
		session = DatabaseConnector.instance.getSession();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = this.session.createQuery(hql);
		return query.list();
	}

	public Meeting findById(Long id) {
		return connector.getSession().get(Meeting.class, id);
	}
}
