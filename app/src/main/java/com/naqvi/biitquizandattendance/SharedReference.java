package com.naqvi.biitquizandattendance;

import android.content.Context;
import android.content.SharedPreferences;

import com.naqvi.biitquizandattendance.Student_Attempt_Quiz.Quiz_Pin_Detail;

public class SharedReference {
    SharedPreferences ShredRef;

    public SharedReference(Context context) {
        ShredRef = context.getSharedPreferences("myRef",Context.MODE_PRIVATE);
    }


    public void saveUser(User User){
        SharedPreferences.Editor editor = ShredRef.edit();
        editor.putString("Name",User.Name);
        editor.putString("User", User.UserName);
        editor.putString("Password", User.Password);
        editor.putString("Type", User.Type);
        editor.commit();
    }

    public User getUser(){
        String Name = ShredRef.getString("Name","No Name");
        String UserName = ShredRef.getString("User","No userName");
        String Password = ShredRef.getString("Password","No password");
        String Type = ShredRef.getString("Type","No Type");
        return new User(Name,UserName,Password,Type);
    }

    public void saveSectionAndSubject(SectiomAndSubject s){
        SharedPreferences.Editor editor = ShredRef.edit();
        editor.putString("Section",s.Section);
        editor.putString("Subject",s.Subject);
        editor.commit();
    }


    public SectiomAndSubject getSectionAndSubject(){
        String Section = ShredRef.getString("Section","No Section");
        String Subject = ShredRef.getString("Subject","No Subject");

        return new SectiomAndSubject(Section,Subject);
    }


    public void saveQuizPinDetail(Quiz_Pin_Detail pin_detail){
        SharedPreferences.Editor editor = ShredRef.edit();
        editor.putString("Quiz_Detail_Id",pin_detail.Quiz_Detail_Id);
        editor.putString("QuizId",pin_detail.QuizId);
        editor.putString("QuizPin",pin_detail.Quiz_Pin);
        editor.putString("QuizSubject",pin_detail.Subject);
        editor.putString("QuizTitle",pin_detail.Quiz_Title);

        editor.commit();
    }

    public Quiz_Pin_Detail getQuizPinDetail(){
        String Quiz_Detail_Id = ShredRef.getString("Quiz_Detail_Id","No Quiz_Detail_Id");
        String QuizId = ShredRef.getString("QuizId","No QuizId");
        String Quiz_Pin = ShredRef.getString("QuizPin","No Pin");
        String Quiz_Subject = ShredRef.getString("QuizSubject","No subject");
        String Quiz_Title = ShredRef.getString("QuizTitle","No QuizTitle");
        return new Quiz_Pin_Detail(Quiz_Detail_Id,QuizId,Quiz_Pin,Quiz_Subject,Quiz_Title);
    }

    public void saveCurrentQuizId(String CurrentQuizId){
        SharedPreferences.Editor editor = ShredRef.edit();
        editor.putString("CurrentQuizId", CurrentQuizId);
        editor.commit();
    }

    public String getCurrentQuizId(){
        String CurrentQuizId = ShredRef.getString("CurrentQuizId","No CurrentQuizId");
        return CurrentQuizId;
    }


    public void saveTeacher(String Teacher){
        SharedPreferences.Editor editor = ShredRef.edit();
        editor.putString("Teacher", Teacher);
        editor.commit();
    }

    public String getTeacher(){
        String Teacher = ShredRef.getString("Teacher","No Teacher");
        return Teacher;
    }


    public void saveCourse_No(String CurrentQuizId){
        SharedPreferences.Editor editor = ShredRef.edit();
        editor.putString("Course_No", CurrentQuizId);
        editor.commit();
    }

    public String getCourse_No(){
        String CurrentQuizId = ShredRef.getString("Course_No","No Course_No");
        return CurrentQuizId;
    }

    public void saveLt_No(String Lt_No){
        SharedPreferences.Editor editor = ShredRef.edit();
        editor.putString("Lt_No", Lt_No);
        editor.commit();
    }

    public String getLt_No(){
        String CurrentQuizId = ShredRef.getString("Lt_No","No Lt_No");
        return CurrentQuizId;
    }


    public void saveImage(String Image){
        SharedPreferences.Editor editor = ShredRef.edit();
        editor.putString("Image1", Image);
        editor.commit();
    }

    public String getImage(){
        String Image1 = ShredRef.getString("Image1","No Image1");
        return Image1;
    }


    public void savebackactivity(String back){
        SharedPreferences.Editor editor = ShredRef.edit();
        editor.putString("back", back);
        editor.commit();
    }

    public String getbackactivity(){
        String Image1 = ShredRef.getString("back","No");
        return Image1;
    }



}