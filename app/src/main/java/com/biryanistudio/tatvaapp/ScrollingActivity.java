package com.biryanistudio.tatvaapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class ScrollingActivity extends AppCompatActivity {

    private RecyclerView eventList;
    private FloatingActionButton fab;
    private List<EventData> eventDataList;
    private List<EventDetailData> eventDetailDataList;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbarnew = (Toolbar) findViewById(R.id.toolbarnew);
        setSupportActionBar(toolbarnew);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        initData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setColors();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab.setTransitionName("fab_maps");
        }
        eventList = (RecyclerView) findViewById(R.id.eventList);
        eventList.setHasFixedSize(true);

        if (isTablet(getApplicationContext()))
            eventList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        else
            eventList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(eventDataList, getApplicationContext());
        recyclerViewAdapter.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                displayDetails(view, position);
            }
        });
        eventList.setAdapter(new ScaleInAnimationAdapter(recyclerViewAdapter));
        eventList.startAnimation(AnimationUtils.loadAnimation(this, R.anim.view_up));

        fab.setVisibility(View.INVISIBLE);
        if ((ContextCompat.checkSelfPermission(ScrollingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(ScrollingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        } else
            fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ScrollingActivity.this, MapsActivity.class);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ScrollingActivity.this, fab, getResources().getString(R.string.fab_transition));
                startActivity(intent, optionsCompat.toBundle());
            }
        });

    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    private void setColors() {

        Glide.with(getApplicationContext()).load(R.drawable.mainposter).centerCrop().into((ImageView) findViewById(R.id.mainPoster));
        Palette.from(BitmapFactory.decodeResource(getResources(), R.drawable.mainposter)).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                Palette.Swatch vibrantDark = palette.getDarkVibrantSwatch();

                if (vibrant != null) {
                    eventList.setBackgroundColor(vibrantDark != null ? vibrantDark.getRgb() : 0);
                    collapsingToolbarLayout.setBackgroundColor(vibrant.getRgb());
                    collapsingToolbarLayout.setCollapsedTitleTextColor(vibrant.getTitleTextColor());
                }

                if (vibrantDark != null) {
                    collapsingToolbarLayout.setExpandedTitleColor(vibrantDark.getTitleTextColor());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setNavigationBarColor(vibrantDark.getRgb());
                        getWindow().setStatusBarColor(vibrantDark.getRgb());
                    }
                }

            }
        });

    }

    public void startInfoActivity(View view) {

        startActivity(new Intent(this, AboutActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(true);
        searchView.clearFocus();
        searchView.setQueryHint("Event name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                List<EventData> filteredList = new ArrayList<>();
                final ArrayList<Integer> positions = new ArrayList<>();
                for (int i = 0; i < eventDataList.size(); i++) {
                    String text = eventDataList.get(i).eventName.toLowerCase();
                    if (text.contains(newText)) {
                        filteredList.add(eventDataList.get(i));
                        positions.add(i);
                    }
                }

                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(filteredList, getApplicationContext());
                recyclerViewAdapter.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        displayDetails(view, positions.get(position));
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        MenuItemCompat.collapseActionView(menu.findItem(R.id.action_search));

                    }
                });
                eventList.setAdapter(new ScaleInAnimationAdapter(new AlphaInAnimationAdapter(recyclerViewAdapter)));
                return true;
            }
        });
        return true;
    }

    private void displayDetails(View view, int position) {

        Intent intent = new Intent(ScrollingActivity.this, EventActivity.class);
        int posterid = eventDetailDataList.get(position).posterid;
        String name = eventDetailDataList.get(position).name;
        String day = eventDetailDataList.get(position).day;
        String time = eventDetailDataList.get(position).time;
        String location = eventDetailDataList.get(position).location;
        String description = eventDetailDataList.get(position).description;
        String teammembers = eventDetailDataList.get(position).teammembers;
        String rate = eventDetailDataList.get(position).rate;
        String firstPrize = eventDetailDataList.get(position).firstPrize;
        String secondPrize = eventDetailDataList.get(position).secondPrize;
        String organizer1 = eventDetailDataList.get(position).organizer1;
        String organizer2 = eventDetailDataList.get(position).organizer2;
        String phone1 = eventDetailDataList.get(position).phone1;
        String phone2 = eventDetailDataList.get(position).phone2;
        intent.putExtra("posterid", posterid);
        intent.putExtra("name", name);
        intent.putExtra("day", day);
        intent.putExtra("time", time);
        intent.putExtra("location", location);
        intent.putExtra("description", description);
        intent.putExtra("teammembers", teammembers);
        intent.putExtra("rate", rate);
        intent.putExtra("firstPrize", firstPrize);
        intent.putExtra("secondPrize", secondPrize);
        intent.putExtra("organizer1", organizer1);
        intent.putExtra("organizer2", organizer2);
        intent.putExtra("phone1", phone1);
        intent.putExtra("phone2", phone2);

        CardView cardView = (CardView) view;
        RelativeLayout relativeLayout = (RelativeLayout) cardView.getChildAt(0);
        ImageView imageView = (ImageView) relativeLayout.getChildAt(0);

        Pair<View, String> cardPair = Pair.create((View) cardView, "eventCardHolder");
        Pair<View, String> posterPair = Pair.create((View) imageView, "eventPosterHolder");
        //noinspection unchecked
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ScrollingActivity.this, cardPair, posterPair);
        ActivityCompat.startActivity(ScrollingActivity.this, intent, activityOptionsCompat.toBundle());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(ScrollingActivity.this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 123) {
            Map<String, Integer> perms = new HashMap<>();
            perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
            for (int i = 0; i < permissions.length; i++)
                perms.put(permissions[i], grantResults[i]);
            if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                fab.setVisibility(View.VISIBLE);
            else {
                fab.setVisibility(View.GONE);
                Snackbar.make(findViewById(R.id.parentView), "Please allow location access to enable all features.", Snackbar.LENGTH_LONG)
                        .setAction("Allow", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(ScrollingActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                            }
                        })
                        .show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initData() {
        eventDataList = new ArrayList<>();
        eventDetailDataList = new ArrayList<>();

        //Names

        String techDCname = getString(R.string.techDCname);
        String technogenname = getString(R.string.technogenName);
        String circuitDebugname = getString(R.string.CircuitDebuggingName);
        String corcongname = getString(R.string.CorCongName);
        String mockStockname = getString(R.string.mockStockName);
        String treasurehuntname = getString(R.string.treasureHuntName);
        String techjamname = getString(R.string.techJAMName);
        String techTalkname = getString(R.string.techTalkName);
        String gamingNFSname = getString(R.string.gamingNFSName);
        String gamingCSname = getString(R.string.gamingCSName);
        String gamingCODname = getString(R.string.gamingCODName);
        String mockGREname = getString(R.string.mockGREName);
        String patternprintname = getString(R.string.patternPrintName);
        String eetm2name = getString(R.string.eeTM2Name);
        String essencename = getString(R.string.essenceName);
        String kartitname = getString(R.string.kartItName);
        String codingname = getString(R.string.codingName);
        String potpourriname = getString(R.string.potporriName);
        String cryptorigname = getString(R.string.cryptORigName);
        String googleitname = getString(R.string.googleItName);
        String chuckglidername = getString(R.string.chuckGliderName);
        String photographyname = getString(R.string.photographyName);
        String hirefirename = getString(R.string.hireFireName);
        String rubikscubename = getString(R.string.rubiksCubeName);
        String trdplname = getString(R.string.trdplName);
        String bbroyname = getString(R.string.bbRoyName);
        String hogathonname = getString(R.string.hogathonName);
        String gamingConsolename = getString(R.string.gamingConsoleName);

        //Descriptions

        String techDCdesc = getString(R.string.techDCDesc);
        String technogendesc = getString(R.string.technogenDesc);
        String circuitDebugdesc = getString(R.string.circuitDebugDesc);
        String corporateCongdesc = getString(R.string.CorpCongDesc);
        String mockStockdesc = getString(R.string.mockStockDesc);
        String treasureHuntdesc = getString(R.string.treasureHuntDesc);
        String techTalkdesc = getString(R.string.techTalkDesc);
        String techjamMdesc = getString(R.string.techJAMDesc);
        String gamingNFSdesc = getString(R.string.gamingPCDesc);
        String gamingCSdesc = getString(R.string.gamingPCDesc);
        String gamingCODdesc = getString(R.string.gamingPCDesc);
        String mockGREdesc = getString(R.string.mockGREDesc);
        String patternPrintdesc = getString(R.string.patternPrintDesc);
        String eETM2desc = getString(R.string.eeTM2Desc);
        String essencedesc = getString(R.string.essenceDesc);
        String kartitdesc = getString(R.string.kartItDesc);
        String codingdesc = getString(R.string.codingDesc);
        String potpourridesc = getString(R.string.potpourriDesc);
        String cryptORigdesc = getString(R.string.cryptORigDesc);
        String googleItdesc = getString(R.string.googleItDesc);
        String chuckGliderdesc = getString(R.string.chuckGliderDesc);
        String photographydesc = getString(R.string.photographyDesc);
        String hireFiredesc = getString(R.string.hireFireDesc);
        String rubiksCubedesc = getString(R.string.rubiksCubeDesc);
        String bbRoydesc = getString(R.string.bbRoyDesc);
        String trdpldesc = getString(R.string.trdplDesc);
        String hogathondesc = getString(R.string.hogathonDesc);
        String gamingConsoledesc = getString(R.string.gamingConsoleDesc);

        //Days

        String technogenday = getString(R.string.technogenDay);
        String techdcday = getString(R.string.techDCDay);
        String patternprintday = getString(R.string.patternPrintDay);
        String circuitdebugday = getString(R.string.circuitDebugDay);
        String techjamday = getString(R.string.techJAMDay);
        String techtalkday = getString(R.string.techTalkDay);
        String corcongday = getString(R.string.corCongDay);
        String mockgreday = getString(R.string.mockGREDay);
        String gamingNFSday = getString(R.string.gamingNFSDay);
        String gamingCSday = getString(R.string.gamingCSDay);
        String gamingCODday = getString(R.string.gamingCODDay);
        String mockstockday = getString(R.string.mockStockDay);
        String treasurehuntday = getString(R.string.treasureHuntDay);
        String essenceday = getString(R.string.essenceDay);
        String eetm2day = getString(R.string.eeTM2Day);
        String potpourriday = getString(R.string.potpourriDay);
        String kartitday = getString(R.string.kartItDay);
        String googleitday = getString(R.string.googleItDay);
        String hirefireday = getString(R.string.hireFireDay);
        String photographyday = getString(R.string.photographyDay);
        String bbroyday = getString(R.string.bbRoyDay);
        String cryptorigday = getString(R.string.cryptORigDay);
        String codingday = getString(R.string.codingDay);
        String chuckgliderday = getString(R.string.chuckGliderDay);
        String rubikscubdeday = getString(R.string.rubiksCubeDay);
        String hogathonday = getString(R.string.hogathonDay);
        String trdplday = getString(R.string.trdplDay);
        String gamingConsoleday = getString(R.string.gamingConsoleDay);

        //Timings

        String technogentime = getString(R.string.technogenTime);
        String techdctime = getString(R.string.techDCTime);
        String patternprinttime = getString(R.string.patternPrintTime);
        String circuitdebugtime = getString(R.string.circuitDebugTime);
        String techjamtime = getString(R.string.techJAMTime);
        String techtalktime = getString(R.string.techTalkTime);
        String corcongtime = getString(R.string.corCongTime);
        String mockgretime = getString(R.string.mockGRETime);
        String gamingNFStime = getString(R.string.gamingNFSTime);
        String gamingCStime = getString(R.string.gamingCSTime);
        String gamingCODtime = getString(R.string.gamingCODTime);
        String mockstocktime = getString(R.string.mockStockTime);
        String treasurehunttime = getString(R.string.treasureHuntTime);
        String essencetime = getString(R.string.essenceTime);
        String eetm2time = getString(R.string.eeTM2Time);
        String potpourritime = getString(R.string.potpourriTime);
        String kartittime = getString(R.string.kartItTime);
        String googleittime = getString(R.string.googleItTime);
        String hirefiretime = getString(R.string.hireFireTime);
        String photographytime = getString(R.string.photographyTime);
        String bbroytime = getString(R.string.bbRoyTime);
        String cryptorigtime = getString(R.string.cryptORigTime);
        String codingtime = getString(R.string.codingTime);
        String chuckglidertime = getString(R.string.chuckGliderTime);
        String rubikscubetime = getString(R.string.rubiksCubeTime);
        String hogathontime = getString(R.string.hogathonTime);
        String trdpltime = getString(R.string.trdplTime);
        String gamingConsoletime = getString(R.string.gamingConsoleTime);

        //Location

        String technogenloc = getString(R.string.technogenLoc);
        String techdcloc = getString(R.string.techDCLoc);
        String patternprintloc = getString(R.string.patternPrintLoc);
        String circuitdebugloc = getString(R.string.circuitDebugLoc);
        String techjamloc = getString(R.string.techJAMLoc);
        String techtalkloc = getString(R.string.techTalkLoc);
        String corcongloc = getString(R.string.corCongLoc);
        String mockgreloc = getString(R.string.mockGRELoc);
        String gamingNFSloc = getString(R.string.gamingNFSLoc);
        String gamingCSloc = getString(R.string.gamingCSLoc);
        String gamingCODloc = getString(R.string.gamingCODLoc);
        String mockstockloc = getString(R.string.mockStockLoc);
        String treasurehuntloc = getString(R.string.treasureHuntLoc);
        String essenceloc = getString(R.string.essenceLoc);
        String eetm2loc = getString(R.string.eeTM2Loc);
        String potpourriloc = getString(R.string.potpourriLoc);
        String kartitloc = getString(R.string.kartItLoc);
        String googleitloc = getString(R.string.googleItLoc);
        String hirefireloc = getString(R.string.hireFireLoc);
        String photographyloc = getString(R.string.photographyLoc);
        String bbroyloc = getString(R.string.bbRoyLoc);
        String cryptorigloc = getString(R.string.cryptORigLoc);
        String codingloc = getString(R.string.codingLoc);
        String chuckgliderloc = getString(R.string.chuckGliderLoc);
        String rubikscubeloc = getString(R.string.rubiksCubeLoc);
        String hogathonloc = getString(R.string.hogathonLoc);
        String trdplloc = getString(R.string.trdplLoc);
        String gamingConsoleloc = getString(R.string.gamingConsoleLoc);

        //Team Members

        String techdctm = getString(R.string.techDCTm);
        String technogentm = getString(R.string.technogenTm);
        String circuitdebugtm = getString(R.string.circuitDebugTm);
        String corcongtm = getString(R.string.corCongTm);
        String mockstocktm = getString(R.string.mockStockTm);
        String treasurehunttm = getString(R.string.treasureHuntTm);
        String techjamtm = getString(R.string.techJAMTm);
        String techtalktm = getString(R.string.techTalkTm);
        String gamingNFStm = getString(R.string.gamingNFSTm);
        String gamingCStm = getString(R.string.gamingCSTm);
        String gamingCODtm = getString(R.string.gamingCODTm);
        String mockgretm = getString(R.string.mockGRETm);
        String patternprinttm = getString(R.string.patternPrintTm);
        String eetm2tm = getString(R.string.eeTM2Tm);
        String essencetm = getString(R.string.essenceTm);
        String kartittm = getString(R.string.kartItTm);
        String codingtm = getString(R.string.codingTm);
        String potpourritm = getString(R.string.potpourriTm);
        String cryptorigtm = getString(R.string.cryptORigTm);
        String googleittm = getString(R.string.googleItTm);
        String chuckglidertm = getString(R.string.chuckGliderTm);
        String photographytm = getString(R.string.photographyTm);
        String hirefiretm = getString(R.string.hireFireTm);
        String rubikscubetm = getString(R.string.rubiksCubeTm);
        String bbroytm = getString(R.string.bbRoyTm);
        String trdpltm = getString(R.string.trdplTm);
        String hogathontm = getString(R.string.hogathonTm);
        String gamingConsoletm = getString(R.string.gamingConsoleTm);

        //Ticket Rate

        String techdctr = getString(R.string.techDCTr);
        String technogentr = getString(R.string.technogenTr);
        String circuitdebugtr = getString(R.string.circuitDebugTr);
        String corcongtr = getString(R.string.corCongTr);
        String mockstocktr = getString(R.string.mockStockTr);
        String treasurehunttr = getString(R.string.treasureHuntTr);
        String techjamtr = getString(R.string.techJAMTr);
        String techtalktr = getString(R.string.techTalkTr);
        String gamingNFStr = getString(R.string.gamingNFSTr);
        String gamingCStr = getString(R.string.gamingCSTr);
        String gamingCODtr = getString(R.string.gamingCODTr);
        String mockgretr = getString(R.string.mockGRETr);
        String patternprinttr = getString(R.string.patternPrintTr);
        String eetm2tr = getString(R.string.eeTM2Tr);
        String essencetr = getString(R.string.essenceTr);
        String kartittr = getString(R.string.kartItTr);
        String codingtr = getString(R.string.codingTr);
        String potpourritr = getString(R.string.potpourriTr);
        String cryptorigtr = getString(R.string.cryptORigTr);
        String googleittr = getString(R.string.googleItTr);
        String chuckglidertr = getString(R.string.chuckGliderTr);
        String photographytr = getString(R.string.photographyTr);
        String hirefiretr = getString(R.string.hireFireTr);
        String rubikscubetr = getString(R.string.rubiksCubeTr);
        String bbroytr = getString(R.string.bbRoyTr);
        String trdpltr = getString(R.string.trdplTr);
        String hogathontr = getString(R.string.hogathonTr);
        String gamingConsoletr = getString(R.string.gamingConsoleTr);

        //First Prize

        String techdcfp = getString(R.string.techDCFp);
        String technogenfp = getString(R.string.technogenFp);
        String circuitdebugfp = getString(R.string.circuitDebugFp);
        String corcongfp = getString(R.string.corCongFp);
        String mockstockfp = getString(R.string.mockStockFp);
        String treasurehuntfp = getString(R.string.treasureHuntFp);
        String techjamfp = getString(R.string.techJAMFp);
        String techtalkfp = getString(R.string.techTalkFp);
        String gamingNFSfp = getString(R.string.gamingNFSFp);
        String gamingCSfp = getString(R.string.gamingCSFp);
        String gamingCODfp = getString(R.string.gamingCODFp);
        String mockgrefp = getString(R.string.mockGREFp);
        String patternprintfp = getString(R.string.patternPrintFp);
        String eetm2fp = getString(R.string.eeTM2Fp);
        String essencefp = getString(R.string.essenceFp);
        String kartitfp = getString(R.string.kartItFp);
        String codingfp = getString(R.string.codingFp);
        String potpourrifp = getString(R.string.potpourriFp);
        String cryptorigfp = getString(R.string.cryptORigFp);
        String googleitfp = getString(R.string.googleItFp);
        String chuckgliderfp = getString(R.string.chuckGliderFp);
        String photographyfp = getString(R.string.photographyFp);
        String hirefirefp = getString(R.string.hireFireFp);
        String rubikscubefp = getString(R.string.rubiksCubeFp);
        String bbroyfp = getString(R.string.bbRoyFp);
        String trdplfp = getString(R.string.trdplFp);
        String hogathonfp = getString(R.string.hogathonFp);
        String gamingConsolefp = getString(R.string.gamingConsoleFp);

        //Second Prize

        String techdcsp = getString(R.string.techDCSp);
        String technogensp = getString(R.string.technogenSp);
        String circuitdebugsp = getString(R.string.circuitDebugSp);
        String corcongsp = getString(R.string.corCongSp);
        String mockstocksp = getString(R.string.mockStockSp);
        String treasurehuntsp = getString(R.string.treasureHuntSp);
        String techjamsp = getString(R.string.techJAMSp);
        String techtalksp = getString(R.string.techTalkSp);
        String gamingNFSsp = getString(R.string.gamingNFSSp);
        String gamingCSsp = getString(R.string.gamingCSSp);
        String gamingCODsp = getString(R.string.gamingCODSp);
        String mockgresp = getString(R.string.mockGRESp);
        String patternprintsp = getString(R.string.patternPrintSp);
        String eetm2sp = getString(R.string.eeTM2Sp);
        String essencesp = getString(R.string.essenceSp);
        String kartitsp = getString(R.string.kartItSp);
        String codingsp = getString(R.string.codingSp);
        String potpourrisp = getString(R.string.potpourriSp);
        String cryptorigsp = getString(R.string.cryptORigSp);
        String googleitsp = getString(R.string.googleItSp);
        String chuckglidersp = getString(R.string.chuckGliderSp);
        String photographysp = getString(R.string.photographySp);
        String hirefiresp = getString(R.string.hireFireSp);
        String rubikscubesp = getString(R.string.rubiksCubeSp);
        String bbroysp = getString(R.string.bbRoySp);
        String trdplsp = getString(R.string.trdplSp);
        String hogathonsp = getString(R.string.hogathonSp);
        String gamingConsolesp = getString(R.string.gamingConsoleSp);

        //Organizer1

        String techdco1 = getString(R.string.techDCO1);
        String technogeno1 = getString(R.string.technogenO1);
        String circuitdebugo1 = getString(R.string.circuitDebugO1);
        String corcongo1 = getString(R.string.corCongO1);
        String mockstocko1 = getString(R.string.mockStockO1);
        String treasurehunto1 = getString(R.string.treasureHuntO1);
        String techjamo1 = getString(R.string.techJAMO1);
        String techtalko1 = getString(R.string.techTalkO1);
        String gamingNFSo1 = getString(R.string.gamingNFSO1);
        String gamingCSo1 = getString(R.string.gamingCSO1);
        String gamingCODo1 = getString(R.string.gamingCODO1);
        String mockgreo1 = getString(R.string.mockGREO1);
        String patternprinto1 = getString(R.string.patternPrintO1);
        String eetm2o1 = getString(R.string.eeTNM2O1);
        String essenceo1 = getString(R.string.essenceO1);
        String kartito1 = getString(R.string.kartItO1);
        String codingo1 = getString(R.string.codingO1);
        String potpourrio1 = getString(R.string.potpourriO1);
        String cryptorigo1 = getString(R.string.cryptORigO1);
        String googleito1 = getString(R.string.googleItO1);
        String chuckglidero1 = getString(R.string.chuckGliderO1);
        String photographyo1 = getString(R.string.photographyO1);
        String hirefireo1 = getString(R.string.hireFireO1);
        String rubikscubeo1 = getString(R.string.rubiksCubeO1);
        String bbroyo1 = getString(R.string.bbRoyO1);
        String trdplo1 = getString(R.string.trdplO1);
        String hogathono1 = getString(R.string.hogathonO1);
        String gamingConsoleo1 = getString(R.string.gamingConsoleO1);

        //Organizer2

        String techdco2 = getString(R.string.techDCO2);
        String technogeno2 = getString(R.string.technogenO2);
        String circuitdebugo2 = getString(R.string.circuitDebuggingO2);
        String corcongo2 = getString(R.string.corCongO2);
        String mockstocko2 = getString(R.string.mockStockO2);
        String treasurehunto2 = getString(R.string.treasureHuntO2);
        String techjamo2 = getString(R.string.techJAMO2);
        String techtalko2 = getString(R.string.techTalkO2);
        String gamingNFSo2 = getString(R.string.gamingNFSO2);
        String gamingCSo2 = getString(R.string.gamingCSO2);
        String gamingCODo2 = getString(R.string.gamingCODO2);
        String mockgreo2 = getString(R.string.mockGREO2);
        String patternprinto2 = getString(R.string.patternPrintO2);
        String eetm2o2 = getString(R.string.eeTM2O2);
        String essenceo2 = getString(R.string.essenceO2);
        String kartito2 = getString(R.string.kartItO2);
        String codingo2 = getString(R.string.codingO2);
        String potpourrio2 = getString(R.string.potpourriO2);
        String cryptorigo2 = getString(R.string.cryptORigO2);
        String googleito2 = getString(R.string.googleItO2);
        String chuckglidero2 = getString(R.string.chuckGliderO2);
        String photographyo2 = getString(R.string.photographyO2);
        String hirefireo2 = getString(R.string.hireFireO2);
        String rubikscubeo2 = getString(R.string.rubiksCubeO2);
        String bbroyo2 = getString(R.string.bbRoyO2);
        String trdplo2 = getString(R.string.trdplO2);
        String hogathono2 = getString(R.string.hogathonO2);
        String gamingConsoleo2 = getString(R.string.gamingConsoleO2);

        //Phone 1

        String techdcp1 = getString(R.string.techDCP1);
        String technogenp1 = getString(R.string.technogenP1);
        String circuitdebugp1 = getString(R.string.circuitDebugP1);
        String corcongp1 = getString(R.string.corCongP1);
        String mockstockp1 = getString(R.string.mockStockP1);
        String treasurehuntp1 = getString(R.string.treasureHuntP1);
        String techjamp1 = getString(R.string.techJAMP1);
        String techtalkp1 = getString(R.string.techTalkP1);
        String gamingNFSp1 = getString(R.string.gamingNFSP1);
        String gamingCSp1 = getString(R.string.gamingCSP1);
        String gamingCODp1 = getString(R.string.gamingCODP1);
        String mockgrep1 = getString(R.string.mockGREP1);
        String patternprintp1 = getString(R.string.patternPrintP1);
        String eetm2p1 = getString(R.string.eeTM2P1);
        String essencep1 = getString(R.string.essenceP1);
        String kartitp1 = getString(R.string.kartItP1);
        String codingp1 = getString(R.string.codingP1);
        String potpourrip1 = getString(R.string.potpourriP1);
        String cryptorigp1 = getString(R.string.cryptORigP1);
        String googleitp1 = getString(R.string.googleItP1);
        String chuckgliderp1 = getString(R.string.chuckGliderP1);
        String photographyp1 = getString(R.string.photographyP1);
        String hirefirep1 = getString(R.string.hireFireP1);
        String rubikscubep1 = getString(R.string.rubiksCubeP1);
        String bbroyp1 = getString(R.string.bbRoyP1);
        String trdplp1 = getString(R.string.trdplP1);
        String hogathonp1 = getString(R.string.hogathonP1);
        String gamingConsolep1 = getString(R.string.gamingConsoleP1);

        //Phone 2

        String techdcp2 = getString(R.string.techDCP2);
        String technogenp2 = getString(R.string.technogenP2);
        String circuitdebugp2 = getString(R.string.circuitDebugP2);
        String corcongp2 = getString(R.string.corCongP2);
        String mockstockp2 = getString(R.string.mockStockP2);
        String treasurehuntp2 = getString(R.string.treasureHuntP2);
        String techjamp2 = getString(R.string.techJAMP2);
        String techtalkp2 = getString(R.string.techtalkP2);
        String gamingNFSp2 = getString(R.string.gamingNFSP2);
        String gamingCSp2 = getString(R.string.gamingCSP2);
        String gamingCODp2 = getString(R.string.gamingCODP2);
        String mockgrep2 = getString(R.string.mockGREP2);
        String patternprintp2 = getString(R.string.patternPrintP2);
        String eetm2p2 = getString(R.string.eeTM2P2);
        String essencep2 = getString(R.string.essenceP2);
        String kartitp2 = getString(R.string.kartItP2);
        String codingp2 = getString(R.string.codingP2);
        String potpourrip2 = getString(R.string.potpourriP2);
        String cryptorigp2 = getString(R.string.cryptORigP2);
        String googleitp2 = getString(R.string.googleItP2);
        String chuckgliderp2 = getString(R.string.chuckGliderP2);
        String photographyp2 = getString(R.string.photographyP2);
        String hirefirep2 = getString(R.string.hireFireP2);
        String rubikscubep2 = getString(R.string.rubiksCubeP2);
        String bbroyp2 = getString(R.string.bbRoyP2);
        String trdplp2 = getString(R.string.trdplP2);
        String hogathonp2 = getString(R.string.hogathonP2);
        String gamingConsolep2 = getString(R.string.gamingConsoleP2);

        //Poster ID

        int techdcpi = R.drawable.techdc;
        int technogenpi = R.drawable.technogen;
        int circuitdebugpi = R.drawable.circuitdebug;
        int corcongpi = R.drawable.corpcong;
        int mockstockpi = R.drawable.mockstock;
        int treasurehuntpi = R.drawable.treasurehunt;
        int techjampi = R.drawable.techjam;
        int techtalkpi = R.drawable.techtalk;
        int gamingNFSpi = R.drawable.gaming;
        int gamingCSpi = R.drawable.gaming;
        int gamingCODpi = R.drawable.gaming;
        int mockgrepi = R.drawable.mockgre;
        int patternprintpi = R.drawable.patternprint;
        int eetm2pi = R.drawable.eetm2;
        int essencepi = R.drawable.essence;
        int kartitpi = R.drawable.kartit;
        int codingpi = R.drawable.coding;
        int potpourripi = R.drawable.potpourri;
        int cryptorigpi = R.drawable.cryptorig;
        int googleitpi = R.drawable.googleit;
        int chuckgliderpi = R.drawable.chuckglider;
        int photographypi = R.drawable.photography;
        int hirefirepi = R.drawable.hireorfire;
        int rubikscubepi = R.drawable.cube;
        int bbroypi = R.drawable.bbroy;
        int trdplpi = R.drawable.raghudixit;
        int hogathonpi = R.drawable.hogathon;
        int gamingConsolepi = R.drawable.gaming;

        //Making ArrayList

        //Special Event

        eventDetailDataList.add(new EventDetailData(trdplpi, trdplname, trdplday, trdpltime, trdplloc, trdpldesc, trdpltm, trdpltr, trdplfp, trdplsp, trdplo1, trdplo2, trdplp1, trdplp2));

        //Day 1

        eventDetailDataList.add(new EventDetailData(circuitdebugpi, circuitDebugname, circuitdebugday, circuitdebugtime, circuitdebugloc, circuitDebugdesc, circuitdebugtm, circuitdebugtr, circuitdebugfp, circuitdebugsp, circuitdebugo1, circuitdebugo2, circuitdebugp1, circuitdebugp2));
        eventDetailDataList.add(new EventDetailData(corcongpi, corcongname, corcongday, corcongtime, corcongloc, corporateCongdesc, corcongtm, corcongtr, corcongfp, corcongsp, corcongo1, corcongo2, corcongp1, corcongp2));
        eventDetailDataList.add(new EventDetailData(gamingNFSpi, gamingNFSname, gamingNFSday, gamingNFStime, gamingNFSloc, gamingNFSdesc, gamingNFStm, gamingNFStr, gamingNFSfp, gamingNFSsp, gamingNFSo1, gamingNFSo2, gamingNFSp1, gamingNFSp2));
        eventDetailDataList.add(new EventDetailData(gamingCSpi, gamingCSname, gamingCSday, gamingCStime, gamingCSloc, gamingCSdesc, gamingCStm, gamingCStr, gamingCSfp, gamingCSsp, gamingCSo1, gamingCSo2, gamingCSp1, gamingCSp2));
        eventDetailDataList.add(new EventDetailData(gamingCODpi, gamingCODname, gamingCODday, gamingCODtime, gamingCODloc, gamingCODdesc, gamingCODtm, gamingCODtr, gamingCODfp, gamingCODsp, gamingCODo1, gamingCODo2, gamingCODp1, gamingCODp2));
        eventDetailDataList.add(new EventDetailData(mockgrepi, mockGREname, mockgreday, mockgretime, mockgreloc, mockGREdesc, mockgretm, mockgretr, mockgrefp, mockgresp, mockgreo1, mockgreo2, mockgrep1, mockgrep2));
        eventDetailDataList.add(new EventDetailData(mockstockpi, mockStockname, mockstockday, mockstocktime, mockstockloc, mockStockdesc, mockstocktm, mockstocktr, mockstockfp, mockstocksp, mockstocko1, mockstocko2, mockstockp1, mockstockp2));
        eventDetailDataList.add(new EventDetailData(patternprintpi, patternprintname, patternprintday, patternprinttime, patternprintloc, patternPrintdesc, patternprinttm, patternprinttr, patternprintfp, patternprintsp, patternprinto1, patternprinto2, patternprintp1, patternprintp2));
        eventDetailDataList.add(new EventDetailData(techdcpi, techDCname, techdcday, techdctime, techdcloc, techDCdesc, techdctm, techdctr, techdcfp, techdcsp, techdco1, techdco2, techdcp1, techdcp2));
        eventDetailDataList.add(new EventDetailData(techjampi, techjamname, techjamday, techjamtime, techjamloc, techjamMdesc, techjamtm, techjamtr, techjamfp, techjamsp, techjamo1, techjamo2, techjamp1, techjamp2));
        eventDetailDataList.add(new EventDetailData(technogenpi, technogenname, technogenday, technogentime, technogenloc, technogendesc, technogentm, technogentr, technogenfp, technogensp, technogeno1, technogeno2, technogenp1, technogenp2));
        eventDetailDataList.add(new EventDetailData(techtalkpi, techTalkname, techtalkday, techtalktime, techtalkloc, techTalkdesc, techtalktm, techtalktr, techtalkfp, techtalksp, techtalko1, techtalko2, techtalkp1, techtalkp2));
        eventDetailDataList.add(new EventDetailData(treasurehuntpi, treasurehuntname, treasurehuntday, treasurehunttime, treasurehuntloc, treasureHuntdesc, treasurehunttm, treasurehunttr, treasurehuntfp, treasurehuntsp, treasurehunto1, treasurehunto2, treasurehuntp1, treasurehuntp2));

        //Day 2

        eventDetailDataList.add(new EventDetailData(bbroypi, bbroyname, bbroyday, bbroytime, bbroyloc, bbRoydesc, bbroytm, bbroytr, bbroyfp, bbroysp, bbroyo1, bbroyo2, bbroyp1, bbroyp2));
        eventDetailDataList.add(new EventDetailData(chuckgliderpi, chuckglidername, chuckgliderday, chuckglidertime, chuckgliderloc, chuckGliderdesc, chuckglidertm, chuckglidertr, chuckgliderfp, chuckglidersp, chuckglidero1, chuckglidero2, chuckgliderp1, chuckgliderp2));
        eventDetailDataList.add(new EventDetailData(codingpi, codingname, codingday, codingtime, codingloc, codingdesc, codingtm, codingtr, codingfp, codingsp, codingo1, codingo2, codingp1, codingp2));
        eventDetailDataList.add(new EventDetailData(cryptorigpi, cryptorigname, cryptorigday, cryptorigtime, cryptorigloc, cryptORigdesc, cryptorigtm, cryptorigtr, cryptorigfp, cryptorigsp, cryptorigo1, cryptorigo2, cryptorigp1, cryptorigp2));
        eventDetailDataList.add(new EventDetailData(rubikscubepi, rubikscubename, rubikscubdeday, rubikscubetime, rubikscubeloc, rubiksCubedesc, rubikscubetm, rubikscubetr, rubikscubefp, rubikscubesp, rubikscubeo1, rubikscubeo2, rubikscubep1, rubikscubep2));
        eventDetailDataList.add(new EventDetailData(eetm2pi, eetm2name, eetm2day, eetm2time, eetm2loc, eETM2desc, eetm2tm, eetm2tr, eetm2fp, eetm2sp, eetm2o1, eetm2o2, eetm2p1, eetm2p2));
        eventDetailDataList.add(new EventDetailData(essencepi, essencename, essenceday, essencetime, essenceloc, essencedesc, essencetm, essencetr, essencefp, essencesp, essenceo1, essenceo2, essencep1, essencep2));
        eventDetailDataList.add(new EventDetailData(gamingConsolepi, gamingConsolename, gamingConsoleday, gamingConsoletime, gamingConsoleloc, gamingConsoledesc, gamingConsoletm, gamingConsoletr, gamingConsolefp, gamingConsolesp, gamingConsoleo1, gamingConsoleo2, gamingConsolep1, gamingConsolep2));
        eventDetailDataList.add(new EventDetailData(googleitpi, googleitname, googleitday, googleittime, googleitloc, googleItdesc, googleittm, googleittr, googleitfp, googleitsp, googleito1, googleito2, googleitp1, googleitp2));
        eventDetailDataList.add(new EventDetailData(hirefirepi, hirefirename, hirefireday, hirefiretime, hirefireloc, hireFiredesc, hirefiretm, hirefiretr, hirefirefp, hirefiresp, hirefireo1, hirefireo2, hirefirep1, hirefirep2));
        eventDetailDataList.add(new EventDetailData(hogathonpi, hogathonname, hogathonday, hogathontime, hogathonloc, hogathondesc, hogathontm, hogathontr, hogathonfp, hogathonsp, hogathono1, hogathono2, hogathonp1, hogathonp2));
        eventDetailDataList.add(new EventDetailData(kartitpi, kartitname, kartitday, kartittime, kartitloc, kartitdesc, kartittm, kartittr, kartitfp, kartitsp, kartito1, kartito2, kartitp1, kartitp2));
        eventDetailDataList.add(new EventDetailData(photographypi, photographyname, photographyday, photographytime, photographyloc, photographydesc, photographytm, photographytr, photographyfp, photographysp, photographyo1, photographyo2, photographyp1, photographyp2));
        eventDetailDataList.add(new EventDetailData(potpourripi, potpourriname, potpourriday, potpourritime, potpourriloc, potpourridesc, potpourritm, potpourritr, potpourrifp, potpourrisp, potpourrio1, potpourrio2, potpourrip1, potpourrip2));

        //Special

        eventDataList.add(new EventData(trdplname, (trdplday + " " + trdpltime), trdplpi));

        //Day 1

        eventDataList.add(new EventData(circuitDebugname, (circuitdebugday + " " + circuitdebugtime), circuitdebugpi));
        eventDataList.add(new EventData(corcongname, (corcongday + " " + corcongtime), corcongpi));
        eventDataList.add(new EventData(gamingNFSname, (gamingNFSday + " " + gamingNFStime), gamingNFSpi));
        eventDataList.add(new EventData(gamingCSname, (gamingCSday + " " + gamingCStime), gamingCSpi));
        eventDataList.add(new EventData(gamingCODname, (gamingCODday + " " + gamingCODtime), gamingCODpi));
        eventDataList.add(new EventData(mockGREname, (mockgreday + " " + mockgretime), mockgrepi));
        eventDataList.add(new EventData(mockStockname, (mockstockday + " " + mockstocktime), mockstockpi));
        eventDataList.add(new EventData(patternprintname, (patternprintday + " " + patternprinttime), patternprintpi));
        eventDataList.add(new EventData(techDCname, (techdcday + " " + techdctime), techdcpi));
        eventDataList.add(new EventData(techjamname, (techjamday + " " + techjamtime), techjampi));
        eventDataList.add(new EventData(technogenname, (technogenday + " " + technogentime), technogenpi));
        eventDataList.add(new EventData(techTalkname, (techtalkday + " " + techtalktime), techtalkpi));
        eventDataList.add(new EventData(treasurehuntname, (treasurehuntday + " " + treasurehunttime), treasurehuntpi));

        //Day 2

        eventDataList.add(new EventData(bbroyname, (bbroyday + " " + bbroytime), bbroypi));
        eventDataList.add(new EventData(chuckglidername, (chuckgliderday + " " + chuckglidertime), chuckgliderpi));
        eventDataList.add(new EventData(codingname, (codingday + " " + codingtime), codingpi));
        eventDataList.add(new EventData(cryptorigname, (cryptorigday + " " + cryptorigtime), cryptorigpi));
        eventDataList.add(new EventData(rubikscubename, (rubikscubdeday + " " + rubikscubetime), rubikscubepi));
        eventDataList.add(new EventData(eetm2name, (eetm2day + " " + eetm2time), eetm2pi));
        eventDataList.add(new EventData(essencename, (essenceday + " " + essencetime), essencepi));
        eventDataList.add(new EventData(gamingConsolename, (gamingConsoleday + " " + gamingConsolename), gamingConsolepi));
        eventDataList.add(new EventData(googleitname, (googleitday + " " + googleittime), googleitpi));
        eventDataList.add(new EventData(hirefirename, (hirefireday + " " + hirefiretime), hirefirepi));
        eventDataList.add(new EventData(hogathonname, (hogathonday + " " + hogathontime), hogathonpi));
        eventDataList.add(new EventData(kartitname, (kartitday + " " + kartittime), kartitpi));
        eventDataList.add(new EventData(photographyname, (photographyday + " " + photographytime), photographypi));
        eventDataList.add(new EventData(potpourriname, (potpourriday + " " + potpourritime), potpourripi));


    }
}

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.EventViewHolder> {

    private final List<EventData> eventDataList;
    private final Context mContext;
    private OnItemClickListener mItemClickListener;

    RecyclerViewAdapter(List<EventData> eventDatas, Context context) {
        this.eventDataList = eventDatas;
        this.mContext = context;
    }


    @Override
    public RecyclerViewAdapter.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.EventViewHolder holder, int position) {

        Glide.with(mContext).load(this.eventDataList.get(position).eventPosterId).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.eventPoster) {

            @Override
            protected void setResource(Bitmap resource) {
                super.setResource(resource);
                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        if (palette != null) {
                            Palette.Swatch vibrantColors = palette.getVibrantSwatch();
                            if (vibrantColors != null) {
                                holder.eventDataHolder.setBackgroundColor(vibrantColors.getRgb());
                                holder.eventDataHolder.setAlpha((float) 0.9);
                                holder.eventTitle.setTextColor(vibrantColors.getTitleTextColor());
                                holder.eventTitle.setAlpha((float) 1.0);
                                holder.eventTiming.setTextColor(vibrantColors.getTitleTextColor());
                                holder.eventTiming.setAlpha((float) 1.0);
                            }
                        }
                    }
                });
            }
        });
        holder.eventTitle.setText(this.eventDataList.get(position).eventName);
        holder.eventTiming.setText(this.eventDataList.get(position).eventTimings);
    }

    @Override
    public int getItemCount() {
        return eventDataList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener {

        final TextView eventTitle;
        final TextView eventTiming;
        final ImageView eventPoster;
        final LinearLayout eventDataHolder;


        public EventViewHolder(final View itemView) {
            super(itemView);
            this.eventTitle = (TextView) itemView.findViewById(R.id.title);
            this.eventTiming = (TextView) itemView.findViewById(R.id.timings);
            this.eventPoster = (ImageView) itemView.findViewById(R.id.poster);
            this.eventDataHolder = (LinearLayout) itemView.findViewById(R.id.eventDataHolder);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

    }
}

class EventData {

    final String eventName;
    final String eventTimings;
    final int eventPosterId;

    EventData(String title, String time, int id) {
        this.eventName = title;
        this.eventTimings = time;
        this.eventPosterId = id;
    }
}

class EventDetailData {

    final int posterid;
    final String name;
    final String day;
    final String time;
    final String location;
    final String description;
    final String teammembers;
    final String rate;
    final String firstPrize;
    final String secondPrize;
    final String organizer1;
    final String organizer2;
    final String phone1;
    final String phone2;

    EventDetailData(int posterid, String name, String day, String time, String location, String description, String teammembers, String rate, String firstPrize, String secondPrize, String organizer1, String organizer2, String phone1, String phone2) {

        this.posterid = posterid;
        this.name = name;
        this.day = day;
        this.time = time;
        this.location = location;
        this.description = description;
        this.teammembers = teammembers;
        this.rate = rate;
        this.firstPrize = firstPrize;
        this.secondPrize = secondPrize;
        this.organizer1 = organizer1;
        this.organizer2 = organizer2;
        this.phone1 = phone1;
        this.phone2 = phone2;
    }
}