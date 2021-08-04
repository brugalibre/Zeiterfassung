package com.adcubum.timerecording.core.businessday.comeandgo.entity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.adcubum.timerecording.core.businessday.common.entity.BaseEntity;

@Entity
@Table(name = "comeandgoes")
public class ComeAndGoesEntity extends BaseEntity {

   @OneToMany(targetEntity = ComeAndGoEntity.class,
         mappedBy = "comeAndGoesEntity",
         cascade = CascadeType.ALL,
         fetch = FetchType.EAGER,
         orphanRemoval = true)
   private List<ComeAndGoEntity> comeAndGoEntriesEntities;

   /**
    * Creates a new {@link ComeAndGoesEntity}
    * 
    * @param id
    *        the id
    */
   public ComeAndGoesEntity() {
      this(null);
   }

   /**
    * Creates a new {@link ComeAndGoesEntity} for a persist {@link ComeAndGoesEntity} referenced by the given id
    * 
    * @param id
    *        the id
    */
   public ComeAndGoesEntity(UUID id) {
      super(id);
      this.comeAndGoEntriesEntities = Collections.emptyList();
   }

   public List<ComeAndGoEntity> getComeAndGoEntriesEntities() {
      return comeAndGoEntriesEntities;
   }

   public void setComeAndGoEntriesEntities(List<ComeAndGoEntity> comeAndGoEntriesEntities) {
      this.comeAndGoEntriesEntities = comeAndGoEntriesEntities;
   }

   @Override
   public String toString() {
      String comeAndGoEntityEntriesRep = comeAndGoEntriesEntities.stream()
            .map(comeAndGoEntity -> String.format("ComeAndGoEntity='%s'", comeAndGoEntity))
            .reduce("", (a, b) -> a + "," + b);
      return String.format(
            "ComeAndGoesEntity[id=%s, comeAndGoEntityEntriesRep='%s']",
            id, comeAndGoEntityEntriesRep);
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((comeAndGoEntriesEntities == null) ? 0 : comeAndGoEntriesEntities.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      ComeAndGoesEntity other = (ComeAndGoesEntity) obj;
      if (comeAndGoEntriesEntities == null) {
         if (other.comeAndGoEntriesEntities != null)
            return false;
      } else if (!comeAndGoEntriesEntities.equals(other.comeAndGoEntriesEntities))
         return false;
      return true;
   }
}
