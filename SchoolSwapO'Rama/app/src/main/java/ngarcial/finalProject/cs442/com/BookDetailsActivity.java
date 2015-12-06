package ngarcial.finalProject.cs442.com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class BookDetailsActivity extends Activity {
    TextView text;
    String[] itemSarray;
    int selectitemid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        Intent intent = getIntent();
        selectitemid = intent.getIntExtra("selectitemid", 0);

        itemSarray = getResources().getStringArray(R.array.book_items);
        text = (TextView)findViewById(R.id.buyerBookName);
        text.setText(itemSarray[selectitemid]);

    }

    public void clickCancel(View view) {
        finish();
    }

}
