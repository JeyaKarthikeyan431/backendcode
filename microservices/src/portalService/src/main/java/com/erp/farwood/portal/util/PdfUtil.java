package com.erp.farwood.portal.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.tika.Tika;
import org.springframework.util.ResourceUtils;

public class PdfUtil {

	public final static byte[] PDF_BYTE = { 37, 80, 68, 70 };

	public final static byte[] JPG_BYTE = { -1, -40, -1, -32 };

	public final static byte[] PNG_BYTE = { -119, 80, 78, 71 };

	public final static byte[] TXT_BYTE = { 97, 115, 100, 97 };

	public final static byte[] DOCX_BYTE = { 80, 75, 3, 4 };

	public final static byte[] DOC_BYTE = { -48, -49, 17, -32 };

	public final static byte[] XLSX_BYTE = { 80, 75, 3, 4 };

	public final static byte[] XLS_BYTE = { -48, -49, 17, -32 };

	public final static byte[] CSV_BYTE = { 68, 101, 102, 101 };

	public final static byte[] EXE_BYTE = { 77, 90, -112, 0 };

	public static File merge(List<String> fileNames, String srcFilesPath, String targetfilePath) {
		try {
			PDFMergerUtility pdfMerger = new PDFMergerUtility();
			for (String fileName : fileNames) {
				if (!fileName.contains("."))
					fileName = fileName.concat(MotorInsuranceConstants.PDF_EXTENSION);
				String filePath = srcFilesPath + File.separator + fileName;
				File file = ResourceUtils.getFile(filePath);
				if (file.exists() && file.isFile() && file.length() > 0) {
					pdfMerger.addSource(file);
				}
			}
			pdfMerger.setDestinationFileName(targetfilePath);
			pdfMerger.mergeDocuments(null);
			return ResourceUtils.getFile(targetfilePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getContentType(byte[] content) {
		String mimeType = "text/plain";
		try {
			mimeType = new Tika().detect(content);
		} catch (Exception | NoClassDefFoundError e) {
			e.printStackTrace();
		}
		return mimeType;

	}

	public static boolean isValidFileType(String fileExtn, byte[] content, String contentType) throws IOException {

		byte[] firstFourBytes = new byte[4];
		InputStream is = null;
		try {
			if (content == null || StringUtils.isBlank(fileExtn)) {
				return false;
			}
			is = new BufferedInputStream(new ByteArrayInputStream(content));
			is.read(firstFourBytes);
			is.close();
			if ("pdf".equalsIgnoreCase(fileExtn) && Arrays.equals(PDF_BYTE, firstFourBytes)
					&& "application/pdf".equalsIgnoreCase(contentType)) {
				return true;
			} else if (("jpg".equalsIgnoreCase(fileExtn) || "jpeg".equalsIgnoreCase(fileExtn)
					|| "jpe".equalsIgnoreCase(fileExtn)) && Arrays.equals(JPG_BYTE, firstFourBytes)
					&& "image/jpeg".equalsIgnoreCase(contentType)) {
				return true;
			} else if ("png".equalsIgnoreCase(fileExtn) && Arrays.equals(PNG_BYTE, firstFourBytes)
					&& "image/png".equalsIgnoreCase(contentType)) {
				return true;
			}

		} catch (IOException e) {
			e.printStackTrace();
			if (is != null)
				is.close();
		}

		return false;

	}

}
