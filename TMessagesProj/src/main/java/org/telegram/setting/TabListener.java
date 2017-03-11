package org.telegram.setting;

public interface TabListener {
	public void onTabSelected(MaterialTab tab);
	
	public void onTabReselected(MaterialTab tab);
	
	public void onTabUnselected(MaterialTab tab);
}
