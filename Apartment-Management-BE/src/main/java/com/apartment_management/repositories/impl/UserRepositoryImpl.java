/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.apartment_management.repositories.impl;

import com.apartment_management.pojo.User;
import com.apartment_management.repositories.UserRepository;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private LocalSessionFactoryBean factory;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User getUserByUserName(String username) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);
        return (User) q.getSingleResult();

    }

    @Override
    public User addUser(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        s.persist(u);

        return u;
    }

    //đăng nhập ở trang admin
    @Override
    public boolean authenticate(String username, String password) {
        User u = this.getUserByUserName(username);
        if (u != null && u.getRole().equals("ADMIN")) {
            return this.passwordEncoder.matches(password, u.getPassword());
        } else {
            return false;
        }
    }

    @Override
    public User authenticateForClient(String username, String password) {
        Session s = this.factory.getObject().getCurrentSession();
        Query q = s.createNamedQuery("User.findByUsername", User.class);
        q.setParameter("username", username);
        User u = (User) q.getSingleResult();

        if (u != null && u.getRole().equals("RESIDENT") && this.passwordEncoder.matches(password, u.getPassword())) {
            return u;
        } else {
            return null; // Nếu đăng nhập thất bại, trả về null
        }
    }

    //dùng cho api editProfile
    @Override
    public User editProfile(User u) {
        Session s = this.factory.getObject().getCurrentSession();
        if (u != null) {
            s.update(u);
        }
        return u;
    }

    @Override
    public User getUserById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createNamedQuery("User.findById", User.class);
        query.setParameter("id", id);
        return (User) query.getSingleResult();
    }

    @Override
    public List<User> getUsers(Map<String, String> params) {
        Session s = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = s.getCriteriaBuilder();
        CriteriaQuery<User> q = b.createQuery(User.class);
        Root root = q.from(User.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("username"), String.format("%%%s%%", kw)));
            }
            String roomNumber = params.get("room_number");
            if (roomNumber != null && !roomNumber.isEmpty()) {
                predicates.add(b.like(root.get("roomId").get("roomNumber"), String.format("%%%s%%", kw)));
            }
            q.where(predicates.toArray(Predicate[]::new));

            String orderBy = params.get("orderBy");
            if (orderBy != null && !orderBy.isEmpty()) {
                q.orderBy(b.asc(root.get(orderBy)));
            }
        }

        Query query = s.createQuery(q);
        return query.getResultList();

    }

    @Override
    public boolean deleteUser(int id) {
        Session s = this.factory.getObject().getCurrentSession();
        User user = s.get(User.class, id);
        if (user != null) {
            s.remove(user);
            return true;
        }
        return false;
    }

    @Override
    public List<User> findByRole(String role) {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createNamedQuery("User.findByRole", User.class);
        query.setParameter("role", role);
        return query.getResultList();
    }
    
    @Override
    public List<Object[]> getUserStatsByPeriod(String period, int year) {
        Session session = factory.getObject().getCurrentSession();
        String hql;
        switch (period.toLowerCase()) {
            case "month":
                hql = "SELECT MONTH(u.createdAt), COUNT(u) FROM User u WHERE YEAR(u.createdAt) = :year GROUP BY MONTH(u.createdAt)";
                break;
            case "quarter":
                hql = "SELECT QUARTER(u.createdAt), COUNT(u) FROM User u WHERE YEAR(u.createdAt) = :year GROUP BY QUARTER(u.createdAt)";
                break;
            case "year":
                hql = "SELECT YEAR(u.createdAt), COUNT(u) FROM User u GROUP BY YEAR(u.createdAt)";
                break;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }
        Query query = session.createQuery(hql);
        if (!period.equalsIgnoreCase("year")) {
            query.setParameter("year", year);
        }
        return query.getResultList();
    }

}
