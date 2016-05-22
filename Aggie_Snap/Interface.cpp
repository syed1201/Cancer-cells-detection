#include "Interface.h"
#include "Browse_window.h"


using namespace Graph_lib;
//numbers for keeping track of images
int img_num = 1;
int pic_num=0;
auto url_length = 20;
//making the interface, positioning, attaching everything to window
Interface::Interface(Point xy,int w,int h,const string& title)
		: Window(xy,w,h,title),
		next_button(Point(730,287), 60, 25, "Next", cb_next),
		prev_button(Point(5,287), 60, 25, "Previous", cb_prev),
		family_button(Point(150,600), 70, 20, "Family", cb_family),
		friends_button(Point(250,600), 70, 20, "Friends", cb_friends),			
		aggieland_button(Point(350,600), 70, 20, "Aggieland", cb_aggieland),
		pets_button(Point(450,600), 70, 20, "Pets", cb_pets),
		vacation_button(Point(550,600), 70, 20, "Vacation", cb_vacation),
		submit_button(Point(300,750), 100, 20, "Submit Image", cb_submit), //------------------
		submit2(Point(400,750),100,20,"Submit URL", cb_next2),
		inp(Point(200,700),100,25,"label"),
		out(Point(200,725),200,25,"output"),
		save_button(Point(730,600),60,25,"Save",cb_save),
		browse_button(Point(730,630),60,25,"Browse",cb_browse),	
		button_pushed(false)
		{
			attach(next_button);
			attach(prev_button);
			attach(family_button);
			attach(friends_button);
			attach(aggieland_button);
			attach(pets_button);
			attach(vacation_button);
		    attach(submit_button);
            attach(submit2);
            attach(inp);
            attach(out);
		    attach(save_button);
			attach(browse_button);
		}


bool Interface::wait_for_button()
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
	
//=================================================================================
void Interface::cb_submit(Address, Address pw)
{  
    reference_to<Interface>(pw).submit();    
}

//------------------------------------------------------------------------------
//reads images from index, displays the image and the tags
void Interface::load_from_file()
{
	ifstream read{"index.txt"};
	
    while(!read.eof())//while there are iamages in index
   {
   	String file_name;
	String tags;
	String tag1,tag2,tag3,tag4,tag5;
	tag1 = "";
	tag2 = "";
	tag3 = "";
	tag4 = "";
	tag5 = "";
	read >> file_name >> tag1 >> tag2 >> tag3 >> tag4 >> tag5;
	if(tag1 == ",")tag1 = "";
	if(tag2 == ",")tag2 = "";
	if(tag3 == ",")tag3 = "";
	if(tag4 == ",")tag4 = "";
	if(tag5 == ",")tag5 = "";
	tags = tag1 + " " + tag2 + " " + tag3 + " " + tag4 + " " + tag5;
	
	out.put(tags);

	list.push_back(new Image(Point(100,100),file_name));
	++current_image;
	}
	read.close();
	if(list.size()==0)
	{//do nothing
	}
	else if(list.size()>0)
	attach(list[current_image]);
	}

//submit image
void Interface::submit()
{
    image();
    button_pushed = true;
    redraw();
}
//image function checks if it exists or correct fil type
//if true, adds the image to the list vector and the index.txt
void Interface::image()
{   String a;
    a=inp.get_string();
	if(a.size()>url_length)a=filename_string;
	int check = system(("cp " + a + " image.jpg").c_str());
    system("rm image.jpg");
    if(get_encoding(a)==Suffix::none) out.put("Incorrect file type");
    else if(check!=0)out.put("Doesn't exist");
    else
    {if(list.size()>0)
    {detach(list[current_image]);}
	
    list.push_back(new Image(Point(100,100),a));
    out.put("");
	current_image = list.size()-1;
    attach(list[list.size()-1]);}
	
	ifstream read{"index.txt"};
    while(!read.eof())
   {String word;
	String line; 

	read>>word;
	if(word==inp.get_string())
	{	getline (read,line);
 		out.put(line);}}
	read.close();}


//callback for url
void Interface::cb_next2(Address, Address pw)
{
    reference_to<Interface>(pw).next2();
}
//funciton for url callback
void Interface::next2()
{
    url();
    button_pushed=true;
    redraw();
}
//url function checks if it exists or correct fil type
//if true, adds the image to the list vector and the index.txt
void Interface::url()
{
    String a;
    a=inp.get_string();
    int b= a.size()-a.find_last_of('.')+1;
    String type=a.substr(a.find_last_of('.')-1,b);
    if(get_encoding(type)==Suffix::none)
    {
        out.put("Incorrect file type");
    }
    else
    {
        filename_string = to_string(img_num++) + a.substr(a.find_last_of('.')-1,b);
        system((string("wget -O ") + filename_string + " " + a).c_str());
        if(list.size()>0)
        {
             detach(list[current_image]);
        }
        list.push_back(new Image (Point(50,50),filename_string));
		current_image = list.size()-1;
        out.put("");
        attach(list[list.size()-1]);
	} 
}


