package com.example.webf.file;

import com.example.webf.fractal.FractalEntity;
import com.example.webf.jpeg.JPEGCreator;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.util.Assert;

/**
 * @author And-4
 * 
 * Утилитный класс для работы с файлами и деректориями
 */

public class FileUtils {
	
	private static String pathToProjectFolder = null;  
	// хранит полный путь к директории fractal_img,
	// создается один раз при старте и хранится все время действия проекта
	private static JPEGCreator jCreator = new JPEGCreator();
	// объект для создания изображений фракталов
	
	/*
	 * после развертывания проекта ищет в директории проекта директорию com и возвращает полный путь к ней
	 * (com уникальна для всего проекта и всегда доступна для поиска)
	 */
	public static URL getFullFolderURL() throws MalformedURLException{ 
		String folderName = "com";
		URL url = new FileUtils().getClass().getClassLoader().getResource(folderName);
		Assert.notNull(url,"There is no access to the file system ");
		return url;
	}
	
	/*
	 * Возвращает полный путь к директории fractal_img
	 * Поиск непосредственно директории fractal_img не дает результата
	 */
	public static String getPathToProjectFolder() throws MalformedURLException {  
		if(pathToProjectFolder!=null){
			return pathToProjectFolder;
		}
		String fileSeparator = System.getProperty("file.separator");
		String inStr = getFullFolderURL().toString();
		String[] incomeArr;  // массив с именами папок
		incomeArr = inStr.split("/");	
		ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(incomeArr));
		if (arrayList.size() < 2){
			return null;
			
		}		
		arrayList.subList(arrayList.size()-3, arrayList.size()).clear();  
		// удаляет 3 последние директории для того, чтобы получить путь в директорию webfractal 
		arrayList.add("fractal_img");
		pathToProjectFolder = "";
		for (String i : arrayList){
			pathToProjectFolder+=i+fileSeparator;		
		}
		return pathToProjectFolder;
	}
	
	/*
	 * формирует относительный адрес изображения	
	 */
	public static String getRelativePathToImg(FractalEntity fractalEntity){
		String path = "/fractal_img/fractal-img_" +fractalEntity.getC_x() + "x" + fractalEntity.getC_y() + ".jpg";
		return path;} 

	/*
	 * создает предварительное изображение и возвращает его относительный адрес 
	 */
	public static String generateAndGetRelativePathToPreviewImg(FractalEntity fractalEntity){ 
		String relativePath = null;
		try {
			relativePath = jCreator.generatePreviewImg(Integer.parseInt(fractalEntity.getC_x()), Integer.parseInt(fractalEntity.getC_y()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return relativePath;}
	
	/*
	 * Проверяет размер директории с изображениями
	 * При превышении размера 200 Мб очищает директорию
	 */
	public static void chekImgFolderSize(){  
    	try {
			String strFile = FileUtils.getPathToProjectFolder();
			File imgDirectory = new File(strFile.substring(6));
			// удаляет лишние символы(file: ) для получения полного адреса
			if (getFolderSize(imgDirectory)>209715200){
				clearImgFolder(imgDirectory);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
	/*
	 * очищает директорию
	 */
    private static void clearImgFolder(File directory){  
    	for (File file : directory.listFiles()) {
        	if (file.isFile()){            	
                file.delete();}
        	}
		}

    /*
     * подсчитывает размер всех файлов в директории
     */
    private static long getFolderSize(File directory) {  
        long length = 0;
        for (File file : directory.listFiles()) {
        	length += file.length();}
        return length;
    }
}