package QLSV_DB;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.*;

public class UXUI {
    private final static String[] btnContent = {"Add","Edit","Delete","Search","Clear","Cancel"};

    private final static List<JButton> btnList = new ArrayList<>();

    private final static String[] labelContent = {"StudentID","Name","Department"};

    private static  String[] major = { "Khoa hoc may tinh", "Ky thuat may tinh", "Bao mat thong tin",
            "Vi mach", "He thong IOT" };

    private static Statement statement;
    private static ResultSet result;

    private static JFrame frm;
    private static DefaultTableModel model;
    private static JTextField tfId;
    private static JTextField tfName;
    private static JComboBox selection;

    private static Connection connection;

    private static EditBoxProperties editUI;


    private static void renderBtn(){
        for (int i = 0; i < btnContent.length; i++) {
            btnList.add(new JButton(btnContent[i]));
        }
    }

    private static void closeConnection() {
        try{
            connection.close();
        }catch (SQLException e){
            System.out.println("Error on close!");
        }
    }

    private static void clearTbl(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
    }

    private static void renderData(List<Object[]> listStudent){
        clearTbl();
        for (int i = 0; i < listStudent.size(); i++) {
            model.addRow(listStudent.get(i));
        }
        model.fireTableDataChanged();
    }

    private static void setDefaultInput(){
        tfId.setText("");
        tfName.setText("");
        selection.setSelectedIndex(0);
    }

    private static void setDefaultEditBox(){
        editUI.getNameInput().setText("");
        editUI.getListOfMajor().setSelectedIndex(0);
    }

