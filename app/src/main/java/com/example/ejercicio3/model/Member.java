package com.example.ejercicio3.model;

public class Member {
   private String Name;
   private String Matricula;
   private String Address;
   private String Expresion;
   private String Image;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Member() {
        super();
    }

    public Member(String name, String matricula, String address, String expresion, String image) {
        Name = name;
        Matricula = matricula;
        Address = address;
        Expresion = expresion;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMatricula() {
        return Matricula;
    }

    public void setMatricula(String matricula) {
        Matricula = matricula;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getExpresion() {
        return Expresion;
    }

    public void setExpresion(String expresion) {
        Expresion = expresion;
    }


}
