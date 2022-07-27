package demo.twitter.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import demo.twitter.entity.Account;
import demo.twitter.entity.Image;
import demo.twitter.repository.ImageRepository;

@Service
public class ImageService {

	@Autowired
	private ImageRepository imageRepo;

	private static final String IMAGE_PATH = "C:\\Users/ademi/Desktop/tweet/image/";

	public Image findById(int imageId) {
		return imageRepo.findById(imageId).orElse(null);
	}

	public Image save(Account account, MultipartFile file, String img) {
		Image image = new Image();
		if (account.getImage() != null) {
			image = account.getImage();
		} else {
			image.setAccount(account);
		}
		if (file != null) {
			image = buildImage(account.getId(), image, file);
		} else {
			if (img.equals("default")) {
				if (image.getPhoto() != null) {
					delete(image);
					image = imageRepo.save(image);
				}
			}
		}
		return image;
	}

	public Image buildImage(String id, Image image, MultipartFile file) {
		String photo = null;
		if (file != null) {
			try {
				photo = id + '-' + file.getOriginalFilename();
				Files.copy(file.getInputStream(), Paths.get(IMAGE_PATH + photo), StandardCopyOption.REPLACE_EXISTING);
				image.setPhoto("/images/" + photo);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return imageRepo.save(image);
	}

	public void delete(Image image) {
		try {
			Files.delete(Paths.get(IMAGE_PATH + image.getPhoto().substring(8)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
