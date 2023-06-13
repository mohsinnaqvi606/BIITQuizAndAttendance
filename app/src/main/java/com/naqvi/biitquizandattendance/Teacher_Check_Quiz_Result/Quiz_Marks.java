package com.naqvi.biitquizandattendance.Teacher_Check_Quiz_Result;

public class Quiz_Marks {

    public String Quiz_Detail_Id;
    public String Image;
    public String Reg_No;
    public String Name;
    public String Marks;


    public Quiz_Marks(String quiz_Detail_Id, String image, String reg_No, String name, String marks) {
        Quiz_Detail_Id = quiz_Detail_Id;
        Image = image;
        Reg_No = reg_No;
        Name = name;
        Marks = marks;
    }

    public Quiz_Marks() {
    }
}
