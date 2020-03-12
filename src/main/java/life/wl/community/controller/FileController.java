package life.wl.community.controller;

import com.alibaba.fastjson.JSONObject;
import life.wl.community.dto.FileDTO;
import life.wl.community.exception.CustomizeErrorCode;
import life.wl.community.exception.CustomizeException;
import life.wl.community.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@Controller
public class FileController {
    @Autowired
    private UploadService uploadService;

    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(@RequestParam(value = "editormd-image-file", required = true) MultipartFile file,
                          HttpServletRequest request, HttpServletResponse response){
        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        String fileName = uploadService.uploadImg(file, request, response);
        fileDTO.setUrl("/msg/upload/"+fileName);
        return fileDTO;
    }
}
