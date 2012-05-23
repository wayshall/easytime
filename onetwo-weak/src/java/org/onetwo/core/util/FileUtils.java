package org.onetwo.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.onetwo.core.exception.BaseException;

@SuppressWarnings("unchecked")
public class FileUtils {
	
	public static final String PACKAGE = "package";

	private FileUtils() {
	}
	
	public static String getResourcePath(String fileName){
		URL path = Thread.currentThread().getContextClassLoader().getResource("");
		String realPath = path.getPath() + fileName;
		return realPath;
	}

	public static String getFileNameWithoutExt(String fileName) {
		int index = fileName.lastIndexOf('.');
		return fileName.substring(0, index);
	}

	public static String getExtendName(String fileName) {
		int index = fileName.lastIndexOf('.');
		if (index == -1)
			return "";
		return fileName.substring(index + 1);
	}

	public static File writeToDisk(File file, String filePath, String fileName) throws Exception {
		InputStream in = null;
		OutputStream out = null;
		File wfile = null;
		try {
			wfile = new File(filePath, fileName);
			int count = 1;
			if (!wfile.getParentFile().exists())
				wfile.getParentFile().mkdirs();
			String newFileName = fileName;
			while (wfile.exists()) {
				if (fileName.lastIndexOf(".") != -1) {
					String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
					newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "(" + count + ")." + ext;
				} else {
					newFileName = fileName + "(" + count + ")";
				}
				wfile = new File(filePath, newFileName);
				count++;
			}
			in = new FileInputStream(file);
			out = new FileOutputStream(wfile);
			byte[] buf = new byte[4096];
			int length = 0;
			while ((length = in.read(buf)) > 0) {
				out.write(buf, 0, length);
			}
		} catch (Exception e) {
			throw new RuntimeException("occur error when write file : " + file.getPath(), e);
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
		return wfile;
	}

	public static void copyFile(File srcFile, File destFile) {
		Assert.notNull(srcFile);
		Assert.notNull(destFile);

		if (destFile.isDirectory())
			throw new BaseException("the file is directory: " + destFile.getPath());
		if (destFile.isHidden() || !destFile.canWrite())
			throw new BaseException("the file is hidden or readonly : " + destFile.getPath());

		BufferedInputStream fin = null;
		BufferedOutputStream fout = null;
		try {
			System.out.println("creating the file : " + destFile.getPath());
			fin = new BufferedInputStream(new FileInputStream(srcFile));
			fout = new BufferedOutputStream(new FileOutputStream(destFile));
			byte[] buf = new byte[1024 * 5];
			int count = 0;
			while ((count = fin.read(buf, 0, buf.length)) != -1) {
				fout.write(buf, 0, count);
			}
			System.out.println("file is created!");
		} catch (Exception e) {
			throw new BaseException("copy file error", e);
		} finally {
			try {
				if (fin != null)
					fin.close();
				if (fout != null)
					fout.close();
			} catch (Exception e) {
				throw new BaseException("close file error!", e);
			}
		}
	}

	public static void copyDir(String srcPath, String destPath) throws Exception {
		File destFile = new File(destPath);
		File srcFile = new File(srcPath);
		if (!srcFile.exists())
			return;
		if (srcFile.isFile()) {
			if (destFile.isDirectory()) {
				if (!destFile.exists())
					destFile.mkdirs();
				destFile = new File(destFile, srcFile.getName());
			}
			copyFile(srcFile, destFile);
		} else {
			if (!destFile.exists()) {
				destFile.mkdirs();
				System.out.println("create drectory : " + destFile.getPath());
			}
			File[] list = srcFile.listFiles();
			for (int i = 0; i < list.length; i++) {
				File file = list[i];
				if (file.isDirectory())
					copyDir(file.getPath(), destPath + "/" + file.getName());
				else
					copyDir(file.getPath(), destPath);
			}
		}
	}

	public static List<File> listFile(String dirPath, String regex) {
		if (!regex.startsWith("^"))
			regex = "^" + regex;

		if (!regex.endsWith("$"))
			regex = regex + "$";

		Pattern pattern = Pattern.compile(regex);
		File dirFile = new File(dirPath);
		
		return listFile(dirFile, pattern);

	}

	public static List<File> listFile(File dirFile, Pattern pattern) {
		File[] files = dirFile.listFiles();
		if (files == null)
			return null;

		List<File> fileList = new ArrayList<File>();
		for (File f : files) {
			if (f.isFile() && pattern.matcher(f.getPath()).matches()) {
				fileList.add(f);
			} 
			else {
				List<File> l = listFile(f, pattern);
				if(l==null || l.isEmpty())
					continue;
				fileList.addAll(l);
			}
		}

		return fileList;
	}
	
	public static String getPackageName(File file){
		String packageName = "";
		BufferedReader br = null;
		try {
			br =new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = null;
			while((line=br.readLine())!=null){
				line = line.trim();
				if(line.startsWith("package")){
					packageName = line.substring(PACKAGE.length(), line.indexOf(';')).trim();
					break;
				}
			}
		} catch (Exception e) {
			throw new BaseException(e);
		}finally{
			if(br!=null)
				try {
					br.close();
				} catch (IOException e) {
					throw new BaseException(e);
				}
		}
		return packageName;
	}
	
	public static Class loadClass(File file){
		Class clazz = null;
		String className = getPackageName(file)+"."+getFileNameWithoutExt(file.getName());
		try {
			clazz = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new BaseException(e);
		}
		return clazz;
	}

	public static void main(String[] args) {
		String cp = "E:/mydev/java_workspace/ciippv3/cipmodelv32/bin;";
		String p = System.getProperty("java.class.path ");
		System.out.println("classPath: " + p);
		System.setProperty("java.class.path ", cp);
		System.out.println("classPath: " + System.getProperty("java.class.path "));
		String path = "E:/mydev/java_workspace/ciippv3/cipmodelv32/src/java/com/ciipp/portal";
		String regex = "(.+)VO\\.java";
		List<File> list = listFile(path, regex);
		int count = 0;
		System.out.println("start!~");
		for(File f : list){
			count++;
			System.out.println(count+":"+loadClass(f).getSimpleName());
		}
	}
}
