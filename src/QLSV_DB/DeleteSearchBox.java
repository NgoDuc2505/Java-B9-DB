package QLSV_DB;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeleteSearchBox {
    private static JFrame frame;
    private static  JTextField idTf;
    private static  JLabel notice;
    private static  JPanel btnCotainer;
    private static  JButton findBtn;
    private static  JButton deleteBtn;
    private static  JButton cancelBtn;

    private static boolean isExit = false;


    private static void rinUI(){
        frame = new JFrame("Box");
        frame.setSize(400,200);
        frame.setLocation(200,400);
        frame.setDefaultCloseOperation(1);
        frame.setLayout(new BorderLayout());
        JLabel title = new JLabel("Delete Form");
        title.setForeground(Color.RED);
        title.setFont(new Font("Arial",Font.BOLD,26));
        JPanel titleCenter = new JPanel(new GridBagLayout());
        titleCenter.add(title);
        JLabel idLbl = new JLabel("Enter ID:");
        idTf = new JTextField();
        notice = new JLabel("  ");
        JPanel noticeCenter = new JPanel(new GridBagLayout());
        noticeCenter.add(notice);
        noticeCenter.setBorder(new MatteBorder(1,10,1,10,Color.black));
        JPanel container = new JPanel(new BorderLayout());
        JPanel subContainer = new JPanel(new GridLayout(1,2));
        subContainer.add(idLbl);
        subContainer.add(idTf);
        noticeCenter.setPreferredSize(new Dimension(200,50));
        container.add(subContainer,BorderLayout.CENTER);
        container.add(noticeCenter,BorderLayout.SOUTH);
        findBtn = new JButton("Find");
        deleteBtn = new JButton("Delete");
        cancelBtn = new JButton("Cancel");
        btnCotainer = new JPanel(new GridLayout(1,2));
        btnCotainer.add(cancelBtn);
        btnCotainer.add(findBtn);
        findIsExits();
        frame.add(container,BorderLayout.CENTER);
        frame.add(btnCotainer,BorderLayout.SOUTH);
        frame.add(titleCenter,BorderLayout.NORTH);
        frame.setVisible(true);
    }

    public static void findIsExits(){
        if(isExit){
            btnCotainer.remove(findBtn);
            btnCotainer.add(deleteBtn);
            frame.revalidate();
            frame.repaint();
        }
    }

    public static void renderAll(){
        rinUI();
        cancelOnclick();
    }

    public static void cancelOnclick(){
        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });
    }

    public static JFrame getFrame() {
        return frame;
    }

    public static JTextField getIdTf() {
        return idTf;
    }

    public static JLabel getNotice() {
        return notice;
    }

    public static JPanel getBtnCotainer() {
        return btnCotainer;
    }

    public static JButton getFindBtn() {
        return findBtn;
    }

    public static JButton getDeleteBtn() {
        return deleteBtn;
    }

    public static JButton getCancelBtn() {
        return cancelBtn;
    }
}
