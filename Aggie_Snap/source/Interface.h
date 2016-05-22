#ifndef SIMPLE_WINDOW_GUARD
#define SIMPLE_WINDOW_GUARD 1

#include "std_lib_facilities_4.h"
#include "GUI.h"
#include "GroupGraph.h"

using namespace Graph_lib;

//Making the Interface Window struct with all the attachments
struct Interface : Graph_lib::Window {
    Interface(Point xy, int w, int h, const string& title);
	Vector_ref<Image> list;	// vector of the tagged images
    bool wait_for_button(); // simple event loop
	int current_image = -1;
	void load_from_file();
	String filename_string;
	
private:
    Button next_button;     // the "next" button
	Button prev_button;		// the "previous" button
	Button save_button;
	// Check buttons
	Button family_button;
	Button friends_button;			
	Button aggieland_button;
	Button pets_button;
	Button vacation_button;
	Button browse_button;
	
	//===========================================-=
	Button submit_button;     // the submit button for image
    Button submit2;	//the submit button for url
    In_box inp;	//input box
    Out_box out;	//output box for errors and tags
    bool button_pushed;     // implementation detail

    static void cb_submit(Address, Address); // callback for image submit
    static void cb_next2(Address, Address);	//callback for url submit
    void submit();            // action to be done when submit image is pressed
    void next2();	// action to be done when submit url is pressed
    void image();	//image adding
    void url();	//url adding
    void save_material();
	//=============================================

    static void cb_next(Address, Address); // callback for next_button
	static void cb_prev(Address, Address); // callback for prev_button
	static void cb_family(Address, Address);	//Callbacks for all the tag buttons
	static void cb_friends(Address, Address);
	static void cb_aggieland(Address, Address);
	static void cb_pets(Address, Address);
	static void cb_vacation(Address, Address);
	static void cb_save(Address, Address);
	static void cb_browse(Address, Address);
	
    void next();            // action to be done when next_button is pressed
	void prev();			// action to be done when prev_button is pressed
	void family();	//functions for the tag buttons
	void friends();
	void aggieland();
	void pets();
	void vacation();
	void save();
	void browse();
	
};

#endif // SIMPLE_WINDOW_GUARD