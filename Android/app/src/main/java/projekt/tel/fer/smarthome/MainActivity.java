package projekt.tel.fer.smarthome;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


public class MainActivity extends AppCompatActivity {


    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
  //   public SharedPreferences settings;
    //Ovom naredbom dohvacamo String URL adresu: settings.getString("URLadr", defaultURL )
   // public static final String defaultURL = "http://iotat.tel.fer.hr:59984/api/sensors/"; //stavljeno kao primjer

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*
        UrlSingleton tmp  = UrlSingleton.getInstance( );
        tmp.setInstance(getSharedPreferences("URLsettings", 0));
       settings = getSharedPreferences("URLsettings", 0);
*/



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.thermometer_big);
        tabLayout.getTabAt(1).setIcon(R.drawable.humidity_92);
        tabLayout.getTabAt(2).setIcon(R.drawable.light_bulb);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.menu_url:
                URLDialogFunction();
                break;


        }

        return super.onOptionsItemSelected(item);
    }

    //poziva se pri pritisku na tipku "changURL"
    public void URLDialogFunction() {


        final Dialog dialog = new Dialog(MainActivity.this);


        dialog.setTitle("Server URL");
        dialog.setContentView(R.layout.url_customdialog_layout);
        dialog.show();


        YourPreference yourPreference = YourPreference.getInstance(getApplicationContext());

        final EditText editText = (EditText) dialog.findViewById(R.id.editURL);
        editText.setText(yourPreference.getData("URL"));
/*
        UrlSingleton tmp  = UrlSingleton.getInstance( );
        editText.setText(tmp.getInstance().getString("URLadr", defaultURL));
*/
        Button saveButton = (Button) dialog.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = getApplicationContext();
                YourPreference yourPreference = YourPreference.getInstance(context);
                yourPreference.saveData("URL", editText.getText().toString());

/*
                SharedPreferences.Editor editor = tmp.getInstance().edit();
                editor.putString("URLadr", editText.getText().toString());
                editor.commit();

                editText.setText(tmp.getInstance().getString("URLadr", defaultURL ));
*/

                CharSequence text = "URL set to: "  +  yourPreference.getData("URL");

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                dialog.cancel();
            }
        });


    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static final int GRAPH = 0;
        public static final int AVERAGE = 1;
        public static final int CURRENT = 2;

        private RecyclerView mRecyclerView;
        private RecyclerView.LayoutManager mLayoutManager;
        private int mDatasetTypes[] = {GRAPH, AVERAGE, CURRENT};

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_view, container, false);
            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
            mLayoutManager = new LinearLayoutManager(this.getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    TempTabAdapter tempAdapter = new TempTabAdapter(getContext());
                    mRecyclerView.offsetChildrenVertical(0);
                    mRecyclerView.setAdapter(tempAdapter);

                    return rootView;
                case 2:
                    HumTabAdapter humAdapter = new HumTabAdapter(getContext());
                    mRecyclerView.offsetChildrenVertical(0);
                    mRecyclerView.setAdapter(humAdapter);
                    return rootView;
                default:
                    final ItabAdapter iAdapter = new ItabAdapter(getContext());
                    mRecyclerView.offsetChildrenVertical(0);
                    mRecyclerView.setAdapter(iAdapter);

                    return rootView;
            }

        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position + 1);
        }


        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}
