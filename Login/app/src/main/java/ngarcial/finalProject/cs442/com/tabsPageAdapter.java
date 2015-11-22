package ngarcial.finalProject.cs442.com;

/**
 * Created by Nick on 11/1/2015.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class tabsPageAdapter extends FragmentPagerAdapter
{

    public tabsPageAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int index)
    {
        switch (index)
        {
            case 0:
                // Top Rated fragment activity
                return new postFragment();
            case 1:
                // Games fragment activity
                return new buyFragment();
        }
        return null;
    }

    @Override
    public int getCount()
    {
        // get item count - equal to number of tabs
        return 2;
    }

}
