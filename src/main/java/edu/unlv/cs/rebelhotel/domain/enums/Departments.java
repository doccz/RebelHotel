package edu.unlv.cs.rebelhotel.domain.enums;

public enum Departments {

    HOSPITALITY_MANAGEMENT, 
    HOTEL_ADMINISTRATION_BEVERAGE_MANAGEMENT, 
    FOOD_SERVICE_MANAGEMENT, 
    LODGING_AND_RESORT_MANAGEMENT, 
    MEETINGS_AND_EVENTS_MANAGEMENT,
    CULINARY_ARTS_MANAGEMENT, 
    CULINARY_ARTS_BEVERAGE_MANAGEMENT,
    GAMING_MANAGEMENT,
    NOVALUE;
    
	public static Departments toDept(String str)
	{
	    try {
	        return valueOf(str);
	    } 
	    catch (Exception ex) {
	        return NOVALUE;
	    }
	}   
}
