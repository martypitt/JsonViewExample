package com.mangofactory.jsonview.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.codehaus.jackson.map.annotate.JsonView;

import com.mangofactory.jsonview.BaseView;

@Data @NoArgsConstructor @AllArgsConstructor
public class Book {

    @JsonView(SummaryView.class)
    private String title;
    @JsonView(SummaryView.class)
    private String author;
    private String review;
    @JsonView(BaseView.class)
	private Integer id;

    public static interface SummaryView extends BaseView {}
    
}
