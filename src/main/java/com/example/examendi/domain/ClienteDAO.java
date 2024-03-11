package com.example.examendi.domain;

import com.example.examendi.utlity.DAO;
import com.example.examendi.utlity.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;

public class ClienteDAO implements DAO<Cliente> {
    @Override
    public ArrayList<Cliente> getAll() {
        var salida = new ArrayList<Cliente>(0);
        try(Session sesion = HibernateUtil.getSessionFactory().openSession()) {
            Query<Cliente> query = sesion.createQuery("from Cliente", Cliente.class);
            salida = (ArrayList<Cliente>) query.getResultList();
        }
        return salida;
    }

    @Override
    public Cliente get(Long id) {
        var salida = new Cliente();
        try(Session s = HibernateUtil.getSessionFactory().openSession()) {
            salida = s.get(Cliente.class,id);
        }
        return salida;
    }

    @Override
    public Cliente save(Cliente data) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(data);

                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
            return data;
        }
    }
}
