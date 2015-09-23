package com.example.webf.fractal_services;

import java.util.List;

import com.example.webf.fractal.FractalEntity;

/**
 * @author And-4
 * 
 * Интерфейс для работы с фракталами с использованием БД
 */
public interface FractalService {
	
	/*
	 * Создает изображение фрактала и сохраняет фрактал в БД
	 * Возвращает относительный путь к изображению
	 */
	String createFractal(FractalEntity fractalEntity);
	
	/*
	 * возвращает список всех фракталов пользователя
	 */
	List<FractalEntity> getAllFractalsByUserID(long id);
	
	/*
	 * Возвращает 3-х уровневый список для создания html таблицы, содержащей все фракталы пользователя.
	 * 1 уровень описывает строки таблицы, 2 - ячейки, 3 - относительные ссылки на preview и подписи 
	 * к preview   
	 */
	List<List<List<String>>> getLinksAndSignaturesToPreviewImagesByUserID(long id);
	
	/*
	 *  Удаляет фрактал из БД
	 */
	boolean deleteFractal(FractalEntity fractalEntity);
	
	/*
	 * Удаляет фрактал по его fid
	 */
	boolean deleteFractalByFid(long fid);
	
}
