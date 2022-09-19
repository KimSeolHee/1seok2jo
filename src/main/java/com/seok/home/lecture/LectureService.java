package com.seok.home.lecture;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.seok.home.util.FileManager;

@Service
public class LectureService {

	@Autowired
	private LectureDAO lectureDAO;
	@Autowired
	private FileManager fileManager;
	
	public List<LectureDTO> getLecture() throws Exception {
		
		return lectureDAO.getLecture();
	}
	
	public int setLecture(LectureDTO lectureDTO, MultipartFile[] files, ServletContext servletContext,LectureVideoDTO lectureVideoDTO) throws Exception {
		int result = lectureDAO.setLecture(lectureDTO);
		String path = "resources/upload/lecture";
		
		for(MultipartFile mf : files) {
			if(mf.isEmpty()) {
				continue;
			}
		
		
		String fileName = fileManager.saveFile(path, servletContext,mf);

		LectureFileDTO lectureFileDTO = new LectureFileDTO();
		lectureFileDTO.setF_name(fileName);
		lectureFileDTO.setF_oriname(mf.getOriginalFilename());
		lectureFileDTO.setL_num(lectureDTO.getL_num());
		lectureDAO.setAddFile(lectureFileDTO);
		}
		String[] url =lectureVideoDTO.getV_url().split(",");
		String[] context = lectureVideoDTO.getV_context().split(",");
		for(int i=0; i<url.length; i++) {
			lectureVideoDTO.setL_num(lectureDTO.getL_num());
			lectureVideoDTO.setV_url(url[i]);
			lectureVideoDTO.setV_context(context[i]);
			lectureVideoDTO.setV_seq((long)i);
			lectureDAO.setAddVideo(lectureVideoDTO);
			
		}
		return result;
	}
	
	
	
	
}
