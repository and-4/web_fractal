package com.example.webf.fractal_services;

import com.example.webf.jpeg.JPEGCreator;
import com.example.webf.file.FileUtils;
import com.example.webf.fractal.FractalEntity;
import com.example.webf.log.SendToLog;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.stereotype.Repository;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

/**
 * @author And-4
 * 
 * Для работы с фракталами без использования БД
 */
@Repository
public class FractalController implements Serializable {
	private static final long serialVersionUID = 5222194951913090224L;
	private JPEGCreator jpegCreator = new JPEGCreator();
	// объект для создания изображений фракталов
	
	/*
	 * создает файл фрактала без сохранения его в БД и возвращает относительный путь к нему
	 */
	public String createFractalWithoutDAO(FractalEntity fractalEntity){  
		String pathToImg = FileUtils.getRelativePathToImg(fractalEntity);
		try {			
			jpegCreator.generateFullImg(Integer.parseInt(fractalEntity.getC_x()),
					Integer.parseInt(fractalEntity.getC_y()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pathToImg;
	}
	
	/*
	 * возвращает подпись к фракталу 
	 */
	public String getFractalSignature(FractalEntity fractalEntity){  
		String fractalSignature = "x = " + fractalEntity.getC_x() + ", y = " + fractalEntity.getC_y();
		return fractalSignature;
	}
	 /*
	  *  создает изображение фрактала, читая данные из параметров URL
	  */
	public String generateImgAndGetFullImgName(){		
		String strValue = getShowMeFractalParameter();
		generateJPGFromFileName(strValue);
		String newStrValue = strValue.replaceFirst("preview", "fractal");
		return newStrValue;
	}
	
	/*
	 *  создает подпись к фракталу из параметра URL
	 */
	public String getSignatureFromParameter(){		 
		String strValue = getShowMeFractalParameter();
		int[] arrInt = getIntegersFromFileName(strValue);
		String newStrValue = "x = " + arrInt[0] + ", y = " + arrInt[1];
		return newStrValue;
	}

	/*
	 * если пользователь просматривает свои старые фракталы,
	 * мы не держим фракталы в памяти, а храним x и y в URL параметре "showMeFractal"
	 * в ссылке на каждый фрактал
	 */
	static String getShowMeFractalParameter() {  
		RequestContext requestContext = RequestContextHolder.getRequestContext();
		ParameterMap pm = requestContext.getRequestParameters();
		String strValue = pm.get("showMeFractal");
		return strValue;
	}
	
	/*
	 * создает изображение фрактала, читая x и y из имени файла
	 * Для тех случаев когда пользователь просматривает свои старые фракталы
	 * - нужно убедится, что есть нужное изображение
	 */
	private void generateJPGFromFileName(String inStr){  
		int[] arrInt = getIntegersFromFileName(inStr);
		try {
			jpegCreator.generateFullImg(arrInt[0], arrInt[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * возвращает x и y в числовом виде из имени файла
	 */
	static int[] getIntegersFromFileName(String inStr){  
		String firstVal = (String) inStr.subSequence(inStr.indexOf("_")+1, inStr.indexOf("x"));
		String secondVal = (String) inStr.subSequence(inStr.indexOf("x")+1, inStr.indexOf("."));
		int firstInt = Integer.parseInt(firstVal);
		int secondInt = Integer.parseInt(secondVal);
		int[] arrInt = new int[2];
		arrInt[0] = firstInt;
		arrInt[1] = secondInt;
		return arrInt;
	}
	
	/*
	 * если пользователь нажал предварительное изображение - 
	 *  в URL передается параметер "showMeFractal" и мы направляем
	 *  пользователя во view showUserFractal для просмотра полного изображения
	 *  Если нет параметра - в первичный view accountHome 
	 */
	public boolean whatNextView(){  
		RequestContext requestContext = RequestContextHolder.getRequestContext();
		ParameterMap pm = requestContext.getRequestParameters();
	    if (pm.contains("showMeFractal")) {
	    	return true;			
		} else {
			return false;}
	}
	
	/*
	 * Превращает 2 уровневый список строк (содержащий в 1 строке имя файла, во 2 - подпись к фракталу)
	 * в 3 уровневый для использования с тегом <ui:repeat> в файле accountHome.xhtml
	 * Данный тег генерирует html таблицу по всем фракталам пользователя
	 */
	static List<List<List<String>>> convert2LevelListTo3LevelList(List<List<String>> inList){  //+
			int start=0;
			int sizeOfList = 3;
			int inSize = inList.size()/sizeOfList + 1;
			List<List<List<String>>> listOfLists = new ArrayList<List<List<String>>>(); 
			for(int i = 0; i<inSize; i++){
				if (start+sizeOfList<=inList.size()) {
					listOfLists.add(new ArrayList<List<String>>(inList.subList(start,start+sizeOfList))); //new ArrayList(inList.subList(start,start+sizeOfList))
				} else if (inList.size()%sizeOfList>0){
					listOfLists.add(new ArrayList<List<String>>(inList.subList(start,start+inList.size()%sizeOfList))); //new ArrayList(inList.subList(start,start+inList.size()%sizeOfList)))
				}
				start += sizeOfList;
			}
			return listOfLists;
		}
	
	/*
	 * выводит в консоль Unix время в виде yyyy-MM-dd HH:mm:ss
	 */
	public static void showNormalTime(long unixSeconds){
		Date date = new Date(unixSeconds); 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		sdf.setTimeZone(TimeZone.getDefault());
		String formattedDate = sdf.format(date);
		SendToLog.log(formattedDate);
		}
}
	