package QLSV_DB;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class UXUI {
    private static String[] btnContent = {"Add","Edit","Delete","Search","Clear","Cancel"};

    private static List<JButton> btnList = new ArrayList<>();

    private static String[] labelContent = {"StudentID","Name","Department"};

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
            String query2 = String.format("INSERT INTO Sinhvien(MaSV,FullName,MaKhoa) VALUES ('%s','%s','%s');",stu.getStuId(),stu.getNameStu(),departmentId);
            statement.executeUpdate(query2);
            List<Object[]> list = MStudent.addNew(stu);
            renderData(list);
        }catch (SQLException e){
            System.out.println("Add error!");
        }
    }

    private static void runUIEdit(){
        JFrame frm2 = new JFrame("Edit student !");
        frm2.setDefaultCloseOperation(1);
        frm2.setSize(300,220);
        frm2.setLocation(300,400);
        String[] listIdTest = {"svo1","sv02"};
        JLabel titleEdit = new JLabel("Edit form");
        JTextField getId = new JTextField("id");
        JComboBox listID = new JComboBox(listIdTest);
        JPanel idPanel = new JPanel(new GridLayout(1,2));
        idPanel.add(getId);
        idPanel.add(listID);
        JLabel newNameLabel = new JLabel("newName");
        JTextField newName = new JTextField();
        JPanel panelName = new JPanel(new GridLayout(1,2));
        panelName.add(newNameLabel);
        panelName.add(newName);

        JComboBox newDepart = new JComboBox(listIdTest);
        JButton okBtn = new JButton("Ok");
        JButton cancelBtnEdit = new JButton("Cancel");
        JPanel panelBtn = new JPanel(new GridLayout(1,2));
        panelBtn.add(okBtn);
        panelBtn.add(cancelBtnEdit);
        frm2.setLayout(new GridLayout(5,1));
        frm2.add(titleEdit);
        frm2.add(idPanel);
        frm2.add(panelName);
        frm2.add(newDepart);
        frm2.add(panelBtn);
        frm2.setVisible(true);
    }

    private static void editStudentOnclick(){
        btnList.get(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("checkk");
                runUIEdit();


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
