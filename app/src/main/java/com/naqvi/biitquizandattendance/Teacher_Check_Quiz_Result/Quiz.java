package com.naqvi.biitquizandattendance.Teacher_Check_Quiz_Result;

public class Quiz {
    public String Quiz_Id;
    public String Quiz_Tile;
    public String Quiz_Date;
    public String Time;

    public Quiz(String quiz_Id, String quiz_Tile, String quiz_Date, String time) {
        Quiz_Id = quiz_Id;
        Quiz_Tile = quiz_Tile;
        Quiz_Date = quiz_Date;
        Time = time;
    }

    public Quiz() {
    }
}
