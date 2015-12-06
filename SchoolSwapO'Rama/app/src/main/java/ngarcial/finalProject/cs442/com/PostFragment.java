package ngarcial.finalProject.cs442.com;

/**
 * Created by Nick on 11/1/2015.
 */


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
public class PostFragment extends Fragment implements OnClickListener
{
    private Button scanBtn, takePic, confirm, cancel;
    private EditText bookName, bookAuthor, bookISBN, bookdescription, bookPrice;
    private ImageView bookView;
    private RadioButton newBook, goodBook, usedBook;
    String scanContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
        scanBtn = (Button) rootView.findViewById(R.id.Post_ISBN_ScanBt);
        takePic = (Button) rootView.findViewById(R.id.Post_photoBt);
        confirm = (Button) rootView.findViewById(R.id.Post_confirmBt);
        cancel = (Button) rootView.findViewById(R.id.Post_cancleBt);
        bookName = (EditText) rootView.findViewById(R.id.Post_inputBookName);
        bookAuthor = (EditText) rootView.findViewById(R.id.Post_inputAuthor);
        bookISBN = (EditText) rootView.findViewById(R.id.Post_inputISPN);
        bookdescription = (EditText) rootView.findViewById(R.id.Post_inputDescription);
        bookPrice = (EditText) rootView.findViewById(R.id.Post_inputPrice);
        bookView = (ImageView) rootView.findViewById(R.id.Post_imageView);
        newBook = (RadioButton) rootView.findViewById(R.id.Post_LikeNewrdoBt);
        goodBook = (RadioButton) rootView.findViewById(R.id.Post_GoodrdoBt);
        usedBook = (RadioButton) rootView.findViewById(R.id.Post_UsedrdoBt);
        scanBtn.setOnClickListener(this);
        takePic.setOnClickListener(this);
        confirm.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.Post_ISBN_ScanBt){
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
            /*Intent intent = new Intent(getActivity(), SellingActivity.class);
            startActivity(intent);*/
        }
        else if (v.getId()==R.id.Post_photoBt){

        }
        else if(v.getId()==R.id.Post_confirmBt){

        }
        else if(v.getId()==R.id.Post_cancleBt){

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        scanContent = scanningResult.getContents();
        String scanFormat = scanningResult.getFormatName();
        Log.v("PostFragment","contents scanned");
        if (scanContent!=null && scanFormat!=null && scanFormat.equalsIgnoreCase("EAN_13")) {
            Log.v("PostFragment", "Inside If");
            String bookSearchString = "https://www.googleapis.com/books/v1/volumes?"+
                    "q=isbn:"+scanContent+"&key=AIzaSyCMVK8TsxHqdDc547mKgZpo2T9CNZZWikc";
            new GetBookInfo().execute(bookSearchString);
            Log.v("PostFragment", "Leaving If");
        }
        else{
            Log.v("PostFragment", "Inside else");
            Toast toast = Toast.makeText(this.getContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private class GetBookInfo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... bookURLs) {
        //request book info
            StringBuilder bookBuilder = new StringBuilder();
            for (String bookSearchURL : bookURLs) {
                //search urls
                HttpClient bookClient = new DefaultHttpClient();
                try {
                    //get the data
                    HttpGet bookGet = new HttpGet(bookSearchURL);
                    HttpResponse bookResponse = bookClient.execute(bookGet);
                    StatusLine bookSearchStatus = bookResponse.getStatusLine();
                    if (bookSearchStatus.getStatusCode()==200) {
                        //we have a result
                        HttpEntity bookEntity = bookResponse.getEntity();
                        InputStream bookContent = bookEntity.getContent();
                        InputStreamReader bookInput = new InputStreamReader(bookContent);
                        BufferedReader bookReader = new BufferedReader(bookInput);
                        String lineIn;
                        while ((lineIn=bookReader.readLine())!=null) {
                            bookBuilder.append(lineIn);
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();

                }
            }
            return bookBuilder.toString();
        }

        protected void onPostExecute(String result) {
            //parse search results
            try {
                //parse results
                JSONObject resultObject = new JSONObject(result);
                JSONArray bookArray = resultObject.getJSONArray("items");
                JSONObject bookObject = bookArray.getJSONObject(0);
                JSONObject volumeObject = bookObject.getJSONObject("volumeInfo");
                try {
                    bookName.setText(volumeObject.getString("title"));
                } catch (JSONException jse) {
                    bookName.setText("");
                    jse.printStackTrace();
                }

                StringBuilder authorBuild = new StringBuilder("");
                try{
                    JSONArray authorArray = volumeObject.getJSONArray("authors");
                    for(int a=0; a<authorArray.length(); a++){
                        if(a>0) authorBuild.append(", ");
                        authorBuild.append(authorArray.getString(a));
                    }
                    bookAuthor.setText(authorBuild.toString());
                }
                catch(JSONException jse){
                    bookAuthor.setText("");
                    jse.printStackTrace();
                }

                try{ bookdescription.setText(volumeObject.getString("description")); }
                catch(JSONException jse){
                    bookdescription.setText("");
                    jse.printStackTrace();
                }

                try{
                    bookISBN.setText(scanContent); }
                catch(Exception jse){
                    bookISBN.setText("");
                    jse.printStackTrace();
                }

            }
            catch (Exception e) {
                //no result
                e.printStackTrace();
                bookName.setText("Not Found");
                bookAuthor.setText("");
                bookISBN.setText("");
                bookdescription.setText("");
                bookPrice.setText("");
            }
        }

    }

}
