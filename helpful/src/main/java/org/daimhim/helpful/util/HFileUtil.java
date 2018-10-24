/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.daimhim.helpful.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.ab.global.AbAppConfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn 名称：HFileUtil.java 描述：文件操作类.
 */
public class HFileUtil {

	/** 默认APP根目录. */
	private static String downloadRootDir = null;

	/** 默认下载图片文件目录. */
	private static String imageDownloadDir = null;

	/** 默认下载文件目录. */
	private static String fileDownloadDir = null;

	/** 默认缓存目录. */
	private static String cacheDownloadDir = null;

	/** 默认下载数据库文件的目录. */
	private static String dbDownloadDir = null;

	/** 剩余空间大于200M才使用SD缓存. */
	private static int freeSdSpaceNeededToCache = 200 * 1024 * 1024;

	/**
	 * 描述：通过文件的网络地址从SD卡中读取图片，如果SD中没有则自动下载并保存.
	 * 
	 * @param url
	 *            文件的网络地址
	 * @param type
	 *            图片的处理类型（剪切或者缩放到指定大小，参考AbImageUtil类） 如果设置为原图，则后边参数无效，得到原图
	 * @param desiredWidth
	 *            新图片的宽
	 * @param desiredHeight
	 *            新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap getBitmapFromSD(String url, int type, int desiredWidth, int desiredHeight) {
		Bitmap bitmap = null;
		try {
			if (HStrUtil.isEmpty(url)) {
				return null;
			}

			// SD卡不存在 或者剩余空间不足了就不缓存到SD卡了
			if (!isCanUseSD() || freeSdSpaceNeededToCache < freeSpaceOnSD()) {
				bitmap = getBitmapFromURL(url, type, desiredWidth, desiredHeight);
				return bitmap;
			}
			// 下载文件，如果不存在就下载，存在直接返回地址
			String downFilePath = downloadFile(url, imageDownloadDir);
			if (downFilePath != null) {
				// 获取图片
				return getBitmapFromSD(new File(downFilePath), type, desiredWidth, desiredHeight);
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 描述：通过文件的本地地址从SD卡读取图片.
	 *
	 * @param file
	 *            the file
	 * @param type
	 *            图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类） 如果设置为原图，则后边参数无效，得到原图
	 * @param desiredWidth
	 *            新图片的宽
	 * @param desiredHeight
	 *            新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap getBitmapFromSD(File file, int type, int desiredWidth, int desiredHeight) {
		Bitmap bitmap = null;
		try {
			// SD卡是否存在
			if (!isCanUseSD()) {
				return null;
			}

			// 文件是否存在
			if (!file.exists()) {
				return null;
			}

			// 文件存在
			if (type == HImageUtil.CUTIMG) {
				bitmap = HImageUtil.cutImg(file, desiredWidth, desiredHeight);
			} else if (type == HImageUtil.SCALEIMG) {
				bitmap = HImageUtil.scaleImg(file, desiredWidth, desiredHeight);
			} else {
				bitmap = HImageUtil.getBitmap(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 描述：通过文件的本地地址从SD卡读取图片.
	 *
	 * @param file
	 *            the file
	 * @return Bitmap 图片
	 */
	public static Bitmap getBitmapFromSD(File file) {
		Bitmap bitmap = null;
		try {
			// SD卡是否存在
			if (!isCanUseSD()) {
				return null;
			}
			// 文件是否存在
			if (!file.exists()) {
				return null;
			}
			// 文件存在
			bitmap = HImageUtil.getBitmap(file);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 描述：将图片的byte[]写入本地文件.
	 * 
	 * @param imgByte
	 *            图片的byte[]形势
	 * @param fileName
	 *            文件名称，需要包含后缀，如.jpg
	 * @param type
	 *            图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类）
	 * @param desiredWidth
	 *            新图片的宽
	 * @param desiredHeight
	 *            新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap getBitmapFromByte(byte[] imgByte, String fileName, int type, int desiredWidth,
			int desiredHeight) {
		FileOutputStream fos = null;
		DataInputStream dis = null;
		ByteArrayInputStream bis = null;
		Bitmap bitmap = null;
		File file = null;
		try {
			if (imgByte != null) {

				file = new File(imageDownloadDir + fileName);
				if (!file.exists()) {
					file.createNewFile();
				}
				fos = new FileOutputStream(file);
				int readLength = 0;
				bis = new ByteArrayInputStream(imgByte);
				dis = new DataInputStream(bis);
				byte[] buffer = new byte[1024];

				while ((readLength = dis.read(buffer)) != -1) {
					fos.write(buffer, 0, readLength);
					try {
						Thread.sleep(500);
					} catch (Exception e) {
					}
				}
				fos.flush();

				bitmap = getBitmapFromSD(file, type, desiredWidth, desiredHeight);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dis != null) {
				try {
					dis.close();
				} catch (Exception e) {
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e) {
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
		return bitmap;
	}

	/**
	 * 描述：根据URL从互连网获取图片.
	 * 
	 * @param url
	 *            要下载文件的网络地址
	 * @param type
	 *            图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类）
	 * @param desiredWidth
	 *            新图片的宽
	 * @param desiredHeight
	 *            新图片的高
	 * @return Bitmap 新图片
	 */
	public static Bitmap getBitmapFromURL(String url, int type, int desiredWidth, int desiredHeight) {
		Bitmap bit = null;
		try {
			bit = HImageUtil.getBitmap(url, type, desiredWidth, desiredHeight);
		} catch (Exception e) {
			HLogUtil.d(HFileUtil.class, "下载图片异常：" + e.getMessage());
		}
		return bit;
	}

	/**
	 * 描述：获取src中的图片资源.
	 *
	 * @param src
	 *            图片的src路径，如（“image/arrow.png”）
	 * @return Bitmap 图片
	 */
	public static Bitmap getBitmapFromSrc(String src) {
		Bitmap bit = null;
		try {
			bit = BitmapFactory.decodeStream(HFileUtil.class.getResourceAsStream(src));
		} catch (Exception e) {
			HLogUtil.d(HFileUtil.class, "获取图片异常：" + e.getMessage());
		}
		return bit;
	}

	/**
	 * 描述：获取Asset中的图片资源.
	 *
	 * @param context
	 *            the context
	 * @param fileName
	 *            the file name
	 * @return Bitmap 图片
	 */
	public static Bitmap getBitmapFromAsset(Context context, String fileName) {
		Bitmap bit = null;
		try {
			AssetManager assetManager = context.getAssets();
			InputStream is = assetManager.open(fileName);
			bit = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			HLogUtil.d(HFileUtil.class, "获取图片异常：" + e.getMessage());
		}
		return bit;
	}

	/**
	 * 描述：获取Asset中的图片资源.
	 *
	 * @param context
	 *            the context
	 * @param fileName
	 *            the file name
	 * @return Drawable 图片
	 */
	public static Drawable getDrawableFromAsset(Context context, String fileName) {
		Drawable drawable = null;
		try {
			AssetManager assetManager = context.getAssets();
			InputStream is = assetManager.open(fileName);
			drawable = Drawable.createFromStream(is, null);
		} catch (Exception e) {
			HLogUtil.d(HFileUtil.class, "获取图片异常：" + e.getMessage());
		}
		return drawable;
	}

	/**
	 * 下载网络文件到SD卡中.如果SD中存在同名文件将不再下载
	 *
	 * @param url
	 *            要下载文件的网络地址
	 * @param dirPath
	 *            the dir path
	 * @return 下载好的本地文件地址
	 */
	public static String downloadFile(String url, String dirPath) {
		InputStream in = null;
		FileOutputStream fileOutputStream = null;
		HttpURLConnection connection = null;
		String downFilePath = null;
		File file = null;
		try {
			if (!isCanUseSD()) {
				return null;
			}
			// 先判断SD卡中有没有这个文件，不比较后缀部分比较
			String fileNameNoMIME = getCacheFileNameFromUrl(url);
			File parentFile = new File(imageDownloadDir);
			File[] files = parentFile.listFiles();
			for (int i = 0; i < files.length; ++i) {
				String fileName = files[i].getName();
				String name = fileName.substring(0, fileName.lastIndexOf("."));
				if (name.equals(fileNameNoMIME)) {
					// 文件已存在
					return files[i].getPath();
				}
			}

			URL mUrl = new URL(url);
			connection = (HttpURLConnection) mUrl.openConnection();
			connection.connect();
			// 获取文件名，下载文件
			String fileName = getCacheFileNameFromUrl(url, connection);

			file = new File(imageDownloadDir, fileName);
			downFilePath = file.getPath();
			if (!file.exists()) {
				file.createNewFile();
			} else {
				// 文件已存在
				return file.getPath();
			}
			in = connection.getInputStream();
			fileOutputStream = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int temp = 0;
			while ((temp = in.read(b)) != -1) {
				fileOutputStream.write(b, 0, temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			HLogUtil.e(HFileUtil.class, "有文件下载出错了,已删除");
			// 检查文件大小,如果文件为0B说明网络不好没有下载成功，要将建立的空文件删除
			if (file != null) {
				file.delete();
			}
			file = null;
			downFilePath = null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (connection != null) {
					connection.disconnect();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return downFilePath;
	}

	/**
	 * 描述：获取网络文件的大小.
	 *
	 * @param Url
	 *            图片的网络路径
	 * @return int 网络文件的大小
	 */
	public static int getContentLengthFromUrl(String Url) {
		int mContentLength = 0;
		try {
			URL url = new URL(Url);
			HttpURLConnection mHttpURLConnection = (HttpURLConnection) url.openConnection();
			mHttpURLConnection.setConnectTimeout(5 * 1000);
			mHttpURLConnection.setRequestMethod("GET");
			mHttpURLConnection.setRequestProperty("Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
			mHttpURLConnection.setRequestProperty("Referer", Url);
			mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
			mHttpURLConnection.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			mHttpURLConnection.connect();
			if (mHttpURLConnection.getResponseCode() == 200) {
				// 根据响应获取文件大小
				mContentLength = mHttpURLConnection.getContentLength();
			}
		} catch (Exception e) {
			e.printStackTrace();
			HLogUtil.d(HFileUtil.class, "获取长度异常：" + e.getMessage());
		}
		return mContentLength;
	}

	/**
	 * 获取文件名，通过网络获取.
	 * 
	 * @param url
	 *            文件地址
	 * @return 文件名
	 */
	public static String getRealFileNameFromUrl(String url) {
		String name = null;
		try {
			if (HStrUtil.isEmpty(url)) {
				return name;
			}

			URL mUrl = new URL(url);
			HttpURLConnection mHttpURLConnection = (HttpURLConnection) mUrl.openConnection();
			mHttpURLConnection.setConnectTimeout(5 * 1000);
			mHttpURLConnection.setRequestMethod("GET");
			mHttpURLConnection.setRequestProperty("Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			mHttpURLConnection.setRequestProperty("Accept-Language", "zh-CN");
			mHttpURLConnection.setRequestProperty("Referer", url);
			mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
			mHttpURLConnection.setRequestProperty("User-Agent", "");
			mHttpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			mHttpURLConnection.connect();
			if (mHttpURLConnection.getResponseCode() == 200) {
				for (int i = 0;; i++) {
					String mine = mHttpURLConnection.getHeaderField(i);
					if (mine == null) {
						break;
					}
					if ("content-disposition".equals(mHttpURLConnection.getHeaderFieldKey(i).toLowerCase())) {
						Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
						if (m.find())
							return m.group(1).replace("\"", "");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			HLogUtil.e(HFileUtil.class, "网络上获取文件名失败");
		}
		return name;
	}

	/**
	 * 获取真实文件名（xx.后缀），通过网络获取.
	 * 
	 * @param connection
	 *            连接
	 * @return 文件名
	 */
	public static String getRealFileName(HttpURLConnection connection) {
		String name = null;
		try {
			if (connection == null) {
				return name;
			}
			if (connection.getResponseCode() == 200) {
				for (int i = 0;; i++) {
					String mime = connection.getHeaderField(i);
					if (mime == null) {
						break;
					}
					// "Content-Disposition","attachment; filename=1.txt"
					// Content-Length
					if ("content-disposition".equals(connection.getHeaderFieldKey(i).toLowerCase())) {
						Matcher m = Pattern.compile(".*filename=(.*)").matcher(mime.toLowerCase());
						if (m.find()) {
							return m.group(1).replace("\"", "");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			HLogUtil.e(HFileUtil.class, "网络上获取文件名失败");
		}
		return name;
	}

	/**
	 * 获取文件名（不含后缀）.
	 *
	 * @param url
	 *            文件地址
	 * @return 文件名
	 */
	public static String getCacheFileNameFromUrl(String url) {
		if (HStrUtil.isEmpty(url)) {
			return null;
		}
		String name = null;
		try {
			name = HMd5.MD5(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 获取文件名（.后缀），外链模式和通过网络获取.
	 *
	 * @param url
	 *            文件地址
	 * @param connection
	 *            the connection
	 * @return 文件名
	 */
	public static String getCacheFileNameFromUrl(String url, HttpURLConnection connection) {
		if (HStrUtil.isEmpty(url)) {
			return null;
		}
		String name = null;
		try {
			// 获取后缀
			String suffix = getMIMEFromUrl(url, connection);
			if (HStrUtil.isEmpty(suffix)) {
				suffix = ".ab";
			}
			name = HMd5.MD5(url) + suffix;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 获取文件后缀，本地.
	 *
	 * @param url
	 *            文件地址
	 * @param connection
	 *            the connection
	 * @return 文件后缀
	 */
	public static String getMIMEFromUrl(String url, HttpURLConnection connection) {

		if (HStrUtil.isEmpty(url)) {
			return null;
		}
		String suffix = null;
		try {
			// 获取后缀
			if (url.lastIndexOf(".") != -1) {
				suffix = url.substring(url.lastIndexOf("."));
				if (suffix.indexOf("/") != -1 || suffix.indexOf("?") != -1 || suffix.indexOf("&") != -1) {
					suffix = null;
				}
			}
			if (HStrUtil.isEmpty(suffix)) {
				// 获取文件名 这个效率不高
				String fileName = getRealFileName(connection);
				if (fileName != null && fileName.lastIndexOf(".") != -1) {
					suffix = fileName.substring(fileName.lastIndexOf("."));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return suffix;
	}

	/**
	 * 描述：从sd卡中的文件读取到byte[].
	 *
	 * @param path
	 *            sd卡中文件路径
	 * @return byte[]
	 */
	public static byte[] getByteArrayFromSD(String path) {
		byte[] bytes = null;
		ByteArrayOutputStream out = null;
		try {
			File file = new File(path);
			// SD卡是否存在
			if (!isCanUseSD()) {
				return null;
			}
			// 文件是否存在
			if (!file.exists()) {
				return null;
			}

			long fileSize = file.length();
			if (fileSize > Integer.MAX_VALUE) {
				return null;
			}

			FileInputStream in = new FileInputStream(path);
			out = new ByteArrayOutputStream(1024);
			byte[] buffer = new byte[1024];
			int size = 0;
			while ((size = in.read(buffer)) != -1) {
				out.write(buffer, 0, size);
			}
			in.close();
			bytes = out.toByteArray();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception e) {
				}
			}
		}
		return bytes;
	}

	/**
	 * 描述：将byte数组写入文件.
	 *
	 * @param path
	 *            the path
	 * @param content
	 *            the content
	 * @param create
	 *            the create
	 */
	public static void writeByteArrayToSD(String path, byte[] content, boolean create) {

		FileOutputStream fos = null;
		try {
			File file = new File(path);
			// SD卡是否存在
			if (!isCanUseSD()) {
				return;
			}
			// 文件是否存在
			if (!file.exists()) {
				if (create) {
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
						file.createNewFile();
					}
				} else {
					return;
				}
			}
			fos = new FileOutputStream(path);
			fos.write(content);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 描述：SD卡是否能用.
	 *
	 * @return true 可用,false不可用
	 */
	public static boolean isCanUseSD() {
		try {
			return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 描述：初始化存储目录.
	 *
	 * @param context
	 *            the context
	 */
	public static void initFileDir(Context context) {

		PackageInfo info = HAppUtil.getPackageInfo(context);

		// 默认下载文件根目录.
		String downloadRootPath = File.separator + AbAppConfig.DOWNLOAD_ROOT_DIR + File.separator + info.packageName
				+ File.separator;

		// 默认下载图片文件目录.
		String imageDownloadPath = downloadRootPath + AbAppConfig.DOWNLOAD_IMAGE_DIR + File.separator;

		// 默认下载文件目录.
		String fileDownloadPath = downloadRootPath + AbAppConfig.DOWNLOAD_FILE_DIR + File.separator;

		// 默认缓存目录.
		String cacheDownloadPath = downloadRootPath + AbAppConfig.CACHE_DIR + File.separator;

		// 默认DB目录.
		String dbDownloadPath = downloadRootPath + AbAppConfig.DB_DIR + File.separator;

		try {
			if (!isCanUseSD()) {
				return;
			} else {

				File root = Environment.getExternalStorageDirectory();
				File downloadDir = new File(root.getAbsolutePath() + downloadRootPath);
				if (!downloadDir.exists()) {
					downloadDir.mkdirs();
				}
				downloadRootDir = downloadDir.getPath();

				File cacheDownloadDirFile = new File(root.getAbsolutePath() + cacheDownloadPath);
				if (!cacheDownloadDirFile.exists()) {
					cacheDownloadDirFile.mkdirs();
				}
				cacheDownloadDir = cacheDownloadDirFile.getPath();

				File imageDownloadDirFile = new File(root.getAbsolutePath() + imageDownloadPath);
				if (!imageDownloadDirFile.exists()) {
					imageDownloadDirFile.mkdirs();
				}
				imageDownloadDir = imageDownloadDirFile.getPath();

				File fileDownloadDirFile = new File(root.getAbsolutePath() + fileDownloadPath);
				if (!fileDownloadDirFile.exists()) {
					fileDownloadDirFile.mkdirs();
				}
				fileDownloadDir = fileDownloadDirFile.getPath();

				File dbDownloadDirFile = new File(root.getAbsolutePath() + dbDownloadPath);
				if (!dbDownloadDirFile.exists()) {
					dbDownloadDirFile.mkdirs();
				}
				dbDownloadDir = dbDownloadDirFile.getPath();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 计算sdcard上的剩余空间.
	 *
	 * @return the int
	 */
	public static int freeSpaceOnSD() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / 1024 * 1024;
		return (int) sdFreeMB;
	}

	/**
	 * 根据文件的最后修改时间进行排序.
	 */
	public static class FileLastModifSort implements Comparator<File> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	/**
	 * 删除所有缓存文件.
	 *
	 * @return true, if successful
	 */
	public static boolean clearDownloadFile() {

		try {
			if (!isCanUseSD()) {
				return false;
			}

			File path = Environment.getExternalStorageDirectory();
			File fileDirectory = new File(path.getAbsolutePath() + downloadRootDir);
			File[] files = fileDirectory.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 描述：读取Assets目录的文件内容.
	 *
	 * @param context
	 *            the context
	 * @param name
	 *            the name
	 * @param encoding
	 *            the encoding
	 * @return the string
	 */
	public static String readAssetsByName(Context context, String name, String encoding) {
		String text = null;
		InputStreamReader inputReader = null;
		BufferedReader bufReader = null;
		try {
			inputReader = new InputStreamReader(context.getAssets().open(name));
			bufReader = new BufferedReader(inputReader);
			String line = null;
			StringBuffer buffer = new StringBuffer();
			while ((line = bufReader.readLine()) != null) {
				buffer.append(line);
			}
			text = new String(buffer.toString().getBytes(), encoding);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufReader != null) {
					bufReader.close();
				}
				if (inputReader != null) {
					inputReader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return text;
	}

	/**
	 * 描述：读取Raw目录的文件内容.
	 *
	 * @param context
	 *            the context
	 * @param id
	 *            the id
	 * @param encoding
	 *            the encoding
	 * @return the string
	 */
	public static String readRawByName(Context context, int id, String encoding) {
		String text = null;
		InputStreamReader inputReader = null;
		BufferedReader bufReader = null;
		try {
			inputReader = new InputStreamReader(context.getResources().openRawResource(id));
			bufReader = new BufferedReader(inputReader);
			String line = null;
			StringBuffer buffer = new StringBuffer();
			while ((line = bufReader.readLine()) != null) {
				buffer.append(line);
			}
			text = new String(buffer.toString().getBytes(), encoding);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufReader != null) {
					bufReader.close();
				}
				if (inputReader != null) {
					inputReader.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return text;
	}

	/**
	 * 解压缩功能. 将zipFile文件解压到folderPath目录下.
	 * 
	 * @param zipFile
	 *            zip文件地址
	 * @param folderPath
	 *            解压目的文件
	 * @return
	 */
	public int upZipFile(File zipFile, String folderPath) {
		try {
			ZipFile zfile = new ZipFile(zipFile);
			Enumeration zList = zfile.entries();
			ZipEntry ze = null;
			byte[] buf = new byte[1024];
			while (zList.hasMoreElements()) {
				ze = (ZipEntry) zList.nextElement();
				if (ze.isDirectory()) {
					Log.d("upZipFile", "ze.getName() = " + ze.getName());
					String dirstr = folderPath + ze.getName();
					// dirstr.trim();
					dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
					Log.d("upZipFile", "str = " + dirstr);
					File f = new File(dirstr);
					f.mkdir();
					continue;
				}
				Log.d("upZipFile", "ze.getName() = " + ze.getName());
				OutputStream os = new BufferedOutputStream(
						new FileOutputStream(getRealFileName(folderPath, ze.getName())));
				InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
				int readLen = 0;
				while ((readLen = is.read(buf, 0, 1024)) != -1) {
					os.write(buf, 0, readLen);
				}
				is.close();
				os.close();
			}
			zfile.close();
		} catch (IOException e) {
			Log.e("tag", "解压失败!");
		}
		return 0;
	}

	/**
	 * 给定根目录，返回一个相对路径所对应的实际文件名.（压缩文件）
	 * 
	 * @param baseDir
	 *            指定根目录
	 * @param absFileName
	 *            相对路径名，来自于ZipEntry中的name
	 * @return java.io.File 实际的文件
	 */
	public static File getRealFileName(String baseDir, String absFileName) {
		String[] dirs = absFileName.split("/");
		File ret = new File(baseDir);
		String substr = null;
		if (dirs.length > 1) {
			for (int i = 0; i < dirs.length - 1; i++) {
				substr = dirs[i];
				try {
					substr = new String(substr.getBytes("8859_1"), "GB2312");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				ret = new File(ret, substr);
			}
			Log.d("upZipFile", "1ret = " + ret);
			if (!ret.exists())
				ret.mkdirs();
			substr = dirs[dirs.length - 1];
			try {
				substr = new String(substr.getBytes("8859_1"), "GB2312");
				Log.d("upZipFile", "substr = " + substr);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			ret = new File(ret, substr);
			Log.d("upZipFile", "2ret = " + ret);
			return ret;
		}
		return ret;
	}

	/**
	 * 打开pdf文件
	 * 
	 * @param strPath
	 *            文件的地址
	 * @param context
	 *            对于的contenxt
	 */
	public String openPdfFile(String strPath, Context context) {
		File file = new File(strPath);
		if (file.exists()) {
			Uri path = Uri.fromFile(file);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(path, "application/pdf");
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			try {
				context.startActivity(intent);
				return "true";
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
				return "请安装wps";
			}
		} else {
			return "文件地址不对、或者文件不存在";
		}
	}

	/**
	 * 删除文件，可以是单个文件或文件夹
	 * 
	 * @param fileName
	 *            待删除的文件名
	 * @return 文件删除成功返回true,否则返回false
	 */
	public static boolean delete(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			System.out.println("删除文件失败：" + fileName + "文件不存在");
			return false;
		} else {
			if (file.isFile()) {

				return deleteFile(fileName);
			} else {
				return deleteDirectory(fileName, false);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param fileName
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true,否则返回false
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
			System.out.println("删除单个文件" + fileName + "成功！");
			return true;
		} else {
			System.out.println("删除单个文件" + fileName + "失败！");
			return false;
		}
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param dir
	 *            被删除目录的文件路径
	 * @param isSave
	 *            是否保存文件
	 * @return 目录删除成功返回true,否则返回false
	 */
	public static boolean deleteDirectory(String dir, boolean isSave) {
		// 如果dir不以文件分隔符结尾，自动添加文件分隔符
		if (!dir.endsWith(File.separator)) {
			dir = dir + File.separator;
		}
		File dirFile = new File(dir);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			System.out.println("删除目录失败" + dir + "目录不存在！");
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag) {
					break;
				}
			}
			// 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath(), false);
				if (!flag) {
					break;
				}
			}
		}

		if (!flag) {
			System.out.println("删除目录失败");
			return false;
		}

		if (isSave) {
			return true;
		} else {
			// 删除当前目录
			if (dirFile.delete()) {
				return true;
			} else {
				System.out.println("删除目录" + dir + "失败！");
				return false;
			}
		}
	}

	/**
	 * Gets the download root dir.
	 *
	 * @param context
	 *            the context
	 * @return the download root dir
	 */
	public static String getDownloadRootDir(Context context) {
		if (downloadRootDir == null) {
			initFileDir(context);
		}
		return downloadRootDir;
	}

	/**
	 * Gets the image download dir.
	 *
	 * @param context
	 *            the context
	 * @return the image download dir
	 */
	public static String getImageDownloadDir(Context context) {
		if (downloadRootDir == null) {
			initFileDir(context);
		}
		return imageDownloadDir;
	}

	/**
	 * Gets the file download dir.
	 *
	 * @param context
	 *            the context
	 * @return the file download dir
	 */
	public static String getFileDownloadDir(Context context) {
		if (downloadRootDir == null) {
			initFileDir(context);
		}
		return fileDownloadDir;
	}

	/**
	 * Gets the cache download dir.
	 *
	 * @param context
	 *            the context
	 * @return the cache download dir
	 */
	public static String getCacheDownloadDir(Context context) {
		if (downloadRootDir == null) {
			initFileDir(context);
		}
		return cacheDownloadDir;
	}

	/**
	 * Gets the db download dir.
	 *
	 * @param context
	 *            the context
	 * @return the db download dir
	 */
	public static String getDbDownloadDir(Context context) {
		if (downloadRootDir == null) {
			initFileDir(context);
		}
		return dbDownloadDir;
	}

	/**
	 * Gets the free sd space needed to cache.
	 *
	 * @return the free sd space needed to cache
	 */
	public static int getFreeSdSpaceNeededToCache() {
		return freeSdSpaceNeededToCache;
	}

}
