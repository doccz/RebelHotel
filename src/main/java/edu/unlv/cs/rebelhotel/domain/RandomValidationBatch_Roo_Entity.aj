// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package edu.unlv.cs.rebelhotel.domain;

import edu.unlv.cs.rebelhotel.domain.RandomValidationBatch;
import java.lang.Integer;
import java.lang.Long;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import org.springframework.transaction.annotation.Transactional;

privileged aspect RandomValidationBatch_Roo_Entity {
    
    declare @type: RandomValidationBatch: @Entity;
    
    @PersistenceContext
    transient EntityManager RandomValidationBatch.entityManager;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long RandomValidationBatch.id;
    
    @Version
    @Column(name = "version")
    private Integer RandomValidationBatch.version;
    
    public Long RandomValidationBatch.getId() {
        return this.id;
    }
    
    public void RandomValidationBatch.setId(Long id) {
        this.id = id;
    }
    
    public Integer RandomValidationBatch.getVersion() {
        return this.version;
    }
    
    public void RandomValidationBatch.setVersion(Integer version) {
        this.version = version;
    }
    
    @Transactional
    public void RandomValidationBatch.persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void RandomValidationBatch.remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            RandomValidationBatch attached = RandomValidationBatch.findRandomValidationBatch(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void RandomValidationBatch.flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public RandomValidationBatch RandomValidationBatch.merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        RandomValidationBatch merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public static final EntityManager RandomValidationBatch.entityManager() {
        EntityManager em = new RandomValidationBatch().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long RandomValidationBatch.countRandomValidationBatches() {
        return entityManager().createQuery("select count(o) from RandomValidationBatch o", Long.class).getSingleResult();
    }
    
    public static List<RandomValidationBatch> RandomValidationBatch.findAllRandomValidationBatches() {
        return entityManager().createQuery("select o from RandomValidationBatch o", RandomValidationBatch.class).getResultList();
    }
    
    public static RandomValidationBatch RandomValidationBatch.findRandomValidationBatch(Long id) {
        if (id == null) return null;
        return entityManager().find(RandomValidationBatch.class, id);
    }
    
    public static List<RandomValidationBatch> RandomValidationBatch.findRandomValidationBatchEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("select o from RandomValidationBatch o", RandomValidationBatch.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
}