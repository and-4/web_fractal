package com.example.webf.jpeg;

import com.example.webf.file.FileUtils;
import com.example.webf.log.SendToLog;

import java.util.Iterator;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

/**
 * @author And-4
 * 
 * Генерирует изображения фракталов и сохраняет в файлы
 */
public class JPEGCreator{
	private static int savingsCounter = 0;
	// счетчик сохранений

	/*
	 * Генерирует полное и предварительное изображение фрактала
	 */
	public void generateFullImg(int income_x, int income_y) throws Exception{   
    	//long startTime2 = System.currentTimeMillis(); для измерения времени работы метода
		
    	String filename = "fractal-img_" + income_x + "x" + income_y + ".jpg";
        try {
        	URL pathToJPG = new URL(FileUtils.getPathToProjectFolder());
        	// получаем полный путь к директории изображений
        	File file = null;
    		file = new File(pathToJPG.getPath(), filename);
			if (file.exists() == false) { 
				// проверяем - существует такой файл фрактала 
				BufferedImage bImage = new BufferedImage(640, 480, BufferedImage.TYPE_3BYTE_BGR);
				Graphics g = (Graphics2D) bImage.getGraphics();
				paintFractal(income_x,income_y,g,170,640,480);    
		        g.dispose();
				saveToFile(bImage, file);
				
				String previewFilename = "preview-img_" + income_x + "x" + income_y + ".jpg";
				// создаем предварительное изображение на основе полного изображения
				File prevFile = null;
				prevFile = new File(pathToJPG.getPath(), previewFilename);
				if (prevFile.exists() == false) {
					// иногда предварительное изображение уже существует
					BufferedImage resizedImage = new BufferedImage(160, 120, BufferedImage.TYPE_3BYTE_BGR);
					Graphics2D g2d = resizedImage.createGraphics();
					// изменяем размер изображения
					g2d.drawImage(bImage, 0, 0, 160, 120, null);
					g2d.dispose();	
					saveToFile(resizedImage, prevFile);}
			} else {
				// файл изображения уже существует
			}
		} catch (Exception e) {
			SendToLog.log("ERROR " + e.getMessage());
		}     
        //long solveTime2 = System.currentTimeMillis() - startTime2;
        //среднее время работы 200 мс
    }
    
	/*
	 * создает предварительное изображение и возвращает относительный путь к нему
	 */
    public String generatePreviewImg(int income_x, int income_y) throws Exception{  
    	String fileName = "preview-img_" + income_x + "x" + income_y + ".jpg";
        try {
        	URL pathToJPG = new URL(FileUtils.getPathToProjectFolder());
        	File file = null;
    		file = new File(pathToJPG.getPath(), fileName);
			if (file.exists() == false) {
				BufferedImage bImage = new BufferedImage(160, 120, BufferedImage.TYPE_3BYTE_BGR);
				Graphics g = (Graphics2D) bImage.getGraphics();
				paintFractal(income_x,income_y,g,40,160,120);
				g.dispose();
				saveToFile(bImage, file);
			} else {
				// файл изображения уже существует
			}			
		} catch (Exception e) {
			SendToLog.log("ERROR " + e.getMessage());
		}     
    	return fileName;
    }
    
    /*
     *  сохраняет изображение в файл
     */
    private static void saveToFile( BufferedImage img, File file ) throws IOException {
    	savingsCounter++;
    	if (savingsCounter>1000){
    		FileUtils.chekImgFolderSize();
    		savingsCounter=0;} 
        ImageWriter writer = null;
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpg");
        if( iter.hasNext() ){
            writer = (ImageWriter)iter.next();
        }
        ImageOutputStream ios = null;
		try {
			ios = ImageIO.createImageOutputStream(file);
			writer.setOutput(ios);
			ImageWriteParam param = new JPEGImageWriteParam(java.util.Locale.getDefault());
	        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT) ;
	        param.setCompressionQuality(0.95f);
	        writer.write(null, new IIOImage( img, null, null ), param);
		} catch (Exception e) {
			SendToLog.log("ERROR " + e.getMessage());
		} finally {
			ios.close();
		} 
    }   
    
    /*
     *     Рисует поточечно фрактал Жюлиа
     *       В основе построения данных фракталов лежит нелинейная функция 
     *       комплексного итерационного процесса с обратной связью z=> z²+c. 
     *       Поскольку z и с -комплексные числа, то z=x+iy и c=p+iq необходимо 
     *       разложить его на х и у чтобы перейти в более наглядную плоскость:
     *       x(k+1)=x(k)²-y(k)² + p
     *       y(k+1)=2*x(k)*y(k) + q.
     *       , где k означает номер итариции.
     *          Плоскость, состоящая из всех пар (x,y), рассматривается при фиксированных
     *        значениях р и q (вводимые пользователем x и y). 
     *            Перебирая все точки (х,у) плоскости и окрашивая их в зависимости от 
     *        количества повторений функции необходимых для выхода из итерационного процесса 
     *        или окрашивая в темно-зеленый цвет при привышении допустимого максимума 
     *        повторений мы получим отображение множества Жюлиа.
     *            Точки окрашены желтым цветом в том случае, если они дошли 
     *        до конца итераций без превышения порогового значения. 
     *        Если точка превысила порог в конце серии итераций - она окрашивается 
     *        в светло-зеленый оттенок. Если точка превысила порог в начале 
     *        серии - она окрашивается в более темный оттенок. Количество итераций - 100 шт.
     *        
     *        @param xRes горизонтальный размер изображения
     *        @param yRes вертикальный размер изображения 
     *        
     */
    private static void paintFractal (int income_x, int income_y, Graphics g,int zoom, int xRes, int yRes ){
	    double s_x, s_y, z_x, z_y, x2, y2, c_x, c_y, absv2, xtrans, ytrans;
		int count, x, y, max_iter;
	
	    max_iter=100;
	    xtrans=0;
	    ytrans=0;
	    
		c_x =(double) (income_x)/100;
		c_y =(double) (income_y)/100;
	
	    for (x=0; x<xRes; x++){
	      s_x=(double)(x-xRes/2)/zoom+xtrans;
	      for (y=0; y<yRes; y++){
	        s_y=(double)(yRes/2-y)/zoom+ytrans;
	        for (count=0,z_x=s_x,z_y=s_y,absv2=0.0; absv2<=4.0 && count<=max_iter; count++){
	          x2=z_x*z_x;
	          y2=z_y*z_y;
	          z_y=2*z_x*z_y+c_y;
	          z_x=x2-y2+c_x;
	          absv2=x2+y2;
	          }
	        Color mycolor = (absv2<=4) ? new Color(250,250,0): new Color(0,50+(count*8)%200,0);
	        g.setColor(mycolor);
	        g.drawLine(x,y,x,y);
	        }
	      }
	    }	
}