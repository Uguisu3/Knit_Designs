# Knit Designs
------------
An App that supports the creation of patterns for knitting and the tracking of where in the pattern the knitter is currently.
Has three different Activities: a Main Menu, an Edit, and a Track.

# Main Menu
------------
  This activity is main location to get everywhere else in the app, with buttons to get to where you want: new, edit, track.
  ![Alt text](Main Menu.png?raw=true "Main Menu")
  
# Edit
------------
  This activity allows the edittiing of an images (either blank or selected from the device) with a "paint bursh" or 
  "pain bucket". There is an option for copy and paste, but it hasn't been implemented yet. There is an options to change
  the size of the image being manipulated by interpolating the image to the new size. Also an options to change the canvas
  size, which cuts of part of the image when the canvas size is smaller or adds white sections where the canvas is bigger 
  than the original image. The colors limited to a list selected by the user or added from the image chosen. These can be
  added to or removed from. There is an option to save the image you have created. Lastly there is a button to get to the
  track activity. Currently the only way to get to the track activity succesufully with your current image is to save the
  image, then track the image.

# Track
------------
  This activity keeps track of where in the image you are currently want to be working on. There are button to increment by 
  one in either the row or column for where the selected "pixel" is. Also you can change the selected "pixel" by either
  choosing the numerical values for rows and columns or by touching the location wanted. There is a square near the bottom 
  that indicates the current color being selected.
