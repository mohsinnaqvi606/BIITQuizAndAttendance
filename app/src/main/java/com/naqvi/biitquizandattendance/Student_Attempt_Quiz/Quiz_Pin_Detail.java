package com.naqvi.biitquizandattendance.Student_Attempt_Quiz;

public class Quiz_Pin_Detail {
    public String Quiz_Detail_Id;
    public String QuizId;
    public String Quiz_Pin;
    public String Subject;
    public String Quiz_Title;

    public Quiz_Pin_Detail(String quiz_Detail_Id, String quizId, String quiz_Pin, String subject, String quiz_Title) {
        Quiz_Detail_Id = quiz_Detail_Id;
        QuizId = quizId;
        Quiz_Pin = quiz_Pin;
        Subject = subject;
        Quiz_Title = quiz_Title;
    }
}
