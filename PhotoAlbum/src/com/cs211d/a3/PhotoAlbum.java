/*****************************************************************************\
|                                                                             |
|    Author :  Galo Paz <galopaz@outlook.com>                                 |
|      Date :  Jul 1, 2013                                                    |
|      Name :  PhotoAlbum.java                                                |
|                                                                             |
| Objective :  Create a simple photo album android application that utilizes  |
|              two Buttons--one for, when pressed, displays the previous      |
|              image in a folder, the second for, when pressed, displays the  |
|              next image in the folder--and an ImageView for which such      |
|              images are to be displayed from.                               |
|                                                                             |
| Extra :   1. Switches between portrait and landscape.                       | 
|           2. Utilizes AsyncTask to cache a specified radius of images       |
|              that come previous to and immediately following the current    |
|              image being displayed to the user.                             |
|                                                                             |
|                                                                             |
\*****************************************************************************/

package com.cs211d.a3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.View;
import android.graphics.*;
import android.os.AsyncTask;
import android.view.WindowManager;
import android.os.Build;
import android.view.Display;

import java.lang.ref.WeakReference;
import java.io.*;
import java.util.ArrayList;


/**
 * 
 * @author Galo Paz <galopaz@outlook.com>
 * @version 1.1
 * @since July 2, 2013
 */
public class PhotoAlbum extends Activity
{
   String[] photo_file_paths;
   int cur_position;
   ArrayList<Bitmap> bufferPrev;
   ArrayList<Bitmap> bufferNext;
   Bitmap on_screen_img;
   
   int buffer_radius;
   
