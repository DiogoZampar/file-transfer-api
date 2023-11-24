package br.com.diogozampar.filetransferapi;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;






@Controller
@RequestMapping("/api/files")
public class fileTransferController {
    private final Path fileStorageLocation;
    

    public fileTransferController(fileTransferProperties fileTransferProperties){
        this.fileStorageLocation = Paths.get(fileTransferProperties.getUploadDir()).toAbsolutePath().normalize();
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("/file") MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try{
            Path targetLocation = fileStorageLocation.resolve(fileName);
            file.transferTo(targetLocation);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/files/download/").path(fileName).toUriString();

            return ResponseEntity.ok("File uploaded! Link for download: " + fileDownloadUri);
        }catch(IOException ioExc){

            return ResponseEntity.badRequest().build();

        }

    }




}
