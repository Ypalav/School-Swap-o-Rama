package ngarcial.finalProject.cs442.com;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by Nick on 11/1/2015.
 */
public class BuyFragment extends Fragment implements OnItemClickListener {

    String[] booklist;
    String[] showbooklist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_buy, container, false);
        final ListView listview = (ListView) rootView.findViewById(R.id.listView);

        booklist = getResources().getStringArray(R.array.book_items);
//        showbooklist = new String[booklist.length];
//        for (int i = 0;i<booklist.length;i++){
//            showbooklist[i] = (i+1)+"."+booklist[i];
//        }

        ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,booklist);
        listview.setAdapter(adapter);
        Toast.makeText(getActivity(),
                "You have selected ", Toast.LENGTH_SHORT).show();
        listview.setOnItemClickListener(this);

        return rootView;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
        intent.putExtra("selectitemid",position);
        startActivity(intent);
    }
}
