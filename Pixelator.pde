import processing.video.*;

Capture cam;

PImage img;

Button snapButton;
Button newPhotoButton;
Button pixalateButton;

boolean updateImage = false;

void setup()
{
  size(480, 540);
  
  img = new PImage(width, height);
  
  snapButton = new Button("Snap", 10, height - 50, 50, 36);
  newPhotoButton = new Button("New Pic", 70, height - 50, 50, 36);
  pixalateButton = new Button("Pixalate", 130, height - 50, 50, 36);
  
  String[] cameras = Capture.list();
  
  if (cameras.length == 0) {
    println("There are no cameras available for capture.");
    exit();
  } else {
    println("Available cameras:");
    for (int i = 0; i < cameras.length; i++) {
      println(cameras[i]);
    }
    
    // The camera can be initialized directly using an 
    // element from the array returned by list():
    cam = new Capture(this, cameras[0]);
    cam.start();     
  }
}

void draw()
{
  background(100);
  
  if(!updateImage)
  {
    if (cam.available() == true)
    {
      cam.read();
      img = cam;
    }
  
    float imgWidth = (height * img.width)/img.height;
    imageMode(CENTER);
    image(img, width/2, width/2, imgWidth, height);
  
    snapButton.display();  
    newPhotoButton.display();
    pixalateButton.display();
  }
  else
  {
    textAlign(CENTER,CENTER);
    text("updating", width/2, width/2);
    updateImage = false;
  }
}

void mouseReleased()
{
  if(snapButton.mouseReleased())
  {
    cam.stop();
  }
  if(newPhotoButton.mouseReleased())
  {
    cam.start();
  }
  if(pixalateButton.mouseReleased())
  {
    Pixalate();
  }
}

void Pixalate()
{ 
  img.loadPixels(); 
  for (int y = 3; y < img.height; y += 5) {
    for (int x = 3; x < img.width; x += 5) {
      int loc = x + y* img.width;
      
      // The functions red(), green(), and blue() pull out the 3 color components from a pixel.
      float r = red(img.pixels[loc]);
      float g = green(img.pixels[loc]);
      float b = blue(img.pixels[loc]);
      
      // Image Processing would go here
      // If we were to change the RGB values, we would do it here, before setting the pixel in the display window.
      
      if((x - 2) >= 0)
      {
        img.pixels[(x-1) + y*img.width] =  color(r,g,b);
        img.pixels[(x-2) + y*img.width] =  color(r,g,b);
      }
      if((y - 2) >= 0)
      {
        img.pixels[x + (y-1)*img.width] =  color(r,g,b);
        img.pixels[x + (y-2)*img.width] =  color(r,g,b);
      }
      if((x - 2) >= 0 && (y - 2) >= 0)
      {
        img.pixels[(x-1) + (y-1)*img.width] =  color(r,g,b);
        img.pixels[(x-2) + (y-1)*img.width] =  color(r,g,b);
        img.pixels[(x-1) + (y-2)*img.width] =  color(r,g,b);
        img.pixels[(x-2) + (y-2)*img.width] =  color(r,g,b);
      }
      if((x + 2) <= img.width)
      {
        img.pixels[(x+1) + y*img.width] =  color(r,g,b);
        img.pixels[(x+2) + y*img.width] =  color(r,g,b);
      }
      if((y + 2) < img.height)
      {        
        img.pixels[x + (y+1)*img.width] =  color(r,g,b);
        img.pixels[x + (y+2)*img.width] =  color(r,g,b);
      }
      if((x + 2) <= img.width && (y + 2) < img.height)
      {
        img.pixels[(x+1) + (y+1)*img.width] =  color(r,g,b);
        img.pixels[(x+2) + (y+1)*img.width] =  color(r,g,b);
        img.pixels[(x+1) + (y+2)*img.width] =  color(r,g,b);
        img.pixels[(x+2) + (y+2)*img.width] =  color(r,g,b);
      }
      
      
      // Set the display pixel to the image pixel
      img.pixels[loc] =  color(r,g,b);          
    }
  }
  img.updatePixels();
}