    private static void connectionDB(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/QLSV-DB-B9","root","2505");
            System.out.println("Connected !");
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            major = Department.getListDepartment(result, statement).toArray(new String[0]);
        }catch (SQLException | ClassNotFoundException e){
            System.out.println("Error !");
            System.out.println(e);
        }
    }

    private static void getDataFRomDB() {
        ResultSet rs = result;
        List<Object[]> listStu = null;
        String query = "SELECT MaSV, FullName,nameDepart FROM Sinhvien LEFT JOIN Department ON Sinhvien.MaKhoa = Department.IdDepartment;";
        try{
            rs = statement.executeQuery(query);
            while (rs.next()){
                System.out.println(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3));
                Student currentSt = new Student(rs.getString(1),rs.getString(2),rs.getString(3));
                listStu =  MStudent.addNew(currentSt);
            }
            renderData(listStu);
        }catch (SQLException e){
            System.out.println("Error DB !");
            System.out.println(e);
        }
    }


    private static void addStudentToDB(Student stu){
        try{
            String[] departmentSplit = stu.getDepartment().toString().split("\\|");
            String departmentId = departmentSplit[0];
            stu.setDepartment(departmentSplit[1]);
            //Validation
            String query2 = String.format("INSERT INTO Sinhvien(MaSV,FullName,MaKhoa) VALUES ('%s','%s','%s');",stu.getStuId(),stu.getNameStu(),departmentId);
            statement.executeUpdate(query2);
            List<Object[]> list = MStudent.addNew(stu);
            renderData(list);
            setDefaultInput();
        }catch (SQLException e){
            System.out.println("Add error!");
        }
    }

    private static void editStudentToDB(Student stu){
        try{
            String[] splitDep = stu.getDepartment().split("\\|");
            String splitIdDep = splitDep[0];
            stu.setDepartment(splitDep[1]);
            //Validation
            String query =String.format(" UPDATE Sinhvien SET FullName = '%s' , MaKhoa = '%s' WHERE MaSV = '%s' ;",stu.getNameStu(),splitIdDep,stu.getStuId()) ;
            statement.executeUpdate(query);
            int indexStu = findStuIndexByID(stu.getStuId());
            List<Object[]> list = MStudent.updateData(indexStu,stu);
            renderData(list);
            setDefaultEditBox();
        }catch(SQLException e){
            System.out.println(e);
            System.out.println("Edit Error");
        }
    }

    private static String[] castArrayID(){
        List<String> listId = new ArrayList<>();
        List<Object[]> listStu = MStudent.getData();
        for (int i = 0; i < listStu.size(); i++) {
            listId.add(listStu.get(i)[0].toString());
        }
        return listId.toArray(new String[0]);
    }

    private static String[] castArrayID(String Id){
        List<String> listId = new ArrayList<>();
        List<Object[]> listStu = MStudent.getData();
        for (int i = 0; i < listStu.size(); i++) {
            if(listStu.get(i)[0].toString().contains(Id)){
                listId.add(listStu.get(i)[0].toString());
            }
        }
        return listId.toArray(new String[0]);
    }


    private static EditBoxProperties runUIEdit(){
        JFrame frm2 = new JFrame("Edit student !");
        frm2.setDefaultCloseOperation(1);
        frm2.setSize(300,220);
        frm2.setLocation(300,400);
        String[] listId =castArrayID();
        JLabel titleEdit = new JLabel("Edit form");
        titleEdit.setFont(new Font("Arial",Font.BOLD,26));
        titleEdit.setForeground(Color.RED);
        JPanel titleCenter = new JPanel(new GridBagLayout());
        titleCenter.add(titleEdit);
        JTextField getId = new JTextField();
        JComboBox listID = new JComboBox(listId);
        JPanel idPanel = new JPanel(new GridLayout(1,2));
        idPanel.add(getId);
        idPanel.add(listID);
        JLabel idInputlbl = new JLabel("Id search");
        JPanel idPanelContainer = new JPanel(new GridLayout(1,2));
        idPanelContainer.add(idInputlbl);
        idPanelContainer.add(idPanel);
        JLabel newNameLabel = new JLabel("newName");
        JTextField newName = new JTextField();
        JPanel panelName = new JPanel(new GridLayout(1,2));
        panelName.add(newNameLabel);
        panelName.add(newName);

        JComboBox newDepart = new JComboBox(major);
        JButton okBtn = new JButton("Ok");
        JButton findBtn = new JButton("Find");
        JButton cancelBtnEdit = new JButton("Finish");
        JPanel panelBtn = new JPanel(new GridLayout(1,3));
        panelBtn.add(findBtn);
        panelBtn.add(okBtn);
        panelBtn.add(cancelBtnEdit);

        frm2.setLayout(new GridLayout(5,1));
        frm2.add(titleCenter);
        frm2.add(idPanelContainer);
        frm2.add(panelName);
        frm2.add(newDepart);
        frm2.add(panelBtn);
        frm2.setVisible(true);
        return new EditBoxProperties(frm2,getId,newName,listID,newDepart,okBtn,cancelBtnEdit,findBtn);
    }

    private static void onInputIDsearch(){
        String id = editUI.getIdInput().getText();
        System.out.println("ID on insert: "+id);
        String[] listId = castArrayID(id);
        System.out.println(Arrays.toString(listId));
        editUI.getListOfFindID().removeAllItems();
        for (int i = 0; i < listId.length; i++) {
            editUI.getListOfFindID().addItem(new ComboItem(listId[i],"test"));
        }
        if(listId.length == 0){
            editUI.getListOfFindID().addItem(new ComboItem("Not found","test"));
        }
    }

    private static void editStudentOnclick(){
        btnList.get(1).addActionListener(e -> {
            editUI = runUIEdit();
            editUIClickOk();
            editUIClickCancel();
            editUIonInputID();
            editUIclickFind();
        });
    }



    private static void editUIonInputID(){
        editUI.getIdInput().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onInputIDsearch();

            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onInputIDsearch();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                System.out.println("Not available !");
            }
        });
    }

    private  static Student findStuByID(String Id){
        List<Object[]> currentStu = MStudent.getData();
        for (int i = 0; i < currentStu.size(); i++) {
            if(currentStu.get(i)[0].toString().equalsIgnoreCase(Id)){
                return new Student(
                        currentStu.get(i)[0].toString(),
                        currentStu.get(i)[1].toString(),
                        currentStu.get(i)[2].toString()
                );
            }
        }
        //Validation
        return new Student("Und","Und","Und");
    }

    private static int findStuIndexByID(String Id){
        List<Object[]> currentStu = MStudent.getData();
        int index = -1;
        for (int i = 0; i < currentStu.size(); i++) {
            if(currentStu.get(i)[0].toString().equalsIgnoreCase(Id)){
                index = i;
            }
        }
        return index;
    }

    private static void setSelectionBox(String caseString){
        for (int i = 0; i < major.length; i++) {
            String currentPattern = major[i].split("\\|")[1];
            if(currentPattern.equalsIgnoreCase(caseString)){
                editUI.getListOfMajor().setSelectedIndex(i);
            }
        }
    }

    private  static void editUIclickFind(){
        editUI.getFindBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = editUI.getListOfFindID();
                Object currentID = comboBox.getSelectedItem();
                if(!currentID.toString().equals("Not found")){
                    Student currentStu  = findStuByID(currentID.toString());
                    editUI.getNameInput().setText(currentStu.getNameStu());
                    setSelectionBox(currentStu.getDepartment());
                }else {
                    System.out.println("Validation");
                }
            }
        });
    }

    private static void editUIClickOk(){
        editUI.getOkBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = editUI.getNameInput().getText();
                String curId = editUI.getListOfFindID().getSelectedItem().toString();
                String newDep = editUI.getListOfMajor().getSelectedItem().toString();
                System.out.println(newName+" "+newDep+" "+curId);
                editStudentToDB(new Student(curId,newName,newDep));
            }
        });
    }

    private static void editUIClickCancel(){
        editUI.getCancelBtn().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("cancellllllll");
            }
        });
    }

    private static void addOnClick(){
        btnList.get(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String majorGetter = selection.getItemAt(selection.getSelectedIndex()).toString();
                String nameInput = tfName.getText();
                String idInput = tfId.getText();
                Student getStu = new Student(idInput,nameInput,majorGetter);
                System.out.println(getStu.toString());
                addStudentToDB(getStu);
            }
        });
    }

    private static void runUI() {
        frm = new JFrame("QLSV-VIP-VERSION");
        frm.setSize(500,220);
        frm.setLocation(200,200);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setLayout(new BorderLayout());
        JLabel title = new JLabel("STUDENT MANAGEMENT");
        title.setForeground(Color.BLUE);
        title.setFont(new Font("Arial",Font.BOLD,26));
        JPanel titleCenter = new JPanel(new GridBagLayout());
        titleCenter.add(title);
        JPanel leftContainer = new JPanel(new BorderLayout());
        JPanel labelContainer = new JPanel(new GridLayout(3,1));
        JPanel TextFContainer = new JPanel(new GridLayout(3,1));
        JLabel lbId = new JLabel(labelContent[0]);
        JLabel lbName = new JLabel(labelContent[1]);
        JLabel lbDep = new JLabel(labelContent[2]);
        labelContainer.add(lbId);
        labelContainer.add(lbName);
        labelContainer.add(lbDep);
        tfId = new JTextField();
        tfName = new JTextField();
        selection = new JComboBox(major);
        selection.setSelectedIndex(2);
        TextFContainer.add(tfId);
        TextFContainer.add(tfName);
        TextFContainer.add(selection);
        JPanel bottomRight = new JPanel(new BorderLayout());
        bottomRight.add(TextFContainer,BorderLayout.CENTER);
        labelContainer.setPreferredSize(new Dimension(85,200));
        bottomRight.add(labelContainer,BorderLayout.WEST);
        JPanel underRight = new JPanel(new BorderLayout());
        JPanel addSearch = new JPanel(new GridLayout(2,1));
        JPanel restBtn = new JPanel(new GridLayout(2,2));
        addSearch.add(btnList.get(0));
        addSearch.setPreferredSize(new Dimension(85,20));
        System.out.println(btnList.get(0).getSize());
        addSearch.add(btnList.get(3));
        restBtn.add(btnList.get(1));
        restBtn.add(btnList.get(2));
        restBtn.add(btnList.get(4));
        restBtn.add(btnList.get(5));
        underRight.add(addSearch,BorderLayout.WEST);
        underRight.add(restBtn,BorderLayout.CENTER);
        leftContainer.add(underRight,BorderLayout.SOUTH);
        leftContainer.add(bottomRight,BorderLayout.CENTER);
        //for table
        model = new DefaultTableModel();
        model.addColumn("StuID");
        model.addColumn("Name");
        model.addColumn("Department");
        JTable table = new JTable(model){
            public boolean isCellEditable(int row, int column) {
                return false;
            };
        };
        JScrollPane tableScroll = new JScrollPane(table);
        JPanel mainContent = new JPanel(new GridLayout(1,2));
        mainContent.add(leftContainer);
        mainContent.add(tableScroll);
        frm.add(mainContent,BorderLayout.CENTER);
        frm.add(titleCenter,BorderLayout.NORTH);
        List<Object[]> listData = MStudent.getData();
        renderData(listData);
        frm.setVisible(true);
    }

    public static void runAll(){
        renderBtn();
        connectionDB();
        runUI();
        getDataFRomDB();
        addOnClick();
        editStudentOnclick();
    }

    public static void main(String[] args) {
        UXUI.runAll();
    }

}
