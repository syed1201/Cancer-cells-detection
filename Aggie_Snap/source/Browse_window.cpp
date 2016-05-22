#include "Browse_window.h"
#include "std_lib_facilities_4.h"
#include "GroupGraph.h"

using namespace Graph_lib;
Browse_window::Browse_window(Point xy,int w,int h,const string& title)
		: Window(xy,w,h,title),
		next_button(Point(730,287), 60, 25, "Next", cb_next),
		prev_button(Point(5,287), 60, 25, "Previous", cb_prev),
		family_button(Point(150,600), 70, 20, "Family", cb_family),
		friends_button(Point(250,600), 70, 20, "Friends", cb_friends),			
		aggieland_button(Point(350,600), 70, 20, "Aggieland", cb_aggieland),
		pets_button(Point(450,600), 70, 20, "Pets", cb_pets),
		vacation_button(Point(550,600), 70, 20, "Vacation", cb_vacation),
		update_button(Point(730,600),60,25,"Update",cb_update),
		button_pushed(false)
		{
			attach(next_button);
			attach(prev_button);
			attach(family_button);
			attach(friends_button);
			attach(aggieland_button);
			attach(pets_button);
			attach(vacation_button);
			attach(update_button);
		}
		
bool Browse_window::wait_for_button()
	{
		show();
		button_pushed = false;
	#if 1
		// Simpler handler
		while (!button_pushed) Fl::wait();
		Fl::redraw();
	#else
		// To handle the case where the user presses the X button in the window frame
		// to kill the application, change the condition to 0 to enable this branch.
		Fl::run();
	#endif
		return button_pushed;
	}
	
//Callback functions for button functions	
	
void Browse_window::cb_next(Address, Address pw)
{
	reference_to<Browse_window>(pw).next();	
}	

void Browse_window::cb_prev(Address, Address pw)
{
	reference_to<Browse_window>(pw).prev();	
}

void Browse_window::cb_family(Address, Address pw)
{
	reference_to<Browse_window>(pw).family();	
}

void Browse_window::cb_friends(Address, Address pw)
{
	reference_to<Browse_window>(pw).friends();	
}

void Browse_window::cb_aggieland(Address, Address pw)
{
	reference_to<Browse_window>(pw).aggieland();	
}

void Browse_window::cb_pets(Address, Address pw)
{
	reference_to<Browse_window>(pw).pets();	
}

void Browse_window::cb_vacation(Address, Address pw)
{
	reference_to<Browse_window>(pw).vacation();	
}

//Definition of function buttons

void Browse_window::Get_vector(Vector_ref<Image>& list)
{	
	for (auto x = 0; x != list.size()-1; ++x)	//Ranged-Based for loop approved by Jay
	{
    list2.push_back(list[x]);
	}
	attach(list2[0]); // increments the value in the vector
}

void Browse_window::cb_update(Address, Address pw) 
{
	reference_to<Browse_window>(pw).update();	
}

void Browse_window::family()
{
	browse_family = "family";
	redraw();
	++family_count;
}
void Browse_window::friends()
{
	browse_friends = "friends";
	redraw();
	++friends_count;
}

void Browse_window::aggieland()
{
	browse_aggieland = "aggieland";
	redraw();
	++aggieland_count;
}

void Browse_window::pets()
{
	browse_pets = "pets";
	redraw();
	++pets_count;
}

void Browse_window::vacation()
{
	browse_vacation = "vacation";
	redraw();
	++vacation_count;
}

void Browse_window::update()
{   String tags;
	String file_name;
	String tag1, tag2, tag3, tag4, tag5;
	tag1 = "";
	tag2 = "";
	tag3 = "";
	tag4 = "";
	tag5 = "";
	
	if(count_update == 0)      detach(list2[current_image]);
	else      detach(Filtered_list[current_image]);
	
	ifstream read{"index.txt"};
    while(!read.eof())
   {bool req1, req2, req3, req4, req5 = false;
	read >> file_name >> tag1 >> tag2 >> tag3 >> tag4 >> tag5;
																			
	tags = tag1 + " " + tag2 + " " + tag3 + " " + tag4 + " " + tag5;
	String Browse_string = browse_family + " " + browse_friends + " " + browse_aggieland + " " + browse_pets + " " + browse_vacation;
	if(Browse_string == tags) Filtered_list.push_back(new Image(Point(100,100),file_name)); //Determine if image meets browse criteria
	}
	attach(Filtered_list[0]);
	cout << Filtered_list.size() << endl;
	current_image = 0;
	count_update++;}

//To display next image
	
void Browse_window::next()
{
	if(family_count == 0 && friends_count == 0 && aggieland_count == 0 && pets_count == 0 && vacation_count == 0)
	{
		if(current_image == list2.size()-1) //Check if current image is last element in vector
		{
		detach(list2[current_image]);		//If so, do nothing
		attach(list2[current_image]);
		}
		else
		{
		detach(list2[current_image]);
		attach(list2[current_image+1]);
		++current_image;
		}
	}
	else if(current_image == Filtered_list.size()-1)
	{detach(Filtered_list[current_image]);
	attach(Filtered_list[current_image]);}
	else
	{detach(Filtered_list[current_image]);
	attach(Filtered_list[current_image+1]);
	++current_image;}
	redraw();}

//To display previous image	
	
void Browse_window::prev()
{
	if(family_count == 0 && friends_count == 0 && aggieland_count == 0 && pets_count == 0 && vacation_count == 0)
	{
		if(current_image-1 < 0)				//Check if current image is first element in vector
		{detach(list2[current_image]);
		attach(list2[current_image]);}		//If so, do nothing
		else
		{detach(list2[current_image]);
		attach(list2[current_image-1]);
		--current_image;}
		}
	   else if(current_image-1 < 0)
	 {  detach(Filtered_list[current_image]);
		attach(Filtered_list[current_image]);}
	else
	{	detach(Filtered_list[current_image]);
		attach(Filtered_list[current_image-1]);
		--current_image;}
	redraw();}



