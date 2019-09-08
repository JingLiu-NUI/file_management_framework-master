package com.studentportal.ui;

import com.studentportal.file_management.Document;
import com.studentportal.file_management.DocumentHelper;
import com.studentportal.http.*;
import com.studentportal.http.documents.GetAllDocumentsRequest;
import com.studentportal.http.documents.SaveDocumentRequest;
import com.studentportal.interceptor.Dispatcher;
import com.studentportal.interceptor.FileLoggingIntercepter;
import com.studentportal.interceptor.FileValidationIntercepter;
import com.studentportal.interceptor.ContextFile;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

public class FileManagementUi extends Ui {  // In intercepter architecture pattern,

    private Dispatcher dispatcher;

    private JPanel pane;
    private GridBagConstraints c;

    private JButton uploadDocBtn;
    private JButton downloadDocBtn;

    private Document document;
    private RequestHandler uploadHandler;

    public FileManagementUi() {

        dispatcher=new Dispatcher();//create a dispatcher object
        initConcreteInterceptorAndAttach_Application();

        this.uploadHandler = new RequestHandler() {
            @Override
            public void onSuccess() {
                JOptionPane.showMessageDialog(getFrame(),"Successfully uploaded");
            }

            @Override
            public void onFailure(Exception e) {
                if (e.getMessage().contains("409")) {
                    String name = JOptionPane.showInputDialog(getFrame(), e.getMessage());
                    if (StringUtils.isBlank(name)) {
                        JOptionPane.showMessageDialog(null,
                                "Request not sent because nothing was entered");
                    } else {
                        document.setFileName(name);
                        String json = DocumentHelper.convertDocToJson(document);

                        RequestAbstractFactory docFactory = RequestFactoryProducer.getFactory(RequestChoice.DOCUMENT);
                        SaveDocumentRequest request = (SaveDocumentRequest) docFactory.saveRequest();
                        request.makeRequest(uploadHandler, json);
                    }
                } else {
                    JOptionPane.showMessageDialog(getFrame(), e.getMessage());
                }
            }
        };
        initComponents();
        setComponentsInPanel();
        prepareUi();
    }

    private void initConcreteInterceptorAndAttach_Application(){ //this is "application class" in interceptor pattern
        dispatcher.attach(new FileLoggingIntercepter());    // Never reverse thoes two files order -> because the Arraylist is ordered data structure.
        dispatcher.attach(new FileValidationIntercepter()); //  Otherwise, some exception like NullPointerException will be throwed!
    }

    private void initComponents() {
        uploadDocBtn = new JButton("Upload");
        uploadDocBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ContextFile fileCtx = new ContextFile(openChooserAndGetFile());  // when user click on upload, a eventContext will be created
                //intercept point, callback
                dispatcher.notifyObsevers(fileCtx);

                //get the file after processed from context object.
                File file = fileCtx.getFile();

                //the following are business logic code
                if (file == null) {
                    // prompt user a dialog saying incorrect.
                    JOptionPane.showMessageDialog(null,
                            "File is not in legitimate format!!!!");
                } else {
                    document = Document.createDocFromFile(file);
                    if (document != null) {
                        String json = DocumentHelper.convertDocToJson(document);
                        RequestAbstractFactory docFactory = RequestFactoryProducer.getFactory(RequestChoice.DOCUMENT);
                        SaveDocumentRequest request = (SaveDocumentRequest) docFactory.saveRequest();
                        request.makeRequest(uploadHandler, json);
                    }
                }
            }
        });

        downloadDocBtn = new JButton("Download");
        downloadDocBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RequestHandler allDocsHandler = new RequestHandler() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Successfully retrieved documents");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        JOptionPane.showMessageDialog(getFrame(), e.getMessage());
                    }
                };

                RequestAbstractFactory docFactory = RequestFactoryProducer.getFactory(RequestChoice.DOCUMENT);
                GetAllDocumentsRequest request = (GetAllDocumentsRequest) docFactory.getAllRequest();
                List<Document> docs = request.makeRequest(allDocsHandler, null);

                if (docs.size() > 0) {
                    DocumentListDisplayUi listDisplayUi = new DocumentListDisplayUi(docs);
                    listDisplayUi.show();
                } else {
                    JOptionPane.showMessageDialog(getFrame(), "No documents to download");
                }
            }
        });
    }

    private File openChooserAndGetFile() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.showOpenDialog(getFrame()); // context object maybe is getFrame()....
        return fc.getSelectedFile();
    }

    private void setComponentsInPanel() {
        pane = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();

        // upload button
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(0,150, 0, 150);
        pane.add(uploadDocBtn, c);

        // download button
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(0,150, 0, 150);
        pane.add(downloadDocBtn, c);
    }

    private void prepareUi() {
        getFrame().setTitle("File Managment");
        getFrame().setSize(500, 300);
        getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new GridLayout(3, 1));
        getFrame().setResizable(false);

        getFrame().add(pane);
        getFrame().pack();
    }

    public JButton getUploadDocBtn() {
        return uploadDocBtn;
    }

    public void setUploadDocBtn(JButton uploadDocBtn) {
        this.uploadDocBtn = uploadDocBtn;
    }

    public JButton getDownloadDocBtn() {
        return downloadDocBtn;
    }

    public void setDownloadDocBtn(JButton downloadDocBtn) {
        this.downloadDocBtn = downloadDocBtn;
    }
}
