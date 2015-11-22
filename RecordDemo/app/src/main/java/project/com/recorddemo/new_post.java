package project.com.recorddemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class new_post extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_new_post);

        Button buttonScanISBN = (Button)findViewById(R.id.Post_ISBN_ScanBt);
        Button buttonCamera = (Button)findViewById(R.id.Post_photoBt);
        buttonCamera.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //use Intent to take picture
                Intent intent_camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, 0);
            }
        });

        buttonScanISBN.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(new_post.this);
                scanIntegrator.initiateScan();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //show pic after take the pic
        ImageView iv = (ImageView)findViewById(R.id.Post_imageView);
        if (resultCode == RESULT_OK)
        {
            //take the pic and return the data
            Bundle extras = intent.getExtras();
            //transfer the data to img
            Bitmap bmp = (Bitmap) extras.get("data");
            //set to imgview
            iv.setImageBitmap(bmp);
        }

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            Toast.makeText(new_post.this, "Scan successful", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    // finish this activity
    public void clickOk(View view) {
        Toast.makeText(new_post.this, "Post Succeed", Toast.LENGTH_SHORT).show();
//        finish();
    }
    public void clickCancle(View view) {
        finish();
    }
}
