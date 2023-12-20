package QLSV_DB;

import QLSV_DB.Student;

import java.util.ArrayList;
import java.util.List;

public class MStudent {

    private static List<Object[]> listStu = new ArrayList<>();

    public static List<Object[]> addNew(QLSV_DB.Student stud) {
        Object[] setCurrent = {stud.getStuId(),stud.getNameStu(),stud.getDepartment()};
        listStu.add(setCurrent);
        return listStu;
    }

    public static List<Object[]> deleteStu(int index){
        listStu.remove(index);
        return listStu;
    }

    public static List<Object[]> updateData(int index, QLSV_DB.Student stud){
        listStu.remove(index);
        Object[] setCurrent = {stud.getStuId(),stud.getNameStu(),stud.getDepartment()};
        listStu.add(setCurrent);
        return listStu;
    }

    public static List<Object[]> searchId(String id){
        List<Object[]> listCopy = new ArrayList<>();
        for (int i = 0; i < listStu.size(); i++) {
            if(listStu.get(i)[0].toString().contains(id)){
                listCopy.add(listStu.get(i));
            }
        }
        if(listCopy.size() == 0){
            System.out.println("Khong thay");
        }
        return listCopy;
    }

    public static List<Object[]> clearData(){
        listStu = new ArrayList<>();
        return listStu;
    }


    public static List<Object[]> getData() {
        return listStu;
    }
}
