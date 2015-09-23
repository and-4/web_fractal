package com.example.webf.fractal_dao;


import java.io.Serializable;
import java.util.List;

/**
 * Базовый интерфейс DAO для работы с фракталами.
 */


public interface GenericFractalDao <T, FID extends Serializable>  {
	
	  T save(T entity);
      T update(T entity);
      void delete(T entity);
      T findByFID(FID fid);
      List<T> findAll();
      void flush();
}
