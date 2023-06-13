package com.naqvi.biitquizandattendance.Student_Quiz_Result;

public class Quiz_Answers {
    public int Question_No;
    public String Question;
    public String Option1;
    public String Option2;
    public String Option3;
    public String Answer;
    public String Correct_Answer;
    public int Mark;

    public Quiz_Answers(int question_No, String question, String option1, String option2, String option3, String answer, String correct_Answer, int mark) {
        Question_No = question_No;
        Question = question;
        Option1 = option1;
        Option2 = option2;
        Option3 = option3;
        Answer = answer;
        Correct_Answer = correct_Answer;
        Mark = mark;
    }
}
