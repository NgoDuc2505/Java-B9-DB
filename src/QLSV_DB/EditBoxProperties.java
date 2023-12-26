package QLSV_DB;

import javax.swing.*;

public class EditBoxProperties {
    private JFrame frame;
    private JTextField idInput;
    private JTextField nameInput;
    private JComboBox listOfFindID;
    private JComboBox listOfMajor;

    private JButton okBtn;

    private JButton cancelBtn;
    private JButton findBtn;


    public EditBoxProperties(JFrame frame, JTextField idInput, JTextField nameInput, JComboBox listOfFindID, JComboBox listOfMajor, JButton okBtn, JButton cancelBtn,JButton findBtn ) {
        this.frame = frame;
        this.idInput = idInput;
        this.nameInput = nameInput;
        this.listOfFindID = listOfFindID;
        this.listOfMajor = listOfMajor;
        this.okBtn = okBtn;
        this.cancelBtn = cancelBtn;
        this.findBtn = findBtn;
    }


    public JFrame getFrame() {
        return frame;
    }

    public JTextField getIdInput() {
        return idInput;
    }

    public JTextField getNameInput() {
        return nameInput;
    }

    public JComboBox getListOfFindID() {
        return listOfFindID;
    }

    public JComboBox getListOfMajor() {
        return listOfMajor;
    }

    public JButton getOkBtn() {
        return okBtn;
    }

    public JButton getCancelBtn() {
        return cancelBtn;
    }

    public JButton getFindBtn() {
        return findBtn;
    }
}
