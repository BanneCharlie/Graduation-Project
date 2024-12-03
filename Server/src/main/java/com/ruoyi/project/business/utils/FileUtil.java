package com.ruoyi.project.business.utils;

import com.ruoyi.project.process.utils.DateUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import sun.misc.BASE64Decoder;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;


public class FileUtil {
//    public static void main(String[] args) {
//        String decode = "5rWL6K+V6ZmE5Lu25LiK5Lyg5pa55rOVQUFB";
//        String decode2 = "5rWL6K+V6ZmE5Lu25LiK5LygQUFB";
//        String decode3 = "5rWL6K+V6ZmE5Lu25LiK5LygQUFBLmRvY3g=";
//        System.out.println(base64Decode(decode));
//        System.out.println(base64Decode(decode2));
//        System.out.println(base64Decode(decode3));
//    }

    /**
     * ---> 对输入的base64密文进行解密
     *      需要注意前端传递base64密文时 = 会被浏览器转换为 空格 ， 所以需要先替换
     * @author xqh, 987072248@qq.com
     * @date 2021/7/8 9:03
     * @param code
     */
    public static String base64Decode(String code) {
        code = code.replaceAll(" ","+");
        code = new String(Base64.getDecoder().decode(code), StandardCharsets.UTF_8);
        return code;
    }
    /**
     *  拼装文件路径 ， 最终会以日期的形式来显示
     * @param diskPath 盘符
     * @param dirPath  文件目录
     * @return
     */
    public String installDateFilePath (String diskPath , String dirPath , String fileName){
        return diskPath+dirPath+(DateUtil.getDate().toString().split(" ")[0] + "/" + fileName);
    }
    public String installSignatureFilePath(String diskPath , String dirPath ,String userName,String serverName){
        return diskPath + dirPath + userName + "/" + serverName;
    }
    /**
     * ---> 组装缴费凭证文件路径方法
     * @author xqh, 987072248@qq.com
     * @date 2021/7/8 9:00
     * @param diskPath 磁盘
     * @param dirPath  服务器的文件路径
     * @param businessRowId 业务表rowId
     * @param serverName    文件在服务器的名字
     */
    public String installPaymentCredenceFilePath(String diskPath , String dirPath ,String businessRowId,String serverName){
        return diskPath + dirPath  + businessRowId  + "/" + DateUtil.getDate().toString().split(" ")[0] + "/" + serverName;
    }
    /**
     *  获得文件类型
     */
    public String getFileType(String fileName){
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
	/**
	 * decode文件名，加入时间防止有名字冲突
	 * 
	 * @param fileName
	 * @return
	 */
	public String getDecodeFileName(String fileName) {
		StringBuilder fileStrName = new StringBuilder("");
		if (fileName != null && fileName.length() > 0) {
			String[] filenames = fileName.split("\\.");
			long times = DateUtil.getDate().getTime();
			String fileSuffix = "";
			if (filenames.length == 2) {
				fileSuffix = filenames[1];
                fileStrName = new StringBuilder(UUID.randomUUID() + "." + fileSuffix);
			}
			else   // 当出现 a.2021-02-03.log 这一类文件 或者更多点的文件时要另外处理
            {
                fileSuffix = filenames[filenames.length-1];
                for (int i = 0; i < filenames.length - 1; i++) {
                    fileStrName.append(UUID.randomUUID()).append(".");
                }
                fileStrName.append(fileSuffix);
            }

			// fileStrName = filenames[0] + times + fileSuffix;
			// fileStrName = UUID.fromString(fileStrName).toString();

		}
		return fileStrName.toString();
	}

	// 第一种获取文件内容方式
	public byte[] getContent(String filePath) throws IOException {
		File file = new File(filePath);

		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			System.out.println("file too big...");
			return null;
		}

		FileInputStream fi = new FileInputStream(file);

		byte[] buffer = new byte[(int) fileSize];

		int offset = 0;

		int numRead = 0;

		while (offset < buffer.length

		&& (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {

			offset += numRead;

		}

		// 确保所有数据均被读取

		if (offset != buffer.length) {

			throw new IOException("Could not completely read file "
					+ file.getName());

		}

		fi.close();

		return buffer;
	}

	// 第二种获取文件内容方式
	public byte[] getContent2(String filePath) throws IOException {
		FileInputStream in = new FileInputStream(filePath);

		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

		System.out.println("bytes available:" + in.available());

		byte[] temp = new byte[1024];

		int size = 0;

		while ((size = in.read(temp)) != -1) {
			out.write(temp, 0, size);
		}

		in.close();

		byte[] bytes = out.toByteArray();
		System.out.println("bytes size got is:" + bytes.length);

		return bytes;
	}

	/**
	 * 将byte数组写入文件
	 * 
	 * @param path
	 * @param content
	 * @throws IOException
	 */
	public File createFile(String path, byte[] content) throws IOException {

		File file = new File(path);
		if (!file.exists()) {// 文件路径不存在，创建
			File pfiles = file.getParentFile();
			pfiles.mkdirs();
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);

		fos.write(content);
		fos.close();
		return file;
	}

	public File createFile(String outPath , File srcFile) throws IOException{
        FileInputStream fis = new FileInputStream(srcFile);
        File destFile = new File(outPath);
        if (!destFile.exists()){
            destFile.getParentFile().mkdirs();
            destFile.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(destFile);

        int len = 0;
        byte[] buffer = new byte[1024];
        while ((len = fis.read(buffer))!= -1){
            fos.write(buffer,0,len);
        }
        fis.close();
        fos.close();

        return destFile;
    }

	/**
	 * 删除文件
	 *
	 * @param filePath
	 *            文件路径
	 */
	/**
	 * 删除文件
	 *
	 * @param filePath
	 *            文件路径
	 * @return 删除成功true;失败false
	 */
	public boolean deleteFile(String filePath) {
		File file = new File(filePath);
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (!file.delete()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 下载文件
	 *
	 * @param downLoadPath
	 *            下载路径
	 * @param response
	 * @param fileName
	 *            文件名
	 */
	public void downloadFile(String downLoadPath, HttpServletRequest request,
                             HttpServletResponse response, String fileName) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			long fileLength = new File(downLoadPath).length();
			/*
			 * response.setContentType("application/x-msdownload;");
			 * response.setHeader("Content-disposition", "attachment; filename="
			 * + fileName);
			 */

			String userAgent = request.getHeader("User-Agent");
			// 针对IE或者以IE为内核的浏览器：
			if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
				fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			} else {
				// 非IE浏览器的处理：
				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
			response.setHeader("Content-disposition",
					String.format("attachment; filename=\"%s\"", fileName));
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setCharacterEncoding("UTF-8");

			response.setHeader("Content-Length", String.valueOf(fileLength));
			bis = new BufferedInputStream(new FileInputStream(downLoadPath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

    /***
     * ---> 获取文件数据 并不是下载 ， 根据合同RowId
     *
     * @author: xqh , 987072248@qq.com
     * @date: 2022/3/7 - 15:19
     **/
    public void getFileNoDownloadByFileRowId(String fileServerPath, HttpServletResponse response) throws IOException {
        getFileNotDown(fileServerPath, response);
    }

    /**
     * 用于图片文件 的显示，不用于下载
     * @param downLoadPath
     * @param response
     * @throws IOException
     */
    public void getFileNotDown(String downLoadPath, HttpServletResponse response)
            throws IOException {
        FileInputStream fis = null;
        OutputStream os = null;
        try {
            String fileType = getFileType(downLoadPath).toLowerCase();
            response.setCharacterEncoding("UTF-8");
            if ("pdf".equals(fileType)) {
                response.setContentType("application/pdf;charset=utf-8");
            } else if ("jpg".equals(fileType)) {
                response.setContentType("image/png");
            } else if ("png".equals(fileType)){
                response.setContentType("image/png");
            } else if ("xlsx".equals(fileType)) {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            } else if ("xls".equals(fileType)) {
                response.setContentType("application/vnd.ms-excel");
            } else if ("docx".equals(fileType)) {
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            }
            File targetFile = new File(downLoadPath);
            fis = new FileInputStream(targetFile);

            os = response.getOutputStream();
            response.setHeader("Content-Length", String.valueOf(targetFile.length()));

            int count;
            byte[] buffer = new byte[1024 * 8];
            while ((count = fis.read(buffer)) != -1) {
                os.write(buffer, 0, count);
                os.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

	/**
	 * 下载文件
	 *
	 * @param downLoadPath
	 *            下载路径
	 * @param response
	 * @param fileName
	 *            文件名
	 */
	public void getFile(String downLoadPath, HttpServletRequest request,
                        HttpServletResponse response, String fileName) {

		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			long fileLength = new File(downLoadPath).length();

			String userAgent = request.getHeader("User-Agent");
			// 针对IE或者以IE为内核的浏览器：
			if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
				fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
			} else {
				// 非IE浏览器的处理：
				fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
			}
			response.setHeader("Content-disposition",
					String.format("attachment; filename=\"%s\"", fileName));
			response.setContentType("application/octet-stream;charset=utf-8");
			response.setCharacterEncoding("UTF-8");

			response.setHeader("Content-Length", String.valueOf(fileLength));
			bis = new BufferedInputStream(new FileInputStream(downLoadPath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 生成唯一文件名
	 *
	 * @param ext
	 *            扩展名
	 * @return String
	 */
	public static String getRadomName(String ext) {
		String name = UUID.randomUUID().toString() + "." + ext;
		return name;
	}

	/**
	 * 获取文件名后缀
	 *
	 * @param fileName
	 * @return
	 */
	public static String getExt(String fileName) {
		String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
		return ext;
	}

	public static String base64TOpic(String imgsURl, String filePath,
			HttpServletRequest req) {
		// 对字节数组字符串进行Base64解码并生成图片
		if (imgsURl == null) // 图像数据为空
			return null;
		// BASE64Decoder decoder = new BASE64Decoder();
		try {
			String[] url = imgsURl.split(",");
			String u = url[1];
			// Base64解码
			byte[] buffer = new BASE64Decoder().decodeBuffer(u);

			String ext = "png";// 获取后缀名的方法
			String tempName = FileUtil.getRadomName(ext);// 生成随机的文件名
			String photoUrl = filePath + "/" + tempName;// 相对路径
			File file = new File(photoUrl);
			// FileUtil.upload(file, tempName, uploadFileDir);// 将文件加载到制定的路径下
			// 生成图片
			OutputStream out = new FileOutputStream(file);
			out.write(buffer);
			out.flush();
			out.close();
			tempName = tempName.split("\\.")[0];
			return tempName;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


    public static String pdfToHtml(MultipartFile file) {
        String result = "";
        try {
            InputStream inputStream = file.getInputStream();
            // 加载PDF文档
            PDDocument document = PDDocument.load(inputStream);

            // 创建一个PDFTextStripper的子类以自定义输出
            PDFTextStripper pdfStripper = new PDFTextStripper() ;
            String text = pdfStripper.getText(document);
            Writer writer = new OutputStreamWriter(new FileOutputStream(new File("output.html")), "UTF-8");
            writer.write("<table><body>");
            writer.write(text.replaceAll("\n", "<br>")); // 将换行符转换为HTML的<br>标签
            writer.write("</body></table>");

            //result = FileUtils.readFileToString(writer);
            writer.close();

            document.close();
            //result = "<table><body>"+text.replaceAll("\n", "<br>"+"</body></table>");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return result;
	}
}
