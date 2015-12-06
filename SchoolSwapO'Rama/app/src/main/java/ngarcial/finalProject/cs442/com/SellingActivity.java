package ngarcial.finalProject.cs442.com;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

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

public class SellingActivity extends AppCompatActivity {

    private Button scanBtn, takePic, confirm, cancel;
    private EditText bookName, bookAuthor, bookISBN, bookdescription, bookPrice;
    private ImageView bookView;
    private RadioButton newBook, goodBook, usedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_post);

        scanBtn = (Button) findViewById(R.id.Post_ISBN_ScanBt);
        takePic = (Button) findViewById(R.id.Post_photoBt);
        confirm = (Button) findViewById(R.id.Post_confirmBt);
        cancel = (Button) findViewById(R.id.Post_cancleBt);
        bookName = (EditText) findViewById(R.id.Post_inputBookName);
        bookAuthor = (EditText) findViewById(R.id.Post_inputAuthor);
        bookISBN = (EditText) findViewById(R.id.Post_inputISPN);
        bookdescription = (EditText) findViewById(R.id.Post_inputDescription);
        bookPrice = (EditText) findViewById(R.id.Post_inputPrice);
        bookView = (ImageView) findViewById(R.id.Post_imageView);
        newBook = (RadioButton) findViewById(R.id.Post_LikeNewrdoBt);
        goodBook = (RadioButton) findViewById(R.id.Post_GoodrdoBt);
        usedBook = (RadioButton) findViewById(R.id.Post_UsedrdoBt);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                catch(Exception e){ e.printStackTrace(); }
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

                try{ bookISBN.setText(volumeObject.getString("ISBN")); }
                catch(JSONException jse){
                    bookdescription.setText("");
                    jse.printStackTrace();
                }

                try{ bookdescription.setText(volumeObject.getString("description")); }
                catch(JSONException jse){
                    bookdescription.setText("");
                    jse.printStackTrace();
                }

                try{ bookdescription.setText(volumeObject.getString("price")); }
                catch(JSONException jse){
                    bookdescription.setText("");
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
