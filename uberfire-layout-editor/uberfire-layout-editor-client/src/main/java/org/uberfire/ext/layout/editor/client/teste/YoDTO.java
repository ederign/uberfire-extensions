package org.uberfire.ext.layout.editor.client.teste;

public class YoDTO {

    private String name;
    private String dogName;


    public YoDTO(  ) {
    }

    public YoDTO( String name, String dogName ) {
        this.name = name;
        this.dogName = dogName;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName( String dogName ) {
        this.dogName = dogName;
    }

}
