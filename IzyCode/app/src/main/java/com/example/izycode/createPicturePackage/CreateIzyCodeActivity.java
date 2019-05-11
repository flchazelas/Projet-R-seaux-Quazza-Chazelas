package com.example.izycode.createPicturePackage;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.izycode.R;
import com.example.izycode.izyCodePackage.IzyCode;

import java.nio.ByteOrder;
import java.util.Random;

public class CreateIzyCodeActivity extends AppCompatActivity {



    private EditText editText;
    private ImageView imageView;
    private int pixelSize = 0;
    //do not touch the border size, it will put the application into unstable state
    private int borderSize = 8;
    private Bitmap izyCodeBitmap;
    private String sentence;
    private IzyCode izyCodeArray;
    private TextView textViewRemainingChar;

    // definition of dimensions of each zone
    //zone1
    final int BEGIN_ZONE1_LEFT = borderSize;
    final int END_ZONE1_RIGHT = borderSize + 224;
    final int BEGIN_ZONE1_TOP = borderSize;
    final int END_ZONE1_BOTTOM = borderSize+384;

    //zone2
    final int BEGIN_ZONE2_LEFT = borderSize+224+borderSize+16+borderSize;
    final int END_ZONE2_RIGHT = borderSize+224+borderSize+16+borderSize+128;
    final int BEGIN_ZONE2_TOP = borderSize;
    final int END_ZONE2_BOTTOM = borderSize+224;

