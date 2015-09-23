package com.example.webf.fractal_dao;

import java.util.List;

import com.example.webf.fractal_dao.GenericFractalDao;
import com.example.webf.fractal.FractalEntity;
	
/**
 * @author And-4
 * 
 * Дополнительный интерфейс DAO для работы с фракталами 
 */

public interface FractalDao extends GenericFractalDao<FractalEntity, Long>{
	 
	/*
	 * проверяет - доступен ли фрактал для создания
	 * true - доступен (еще нет в БД)
	 */
    boolean checkAvailable(FractalEntity fractalEntity);
    
    /*
     * Возвращает фрактал по fid
     */
	FractalEntity loadFractalByFid(long l);
	
	/*
	 *  Возвращвет список все фракталов пользователя по его id
	 */
	List<FractalEntity>  loadAllByUserID(long id);
	
	/*
	 * Возвращает фрактал по значениям x, y и id пользователя
	 */
	FractalEntity loadFractalByXYUserID(String x, String y, long id);

}
