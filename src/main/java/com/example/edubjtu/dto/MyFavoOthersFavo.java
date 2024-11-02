package com.example.edubjtu.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MyFavoOthersFavo {
    private String otherFavoNum;
    private String otherFavoName;
    private String otherFavoCreaterName;

    public MyFavoOthersFavo(String otherFavoNum, String otherFavoName, String otherFavoCreaterName) {
        this.otherFavoNum = otherFavoNum;
        this.otherFavoName = otherFavoName;
        this.otherFavoCreaterName = otherFavoCreaterName;
    }
}