    //zone3
    final int BEGIN_ZONE3_LEFT = borderSize+224+borderSize+16+borderSize;
    final int END_ZONE3_RIGHT = borderSize+224+borderSize+16+borderSize+128;
    final int BEGIN_ZONE3_TOP = borderSize+224+borderSize+16+borderSize;
    final int END_ZONE3_BOTTOM = borderSize+384;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createizycode);

        Button getTextButton;
        Button resetIzyCodeButton;

        editText = findViewById(R.id.saisie_text);
        imageView= findViewById(R.id.textViewIzyCode);
        getTextButton = findViewById(R.id.getTextButton);
        resetIzyCodeButton = findViewById(R.id.resetIzyCode);
        textViewRemainingChar = findViewById(R.id.textview_remaining_char);

        //clear the imageviaw display
        resetIzyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                izyCodeBitmap.eraseColor(Color.TRANSPARENT);
                imageView.setImageBitmap(izyCodeBitmap);
            }
        });

        izyCodeArray = new IzyCode();
        izyCodeBitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);

        getTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentence = editText.getText().toString();
                int izyCodeArraySize = 0;
                //if the user do not tap text, a dialog alert will inform him
                if(sentence.length() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateIzyCodeActivity.this);

                    builder.setMessage("Veuillez remplir le champ de saisie de texte avant de générer l'IzyCode.");
                    builder.setTitle("Attention !");
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

                else {
                    izyCodeBitmap.eraseColor(Color.TRANSPARENT);
                    imageView.setImageBitmap(izyCodeBitmap);

                    izyCodeArray.fillIzyCode(sentence);
                    izyCodeArraySize = izyCodeArray.getIzyCodeSize();

                    Log.i("taille ", String.valueOf(izyCodeArraySize));

                    if(izyCodeArraySize < 60){
                        pixelSize = 16;

                        //45 char because the small_zone1 is physically(without border)
                        //224px larger and 384px higher
                        //So 224/16 = 14 bits larger(14 squares)
                        // and 384/16 = 24 bits high => 24/8 = 3 bytes high(3 char)
                        // and 3*14 = 42 char in the zone 1 (+ 3 for the numer of char) etc...

                        completeIzycode( izyCodeArraySize);

                    }

                    else if( izyCodeArraySize <= 224){
                        pixelSize = 8;

                        completeIzycode(izyCodeArraySize);
                    }


                    /*else if(izyCodeArraySize <= 896){

                    }*/

                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(CreateIzyCodeActivity.this);

                        builder.setMessage("Le message que vous essayez de convertir est trop grand.");
                        builder.setTitle("Attention !");
                        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            }
        });
    }


    @Override
    protected void onResume(){
        super.onResume();
    }

    void completeIzycode( int izyCodeArraySize){
        drawBorder(izyCodeBitmap);
        fillInModeTwo(izyCodeBitmap, izyCodeArray, izyCodeArraySize);

        //set the bitmap into imageView
        imageView.setImageBitmap(izyCodeBitmap);

        //used to hide the keyboard
        /*InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }catch (NullPointerException e){
            e.printStackTrace();
        }*/
    }

    //used to fill the bitmap with the array
    void fillInModeTwo(Bitmap izyCodeBitmap, IzyCode izyCodeArray, int izyCodeArraySize){
        //used to work on a copy of the original bitmap
        byte [][] temp = izyCodeArray.getIzyCodeArrayOfBits();

        int bitmapHeight = izyCodeBitmap.getHeight() - 1;   //399
        int bitmapWidth = izyCodeBitmap.getWidth() - 1;     //399

        int arrayLine = 0, arrayColumn = 0, bitmapLine = BEGIN_ZONE1_LEFT, bitmapColumn = BEGIN_ZONE1_TOP;

        while((arrayLine < izyCodeArraySize - 3) && (bitmapLine <= END_ZONE1_BOTTOM) && (bitmapColumn <= END_ZONE1_RIGHT)){

            if(bitmapLine >= END_ZONE1_BOTTOM){
                bitmapLine = BEGIN_ZONE1_TOP;
                bitmapColumn += pixelSize;
            }

            if(bitmapColumn >= END_ZONE1_RIGHT){
                bitmapColumn = BEGIN_ZONE1_LEFT;
                bitmapLine += pixelSize;
            }

            if(temp[arrayLine][arrayColumn] == 1){
                drawPixel(izyCodeBitmap, 1, bitmapLine, bitmapColumn);
            }

            bitmapLine += pixelSize;

            arrayColumn += 1;

            if(arrayColumn == temp[arrayLine].length){
                arrayColumn = 0;
                arrayLine += 1;
            }

            /*else if(bitmapColumn >= END_ZONE1_RIGHT){
                bitmapLine += pixelSize;
                bitmapColumn = BEGIN_ZONE1_LEFT;
            }*/
        }

        if((arrayLine < (izyCodeArraySize - 3))/*bitmapLine >= END_ZONE1_BOTTOM && bitmapColumn >= END_ZONE1_RIGHT*/){
            bitmapColumn = BEGIN_ZONE2_LEFT;
            bitmapLine = BEGIN_ZONE2_TOP;

            while ((arrayLine < izyCodeArraySize - 3) && (bitmapLine < END_ZONE2_BOTTOM) && (bitmapColumn < END_ZONE2_RIGHT)) {
                if (temp[arrayLine][arrayColumn] == 1) {
                    drawPixel(izyCodeBitmap, 1, bitmapLine, bitmapColumn);
                }
                arrayColumn += 1;
                bitmapColumn += pixelSize;
                if (arrayColumn == temp[arrayLine].length) {
                    arrayColumn = 0;
                    arrayLine += 1;
                }

                if (bitmapColumn > END_ZONE2_RIGHT) {
                    bitmapColumn = BEGIN_ZONE2_LEFT;
                    bitmapLine += pixelSize;

                    if (bitmapLine > END_ZONE2_BOTTOM) {
                        bitmapColumn += pixelSize;
                        bitmapLine = borderSize;
                    }
                }
            }
        }




        /*if(bitmapLine < 224-borderSize && bitmapColumn < bitmapColumn-borderSize){
            Random randomGenerator = new Random();
            while((bitmapLine + borderSize <= bitmapHeight) && (bitmapColumn + borderSize <= bitmapWidth)){
                int randomInt = randomGenerator.nextInt(2);

                if(randomInt == 1 && ((bitmapLine < 384 + borderSize && bitmapColumn <= 224)
                                || ((bitmapLine < 224) && (bitmapColumn >= 224+32+(borderSize)) && bitmapColumn < bitmapWidth - borderSize))//line 2
                ){
                    drawPixel(izyCodeBitmap, 1, bitmapLine, bitmapColumn);
                }

                bitmapLine += pixelSize;

                if(bitmapLine > bitmapHeight - borderSize){
                    bitmapLine = borderSize;
                    bitmapColumn += pixelSize;

                    if(bitmapColumn > bitmapWidth ){
                        bitmapLine += pixelSize;
                        bitmapColumn = borderSize;
                    }
                }
            }
        }*/

        //put the size into its place
        if(arrayLine == izyCodeArraySize - 3){

            bitmapLine = BEGIN_ZONE1_TOP;
            bitmapColumn = END_ZONE1_RIGHT+borderSize;

            while(arrayLine < izyCodeArraySize){
                if(temp[arrayLine][arrayColumn] == 1){
                    drawPixel(izyCodeBitmap, 1, bitmapLine, bitmapColumn);
                }
                arrayColumn+=1;
                if(arrayColumn == 8){
                    arrayColumn = 0;
                    arrayLine+=1;
                }
                bitmapLine+= pixelSize;
            }
        }
    }

    //draw the contouring of the IzyCode
    void drawBorder(Bitmap izyCode){
        int bitmapHeight = izyCode.getHeight() - 1;
        int bitmapWidth = izyCode.getWidth() - 1;
        for (int height = 0; height < bitmapHeight; height += borderSize){

            for (int width = 0; width < bitmapWidth; width += borderSize) {

                //borderside
                if (        ((height >= 0) && (height < borderSize))
                        || ((width >= 0) && (width < borderSize))
                        || ((height > ((bitmapHeight) - borderSize)) && (height <= bitmapHeight))
                        || ((width > ((bitmapWidth) - borderSize)) && (width <= bitmapWidth))
                ){
                    drawPixel(izyCode,2, width, height);
                }

                /*
                 *      |  |2
                 *      |  |
                 *      |  |_____3__
                 *    1 |   ________
                 *      |  |    4
                 *      |  |
                 *      |  |5
                 */

               if(        (width >= END_ZONE1_RIGHT && width < END_ZONE1_RIGHT + borderSize)//line 1
                        || ((width >= BEGIN_ZONE2_LEFT - borderSize) && ( width < BEGIN_ZONE2_LEFT) && (height < END_ZONE2_BOTTOM))//line 2
                        || ((height >= END_ZONE2_BOTTOM) && (height < END_ZONE2_BOTTOM + borderSize) && (width >= BEGIN_ZONE2_LEFT - borderSize)) // line 3
                        || ((height >= BEGIN_ZONE3_TOP - borderSize) && (height < BEGIN_ZONE3_TOP) && (width >= BEGIN_ZONE3_LEFT - borderSize)) // line4
                        || ((width >= BEGIN_ZONE3_LEFT - borderSize) && (width < BEGIN_ZONE3_LEFT) && height >= BEGIN_ZONE3_TOP - borderSize) //line 5
               ){
                    drawPixel(izyCode,2, height, width);
               }

                //store the size of a pixel in the izycode
                switch (pixelSize){
                    case 16:
                        if((width >= 328) &&  (width < 328 + borderSize) && (height >= 240) && (height < 240 + borderSize)) {
                            drawPixel(izyCode, 1, height, width);
                        }
                        break;
                    case 8:
                        if((width >= 328) &&  (width < 328 + borderSize) && (height >= 232+borderSize) && (height < 232 + borderSize*2)) {
                            drawPixel(izyCode, 1, height, width);
                        }
                        break;
                    case 4:

                        break;
                }
            }
        }
    }

    //draw a square on the sreen to represent a bit
    void drawPixel(Bitmap izyCode,int mode, int x, int y) {
        switch (mode) {
            //fill the izycode bitmap
            case 1:
                for (int i = x; i < x + pixelSize; i++) {
                    for (int j = y; j < y + pixelSize; j++) {
                        izyCode.setPixel(j, i, Color.BLACK);
                    }
                }
                break;

            //draw the border line
            case 2:
                drawPixelBorder(izyCode, y, x);
                break;

        }
    }

    void drawPixelBorder(Bitmap izyCodeBitmap, int height, int width){
        if(width + borderSize <= izyCodeBitmap.getWidth() && height + borderSize <= izyCodeBitmap.getHeight()) {
            for (int i = height; i < height + borderSize; i++) {
                for (int j = width; j < width + borderSize; j++) {
                    izyCodeBitmap.setPixel(i, j, Color.GRAY);
                }
            }
        }
    }
}
