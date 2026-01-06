package com.spyatmycode.myjobhuntai.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.spyatmycode.myjobhuntai.exception.FileTransferException;
import com.spyatmycode.myjobhuntai.exception.ParseDocumentException;

@Service
public class DocumentParsingService {

    private final Tika tika;
    private final SupabaseStorageService supabaseStorageService;

    public DocumentParsingService(
        Tika tika,
        SupabaseStorageService supabaseStorageService
    ){
        this.tika = tika;
        this.supabaseStorageService = supabaseStorageService;
    };

    public String parsePDF(MultipartFile file) {
        try {
             return tika.parseToString(file.getInputStream());
        } catch (IOException ioException) {
            throw new FileTransferException("An error occured trying to upload your document.");
        } catch(TikaException tikaException){
            throw new ParseDocumentException("An error trying to parse your document.");
        }

    }

    public String parsePDF(String resumeUrl){

        InputStream stream = supabaseStorageService.downloadResume(resumeUrl);

        TikaDocumentReader reader = new TikaDocumentReader(new InputStreamResource(stream));

        return reader.get().get(0).getContent();

    }
    
}
