package cn.dyc.myshanxin.client.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class AppUtil {
	/**
	 * 从本地取图片(在cdcard中获取)
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 是否存在SDCard
	 * 
	 */
	public static boolean hasSdCard() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return true;
		}
		return false;
	}

	/**
	 * 将Bitmap保存在本地
	 * 
	 * @param mBitmap
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String saveBitmap(Bitmap mBitmap) {

		try {
			String sdCardPath = "";
			if (hasSdCard()) {
				sdCardPath = Environment.getExternalStorageDirectory()
						.getPath();
			} else {

			}

			String filePath = sdCardPath + "/" + "MicroShare/";

			Date date = new Date(System.currentTimeMillis());

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 时间格式-显示方式

			String imgPath = filePath + sdf.format(date) + ".png";

			File file = new File(filePath);

			if (!file.exists()) {
				file.mkdirs();
			}
			File imgFile = new File(imgPath);

			if (!imgFile.exists()) {
				imgFile.createNewFile();
			}

			FileOutputStream fOut = new FileOutputStream(imgFile);

			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

			fOut.flush();

			if (fOut != null) {

				fOut.close();
			}
			return imgPath;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
