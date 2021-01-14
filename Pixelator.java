import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.video.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Pixelator extends PApplet {



Capture cam;

PImage img;

Button snapButton;
Button newPhotoButton;
Button pixalateButton;

boolean updateImage = false;

public void setup()
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

public void draw()
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

public void mouseReleased()
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

public void Pixalate()
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

int HORIZONTAL = 0;
int VERTICAL   = 1;
int UPWARDS    = 2;
int DOWNWARDS  = 3;

class Widget
{

  
  PVector pos;
  PVector extents;
  String name;

  int inactiveColor = color(60, 60, 100);
  int activeColor = color(100, 100, 160);
  int bgColor = inactiveColor;
  int lineColor = color(255);
  
  
  
  public void setInactiveColor(int c)
  {
    inactiveColor = c;
    bgColor = inactiveColor;
  }
  
  public int getInactiveColor()
  {
    return inactiveColor;
  }
  
  public void setActiveColor(int c)
  {
    activeColor = c;
  }
  
  public int getActiveColor()
  {
    return activeColor;
  }
  
  public void setLineColor(int c)
  {
    lineColor = c;
  }
  
  public int getLineColor()
  {
    return lineColor;
  }
  
  public String getName()
  {
    return name;
  }
  
  public void setName(String nm)
  {
    name = nm;
  }


  Widget(String t, int x, int y, int w, int h)
  {
    pos = new PVector(x, y);
    extents = new PVector (w, h);
    name = t;
    //registerMethod("mouseEvent", this);
  }

  public void display()
  {
  }

  public boolean isClicked()
  {
    
    if (mouseX > pos.x && mouseX < pos.x+extents.x 
      && mouseY > pos.y && mouseY < pos.y+extents.y)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  public void mouseEvent(MouseEvent event)
  {
    //if (event.getFlavor() == MouseEvent.PRESS)
    //{
    //  mousePressed();
    //}
  }
  
  
  public boolean mousePressed()
  {
    return isClicked();
  }
  
  public boolean mouseDragged()
  {
    return isClicked();
  }
  
  
  public boolean mouseReleased()
  {
    return isClicked();
  }
}

class Button extends Widget
{
  PImage activeImage = null;
  PImage inactiveImage = null;
  PImage currentImage = null;
  int imageTint = color(255);
  
  Button(String nm, int x, int y, int w, int h)
  {
    super(nm, x, y, w, h);
  }
  
  public void setImage(PImage img)
  {
    setInactiveImage(img);
    setActiveImage(img);
  }
  
  public void setInactiveImage(PImage img)
  {
    if(currentImage == inactiveImage || currentImage == null)
    {
      inactiveImage = img;
      currentImage = inactiveImage;
    }
    else
    {
      inactiveImage = img;
    }
  }
  
  public void setActiveImage(PImage img)
  {
    if(currentImage == activeImage || currentImage == null)
    {
      activeImage = img;
      currentImage = activeImage;
    }
    else
    {
      activeImage = img;
    }
  }
  
  public void setImageTint(float r, float g, float b)
  {
    imageTint = color(r,g,b);
  }

  public void display()
  {
    if(currentImage != null)
    {
      //float imgHeight = (extents.x*currentImage.height)/currentImage.width;
      float imgWidth = (extents.y*currentImage.width)/currentImage.height;
      
      
      pushStyle();
      imageMode(CORNER);
      tint(imageTint);
      image(currentImage, pos.x, pos.y, imgWidth, extents.y);
      stroke(bgColor);
      noFill();
      rect(pos.x, pos.y, imgWidth,  extents.y);
      noTint();
      popStyle();
    }
    else
    {
      pushStyle();
      stroke(lineColor);
      fill(bgColor);
      rect(pos.x, pos.y, extents.x, extents.y);
  
      fill(lineColor);
      textAlign(CENTER, CENTER);
      text(name, pos.x + 0.5f*extents.x, pos.y + 0.5f* extents.y);
      popStyle();
    }
  }
  
  public boolean mousePressed()
  {
    if (super.mousePressed())
    {
      if (bgColor == activeColor)
      {
        bgColor = inactiveColor;
      } else
      {
        bgColor = activeColor;
      }
      
      if(activeImage != null)
        currentImage = activeImage;
      return true;
    }
    return false;
  }
  
  public boolean mouseReleased()
  {
    if (super.mouseReleased())
    {
      bgColor = inactiveColor;
      if(inactiveImage != null)
        currentImage = inactiveImage;
      return true;
    }
    return false;
  }
}

class Toggle extends Button
{
  boolean on = false;

  Toggle(String nm, int x, int y, int w, int h)
  {
    super(nm, x, y, w, h);
  }


  public boolean get()
  {
    return on;
  }

  public void set(boolean val)
  {
    on = val;
    if (on)
    {
      bgColor = activeColor;
      if(activeImage != null)
        currentImage = activeImage;
    }
    else
    {
      bgColor = inactiveColor;
      if(inactiveImage != null)
        currentImage = inactiveImage;
    }
  }

  public void toggle()
  {
    set(!on);
  }

  
  public boolean mousePressed()
  {
    return super.isClicked();
  }

  public boolean mouseReleased()
  {
    if (super.mouseReleased())
    {
      toggle();
      return true;
    }
    return false;
  }
}

class RadioButtons extends Widget
{
  public Toggle [] buttons;
  
  RadioButtons (String [] names,int numButtons, int x, int y, int w, int h, int orientation)
  {
    super("", x, y, w*numButtons, h);
    buttons = new Toggle[numButtons];
    for (int i = 0; i < buttons.length; i++)
    {
      int bx, by;
      if(orientation == HORIZONTAL)
      {
        bx = x+i*(w+5);
        by = y;
      }
      else
      {
        bx = x;
        by = y+i*(h+5);
      }
      buttons[i] = new Toggle(names[i], bx, by, w, h);
    }
  }
  
  public void setNames(String [] names)
  {
    for (int i = 0; i < buttons.length; i++)
    {
      if(i >= names.length)
        break;
      buttons[i].setName(names[i]);
    }
  }
  
  public void setImage(int i, PImage img)
  {
    setInactiveImage(i, img);
    setActiveImage(i, img);
  }
  
  public void setAllImages(PImage [] img)
  {
    setAllInactiveImages(img);
    setAllActiveImages(img);
  }
  
  public void setInactiveImage(int i, PImage img)
  {
    buttons[i].setInactiveImage(img);
  }

  
  public void setAllInactiveImages(PImage [] img)
  {
    for (int i = 0; i < buttons.length; i++)
    {
      buttons[i].setInactiveImage(img[i]);
    }
  }
  
  public void setActiveImage(int i, PImage img)
  {
    
    buttons[i].setActiveImage(img);
  }
  
  
  
  public void setAllActiveImages(PImage [] img)
  {
    for (int i = 0; i < buttons.length; i++)
    {
      buttons[i].setActiveImage(img[i]);
    }
  }

  public void set(String buttonName)
  {
    for (int i = 0; i < buttons.length; i++)
    {
      if(buttons[i].getName().equals(buttonName))
      {
        buttons[i].set(true);
      }
      else
      {
        buttons[i].set(false);
      }
    }
  }
  
  public int get()
  {
    for (int i = 0; i < buttons.length; i++)
    {
      if(buttons[i].get())
      {
        return i;
      }
    }
    return -1;
  }
  
  public String getString()
  {
    for (int i = 0; i < buttons.length; i++)
    {
      if(buttons[i].get())
      {
        return buttons[i].getName();
      }
    }
    return "";
  }

  public void display()
  {
    for (int i = 0; i < buttons.length; i++)
    {
      buttons[i].display();
    }
  }

  public boolean mousePressed()
  {
    for (int i = 0; i < buttons.length; i++)
    {
      if(buttons[i].mousePressed())
      {
        return true;
      }
    }
    return false;
  }
  
  public boolean mouseDragged()
  {
    for (int i = 0; i < buttons.length; i++)
    {
      if(buttons[i].mouseDragged())
      {
        return true;
      }
    }
    return false;
  }

  public boolean mouseReleased()
  {
    for (int i = 0; i < buttons.length; i++)
    {
      if(buttons[i].mouseReleased())
      {
        for(int j = 0; j < buttons.length; j++)
        {
          if(i != j)
            buttons[j].set(false);
        }
        //buttons[i].set(true);
        return true;
      }
    }
    return false;
  }
}

class Slider extends Widget
{
  float minimum;
  float maximum;
  float val;
  int textWidth = 60;
  int orientation = HORIZONTAL;

  Slider(String nm, float v, float min, float max, int x, int y, int w, int h, int ori)
  {
    super(nm, x, y, w, h);
    val = v;
    minimum = min;
    maximum = max;
    orientation = ori;
    if(orientation == HORIZONTAL)
      textWidth = 60;
    else
      textWidth = 20;
    
  }

  public float get()
  {
    return val;
  }

  public void set(float v)
  {
    val = v;
    val = constrain(val, minimum, maximum);
  }

  public void display()
  {
    
    float textW = textWidth;
    if(name == "")
      textW = 0;
    pushStyle();
    textAlign(LEFT, TOP);
    fill(lineColor);
    text(name, pos.x, pos.y);
    stroke(lineColor);
    noFill();
    if(orientation ==  HORIZONTAL){
      rect(pos.x+textW, pos.y, extents.x-textWidth, extents.y);
    } else {
      rect(pos.x, pos.y+textW, extents.x, extents.y-textW);
    }
    noStroke();
    fill(bgColor);
    float sliderPos; 
    if(orientation ==  HORIZONTAL){
        sliderPos = map(val, minimum, maximum, 0, extents.x-textW-4); 
        rect(pos.x+textW+2, pos.y+2, sliderPos, extents.y-4);
    } else if(orientation ==  VERTICAL || orientation == DOWNWARDS){
        sliderPos = map(val, minimum, maximum, 0, extents.y-textW-4); 
        rect(pos.x+2, pos.y+textW+2, extents.x-4, sliderPos);
    } else if(orientation == UPWARDS){
        sliderPos = map(val, minimum, maximum, 0, extents.y-textW-4); 
        rect(pos.x+2, pos.y+textW+2 + (extents.y-textW-4-sliderPos), extents.x-4, sliderPos);
    };
    popStyle();
  }

  
  public boolean mouseDragged()
  {
    if (super.mouseDragged())
    {
      float textW = textWidth;
      if(name == "")
        textW = 0;
      if(orientation ==  HORIZONTAL){
        set(map(mouseX, pos.x+textW, pos.x+extents.x-4, minimum, maximum));
      } else if(orientation ==  VERTICAL || orientation == DOWNWARDS){
        set(map(mouseY, pos.y+textW, pos.y+extents.y-4, minimum, maximum));
      } else if(orientation == UPWARDS){
        set(map(mouseY, pos.y+textW, pos.y+extents.y-4, maximum, minimum));
      };
      return true;
    }
    return false;
  }

  public boolean mouseReleased()
  {
    if (super.mouseReleased())
    {
      float textW = textWidth;
      if(name == "")
        textW = 0;
      if(orientation ==  HORIZONTAL){
        set(map(mouseX, pos.x+textW, pos.x+extents.x-10, minimum, maximum));
      } else if(orientation ==  VERTICAL || orientation == DOWNWARDS){
        set(map(mouseY, pos.y+textW, pos.y+extents.y-10, minimum, maximum));
      } else if(orientation == UPWARDS){
        set(map(mouseY, pos.y+textW, pos.y+extents.y-10, maximum, minimum));
      };
      return true;
    }
    return false;
  }
}

class MultiSlider extends Widget
{
  Slider [] sliders;
  /*
  MultiSlider(String [] nm, float min, float max, int x, int y, int w, int h, int orientation)
  {
    super(nm[0], x, y, w, h*nm.length);
    sliders = new Slider[nm.length];
    for (int i = 0; i < sliders.length; i++)
    {
      int bx, by;
      if(orientation == HORIZONTAL)
      {
        bx = x;
        by = y+i*h;
      }
      else
      {
        bx = x+i*w;
        by = y;
      }
      sliders[i] = new Slider(nm[i], 0, min, max, bx, by, w, h, orientation);
    }
  }
  */
  MultiSlider(int numSliders, float min, float max, int x, int y, int w, int h, int orientation)
  {
    super("", x, y, w, h*numSliders);
    sliders = new Slider[numSliders];
    for (int i = 0; i < sliders.length; i++)
    {
      int bx, by;
      if(orientation == HORIZONTAL)
      {
        bx = x;
        by = y+i*h;
      }
      else
      {
        bx = x+i*w;
        by = y;
      }
      sliders[i] = new Slider("", 0, min, max, bx, by, w, h, orientation);
    }
  }
  
  public void setNames(String [] names)
  {
    for (int i = 0; i < sliders.length; i++)
    {
      if(i >= names.length)
        break;
      sliders[i].setName(names[i]);
    }
  }

  public void set(int i, float v)
  {
    if(i >= 0 && i < sliders.length)
    {
      sliders[i].set(v);
    }
  }
  
  public float get(int i)
  {
    if(i >= 0 && i < sliders.length)
    {
      return sliders[i].get();
    }
    else
    {
      return -1;
    }
    
  }

  public void display()
  {
    for (int i = 0; i < sliders.length; i++)
    {
      sliders[i].display();
    }
  }

  
  public boolean mouseDragged()
  {
    for (int i = 0; i < sliders.length; i++)
    {
      if(sliders[i].mouseDragged())
      {
        return true;
      }
    }
    return false;
  }

  public boolean mouseReleased()
  {
    for (int i = 0; i < sliders.length; i++)
    {
      if(sliders[i].mouseReleased())
      {
        return true;
      }
    }
    return false;
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Pixelator" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
