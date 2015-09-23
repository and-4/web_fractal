package com.example.webf.fractal_services;

import java.util.ArrayList;
import java.util.List;

import com.example.webf.domain.UserEntity;
import com.example.webf.file.FileUtils;
import com.example.webf.fractal.FractalEntity;
import com.example.webf.fractal_dao.FractalDao;
import com.example.webf.jpeg.JPEGCreator;

import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

/**
 * @author And-4
 * 
 * Для работы с фракталами с использованием БД
 */
public class FractalServiceImpl implements FractalService{
	
	private FractalDao fractalDao;  // ДАО для работы с фракталами
	private boolean isFractalInDB = false;  // показывает, хранился ли этот новый фрактал в базе данных
	private static JPEGCreator jpegCreator = new JPEGCreator(); // объект для создания изображений фракталов

	
	/*
	 * Создает изображение фрактала и сохраняет фрактал в БД
	 * Возвращает относительный путь к изображению
	 */
	public String createFractal(FractalEntity fractalEntity) {  
		setFractalInDB(false);
		String pathToImg = FileUtils.getRelativePathToImg(fractalEntity);
		try {
			fractalEntity.setId(getCurrentUserEntity().getId());
			jpegCreator.generateFullImg(Integer.parseInt(fractalEntity.getC_x()), Integer.parseInt(fractalEntity.getC_y()));
			if (fractalDao.checkAvailable(fractalEntity)){
				fractalDao.save(fractalEntity);
			}
			else {
				setFractalInDB(true);
			}
		} catch (Exception e) {
		}
		return pathToImg;
	}
	
	/*
	 * Возвращает fid фрактала, который открыт для просмотра пользователем
	 */
	public long getFIDFromCurrentFileNameAndUserID(){ 
		FractalEntity fEntity = null; 
		try {
			//FractalController fc = new FractalController();
			String strValue = FractalController.getShowMeFractalParameter();
			if (strValue == null){
				return -1;
			}
			int[] arrInt = FractalController.getIntegersFromFileName(strValue);
			UserEntity uEntity = getCurrentUserEntity();
			fEntity = fractalDao.loadFractalByXYUserID( Integer.toString(arrInt[0]) ,Integer.toString(arrInt[1]),uEntity.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return fEntity.getFid();
		
	}
	
	
	/*
	 * возвращает список всех фракталов пользователя
	 */
	public List<FractalEntity> getAllFractalsByUserID(long id){
			List<FractalEntity> fractalList = fractalDao.loadAllByUserID(id);
			return fractalList;
	}
	
	
	/*
	 * Возвращает 3-х уровневый список для создания html таблицы, содержащей все фракталы пользователя.
	 * 1 уровень описывает строки таблицы, 2 - ячейки, 3 - относительные ссылки на preview и подписи 
	 * к preview   
	 */
	public List<List<List<String>>> getLinksAndSignaturesToPreviewImagesByUserID(long id){		
		List<FractalEntity> fractalList = getAllFractalsByUserID(id);
		if (fractalList.size()==0 || fractalList == null){
			return null;
		}
		List<String> linksList = new ArrayList<String>(fractalList.size());
		List<String> signatureList = new ArrayList<String>(fractalList.size());
		for (int i = 0; i < fractalList.size(); i++) {
			linksList.add(FileUtils.generateAndGetRelativePathToPreviewImg(fractalList.get(i)));
			signatureList.add("x = " + fractalList.get(i).getC_x() + ", y = " + fractalList.get(i).getC_y());
			}
		List<List<List<String>>> listOfListsOfLists = create3LevelList(linksList,signatureList);
		return listOfListsOfLists;
	}
	
	/*
	 * Попарно складывает относительные ссылки на preview и подписи к фракталам.
	 * Возвращает 3 уровневый список для итерирования с помощью тега <ui:repeat> 
	 * в файле accountHome.xhtml
	 */
	public List<List<List<String>>> create3LevelList(List<String> linksList,List<String> signatureList){
		if(linksList.size()!= signatureList.size()){
			return null;
		}
		List<List<String>> newListOfLists = new ArrayList<List<String>>(); 
		for(int i = 0; i<linksList.size(); i++){
			List<String> ll = new ArrayList<String>();
			ll.add(linksList.get(i));
			ll.add(signatureList.get(i));
			newListOfLists.add(ll);
		}
		List<List<List<String>>> listOfListsOfLists = FractalController.convert2LevelListTo3LevelList(newListOfLists);
		return listOfListsOfLists;
	}
	
	/*
	 *  Удаляет фрактал из БД
	 */
	public boolean deleteFractal(FractalEntity fractalEntity){
		try {
			fractalDao.delete(fractalEntity);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/*
	 * Удаляет фрактал, просматриваемый пользователем
	 */
	public boolean deleteFractalByCurrentXYUserID(){
		boolean isDeleted = false;
		long fid = getFlowScopeFractalFID();
		isDeleted = deleteFractalByFid(fid);
		return isDeleted;
		}
	
	/*
	 * Удаляет фрактал по его fid
	 */
	public boolean deleteFractalByFid(long fid){
		FractalEntity fEntity = fractalDao.loadFractalByFid(fid);
		if (fEntity==null){
			return false;
		}
		boolean result = deleteFractal(fEntity);		
		return result;
	}
	
	/*
	 * Возвращает значение переменной flowScope.fractalFID
	 */
	private long getFlowScopeFractalFID(){
		RequestContext requestContext = RequestContextHolder.getRequestContext();
		long fid = -1;
		try {
			fid = (Long) requestContext.getFlowScope().get("fractalFID");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}		
		return fid;}

	
	/*
	 * Возвращает текущего пользователя
	 */
	public UserEntity getCurrentUserEntity(){
		RequestContext requestContext = RequestContextHolder.getRequestContext();
		UserEntity currentUser =(UserEntity) requestContext.getFlowScope().get("user");
		return currentUser;
	}

	
	public FractalDao getFractalDao(){
		return fractalDao;
	}
	
	public void setFractalDao(FractalDao fractalDao){
		this.fractalDao = fractalDao;
	}
	
	public boolean isFractalInDB() {
		return isFractalInDB;
	}

	public void setFractalInDB(boolean isFractalInDB) {
		this.isFractalInDB = isFractalInDB;
	}
}