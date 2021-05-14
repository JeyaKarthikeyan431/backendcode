package com.erp.farwood.portal.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.farwood.portal.util.MotorInsuranceConstants;

import net.coobird.thumbnailator.Thumbnails;

@Service
@EnableAutoConfiguration
public class AsyncThumbnailService {

	private static Logger logger = LoggerFactory.getLogger(AsyncThumbnailService.class);

	@Async
	public CompletableFuture<Long> copyFiles(MultipartFile file, Path targetLocation) {

		try {
			long results = Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return CompletableFuture.completedFuture(results);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Async
	public Future<Boolean> moveFiles(Path sourcePath, Path targetLocation) {

		try {
			Files.move(sourcePath, targetLocation, StandardCopyOption.REPLACE_EXISTING);
			return new AsyncResult<Boolean>(true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// @Async
	public void createThumbnail(File file) {

		try {
			generateThumbnail(file);
		} catch (IOException ex) {
			logger.error("Error occured while creating the thumbnail - ", ex);
		}
	}

	private String generateThumbnail(File file) throws IOException {

		String thumbnail = null;
		String extension = FilenameUtils.getExtension(file.getAbsolutePath()).toUpperCase();
		switch (extension) {

		case "PDF":
			getThumbnailForPDF(file);
			break;
		case "PNG":
		case "JPG":
		case "JPEG":
			getThumbnailForImage(file);
			break;
		}
		return thumbnail;
	}

	private void getThumbnailForPDF(File pdf) throws IOException {

		try (PDDocument document = PDDocument.load(pdf)) {
			PDFRenderer renderer = new PDFRenderer(document);
			BufferedImage image = renderer.renderImage(0);

			String tempImgPath = pdf.getParent() + File.separator + "preview" + File.separator
					+ FilenameUtils.removeExtension(pdf.getName()) + MotorInsuranceConstants.THUMBNAIL_FILE_NAME_SUFFIX
					+ MotorInsuranceConstants.PNG_EXTENSION;
			File tempImg = new File(tempImgPath);
			/*
			 * if(!tempImg.getParentFile().exists()) tempImg.getParentFile().mkdir(); if
			 * (!tempImg.exists())
			 */
			ImageIO.write(image, MotorInsuranceConstants.PNG_TYPE, tempImg);
		}
		/*
		 * ByteArrayOutputStream ous = new ByteArrayOutputStream();
		 * Thumbnails.of(tempImg).size(250, 250).toOutputStream(ous); tempImg.delete();
		 * byte[] thumbnail = ous.toByteArray(); FileUtils.writeByteArrayToFile(new
		 * File(tempImgPath), thumbnail); ous.close();
		 */
	}

	private void getThumbnailForImage(File img) throws IOException {

		String tempImgPath = img.getParent() + File.separator + "preview" + File.separator
				+ FilenameUtils.removeExtension(img.getName()) + MotorInsuranceConstants.THUMBNAIL_FILE_NAME_SUFFIX
				+ MotorInsuranceConstants.PNG_EXTENSION;
		File file = new File(tempImgPath);
		// if (!file.exists()) {
		ByteArrayOutputStream ous = new ByteArrayOutputStream();
		Thumbnails.of(img).size(250, 250).toOutputStream(ous);
		byte[] thumbnail = ous.toByteArray();
		FileUtils.writeByteArrayToFile(file, thumbnail);
		ous.close();
		/*
		 * } else { ByteArrayOutputStream ous = new ByteArrayOutputStream();
		 * Thumbnails.of(img).size(250, 250).toOutputStream(ous); byte[] thumbnail =
		 * ous.toByteArray(); FileUtils.writeByteArrayToFile(file, thumbnail);
		 * ous.close(); }
		 */
	}
}
