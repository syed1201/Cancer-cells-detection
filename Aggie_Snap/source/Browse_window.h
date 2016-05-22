//#ifndef SIMPLE_WINDOW_GUARD
//#define SIMPLE_WINDOW_GUARD 1

#include "GUI.h"
#include "GroupGraph.h"
//#include "Tagged_Image.h"
//#include <FL/Fl.H>
//#include <FL/Fl_Button.H>
//#include <FL/Fl_Check_Button.H>
//#include <FL/Fl_Output.H>
#include "std_lib_facilities_4.h"

using namespace Graph_lib;

struct Browse_window : Graph_lib::Window {
    Browse_window(Point xy, int w, int h, const string& title);
	
	Vector_ref<Image> list2;
	Vector_ref<Image> Filtered_list;
	
	Button next_button;     // the "next" button
	Button prev_button;		// the "previous" button
	Button family_button;
	Button friends_button;			
	Button aggieland_button;
	Button pets_button;
	Button vacation_button;
	Button update_button;
	
	bool button_pushed;
	bool wait_for_button();
	
	static void cb_next(Address, Address); // callback for next_button
	static void cb_prev(Address, Address); // callback for prev_button
	static void cb_family(Address, Address);
	static void cb_friends(Address, Address);
	static void cb_aggieland(Address, Address);
	static void cb_pets(Address, Address);
	static void cb_vacation(Address, Address);
	static void cb_update(Address, Address);
	
	void next();            // action to be done when next_button is pressed
	void prev();
	void Get_vector(Vector_ref<Image>& Pictures);
	void family();
	void friends();
	void aggieland();
	void pets();
	void vacation();
	void update();
	
	String browse_family = ",";
	String browse_friends = ",";
	String browse_aggieland = ",";
	String browse_pets = ",";
	String browse_vacation = ",";
	
	int current_image = 0;
	int count_update = 0;
	int family_count = 0;
	int friends_count = 0;
	int aggieland_count = 0;
	int pets_count = 0;
	int vacation_count = 0;
	};
	
//#endif