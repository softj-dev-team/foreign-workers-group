package com.softj.pwg.vo;

import lombok.Data;

import java.util.Objects;

@Data
public class ParamVO {
    private long seq;
    private long boardSeq;
    private long userSeq;
    private String nickname;
    private String platform;
    private String id;
    private String nation;
    private String nationName;
    private String content;
    private String subject;
    private long views;
    private String search;
    private Boolean isMyWritePage;
    private Boolean isMyCommentPage;
    private Boolean isMyLikePage;

    public Boolean isMyWritePage() {
        if (Objects.isNull(isMyWritePage)) {//참조형으로바꿔줄때
            return Boolean.FALSE;
        }
        return isMyWritePage;
    }

    public Boolean isMyCommentPage() {
        if (Objects.isNull(isMyCommentPage)) {
            return Boolean.FALSE;
        }
        return isMyCommentPage;
    }

    public Boolean isMyLikePage() {
        if (Objects.isNull(isMyLikePage)) {
            return Boolean.FALSE;
        }
        return isMyLikePage;
    }
}
