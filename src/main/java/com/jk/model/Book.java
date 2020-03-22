package com.jk.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Data
@Document(indexName = "book2index")
public class Book implements Serializable {

    private static final long serialVersionUID = 5940649981185764763L;
    @Id
    private String id;

    @Field(type = FieldType.Text,copyTo = "copy")
    private String bname;

    @Field(type = FieldType.Text,copyTo = "copy")
    private String bjianjie;

    @Field(type=FieldType.Long)
    private Integer price;

    private Integer btype;

    private Integer minprice;

    private Integer maxprice;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getBjianjie() {
        return bjianjie;
    }

    public void setBjianjie(String bjianjie) {
        this.bjianjie = bjianjie;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getBtype() {
        return btype;
    }

    public void setBtype(Integer btype) {
        this.btype = btype;
    }

    public Integer getMinprice() {
        return minprice;
    }

    public void setMinprice(Integer minprice) {
        this.minprice = minprice;
    }

    public Integer getMaxprice() {
        return maxprice;
    }

    public void setMaxprice(Integer maxprice) {
        this.maxprice = maxprice;
    }
}
