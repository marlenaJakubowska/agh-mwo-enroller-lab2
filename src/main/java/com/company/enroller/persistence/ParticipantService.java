package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.*;
import java.util.Collection;
import java.util.List;

@Component("participantService")
public class ParticipantService {

    DatabaseConnector connector;

    public ParticipantService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Participant> getAll() {
        String hql = "FROM Participant";
        Query query = connector.getSession().createQuery(hql);
        return query.list();
    }

    public List<Participant> getAll(String sortOrder, String sortBy, String key) {
        CriteriaBuilder cb = connector.getSession().getCriteriaBuilder();
        CriteriaQuery<Participant> cq = cb.createQuery(Participant.class);
        Root<Participant> root = cq.from(Participant.class);

        if (sortBy != null && !sortBy.isEmpty()) {
            Order order;
            if ("ASC".equalsIgnoreCase(sortOrder)) {
                order = cb.asc(root.get(sortBy));
            } else if ("DESC".equalsIgnoreCase(sortOrder)) {
                order = cb.desc(root.get(sortBy));
            } else {
                order = cb.asc(root.get(sortBy)); // default sort order
            }
            cq.orderBy(order);
        }

        if (key != null && !key.isEmpty()) {
            Predicate filter = cb.like(root.get("login"), "%" + key + "%");
            cq.where(filter);
        }

        return connector.getSession().createQuery(cq).getResultList();
    }


    public Participant findByLogin(String login) {
        return connector.getSession().get(Participant.class, login);
    }

    public Participant add(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(participant);
        transaction.commit();
        return participant;
    }

    public void update(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().merge(participant);
        transaction.commit();
    }

    public void delete(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(participant);
        transaction.commit();
    }

    public Collection<Participant> getAllFilteredSorted(String sortBy, String sortOrder, String key) {
        try (Session session = connector.getSession()) {
            String hql = "FROM Participant";
            if (key != null && !key.isEmpty()) {
                hql += " WHERE login LIKE :key";
            }
            if (sortBy != null && (sortBy.equalsIgnoreCase("login"))) {
                hql += " ORDER BY login " + (sortOrder.equalsIgnoreCase("DESC") ? "DESC" : "ASC");
            }
            Query<Participant> query = session.createQuery(hql, Participant.class);
            if (key != null && !key.isEmpty()) {
                query.setParameter("key", "%" + key + "%");
            }
            return query.list();
        }
    }

}