   /***************************** onCreate() **************************/
   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.main);
      if(savedInstanceState != null)
      {
         restoreState(savedInstanceState);
         
         if(photo_file_paths.length > 0){
            int[] size = getScreenSize();
            on_screen_img = PhotoAlbum.decodeSampledBitmapFromFile(
                    photo_file_paths[cur_position], size[0], size[1]);
         }else //display defalut image if no images are present in the folder.
            setToDefaultImage();
         
         cacheWithScreenSize();
      }else
         initialize();
   }
   
   /****************************** onStart() *******************************/
   @Override
   protected void onStart()
   {
      super.onStart();
      displayImage(0);
   }
   
   /************************** restoreState() *******************************/
   /**
    * Restores saved information to some private member values and initializes 
    * others with new objects.
    * @param inState 
    */
   private void restoreState(Bundle inState)
   {
      photo_file_paths = inState.getStringArray("file_paths");
      cur_position = inState.getInt("cur_pos");
      buffer_radius = inState.getInt("radius");
      bufferNext = new ArrayList<Bitmap>();
      bufferPrev = new ArrayList<Bitmap>();
   }
   
   /**************************** initialize() ******************************/
   /**
    * Initializes the PhotoAlbum Activity's private member values.
    */
   private void initialize()
   {
      //Should make smoother transitions for longer than if set less, should
      //make better use of memory than if set more.  Depending on device.
      buffer_radius = 6; 
      cur_position = 0;
      bufferPrev = new ArrayList<Bitmap>();
      bufferNext = new ArrayList<Bitmap>();
      photo_file_paths = new String[0];
      File dir;
      try
      {
         dir = new File(Environment.getExternalStorageDirectory(),
                 getResources().getString(R.string.image_directory));
      }catch(Exception e)
      {
         toastThis("SD Card not found");
         dir = new File("");
      }
      if(dir.exists())
      {
         storeFilenames(dir);
         int n = (int)Math.floor(photo_file_paths.length/2);
         if(n < buffer_radius)
         {
            buffer_radius = n;
         }
         cacheWithScreenSize();
      }else
      {
         setToDefaultImage();
         toastThis("Directory or file \""+ dir.getAbsolutePath() +
                   "\" does not exist.");
      }
   }
   
   /*********************** cacheWithScreenSize() ***************************/
   /**
    * Used to cache the images to a size in closer to that of the device's
    * screen size when the ImageView is not yet fully built, at which point 
    * the activity is unable to retrieve the current size of the ImageView.
    */
   private void cacheWithScreenSize()
   {
      int[] dim = this.getScreenSize();
      for(int i = 1; i <= buffer_radius; i++)
      {
         int pos = cur_position+i;
         int neg = cur_position-i;
         int len = photo_file_paths.length;
         if(len > 1)
         {
            pos = pos >= len ? pos-len : pos;
            neg= neg < 0 ? len+neg : neg;
            bufferNext.add(PhotoAlbum.decodeSampledBitmapFromFile(
                    photo_file_paths[pos], dim[0], dim[1]));
            bufferPrev.add(PhotoAlbum.decodeSampledBitmapFromFile(
                    photo_file_paths[neg], dim[0], dim[1]));
         }
      }
   }
   
   /******************** decodeSampledBitmapFromFile **********************/
   /**
    * Returns a, hopefully smaller, sampled bitmap object from the image at 
    * the location of the supplied filename that satisfies the minimal 
    * dimensions passed in.
    * 
    * @param path absolute file path and name of an image file.
    * @param reqWidth minimal width requirement.
    * @param reqHeight minimal height requirement.
    * @return a Sampled Bitmap object from a file with the input absolute 
    *         file path that minimally satisfies the passed in dimensions.
    */
   public static Bitmap decodeSampledBitmapFromFile(String path,
           int reqWidth, int reqHeight) 
   {

      // First decode with inJustDecodeBounds=true to check dimensions
      final BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(path, options);
      // Calculate inSampleSize
      options.inSampleSize = calculateInSampleSize(options,reqWidth,reqHeight);
      // Decode bitmap with inSampleSize set
      options.inJustDecodeBounds = false;
      return BitmapFactory.decodeFile(path, options);
   }
   
   /********************** calculateInSampleSize **************************/
   /**
    * Calculates the ratio between the actual size of the image with the 
    * passed in required width and hight parameters that it must minimally 
    * sample from.
    * 
    * @param options set options of an image bitmap.
    * @param reqWidth minimal condition of width.
    * @param reqHeight minimal condition of height.
    * @return the integer ratio value
    */
   public static int calculateInSampleSize(
               BitmapFactory.Options options, int reqWidth, int reqHeight)
   {
   // Raw height and width of image
      int height = options.outHeight;
      int width = options.outWidth;
      int inSampleSize = 1;
   
      if (height > reqHeight || width > reqWidth)
      {
         // Calculate ratios of height and width to requested height and width
         int hRatio = Math.round((float) height / (float) reqHeight);
         int wRatio = Math.round((float) width / (float) reqWidth);
         //choose the smallest ratio
         inSampleSize = hRatio < wRatio ? hRatio : wRatio;
      }
      return inSampleSize;
   }

   /********************** onSaveInstanceState() ***************************/
   /**
    * Persists information needed when changing configurations between 
    * landscape and portrait.
    * @param outState Bundle
    */
   @Override
   protected void onSaveInstanceState(Bundle outState)
   {
      outState.putStringArray("file_paths", photo_file_paths);
      outState.putInt("cur_pos", cur_position);
      outState.putInt("radius", buffer_radius);
      super.onSaveInstanceState(outState);
   }
   
   /************************* storeFilenames() *****************************/
   /**
    * Stores the filenames that are in the DCIM/Camera directory.
    * 
    * @param files Image file or directory of images.
    */
   private void storeFilenames(File files)
   {
      ArrayList<String> temp = new ArrayList<String>();
      if(files.isDirectory()) //if 'files' refers to a directory
         for(File f : files.listFiles())
            if(hasSupportedImageExt(f.getName()))
               temp.add(f.getAbsolutePath());
      else //else if 'files' refers to a file and not null.
         if(hasSupportedImageExt(files.getName()))
            temp.add(files.getAbsolutePath());
      photo_file_paths = temp.toArray(new String[0]);
      if(photo_file_paths.length > 0)
      {
         int[] size = getScreenSize();
         on_screen_img = PhotoAlbum.decodeSampledBitmapFromFile(
                 photo_file_paths[0], size[0], size[1]);
      }else //display defalut image if no images are present in the folder.
         setToDefaultImage();
   }
   
   /****************************** getScreenSize() *************************/
   /**
    * Retrieves and returns the full screen dimensions of the device.
    * 
    * @return int[0] = Width of screen
    *         int[1] = Height of screen
    */
   @SuppressWarnings("deprecation")
