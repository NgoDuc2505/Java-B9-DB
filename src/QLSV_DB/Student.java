package QLSV_DB;

public class Student {
    private String stuId;
    private String nameStu;
    private String department;

    public Student(String stuId, String nameStu, String department) {
        this.stuId = stuId;
        this.nameStu = nameStu;
        this.department = department;
    }


    public void setNameStu(String nameStu) {
        this.nameStu = nameStu;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getStuId() {
        return stuId;
    }

    public String getNameStu() {
        return nameStu;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return "Student{" +
                "stuId='" + stuId + '\'' +
                ", nameStu='" + nameStu + '\'' +
                ", department='" + department + '\'' +
                '}';
    }
}
