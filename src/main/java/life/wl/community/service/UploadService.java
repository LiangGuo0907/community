package life.wl.community.service;

import life.wl.community.exception.CustomizeErrorCode;
import life.wl.community.exception.CustomizeException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;


@Service
public class UploadService {

    public String uploadImg(MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        if (file.getSize() > 1048576){
            throw new CustomizeException(CustomizeErrorCode.IMAGE_TOO_BIG);
        }
        String trueFileName = file.getOriginalFilename();
        String suffix = trueFileName.substring(trueFileName.lastIndexOf("."));
        String fileName = System.currentTimeMillis()+"_"+suffix;
        String path = request.getSession().getServletContext().getRealPath("/msg/upload/");
        System.out.println(path);
        File targetFile = new File(path, fileName);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }
}
