package com.org.palmcampus.email.bean;
/**
 * @类名 附件类
 * @作者 Rolant
 * @版本 1.0
 */
import java.io.File;
import java.io.Serializable;

import android.graphics.Bitmap;
//附件类
public class Attachment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String filePath;//附件路径
	private String fileName;//文件名称
	private long fileSize;//文件大小
	private Bitmap image;//附件包含的图像
	private String imageBuffer;//将要上传的图片字符串流
	
	public String getImageBuffer() {
		return imageBuffer;
	}

	public void setImageBuffer(String imageBuffer) {
		this.imageBuffer = imageBuffer;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public Attachment() {
		super();
	}
	
	public Attachment(String filePath, String fileName, long fileSize) {
		super();
		this.filePath = filePath;
		this.fileName = fileName;
		this.fileSize = fileSize;
	}

	
	/**
	 * 鏂囦欢澶у皬杞崲
	 * @param size
	 * @return
	 */
	public static String convertStorage(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }
	
	/**
	 * 鑾峰彇瀹屾暣鐨勭洰褰曞悕瀛�
	 * @param filepath
	 * @return
	 */
	public static String getNameFromFilepath(String filepath) {
        int pos = filepath.lastIndexOf('/');
        if (pos != -1) {
            return filepath.substring(pos + 1);
        }
        return "";
    }
	
	
	/**
	 * 鑾峰彇闄勪欢灞炴�
	 * @param filePath
	 * @return
	 */
    public static Attachment GetFileInfo(String filePath) {
        File file = new File(filePath);
        if (!file.exists())
            return null;
        Attachment fileInfo = new Attachment(); 
        System.out.println(fileInfo);
        fileInfo.fileName = getNameFromFilepath(filePath);
        System.out.println(fileInfo.fileName);
        fileInfo.filePath = filePath;
        fileInfo.fileSize = file.length();
        return fileInfo;
    }

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

}
