package com.studentportal.api;

import com.studentportal.commands.DownloadDocumentCommand;
import com.studentportal.commands.GetAllDocumentsCommand;
import com.studentportal.commands.SaveProjectDocumentCommand;
import com.studentportal.commands.UploadDocumentCommand;
import com.studentportal.file_management.Document;
import com.studentportal.file_management.DocumentHelper;
import com.studentportal.file_management.StudentProjectDocument;
import com.studentportal.hibernate.DocumentService;
import com.studentportal.hibernate.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.logging.Logger;

@Path("/file-mgmt")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FileManagementApi {

    private static Logger LOG = Logger.getLogger(FileManagementApi.class.getName());

    private DocumentService docService = new DocumentService();
    private UserService uService = new UserService();

    @POST
    @Path("/document/upload")
    public void uploadDocument(String json, @Context HttpHeaders headers) {
        LOG.info("hit upload api");

        //
        if (headers != null) {
            String token = headers.getRequestHeader("Developer-Token").get(0);
            System.out.println(token);
        }

        Document doc = DocumentHelper.extractDocumentFromJson(json);
        UploadDocumentCommand cmd = new UploadDocumentCommand(doc, docService);
        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        apiControl.doWork();
    }

    @POST
    @Path("/document/student/project")
    public void saveProjectDocumentByStudent(String json) {
        StudentProjectDocument spd = (StudentProjectDocument) DocumentHelper
                .extractDocumentFromJson(json);
        SaveProjectDocumentCommand cmd = new SaveProjectDocumentCommand(
                docService, uService, spd);
        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        apiControl.doWork();
    }

    @GET
    @Path("document/download/{docId}")
    public Document downloadDocument(@PathParam("docId") int docId) {
        LOG.info("hit download api");
        DownloadDocumentCommand cmd = new DownloadDocumentCommand(docId);
        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        return (Document) apiControl.doWork();
    }

    @GET
    @Path("document/download/all")
    public List<Document> getAllDocuments() {
        GetAllDocumentsCommand cmd = new GetAllDocumentsCommand(docService);
        ApiControl apiControl = new ApiControl();
        apiControl.setCommand(cmd);
        return (List<Document>) apiControl.doWork();
    }
}
