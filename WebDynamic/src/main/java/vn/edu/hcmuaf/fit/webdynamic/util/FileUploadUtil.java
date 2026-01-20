package vn.edu.hcmuaf.fit.webdynamic.util;

import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUploadUtil {

    private static final String UPLOAD_DIR = "uploads";

    public static String saveImage(Part part, String realPath) throws Exception {
        if (part == null || part.getSize() == 0) return null;

        String fileName = Paths.get(part.getSubmittedFileName())
                .getFileName().toString();

        String ext = fileName.substring(fileName.lastIndexOf("."));
        String newName = UUID.randomUUID() + ext;

        File dir = new File(realPath + File.separator + UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        part.write(dir.getAbsolutePath() + File.separator + newName);

        return  newName;
    }
}
