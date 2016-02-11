package com.biryanistudio.tatvaapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class ScrollingActivity extends AppCompatActivity {

    RecyclerView eventList;
    FloatingActionButton fab;
    List<EventData> eventDataList;
    List<EventDetailData> eventDetailDataList;
    Toolbar toolbarnew;
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        toolbarnew = (Toolbar) findViewById(R.id.toolbarnew);
        setSupportActionBar(toolbarnew);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        initData();
        initDetailData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            setColors();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        eventList = (RecyclerView) findViewById(R.id.eventList);
        eventList.setHasFixedSize(true);
        eventList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        final RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(eventDataList, getApplicationContext());
        recyclerViewAdapter.SetOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

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
                LinearLayout linearLayout = (LinearLayout) relativeLayout.getChildAt(1);
                TextView titleText = (TextView) linearLayout.getChildAt(0);
                TextView timingsText = (TextView) linearLayout.getChildAt(1);

                Pair<View, String> cardPair = Pair.create((View) cardView, "eventCardHolder");
                Pair<View, String> posterPair = Pair.create((View) imageView, "eventPosterHolder");
                Pair<View, String> titlePair = Pair.create((View) titleText, "eventNameHolder");
                Pair<View, String> timingPair = Pair.create((View) timingsText, "eventTimingHolder");

                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ScrollingActivity.this, cardPair, posterPair, titlePair, timingPair);
                ActivityCompat.startActivity(ScrollingActivity.this, intent, activityOptionsCompat.toBundle());
            }
        });
        eventList.setAdapter(recyclerViewAdapter);

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

    private void setColors() {

        Palette.from(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                Palette.Swatch vibrantDark = palette.getDarkVibrantSwatch();
                Palette.Swatch mutedLight = palette.getLightMutedSwatch();

                if (vibrant != null) {
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

                if (mutedLight != null) {
                    eventList.setBackgroundColor(mutedLight.getRgb());
                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {

            Intent i = new Intent(ScrollingActivity.this, AboutActivity.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
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

        //Day 1

        eventDataList.add(new EventData("Circuit Debugging", "Day 1 - 10:00 AM", R.drawable.circuit_debugging));
        eventDataList.add(new EventData("Corporate Conglomerate", "Day 1 - 9:30 AM", R.mipmap.ic_launcher));
        eventDataList.add(new EventData("Gaming PC", "Day 1  - 9:30 AM", R.mipmap.ic_launcher));
        eventDataList.add(new EventData("Tech JAM", "Day 1 - 10:00 AM", R.mipmap.ic_launcher));
        eventDataList.add(new EventData("Mock GRE", "Day 1 - 10:00 AM", R.drawable.mock_gre));
        eventDataList.add(new EventData("Mock Stock", "Day 1 - 1:30 PM", R.mipmap.ic_launcher));
        eventDataList.add(new EventData("Pattern Print", "Day 1 - 9:30 AM", R.drawable.pattern_print));
        eventDataList.add(new EventData("Product Launch", "Day 1 - 9:30 AM", R.mipmap.ic_launcher));
        eventDataList.add(new EventData("Tech DC", "Day 1 - 9:30 AM", R.drawable.tech_dc));
        eventDataList.add(new EventData("Wit'Wiki'", "Day 1 - 10:00 AM", R.mipmap.ic_launcher));
        eventDataList.add(new EventData("Tech Talk", "Day 1 - 10:00 AM", R.drawable.tech_talk));
        eventDataList.add(new EventData("Treasure Hunt", "Day 1 - 2:30 PM", R.drawable.treasure_hunt));

        //Special

        eventDataList.add(new EventData("The Raghu Dixit Project Live", "Day 2 - 6:00 PM", R.mipmap.ic_launcher));

        //Day 2

        eventDataList.add(new EventData("BB Roy", "Day 2 - 10:00 AM", R.mipmap.ic_launcher));
        eventDataList.add(new EventData("Chuck Glider", "Day 2 - 9:30 AM", R.drawable.chuck_glider));
        eventDataList.add(new EventData("Coding", "Day 2 - 10:00 AM", R.drawable.coding));
        eventDataList.add(new EventData("Crypt-O-Rig", "Day 2 - 1:00 PM", R.mipmap.ic_launcher));
        eventDataList.add(new EventData("E=TM2", "Day 2 - 10:00 AM", R.mipmap.ic_launcher));
        eventDataList.add(new EventData("Essence", "Day 2 - 10:00 AM", R.drawable.essence));
        eventDataList.add(new EventData("Gaming Console", "Day 2  - 9:30 AM", R.mipmap.ic_launcher));
        eventDataList.add(new EventData("Google-it", "Day 2 - 3:00 PM", R.drawable.google_it));
        eventDataList.add(new EventData("Hire/Fire", "Day 2 - 10:00 AM", R.drawable.hire_fire));
        eventDataList.add(new EventData("Hogathon", "Day 2 - 10:00 AM", R.drawable.hogathon));
        eventDataList.add(new EventData("Kart-it", "Day 2 - 2:00 PM", R.drawable.kart_it));
        eventDataList.add(new EventData("Photography", "Day 2 - 9:00 AM", R.drawable.photography));
        eventDataList.add(new EventData("Potpourri", "Day 2 - 9:00 AM", R.drawable.potpourri));
        eventDataList.add(new EventData("Cube On 2016", "Day 2 - 9:30 AM", R.drawable.cube_on));
    }

    private void initDetailData() {

        eventDetailDataList = new ArrayList<>();

        //Names

        String techDCname = "Tech DC";
        String technogenname = "Wit'Wiki'";
        String circuitDebugname = "Circuit Debugging";
        String corcongname = "Corporate Conglomerate";
        String mockStockname = "Mock Stock";
        String treasurehuntname = "Treasure Hunt";
        String techjamname = "Tech JAM";
        String techTalkname = "Tech Talk";
        String gamingPCname = "Gaming PC";
        String productLaunchname = "Product Launch";
        String mockGREname = "Mock GRE";
        String patternprintname = "Pattern Print";
        String eetm2name = "E=TM2";
        String essencename = "Essence";
        String kartitname = "Kart-it";
        String codingname = "Coding";
        String potpourriname = "Potpourri";
        String cryptorigname = "Crypt-O-Rig";
        String googleitname = "Google-it";
        String chuckglidername = "Chuck Glider";
        String photographyname = "Photography";
        String hirefirename = "Hire/Fire";
        String rubikscubename = "Cube On 2016";
        String trdplname = "The Raghu Dixit Prooject Live";
        String bbroyname = "BB Roy";
        String hogathonname = "Hogathon";
        String gamingConsolename = "Gaming Console";

        //Descriptions

        String techDCdesc = "Dumb charades with a twist! Get your team members to guess words you use everyday in your classrooms and labs - and more!";
        String technogendesc = "A Quiz that will test your IT & General Knowledge trivia thoroughly. With innovative rounds, we promise to make this a technical quiz unlike any other. Will be held in two rounds – written Prelims and a final. 6 teams will make it to the finals from the prelims.";
        String circuitDebugdesc = "Become the “ter-bug-ator” ! This an opportunity to showcase your circuit debugging skills by fixing simple bugs in a circuit, thus tweaking your way to victory. It's time to brush up on your electronics skills, because the event will have basic and easy rounds that will not require you to scratch that pretty brain of yours!";
        String corporateCongdesc = "A collaboration between David and Goliath! CC is a one-of-a-kind event providing a very realistic idea of how the corporate world works. Teams are divided into councils and assigned with companies accordingly. Giants and Start-ups need to form a symbiotic collaboration, bring out a product and present it.The final round will require the selected teams to face a jury which will decide the ultimate winner. ";
        String mockStockdesc = "Are you the Wolf of Wall Street? We've got here an event that tries to emulate a stock market with real time fluctuations in the stock price and news relating to the company. The original companies that we've created and the companies’ details will be given to the contestants on the day of the event.";
        String treasureHuntdesc = "Captain Jack Sparrow, The Famous Five, Indiana Jones and Robert Langdon all have one thing in common - an uncommon curiosity for hidden treasures! If you're the kind that likes solving clues and finding Easter Eggs (wink wink!) and if you think you know BNMIT well enough (or don't) you should totally take part in this Around-the-campus-in-60-minutes Amazing Race, crafted exclusively for BNM students!";
        String techTalkdesc = "A talk to remember. Participants would be given a published paper in a domain of their choice - EC, CS or Mechanical - a few days before the event and they would have to prepare for it and present it. Basically, participants would be presenting published papers, unless they have published papers of their own, in which case we would be more than happy to let them present it!";
        String techjamMdesc = "For people who can rant about Android L and the likes! A panel game in which the contestants are challenged to speak for one minute without hesitation, deviation or repetition of any subject that comes up. The final round is Air Crash, in which contestants will be assigned fun sci-fi characters and will be given their details, and participants would have to justify their survival.";
        String gamingPCdesc = "One of the most exciting events of Tatva! The rush of adrenaline and the nail-biting finishes make this one of the most anticipated events. We have classic PC games like Call of Duty, Counter Strike and Need for Speed. Come one, Come all.";
        String productLaunchdesc = "null";
        String mockGREdesc = "Planning to chase your dreams? Come take a step forward and give these challenging exams a shot to know where you stand! Tag your friends along to make it more competitive! Exciting cash prizes await the winners!";
        String patternPrintdesc = "Print away! Teams will have to create a HackerRank profile. They will be provided with a set of patterns which they have to code in any language of their choice to obtain that pattern as the output.";
        String eETM2desc = "An Entertainment Quiz. TV shows, music and movies. By this, we solemly swear that we're upto no good. Why? Cause this is like a box of chocolates. You never know what you're gonna get. For this is our design. So you'd better kick your can all over the place.";
        String essencedesc = "Petrol Heads Unite! This is a quiz about all things fast with four wheels. Test your speed and handling with 2 rounds - Prelims and a finals consisting of 6 teams.";
        String kartitdesc = "Want it? KART IT! The participants will be given a set of product images from an e-commerce website. They will have to search for the product on Flipkart using the image given as reference.";
        String codingdesc = "Attention Coders! Here's a chance for you to test your skills! The first round,has 30 questions to be answered. The second round will have problem statements for which teams have to code to solve it. You can code in any language, as long as you get an output!";
        String potpourridesc = "Dumb Charades, Pictionary and everything else that you do to pass time - think you're good at it? Think you can name famous movies and TV Shows and personalities off the top of your head? Then why don't you make some money out of it? Tag your friends along and register for a super fun event!";
        String cryptORigdesc = "Sherlock Holmes or Hercule Poirot - is one of them your detective heroes? Do you find it amusing to solve perplexing mysteries? If your answer is yes, sign up for Crypt-o-rig, a mind boggling event (literally!) where participants are required to solve the black box mystery and they, in turn win a clue to the final round with their detective skills. Only, here, it's an exciting walk through circuits and codes!";
        String googleItdesc = "Are you good at googling stuff? Then this is the right event for you! You'll be given an image of a webpage, and you need to find the exact URL of the webpage by using the clues in the image. The team which gets the maximum URLs right out of 15 such questions, will be declared the winner.";
        String chuckGliderdesc = "Chuck gliding. Learn, build and fly your own chuck glider. You will only need your enthusiasm. All materials will be provided with the workshop.";
        String photographydesc = "Make a statement without saying a word! The participants will be given a topic on the day of the event and they will have to click a picture using DSLR/Digital camera and submit within 3 hours. No post processing will be allowed.";
        String hireFiredesc = "Clearing interviews has never been easy - but we'll make it slightly easier for you! In Hire or Fire, participants will undergo a group discussion round and will have to clear it, following which they will face judges in the Personal Interview round.";
        String rubiksCubedesc = "Cube On 2016, the first ever speed cubing event in Tatva 2016, with WCA recognition. Cubers from all over the country are welcome to showcase their speed cubing skills at this breathtaking event. Age is never a limitation to challenge the world of cubers here. If you're one of those cubers who can solve the Rubik's Cube with ease and are willing to prove your talent by competing with the best cubers in India then join us at Cube On 2016.";
        String bbRoydesc = "An event for the Sherlock \"Ohms\"! There are two kinds of resistors - BBROY of Great Britain had a Very Good Wife or BBROY Goes to Bombay Via Gate Way! Whichever side you're on, you'd better stick to it because BBROY is the guy helping you out in the two rounds of this resistor-filled contest!";
        String trdpldesc = "null";
        String hogathondesc = "Eat! Consume! Gobble! Devour! Smack! Crunch! Yum! Gollop! Guzzle! Gulp! Do we need add more? If you've got an appetite that can overtake the food we have on offer, we challenge you to prove it! Come on, you can't say no to food!";
        String gamingConsoledesc = "For the first time ever, BNMIT plays host to console wars! Get ready to play one of the most exciting games on the PlayStation - FIFA 16! You think you can keep up?";

        //Days

        String technogenday = "26th February, 2016";
        String techdcday = "26th February, 2016";
        String productlaunchday = "26th February, 2016";
        String patternprintday = "26th February, 2016";
        String circuitdebugday = "26th February, 2016";
        String techjamday = "26th February, 2016";
        String techtalkday = "26th February, 2016";
        String corcongday = "26th February, 2016";
        String mockgreday = "26th February, 2016";
        String gamingPCday = "26th February, 2016";
        String mockstockday = "26th February, 2016";
        String treasurehuntday = "26th February, 2016";
        String essenceday = "27th February, 2016";
        String eetm2day = "27th February, 2016";
        String potpourriday = "27th February, 2016";
        String kartitday = "27th February, 2016";
        String googleitday = "27th February, 2016";
        String hirefireday = "27th February, 2016";
        String photographyday = "27th February, 2016";
        String bbroyday = "27th February, 2016";
        String cryptorigday = "27th February, 2016";
        String codingday = "27th February, 2016";
        String chuckgliderday = "27th February, 2016";
        String rubikscubdeday = "27th February, 2016";
        String hogathonday = "27th February, 2016";
        String trdplday = "27th February, 2016";
        String gamingConsoleday = "27th February, 2016";

        //Timings

        String technogentime = "9:30 AM";
        String techdctime = "9:30 AM";
        String productlaunchtime = "null";
        String patternprinttime = "9:30 AM";
        String circuitdebugtime = "9:30 AM";
        String techjamtime = "10:00 AM";
        String techtalktime = "9:30 AM";
        String corcongtime = "9:30 AM";
        String mockgretime = "9:30 AM";
        String gamingPCtime = "9:30 AM";
        String mockstocktime = "1:30 PM";
        String treasurehunttime = "3:00 PM";
        String essencetime = "9:30 AM";
        String eetm2time = "9:30 AM";
        String potpourritime = "9:00 AM";
        String kartittime = "1:30 PM";
        String googleittime = "2:30 PM";
        String hirefiretime = "9:30 AM";
        String photographytime = "9:30 AM";
        String bbroytime = "9:300 AM";
        String cryptorigtime = "12:30 PM";
        String codingtime = "9:30 AM";
        String chuckglidertime = "9:00 AM";
        String rubikscubetime = "9:00 AM";
        String hogathontime = "9:30 AM";
        String trdpltime = "null";
        String gamingConsoletime = "9:30 AM";

        //Location

        String technogenloc = "Auditorium, Auditorium Building";
        String techdcloc = "Seminar Hall, Main Building";
        String patternprintloc = "2 labs, Main Building";
        String productlaunchloc = "null";
        String circuitdebugloc = "2 classrooms, 1 lab, New Building";
        String techjamloc = "MBA Seminar Hall, New Building";
        String techtalkloc = "Classroom, New Building";
        String corcongloc = "Srishti Sambhrama Hall, Auditorium Building";
        String mockgreloc = "2 labs, New Building";
        String gamingPCloc = "labs, New Building";
        String mockstockloc = "2nd floor labs, Main Building";
        String treasurehuntloc = "2nd floor labs, Main Building";
        String essenceloc = "MBA Seminar Hall, New Building";
        String eetm2loc = "Auditorium, Auditorium Building";
        String potpourriloc = "Seminar Hall, Main Building";
        String kartitloc = "5 labs, Main Building";
        String googleitloc = "5 labs, Main Building";
        String hirefireloc = "classrooms, Auditorium Building";
        String photographyloc = "Complete college campus";
        String bbroyloc = "2 labs, New Building";
        String cryptorigloc = "AEC & LD Labs, New Building";
        String codingloc = "2 labs, Main Building";
        String chuckgliderloc = "Canteen Area, Auditorium Building";
        String rubikscubeloc = "Srishti Sambhrama Hall, Auditorium Building";
        String hogathonloc = "Quadrangle, Main Building";
        String trdplloc = "Auditorium, Auditorium Building";
        String gamingConsoleloc = "null";

        //Team Members

        String techdctm = "Team of 3";
        String technogentm = "Team of 3";
        String circuitdebugtm = "Team of 2";
        String corcongtm = "Team of 2";
        String mockstocktm = "Team of 2";
        String treasurehunttm = "Team of 3";
        String techjamtm = "Team of 1";
        String techtalktm = "Team of 1";
        String gamingPCtm = "null";
        String productlaunchtm = "null";
        String mockgretm = "Team of 1";
        String patternprinttm = "Team of 2";
        String eetm2tm = "Team of 2";
        String essencetm = "Team of 2";
        String kartittm = "Team of 2";
        String codingtm = "Team of 2";
        String potpourritm = "Team of 3";
        String cryptorigtm = "Team of 2";
        String googleittm = "Team of 2";
        String chuckglidertm = "Team of 2 to 4";
        String photographytm = "Team of 1";
        String hirefiretm = "Team of 1";
        String rubikscubetm = "Team of 1";
        String bbroytm = "Team of 2";
        String trdpltm = "null";
        String hogathontm = "Team of 1";
        String gamingConsoletm = "Team of 1";

        //Ticket Rate

        String techdctr = "\u20B990/team";
        String technogentr = "\u20B9120/team";
        String circuitdebugtr = "\u20B960/team";
        String corcongtr = "\u20B9100/team";
        String mockstocktr = "\u20B980/team";
        String treasurehunttr = "\u20B9120/team";
        String techjamtr = "\u20B960/team";
        String techtalktr = "null";
        String gamingPCtr = "NFS: \u20B950/team\nCoD: \u20B9200/team\nCS: \u20B9200/team\nDoTA 2: \u20B9300/team";
        String productlaunchtr = "null";
        String mockgretr = "\u20B9100/team";
        String patternprinttr = "\u20B9100/team";
        String eetm2tr = "\u20B980/team";
        String essencetr = "\u20B980/team";
        String kartittr = "\u20B960/team";
        String codingtr = "\u20B9100/team";
        String potpourritr = "\u20B9120/team";
        String cryptorigtr = "\u20B990/team";
        String googleittr = "\u20B960/team";
        String chuckglidertr = "\u20B9400/team";
        String photographytr = "Professional: \u20B9100/team\nAmateur: \u20B970/team";
        String hirefiretr = "\u20B950/team";
        String rubikscubetr = "null";
        String bbroytr = "\u20B950/team";
        String trdpltr = "null";
        String hogathontr = "\u20B950/team";
        String gamingConsoletr = "\u20B9100/team";

        //First Prize

        String techdcfp = "null";
        String technogenfp = "null";
        String circuitdebugfp = "null";
        String corcongfp = "null";
        String mockstockfp = "null";
        String treasurehuntfp = "null";
        String techjamfp = "null";
        String techtalkfp = "null";
        String gamingPCfp = "null";
        String productlaunchfp = "null";
        String mockgrefp = "null";
        String patternprintfp = "null";
        String eetm2fp = "null";
        String essencefp = "null";
        String kartitfp = "null";
        String codingfp = "null";
        String potpourrifp = "null";
        String cryptorigfp = "null";
        String googleitfp = "null";
        String chuckgliderfp = "null";
        String photographyfp = "null";
        String hirefirefp = "null";
        String rubikscubefp = "null";
        String bbroyfp = "null";
        String trdplfp = "null";
        String hogathonfp = "null";
        String gamingConsolefp = "null";

        //Second Prize

        String techdcsp = "null";
        String technogensp = "null";
        String circuitdebugsp = "null";
        String corcongsp = "null";
        String mockstocksp = "null";
        String treasurehuntsp = "null";
        String techjamsp = "null";
        String techtalksp = "null";
        String gamingPCsp = "null";
        String productlaunchsp = "null";
        String mockgresp = "null";
        String patternprintsp = "null";
        String eetm2sp = "null";
        String essencesp = "null";
        String kartitsp = "null";
        String codingsp = "null";
        String potpourrisp = "null";
        String cryptorigsp = "null";
        String googleitsp = "null";
        String chuckglidersp = "null";
        String photographysp = "null";
        String hirefiresp = "null";
        String rubikscubesp = "null";
        String bbroysp = "null";
        String trdplsp = "null";
        String hogathonsp = "null";
        String gamingConsolesp = "null";

        //Organizer1

        String techdco1 = "Vidya";
        String technogeno1 = "Varun";
        String circuitdebugo1 = "Karthik";
        String corcongo1 = "Keerthan";
        String mockstocko1 = "Shreyas";
        String treasurehunto1 = "Kirthan";
        String techjamo1 = "Suhas";
        String techtalko1 = "Sharanya";
        String gamingPCo1 = "Shrey";
        String productlauncho1 = "null";
        String mockgreo1 = "Vidya";
        String patternprinto1 = "Sugosh";
        String eetm2o1 = "Gayathri";
        String essenceo1 = "Tejas";
        String kartito1 = "Pranav";
        String codingo1 = "Sugosh";
        String potpourrio1 = "Vivek";
        String cryptorigo1 = "Bindhiya Shree";
        String googleito1 = "Kirthan";
        String chuckglidero1 = "Rajat";
        String photographyo1 = "Pranav";
        String hirefireo1 = "MBA Department";
        String rubikscubeo1 = "Karan";
        String bbroyo1 = "Amulya";
        String trdplo1 = "null";
        String hogathono1 = "null";
        String gamingConsoleo1 = "null";

        //Organizer2

        String techdco2 = "Sthuthie";
        String technogeno2 = "Suraj";
        String circuitdebugo2 = "Vinayak";
        String corcongo2 = "Gaurav";
        String mockstocko2 = "null";
        String treasurehunto2 = "Anasuya";
        String techjamo2 = "Srinishi";
        String techtalko2 = "Shwetha";
        String gamingPCo2 = "null";
        String productlauncho2 = "null";
        String mockgreo2 = "null";
        String patternprinto2 = "Dheeraj";
        String eetm2o2 = "null";
        String essenceo2 = "Sudeep";
        String kartito2 = "Meghana";
        String codingo2 = "null";
        String potpourrio2 = "null";
        String cryptorigo2 = "Harshapriya";
        String googleito2 = "Akshatha";
        String chuckglidero2 = "Aditi";
        String photographyo2 = "Aakaash";
        String hirefireo2 = "null";
        String rubikscubeo2 = "null";
        String bbroyo2 = "Ranjitha";
        String trdplo2 = "null";
        String hogathono2 = "null";
        String gamingConsoleo2 = "null";

        //Phone 1

        String techdcp1 = "9535693620";
        String technogenp1 = "8861469332";
        String circuitdebugp1 = "8861670102";
        String corcongp1 = "9845041734";
        String mockstockp1 = "9880085543";
        String treasurehuntp1 = "8123995823";
        String techjamp1 = "9538946191";
        String techtalkp1 = "null";
        String gamingPCp1 = "null";
        String productlaunchp1 = "null";
        String mockgrep1 = "9916438368";
        String patternprintp1 = "9880085543";
        String eetm2p1 = "null";
        String essencep1 = "9035211489";
        String kartitp1 = "null";
        String codingp1 = "9880085543";
        String potpourrip1 = "null";
        String cryptorigp1 = "9035184719";
        String googleitp1 = "null";
        String chuckgliderp1 = "null";
        String photographyp1 = "null";
        String hirefirep1 = "null";
        String rubikscubep1 = "8197526224";
        String bbroyp1 = "9482480881";
        String trdplp1 = "null";
        String hogathonp1 = "null";
        String gamingConsolep1 = "null";

        //Phone 2

        String techdcp2 = "9845772581";
        String technogenp2 = "9886107706";
        String circuitdebugp2 = "8147162287";
        String corcongp2 = "7795583007";
        String mockstockp2 = "8861469332";
        String treasurehuntp2 = "9632508127";
        String techjamp2 = "8970970532";
        String techtalkp2 = "null";
        String gamingPCp2 = "null";
        String productlaunchp2 = "null";
        String mockgrep2 = "null";
        String patternprintp2 = "9483918712";
        String eetm2p2 = "null";
        String essencep2 = "9538213512";
        String kartitp2 = "null";
        String codingp2 = "null";
        String potpourrip2 = "null";
        String cryptorigp2 = "9986956094";
        String googleitp2 = "null";
        String chuckgliderp2 = "null";
        String photographyp2 = "9902512464";
        String hirefirep2 = "null";
        String rubikscubep2 = "null";
        String bbroyp2 = "9880783959";
        String trdplp2 = "null";
        String hogathonp2 = "null";
        String gamingConsolep2 = "null";

        //Poster ID

        int techdcpi = R.drawable.tech_dc;
        int technogenpi = R.mipmap.ic_launcher;
        int circuitdebugpi = R.drawable.circuit_debugging;
        int corcongpi = R.mipmap.ic_launcher;
        int mockstockpi = R.mipmap.ic_launcher;
        int treasurehuntpi = R.drawable.treasure_hunt;
        int techjampi = R.mipmap.ic_launcher;
        int techtalkpi = R.drawable.tech_talk;
        int gamingPCpi = R.mipmap.ic_launcher;
        int productlaunchpi = R.mipmap.ic_launcher;
        int mockgrepi = R.drawable.mock_gre;
        int patternprintpi = R.drawable.pattern_print;
        int eetm2pi = R.mipmap.ic_launcher;
        int essencepi = R.drawable.essence;
        int kartitpi = R.drawable.kart_it;
        int codingpi = R.drawable.coding;
        int potpourripi = R.drawable.potpourri;
        int cryptorigpi = R.mipmap.ic_launcher;
        int googleitpi = R.drawable.google_it;
        int chuckgliderpi = R.drawable.chuck_glider;
        int photographypi = R.drawable.photography;
        int hirefirepi = R.drawable.hire_fire;
        int rubikscubepi = R.drawable.cube_on;
        int bbroypi = R.mipmap.ic_launcher;
        int trdplpi = R.mipmap.ic_launcher;
        int hogathonpi = R.drawable.hogathon;
        int gamingConsolepi = R.mipmap.ic_launcher;

        //Making ArrayList

        //Day 1

        eventDetailDataList.add(new EventDetailData(circuitdebugpi, circuitDebugname, circuitdebugday, circuitdebugtime, circuitdebugloc, circuitDebugdesc, circuitdebugtm, circuitdebugtr, circuitdebugfp, circuitdebugsp, circuitdebugo1, circuitdebugo2, circuitdebugp1, circuitdebugp2));
        eventDetailDataList.add(new EventDetailData(corcongpi, corcongname, corcongday, corcongtime, corcongloc, corporateCongdesc, corcongtm, corcongtr, corcongfp, corcongsp, corcongo1, corcongo2, corcongp1, corcongp2));
        eventDetailDataList.add(new EventDetailData(gamingPCpi, gamingPCname, gamingPCday, gamingPCtime, gamingPCloc, gamingPCdesc, gamingPCtm, gamingPCtr, gamingPCfp, gamingPCsp, gamingPCo1, gamingPCo2, gamingPCp1, gamingPCp2));
        eventDetailDataList.add(new EventDetailData(techjampi, techjamname, techjamday, techjamtime, techjamloc, techjamMdesc, techjamtm, techjamtr, techjamfp, techjamsp, techjamo1, techjamo2, techjamp1, techjamp2));
        eventDetailDataList.add(new EventDetailData(mockgrepi, mockGREname, mockgreday, mockgretime, mockgreloc, mockGREdesc, mockgretm, mockgretr, mockgrefp, mockgresp, mockgreo1, mockgreo2, mockgrep1, mockgrep2));
        eventDetailDataList.add(new EventDetailData(mockstockpi, mockStockname, mockstockday, mockstocktime, mockstockloc, mockStockdesc, mockstocktm, mockstocktr, mockstockfp, mockstocksp, mockstocko1, mockstocko2, mockstockp1, mockstockp2));
        eventDetailDataList.add(new EventDetailData(patternprintpi, patternprintname, patternprintday, patternprinttime, patternprintloc, patternPrintdesc, patternprinttm, patternprinttr, patternprintfp, patternprintsp, patternprinto1, patternprinto2, patternprintp1, patternprintp2));
        eventDetailDataList.add(new EventDetailData(productlaunchpi, productLaunchname, productlaunchday, productlaunchtime, productlaunchloc, productLaunchdesc, productlaunchtm, productlaunchtr, productlaunchfp, productlaunchsp, productlauncho1, productlauncho2, productlaunchp1, productlaunchp2));
        eventDetailDataList.add(new EventDetailData(techdcpi, techDCname, techdcday, techdctime, techdcloc, techDCdesc, techdctm, techdctr, techdcfp, techdcsp, techdco1, techdco2, techdcp1, techdcp2));
        eventDetailDataList.add(new EventDetailData(technogenpi, technogenname, technogenday, technogentime, technogenloc, technogendesc, technogentm, technogentr, technogenfp, technogensp, technogeno1, technogeno2, technogenp1, technogenp2));
        eventDetailDataList.add(new EventDetailData(techtalkpi, techTalkname, techtalkday, techtalktime, techtalkloc, techTalkdesc, techtalktm, techtalktr, techtalkfp, techtalksp, techtalko1, techtalko2, techtalkp1, techtalkp2));
        eventDetailDataList.add(new EventDetailData(treasurehuntpi, treasurehuntname, treasurehuntday, treasurehunttime, treasurehuntloc, treasureHuntdesc, treasurehunttm, treasurehunttr, treasurehuntfp, treasurehuntsp, treasurehunto1, treasurehunto2, treasurehuntp1, treasurehuntp2));

        //Special Event

        eventDetailDataList.add(new EventDetailData(trdplpi, trdplname, trdplday, trdpltime, trdplloc, trdpldesc, trdpltm, trdpltr, trdplfp, trdplsp, trdplo1, trdplo2, trdplp1, trdplp2));

        //Day 2

        eventDetailDataList.add(new EventDetailData(bbroypi, bbroyname, bbroyday, bbroytime, bbroyloc, bbRoydesc, bbroytm, bbroytr, bbroyfp, bbroysp, bbroyo1, bbroyo2, bbroyp1, bbroyp2));
        eventDetailDataList.add(new EventDetailData(chuckgliderpi, chuckglidername, chuckgliderday, chuckglidertime, chuckgliderloc, chuckGliderdesc, chuckglidertm, chuckglidertr, chuckgliderfp, chuckglidersp, chuckglidero1, chuckglidero2, chuckgliderp1, chuckgliderp2));
        eventDetailDataList.add(new EventDetailData(codingpi, codingname, codingday, codingtime, codingloc, codingdesc, codingtm, codingtr, codingfp, codingsp, codingo1, codingo2, codingp1, codingp2));
        eventDetailDataList.add(new EventDetailData(cryptorigpi, cryptorigname, cryptorigday, cryptorigtime, cryptorigloc, cryptORigdesc, cryptorigtm, cryptorigtr, cryptorigfp, cryptorigsp, cryptorigo1, cryptorigo2, cryptorigp1, cryptorigp2));
        eventDetailDataList.add(new EventDetailData(eetm2pi, eetm2name, eetm2day, eetm2time, eetm2loc, eETM2desc, eetm2tm, eetm2tr, eetm2fp, eetm2sp, eetm2o1, eetm2o2, eetm2p1, eetm2p2));
        eventDetailDataList.add(new EventDetailData(essencepi, essencename, essenceday, essencetime, essenceloc, essencedesc, essencetm, essencetr, essencefp, essencesp, essenceo1, essenceo2, essencep1, essencep2));
        eventDetailDataList.add(new EventDetailData(gamingConsolepi, gamingConsolename, gamingConsoleday, gamingConsoletime, gamingConsoleloc, gamingConsoledesc, gamingConsoletm, gamingConsoletr, gamingConsolefp, gamingConsolesp, gamingConsoleo1, gamingConsoleo2, gamingConsolep1, gamingConsolep2));
        eventDetailDataList.add(new EventDetailData(googleitpi, googleitname, googleitday, googleittime, googleitloc, googleItdesc, googleittm, googleittr, googleitfp, googleitsp, googleito1, googleito2, googleitp1, googleitp2));
        eventDetailDataList.add(new EventDetailData(hirefirepi, hirefirename, hirefireday, hirefiretime, hirefireloc, hireFiredesc, hirefiretm, hirefiretr, hirefirefp, hirefiresp, hirefireo1, hirefireo2, hirefirep1, hirefirep2));
        eventDetailDataList.add(new EventDetailData(hogathonpi, hogathonname, hogathonday, hogathontime, hogathonloc, hogathondesc, hogathontm, hogathontr, hogathonfp, hogathonsp, hogathono1, hogathono2, hogathonp1, hogathonp2));
        eventDetailDataList.add(new EventDetailData(kartitpi, kartitname, kartitday, kartittime, kartitloc, kartitdesc, kartittm, kartittr, kartitfp, kartitsp, kartito1, kartito2, kartitp1, kartitp2));
        eventDetailDataList.add(new EventDetailData(photographypi, photographyname, photographyday, photographytime, photographyloc, photographydesc, photographytm, photographytr, photographyfp, photographysp, photographyo1, photographyo2, photographyp1, photographyp2));
        eventDetailDataList.add(new EventDetailData(potpourripi, potpourriname, potpourriday, potpourritime, potpourriloc, potpourridesc, potpourritm, potpourritr, potpourrifp, potpourrisp, potpourrio1, potpourrio2, potpourrip1, potpourrip2));
        eventDetailDataList.add(new EventDetailData(rubikscubepi, rubikscubename, rubikscubdeday, rubikscubetime, rubikscubeloc, rubiksCubedesc, rubikscubetm, rubikscubetr, rubikscubefp, rubikscubesp, rubikscubeo1, rubikscubeo2, rubikscubep1, rubikscubep2));
    }
}

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.EventViewHolder> {

    OnItemClickListener mItemClickListener;
    List<EventData> eventDataList;
    Context mContext;

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
                                holder.eventTitle.setTextColor(vibrantColors.getTitleTextColor());
                                holder.eventTiming.setTextColor(vibrantColors.getTitleTextColor());
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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnClickListener {

        TextView eventTitle;
        TextView eventTiming;
        ImageView eventPoster;
        CardView eventCard;
        LinearLayout eventDataHolder;


        public EventViewHolder(final View itemView) {
            super(itemView);
            this.eventTitle = (TextView) itemView.findViewById(R.id.title);
            this.eventTiming = (TextView) itemView.findViewById(R.id.timings);
            this.eventCard = (CardView) itemView.findViewById(R.id.card);
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

    String eventName;
    String eventTimings;
    int eventPosterId;

    EventData(String title, String time, int id) {
        this.eventName = title;
        this.eventTimings = time;
        this.eventPosterId = id;
    }
}

class EventDetailData {

    int posterid;
    String name;
    String day;
    String time;
    String location;
    String description;
    String teammembers;
    String rate;
    String firstPrize;
    String secondPrize;
    String organizer1;
    String organizer2;
    String phone1;
    String phone2;

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