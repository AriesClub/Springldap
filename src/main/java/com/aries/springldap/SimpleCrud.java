package com.aries.springldap;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
@Configurable

public class SimpleCrud {

    @NotNull
    private String message;

	@PersistenceContext
    transient EntityManager entityManager;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            SimpleCrud attached = SimpleCrud.findSimpleCrud(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public SimpleCrud merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SimpleCrud merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static final EntityManager entityManager() {
        EntityManager em = new SimpleCrud().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countSimpleCruds() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SimpleCrud o", Long.class).getSingleResult();
    }

	public static List<SimpleCrud> findAllSimpleCruds() {
        return entityManager().createQuery("SELECT o FROM SimpleCrud o", SimpleCrud.class).getResultList();
    }

	public static SimpleCrud findSimpleCrud(Long id) {
        if (id == null) return null;
        return entityManager().find(SimpleCrud.class, id);
    }

	public static List<SimpleCrud> findSimpleCrudEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SimpleCrud o", SimpleCrud.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Message: ").append(getMessage());
        return sb.toString();
    }

	public String getMessage() {
        return this.message;
    }

	public void setMessage(String message) {
        this.message = message;
    }
}
