#include "Interface.h"
#include "Browse_window.h"

//Main program of AggieSnap project
int main()
{
	
	try
	{	
		//Making the program run from the Interface files
		Interface win1(Point(100,200),800,800,"Simple_window");
		win1.load_from_file();
		return gui_main();
	}//Errors with main programs diplays errors message
	catch(exception& e)
	{
		cerr << "error: " << e.what() << '\n';
		keep_window_open();
		return 1;
	}
	catch(...)
	{
		cerr << "Oops: unknown exception!\n";
		keep_window_open();
		return 2;
	}
}   
