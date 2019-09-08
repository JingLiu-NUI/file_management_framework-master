package com.studentportal.ui;

import com.studentportal.file_management.Document;
import com.studentportal.file_management.DocumentDetails;
import com.studentportal.http.*;
import com.studentportal.http.documents.GetDocumentRequest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DocumentListDisplayUi extends Ui {

    private JPanel pane;
    private JTable docTable;

    private JButton selectBtn;

    private List<Document> docList;

    private RequestHandler downloadHandler;

    public DocumentListDisplayUi(List<Document> docList) {
        this.docList = docList;
        this.downloadHandler = new RequestHandler() {
            @Override
            public void onSuccess() {
                JOptionPane.showMessageDialog(getFrame(),"Successfully downloaded");
                close();
            }

            @Override
            public void onFailure(Exception e) {
                JOptionPane.showMessageDialog(getFrame(), e.getMessage());
                close();
            }
        };
        initTable();
        initComponents();
        setComponentsInPane();
        prepareUi();
    }

    private void initTable() {
        docTable = new JTable();
        List<Object[]> list = new ArrayList<>();
        for (Document doc : docList) {
            list.add(new Object[] {
                    doc.getId(),
                    doc.getFileName(),
                    doc.getCreated(),
                    doc.getFileSize()
            });
        }

        TableModel docModel = new DefaultTableModel(list.toArray(new Object[][] {}),
                new String[] {"ID", "Filename", "Created", "Size"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        docTable.setModel(docModel);
    }

    private void initComponents() {
        selectBtn = new JButton("Select");
        selectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (docTable.getSelectedRow() < 0) {
                    JOptionPane.showMessageDialog(getFrame(),
                            "Please select a document to download");
                } else {
                    int docId = getSelectedDocId();
                    String location = getDownloadLocationFromUser();
                    String newFilename = getNewFilenameFromUser();

                    RequestAbstractFactory docFactory = RequestFactoryProducer.getFactory(RequestChoice.DOCUMENT);
                    GetDocumentRequest request = (GetDocumentRequest) docFactory.getRequest();
                    request.makeRequest(downloadHandler, new DocumentDetails(docId, location, newFilename));
                }
            }
        });
    }

    private void setComponentsInPane() {
        pane = new JPanel();
        pane.setLayout(new BorderLayout());
        pane.add(docTable, BorderLayout.CENTER);
        pane.add(new JScrollPane(docTable));
        pane.add(selectBtn, BorderLayout.SOUTH);
    }

    private void prepareUi() {
        getFrame().setTitle("Documents");
        getFrame().setSize(600, 400);
        getFrame().setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        getFrame().setLocationRelativeTo(null);
        getFrame().setLayout(new GridLayout());

        getFrame().add(pane);
        getFrame().pack();
    }

    private String getNewFilenameFromUser() {
        String newFilename = JOptionPane.showInputDialog(getFrame(), "Input name of the file");
        return newFilename;
    }

    private int getSelectedDocId() {
        return (int) docTable.getValueAt(docTable.getSelectedRow(), 0);
    }

    private String getDownloadLocationFromUser() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.showOpenDialog(getFrame());
        return fc.getSelectedFile().getAbsolutePath();
    }
}