//=================================================================
//all the callbacks for the tag buttons
void Interface::cb_next(Address, Address pw)
{
	reference_to<Interface>(pw).next();	
}	

void Interface::cb_prev(Address, Address pw)
{
	reference_to<Interface>(pw).prev();	
}

void Interface::cb_family(Address, Address pw)
{
	reference_to<Interface>(pw).family();	
}

void Interface::cb_friends(Address, Address pw)
{
	reference_to<Interface>(pw).friends();	
}

void Interface::cb_aggieland(Address, Address pw)
{
	reference_to<Interface>(pw).aggieland();	
}

void Interface::cb_pets(Address, Address pw)
{
	reference_to<Interface>(pw).pets();	
}

void Interface::cb_vacation(Address, Address pw)
{
	reference_to<Interface>(pw).vacation();	
}

void Interface::cb_save(Address, Address pw)
{
	reference_to<Interface>(pw).save();	
}

void Interface::cb_browse(Address, Address pw)
{
	reference_to<Interface>(pw).browse();	
}
//function for next, showing the next image
void Interface::next()
 {   if(current_image == list.size()-1)
	{//do nothing
	}
	
	else
	{detach(list[current_image]);
	++current_image;
	attach(list[current_image]);
	ifstream read{"index.txt"};
    while(!read.eof())
   {String file_name;
	String line; 
	String tags;
	String trash;
	
	read >> file_name;
	
	if (file_name == list[current_image].filename)
	{getline(read,tags);
	out.put(tags);}
	else
	getline(read,trash);}
	read.close();}
	redraw();}



//function for previous, shows the previous image
void Interface::prev()
{if(current_image-1<0)
	{//do nothing
	}
	else
	{detach(list[current_image]);
	--current_image;
	attach(list[current_image]);
	ifstream read{"index.txt"};
    while(!read.eof())
   {String file_name;
	String line; 
	String tags;
	String trash;
	
	read >> file_name;
	
	if(file_name == list[current_image].filename)
	{getline(read,tags);
	out.put(tags);}
	
	else getline(read,trash);}
	read.close();}
	redraw();}

//functions for the tag buttons
void Interface::family()
{
	{list[list.size()-1].family_val=true;
	redraw();}
}
void Interface::friends()
{
	{list[list.size()-1].friends_val = true;
	redraw();}
}

void Interface::aggieland()
{

	{list[list.size()-1].aggieland_val = true;
	redraw();}
}

void Interface::pets()
{

	{list[list.size()-1].pets_val = true;
	redraw();}
}
void Interface::vacation()
{

{	list[list.size()-1].vacation_val = true;
	redraw();}
}
//function for saving an image with tags to the .txt
void Interface::save()
{   String a;
    a=inp.get_string();
	if(a.size()>url_length)
	{string copyname="copy"+filename_string;
	button_pushed=true;
	if(button_pushed)
	{ifstream infile(filename_string,ios_base::binary);
	ofstream ost{copyname,ios_base::binary};
	
    infile.seekg(0,infile.end);
	long size = infile.tellg();
	infile.seekg(0);
	char*buffer = new char[size];
		
	infile.read(buffer, size);
	ost.write(buffer, size);
	delete[] buffer;	
	ost.close();
	infile.close();
	save_material();}}
	
	else{string copyname="copy"+a;
	button_pushed=true;
	if(button_pushed)
	{ifstream infile(a,ios_base::binary);
	ofstream ost{copyname,ios_base::binary};
	
    infile.seekg(0,infile.end);
	long size = infile.tellg();
	infile.seekg(0);
	char*buffer = new char[size];
		
	infile.read(buffer, size);
	ost.write(buffer, size);
	delete[] buffer;	
	ost.close();
	infile.close();
	save_material();}}}


//opens a new window for browsing with tags
void Interface::browse()
{
	Browse_window win2(Point(100,200),800,800,"Simple_window");
	win2.Get_vector(list);
	win2.wait_for_button();
	redraw();
}	
//function for saving to index file
void Interface::save_material()
{	String a;
	a=inp.get_string();
	if(a.size()>url_length)
	a = filename_string;
	ofstream ot{"index.txt",ios::out | ios::app };
	ot<<"\n";
	ot<< a << " ";
	
	if(list[list.size()-1].family_val) ot<<"family ";
	else ot<<", ";
	if(list[list.size()-1].friends_val) ot<<"friends ";
	else ot<<", ";
	if(list[list.size()-1].aggieland_val)  ot<<"aggieland ";
	else ot<<", ";
	if(list[list.size()-1].pets_val)   ot<<"pets ";
	else ot<<", ";
	if(list[list.size()-1].vacation_val)   ot<<"vacation ";
	else   ot<<", ";
		
    ot.close();}