public int[] getScreenSize()
   {
      int[] dim = {0,0};
      Point p = new Point();
      WindowManager wm = getWindowManager();
      if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
      {
         wm.getDefaultDisplay().getSize(p);
         dim[0] = p.x;
         dim[1] = p.y;
      }else
      {
         Display d = wm.getDefaultDisplay();
         dim[0] = d.getWidth();
         dim[1] = d.getHeight();
      }
      
      return dim;
   }
   
   /**************************** setToDefaultImage() *************************/
   /**
    * Sets the Default Image to display to to the user instead of an image file.
    * Usually invoked when no image files are found.
    */
   private void setToDefaultImage()
   {
      on_screen_img = BitmapFactory.decodeResource(
                 getResources(), R.drawable.ic_launcher);
   }
   
   /************************ hasSupportedImageExt() *************************/
   /**
    * Checks if the name of the file has a supported image extension.
    * 
    * @param name filename with extension.
    * @return true if name is of a supported image extension;
    *         false otherwise.
    */
   private boolean hasSupportedImageExt(String name)
   {
      for(String s : getResources().getStringArray(R.array.image_extensions))
         if(name.endsWith(s)) //ends with .jpg, .jpeg, .png, or .bmp
            return true;
      //if name does not end with supported image file format extensions.
      return false; 
   }
   
   /****************************** toastThis() *******************************/
   /**
    * Displays short-length toast with given message String.
    * @param msg String of desired message desired to be displayed to the user.
    */
   private void toastThis(String msg)
   {
      Context con = getApplicationContext();
      Toast t = Toast.makeText(con, msg, Toast.LENGTH_SHORT);
      t.show();
   }
   
   /****************************** displayImage() **************************/
   /**
    * Displays the image that is currently established or either the previous 
    * or the next image in the folder, given the input direction.
    * @param direction 0 to display current image posted.
    *                  +  positive number for the next image.
    *                  -  negative number for the previous number.
    */
   private void displayImage(int direction)
   {
      try
      {
         Bitmap last;
         if(photo_file_paths.length > 0)
         {
            ImageView iv = (ImageView)findViewById(R.id.screen_img);
            if(direction < 0)
            {
               if(bufferPrev.isEmpty() || photo_file_paths.length <= 1)
               {
                  // Do nothing, let the user click again when cache is ready.
               }else
               {
                  cur_position--;
                  if(cur_position < 0)
                     cur_position = photo_file_paths.length - 1;
                  last = on_screen_img;
                  on_screen_img = bufferPrev.remove(0);
                  bufferNext.add(0, last);
                  while(bufferNext.size() > buffer_radius)
                  {
                     bufferNext.remove(bufferNext.size()-1).recycle();
                  }
                  addToBuffer(-buffer_radius);
               }
            }else if (direction > 0)
            {
               if(bufferNext.isEmpty() || photo_file_paths.length <= 1)
               {
                  // nothing and wait for user to click button again
                  // giving time for async threads to finish 
               }else
               {
                  cur_position++;
                  if(cur_position >= photo_file_paths.length)
                     cur_position = 0;
                  last = on_screen_img;
                  on_screen_img = bufferNext.remove(0);
                  bufferPrev.add(0, last);
                  while(bufferPrev.size() > buffer_radius)
                  {
                     bufferPrev.remove(bufferPrev.size()-1).recycle();
                  }
                  addToBuffer( buffer_radius);
               }
            }else
            {
               //do nothing, display the current image set to on_screen_img
            }
            iv.setImageBitmap(on_screen_img);
         }else
            toastThis(getResources().getString(R.string.msg_no_images));
      }catch (Exception e)
      {
         toastThis(getResources().getString(R.string.msg_out_of_bounds));
      }
   }

   /*************************** addToBuffer *******************************/
   /**
    * Appends the image that is 'radius' distance from the current shown 
    * image to the appropriate cache space.
    * @param radius 
    */
   private void addToBuffer(int radius)
   {
      int count;
      int len = photo_file_paths.length;
      int div = radius/Math.abs(radius);
      if(div < 0)
      {
         count = cur_position + radius;
         count = count < 0 ? len+count : count;
         AsyncBufferTask task;
         task = new AsyncBufferTask(bufferPrev);
         task.execute(photo_file_paths[count]);  
      }else if(div > 0)
      {
         count = cur_position + radius;
         count = count >= len ? count-len : count;
         AsyncBufferTask task;
         task = new AsyncBufferTask(bufferNext);
         task.execute(photo_file_paths[count]);
      }else
      {
         /*do nothing*/
      }   
   }//done

   /********************************* nextImage() ***************************/
   /**
    * Button handler for the 'Next' Button.
    * @param view 
    */
   public void nextImage(View view)
   {
      displayImage(1);
   }
   
   /******************************** prevImage() ****************************/
   /**
    * Button Handler for the 'Previous' Button.
    * @param view 
    */
   public void prevImage(View view)
   {
      displayImage(-1);
   }

   /******************************* onDestroy() ****************************/
   @Override
   protected void onDestroy()
   {
      for(Bitmap b : bufferNext)
         b.recycle();
      for(Bitmap b : bufferPrev)
         b.recycle();
      bufferNext = null;
      bufferPrev = null;
      on_screen_img.recycle();
      super.onDestroy();
   }
   
   /************************* AsyncBufferTask CLASS *************************/
   /**
    * Inner class for loading and sampling images and then loading them to the
    * appropriate cache space.
    */
   public class AsyncBufferTask extends AsyncTask<String, String, Bitmap> 
   {
      private WeakReference<ArrayList<Bitmap>> buf;
      private ImageView iv;
      
      public AsyncBufferTask(ArrayList<Bitmap> buffer)
      {
         buf = new WeakReference<ArrayList<Bitmap>>(buffer);
      }
      
      /************************* onCancelled() *****************************/
      @Override
      protected void onCancelled()
      {
         buf.clear();
         iv = null;
         buf = null;
      }
      
      /************************* doInBackground() **************************/
      /**
       * Load sampled image, based on the ImageView size, to a Bitmap object.
       * @param params
       * @return 
       */
      @Override
      protected Bitmap doInBackground(String... params) 
      {
         Bitmap bmap = null;
         if(!isCancelled())
         {
            iv = (ImageView)findViewById(R.id.screen_img);
            bmap = PhotoAlbum.decodeSampledBitmapFromFile(params[0], 
                    iv.getWidth(), iv.getHeight());
            iv = null;
            if(isCancelled())
            {
               bmap.recycle();
               bmap = null;
            }
         }
         
         return (bmap);
      }

      /*************************** onPostExecute() ***********************/
     /**
      * Adds Bitmap object of loaded file to supplied array of Bitmap type
      * if available.
      * 
      * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
      */
      @Override
      protected void onPostExecute(Bitmap result) 
      {
         if(!this.isCancelled())
         {
            if (buf != null && result != null) 
            {
               ArrayList<Bitmap> buffer = buf.get();
               if (buffer != null)
                  buffer.add(result);
               else
                  result.recycle();
            }
         }else
            result.recycle();
         
         buf.clear();
         iv = null;
      }
   }
}