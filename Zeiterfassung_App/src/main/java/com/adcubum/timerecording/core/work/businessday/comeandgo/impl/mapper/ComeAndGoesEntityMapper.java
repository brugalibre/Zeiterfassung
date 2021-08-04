package com.adcubum.timerecording.core.work.businessday.comeandgo.impl.mapper;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

import com.adcubum.timerecording.core.businessday.comeandgo.entity.ComeAndGoEntity;
import com.adcubum.timerecording.core.businessday.comeandgo.entity.ComeAndGoesEntity;
import com.adcubum.timerecording.core.businessday.entity.TimeSnippetEntity;
import com.adcubum.timerecording.core.work.businessday.TimeSnippet;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGo;
import com.adcubum.timerecording.core.work.businessday.comeandgo.ComeAndGoes;
import com.adcubum.timerecording.core.work.businessday.comeandgo.impl.ComeAndGoImpl;
import com.adcubum.timerecording.core.work.businessday.comeandgo.impl.ComeAndGoesImpl;
import com.adcubum.timerecording.core.work.businessday.mapper.TimeSnippetEntityMapper;

/**
 * The {@link ComeAndGoesEntityMapper} is used as a Mapper in order to map from a {@link ComeAndGoes} into a {@link ComeAndGoesEntity}
 * and vice versa
 * 
 * @author dstalder
 *
 */
public class ComeAndGoesEntityMapper {

   /** Singleton instance of the {@link ComeAndGoesEntityMapper} */
   public static final ComeAndGoesEntityMapper INSTANCE = new ComeAndGoesEntityMapper();

   private ComeAndGoesEntityMapper() {
      // private
   }

   /**
    * Maps the given {@link ComeAndGoesEntity} into a {@link ComeAndGoes}
    * 
    * @param comeAndGoesEntity
    *        the {@link ComeAndGoesEntity} to map from
    * @return a transient {@link ComeAndGoes}
    */
   public ComeAndGoes map2ComeAndGoes(ComeAndGoesEntity comeAndGoesEntity) {
      requireNonNull(comeAndGoesEntity);
      List<ComeAndGo> comeAndGoEntityEntries = map2ComeAndGoEntries(comeAndGoesEntity);
      return ComeAndGoesImpl.of(comeAndGoesEntity.getId(), comeAndGoEntityEntries);
   }

   /**
    * Maps the given {@link ComeAndGoes} into a {@link ComeAndGoesEntity}
    * 
    * @param comeAndGoes
    *        the {@link ComeAndGoes} to map from
    * @return a new {@link ComeAndGoesEntity}
    */
   public ComeAndGoesEntity map2ComeAndGoesEntity(ComeAndGoes comeAndGoes) {
      requireNonNull(comeAndGoes);
      ComeAndGoesEntity comeAndGoesEntity = new ComeAndGoesEntity(comeAndGoes.getId());
      List<ComeAndGoEntity> comeAndGoEntityEntries = map2ComeAndGoEntities(comeAndGoes.getComeAndGoEntries(), comeAndGoesEntity);
      comeAndGoesEntity.setComeAndGoEntriesEntities(comeAndGoEntityEntries);
      return comeAndGoesEntity;
   }

   private static List<ComeAndGoEntity> map2ComeAndGoEntities(List<ComeAndGo> comeAndGoEntityEntries,
         ComeAndGoesEntity comeAndGoesEntity) {
      return comeAndGoEntityEntries
            .stream()
            .map(comeAndGo -> map2CompleteComeAndGoEntity(comeAndGo, comeAndGoesEntity))
            .collect(Collectors.toList());
   }

   private static List<ComeAndGo> map2ComeAndGoEntries(ComeAndGoesEntity comeAndGoesEntity) {
      return comeAndGoesEntity.getComeAndGoEntriesEntities()
            .stream()
            .map(ComeAndGoesEntityMapper::map2ComeAndGoEntity)
            .collect(Collectors.toList());
   }

   private static ComeAndGoEntity map2CompleteComeAndGoEntity(ComeAndGo comeAndGo, ComeAndGoesEntity comeAndGoesEntity) {
      ComeAndGoEntity comeAndGoEntity = new ComeAndGoEntity(comeAndGo.getId(), comeAndGoesEntity, !comeAndGo.isNotRecorded());
      TimeSnippetEntity timeSnippetEntity = TimeSnippetEntityMapper.INSTANCE.map2TimeSnippetEntity(comeAndGo.getComeAndGoTimeStamp());
      comeAndGoEntity.setTimeSnippetEntity(timeSnippetEntity);
      return comeAndGoEntity;
   }

   /**
    * Creates a new {@link ComeAndGoesEntity} for the given {@link ComeAndGoesEntity}
    * 
    * @param comeAndGoesEntity
    *        the given {@link ComeAndGoesEntity}
    * @return a new {@link ComeAndGoesEntity} for the given {@link ComeAndGoesEntity}
    */
   private static ComeAndGo map2ComeAndGoEntity(ComeAndGoEntity comeAndGoEntity) {
      TimeSnippet timeSnippet = TimeSnippetEntityMapper.INSTANCE.map2TimeSnippet(comeAndGoEntity.getTimeSnippetEntity());
      return ComeAndGoImpl.of(comeAndGoEntity.getId(), timeSnippet, comeAndGoEntity.isRecorded());
   }
}
