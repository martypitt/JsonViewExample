package com.mangofactory.jsonview;

public interface DataView {
	boolean hasView();
	Class<? extends BaseView> getView();
	Object getData();
}
