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

                Pair<View, String> cardPair = Pair.create((View) cardView, "eventCardHolder");
                Pair<View, String> posterPair = Pair.create((View) imageView, "eventPosterHolder");
                //noinspection unchecked
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ScrollingActivity.this, cardPair, posterPair);
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
        eventDetailDataList = new ArrayList<>();

        //Names

        String techDCname = "Tech DC";
        String technogenname = "Technogen";
        String circuitDebugname = "Circuit Debugging";
        String corcongname = "Corporate Conglomerate";
        String mockStockname = "Mock Stock";
        String treasurehuntname = "Treasure Hunt";
        String techjamname = "Tech JAM";
        String techTalkname = "Tech Talk";
        String gamingPCname = "Gaming PC";
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
        String trdplname = "The Raghu Dixit Project Live";
        String bbroyname = "BB Roy";
        String hogathonname = "Hogathon";
        String gamingConsolename = "Gaming Console";

        //Descriptions

        String techDCdesc = "Dumb charades with a twist! Get your team members to guess words you use everyday in your classrooms and labs - and more!";
        String technogendesc = "A Quiz that will test your IT & General Knowledge trivia thoroughly. With innovative rounds, we promise to make this a technical quiz unlike any other. Will be held in two rounds – written Prelims and a final. 6 teams will make it to the finals from the prelims.";
        String circuitDebugdesc = "Become the “ter-bug-ator” ! This is an opportunity to showcase your circuit debugging skills by fixing simple bugs in a circuit, thus tweaking your way to victory. It's time to brush up on your electronics skills, because the event will have basic and easy rounds that will not require you to scratch that pretty brain of yours!";
        String corporateCongdesc = "A collaboration between David and Goliath! CC is a one-of-a-kind event providing a very realistic idea of how the corporate world works. Teams are divided into councils and assigned with companies accordingly. Giants and Start-ups need to form a symbiotic collaboration, bring out a product and present it.The final round will require the selected teams to face a jury which will decide the ultimate winner. ";
        String mockStockdesc = "Are you the Wolf of Wall Street? We've got here an event that tries to emulate a stock market with real time fluctuations in the stock price and news relating to the company. The original companies that we've created and the companies’ details will be given to the contestants on the day of the event.";
        String treasureHuntdesc = "Captain Jack Sparrow, The Famous Five, Indiana Jones and Robert Langdon all have one thing in common - an uncommon curiosity for hidden treasures! If you're the kind that likes solving clues and finding Easter Eggs (wink wink!) and if you think you know BNMIT well enough (or don't) you should totally take part in this Around-the-campus-in-60-minutes Amazing Race, crafted exclusively for BNM students!";
        String techTalkdesc = "A talk to remember. Participants would be given a published paper in a domain of their choice - EC, CS or Mechanical - a few days before the event and they would have to prepare for it and present it. Basically, participants would be presenting published papers, unless they have published papers of their own, in which case we would be more than happy to let them present it!";
        String techjamMdesc = "For people who can rant about Android L and the likes! A panel game in which the contestants are challenged to speak for one minute without hesitation, deviation or repetition of any subject that comes up. The final round is Air Crash, in which contestants will be assigned fun sci-fi characters and will be given their details, and participants would have to justify their survival.";
        String gamingPCdesc = "One of the most exciting events of Tatva! The rush of adrenaline and the nail-biting finishes make this one of the most anticipated events. We have classic PC games like Call of Duty, Counter Strike and Need for Speed. Come one, Come all.";
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
        String trdpldesc = "We present to you, a man who, along with his band, has performed and enthralled audiences not just in Bengaluru, not just in India, but all across the world! Yes, he's the man who has mesmerized us with songs like ‘Munjaane manjalli’ and ‘Gudugudiya’. We're talking about inimitable, nammellara acchu mecchina RAGHU DIXIT! And we’re just so, so proud to be making this announcement. Make sure you have your seat reserved, because nothing can be more gratifying than having The Raghu Dixit Project perform to a packed audotorium. It's all happening at TATVA 2016! We did say we’re making this Tatva better than ever, didn’t we? See you all there!";
        String hogathondesc = "Eat! Consume! Gobble! Devour! Smack! Crunch! Yum! Gollop! Guzzle! Gulp! Do we need add more? If you've got an appetite that can overtake the food we have on offer, we challenge you to prove it! Come on, you can't say no to food!";
        String gamingConsoledesc = "For the first time ever, BNMIT plays host to console wars! Get ready to play one of the most exciting games on the PlayStation - FIFA 16! You think you can keep up?";

        //Days

        String technogenday = "26th February, 2016";
        String techdcday = "26th February, 2016";
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
        String trdplday = "26th February, 2016";
        String gamingConsoleday = "27th February, 2016";

        //Timings

        String technogentime = "9:30 AM";
        String techdctime = "9:30 AM";
        String patternprinttime = "9:30 AM";
        String circuitdebugtime = "9:30 AM";
        String techjamtime = "9:30 AM";
        String techtalktime = "9:30 AM";
        String corcongtime = "9:30 AM";
        String mockgretime = "9:30 AM";
        String gamingPCtime = "9:30 AM";
        String mockstocktime = "1:30 PM";
        String treasurehunttime = "3:00 PM";
        String essencetime = "9:30 AM";
        String eetm2time = "9:30 AM";
        String potpourritime = "9:30 AM";
        String kartittime = "1:30 PM";
        String googleittime = "2:30 PM";
        String hirefiretime = "9:30 AM";
        String photographytime = "9:00 AM";
        String bbroytime = "9:30 AM";
        String cryptorigtime = "12:30 PM";
        String codingtime = "10:00 AM";
        String chuckglidertime = "9:00 AM";
        String rubikscubetime = "9:30 AM";
        String hogathontime = "9:30 AM";
        String trdpltime = "6:00 PM";
        String gamingConsoletime = "9:30 AM";

        //Location

        String technogenloc = "Auditorium, Auditorium Building";
        String techdcloc = "Seminar Hall, Main Building";
        String patternprintloc = "Labs, Main Building";
        String circuitdebugloc = "Classrooms, New Building";
        String techjamloc = "Seminar Hall, New Building";
        String techtalkloc = "Classrooms, New Building";
        String corcongloc = "Srushti Sambhrama Hall, Auditorium Building";
        String mockgreloc = "Labs, New Building";
        String gamingPCloc = "Labs, New Building";
        String mockstockloc = "2nd floor Labs, Main Building";
        String treasurehuntloc = "2nd floor Labs, Main Building";
        String essenceloc = "Seminar Hall, New Building";
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
        String gamingPCtm = "NFS: Team of 1\n" +
                "CoD: Team of 4\n" +
                "CS: Team of 4";
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
        String techtalktr = "\u20B980/team";
        String gamingPCtr = "NFS: \u20B950/team\nCoD: \u20B9200/team\nCS: \u20B9200/team";
        String mockgretr = "\u20B9100/team";
        String patternprinttr = "\u20B960/team";
        String eetm2tr = "\u20B980/team";
        String essencetr = "\u20B980/team";
        String kartittr = "\u20B960/team";
        String codingtr = "\u20B9100/team";
        String potpourritr = "\u20B9120/team";
        String cryptorigtr = "\u20B960/team";
        String googleittr = "\u20B960/team";
        String chuckglidertr = "\u20B9400/team";
        String photographytr = "Professional: \u20B9100/team\nAmateur: \u20B970/team";
        String hirefiretr = "\u20B950/team";
        String rubikscubetr = "Register at: scmustore.com/cubeon2016";
        String bbroytr = "\u20B960/team";
        String trdpltr = "null";
        String hogathontr = "\u20B960/team";
        String gamingConsoletr = "FIFA 16 PS4: \u20B9100/team";

        //First Prize

        String techdcfp = "\u20B93000/team";
        String technogenfp = "\u20B93000/team";
        String circuitdebugfp = "\u20B92000/team";
        String corcongfp = "\u20B94000/team";
        String mockstockfp = "\u20B92000/team";
        String treasurehuntfp = "\u20B93000/team";
        String techjamfp = "\u20B92000/team";
        String techtalkfp = "\u20B92000/team";
        String gamingPCfp = "NFS: \u20B92000/team\nCoD: \u20B94000/team\nCS: \u20B94000/team";
        String mockgrefp = "\u20B92000/team";
        String patternprintfp = "\u20B92000/team";
        String eetm2fp = "\u20B93000/team";
        String essencefp = "\u20B92000/team";
        String kartitfp = "\u20B92000/team";
        String codingfp = "\u20B93000/team";
        String potpourrifp = "\u20B93000/team";
        String cryptorigfp = "\u20B92000/team";
        String googleitfp = "\u20B93000/team";
        String chuckgliderfp = "\u20B94000/team";
        String photographyfp = "Professional: \u20B92000/team\nAmateur:\u20B91500/team ";
        String hirefirefp = "\u20B92000/team";
        String rubikscubefp = "null";
        String bbroyfp = "\u20B92000/team";
        String trdplfp = "null";
        String hogathonfp = "\u20B93000/team";
        String gamingConsolefp = "\u20B93000/team";

        //Second Prize

        String techdcsp = "\u20B91500/team";
        String technogensp = "\u20B91500/team";
        String circuitdebugsp = "\u20B91000/team";
        String corcongsp = "\u20B92000/team";
        String mockstocksp = "\u20B91000/team";
        String treasurehuntsp = "\u20B91500/team";
        String techjamsp = "\u20B91000/team";
        String techtalksp = "\u20B91000/team";
        String gamingPCsp = "NFS: \u20B91000/team\nCoD: \u20B92000/team\nCS: \u20B92000/team";
        String mockgresp = "\u20B91000/team";
        String patternprintsp = "\u20B91000/team";
        String eetm2sp = "\u20B91500/team";
        String essencesp = "\u20B91000/team";
        String kartitsp = "\u20B91000/team";
        String codingsp = "\u20B91500/team";
        String potpourrisp = "\u20B91500/team";
        String cryptorigsp = "\u20B91000/team";
        String googleitsp = "\u20B91500/team";
        String chuckglidersp = "\u20B92000/team";
        String photographysp = "Professional: \u20B91000/team\nAmateur: \u20B9800/team";
        String hirefiresp = "\u20B91000/team";
        String rubikscubesp = "null";
        String bbroysp = "\u20B91000/team";
        String trdplsp = "null";
        String hogathonsp = "\u20B91500/team";
        String gamingConsolesp = "\u20B91500/team";

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
        String mockgreo1 = "Vidya";
        String patternprinto1 = "Sugosh";
        String eetm2o1 = "Gayathri";
        String essenceo1 = "Tejas";
        String kartito1 = "Pranav";
        String codingo1 = "Sugosh";
        String potpourrio1 = "Tejaswini";
        String cryptorigo1 = "Bindhiya Shree";
        String googleito1 = "Kirthan";
        String chuckglidero1 = "Rajat";
        String photographyo1 = "Pranav";
        String hirefireo1 = "MBA Department";
        String rubikscubeo1 = "Karan";
        String bbroyo1 = "Amulya";
        String trdplo1 = "Akshara";
        String hogathono1 = "Joshua";
        String gamingConsoleo1 = "Shrey";

        //Organizer2

        String techdco2 = "Sthuthie";
        String technogeno2 = "Suraj";
        String circuitdebugo2 = "Vinayak";
        String corcongo2 = "Gaurav";
        String mockstocko2 = "Anand";
        String treasurehunto2 = "Anasuya";
        String techjamo2 = "Srinidhi";
        String techtalko2 = "Shwetha";
        String gamingPCo2 = "Nayan";
        String mockgreo2 = "null";
        String patternprinto2 = "Dheeraj";
        String eetm2o2 = "null";
        String essenceo2 = "Sudeep";
        String kartito2 = "Meghana";
        String codingo2 = "null";
        String potpourrio2 = "Suhas";
        String cryptorigo2 = "Harshapriya";
        String googleito2 = "Akshatha";
        String chuckglidero2 = "Aditi";
        String photographyo2 = "Aakaash";
        String hirefireo2 = "null";
        String rubikscubeo2 = "null";
        String bbroyo2 = "Ranjitha";
        String trdplo2 = "Passes";
        String hogathono2 = "Maaz";
        String gamingConsoleo2 = "null";

        //Phone 1

        String techdcp1 = "9535693620";
        String technogenp1 = "8861469332";
        String circuitdebugp1 = "8861670102";
        String corcongp1 = "9845041734";
        String mockstockp1 = "9880085543";
        String treasurehuntp1 = "8123995823";
        String techjamp1 = "9538946191";
        String techtalkp1 = "9916841248";
        String gamingPCp1 = "8867580816";
        String mockgrep1 = "9916438368";
        String patternprintp1 = "9880085543";
        String eetm2p1 = "9538941398";
        String essencep1 = "9035211489";
        String kartitp1 = "8884716101";
        String codingp1 = "9880085543";
        String potpourrip1 = "9019985291";
        String cryptorigp1 = "9035184719";
        String googleitp1 = "8123995823";
        String chuckgliderp1 = "null";
        String photographyp1 = "8884716101";
        String hirefirep1 = "null";
        String rubikscubep1 = "8197526224";
        String bbroyp1 = "9482480881";
        String trdplp1 = "9686597805";
        String hogathonp1 = "8904448086";
        String gamingConsolep1 = "8867580816";

        //Phone 2

        String techdcp2 = "9845772581";
        String technogenp2 = "9886107706";
        String circuitdebugp2 = "8147162287";
        String corcongp2 = "7795583007";
        String mockstockp2 = "8861469332";
        String treasurehuntp2 = "9632508127";
        String techjamp2 = "8970970532";
        String techtalkp2 = "8861606232";
        String gamingPCp2 = "null";
        String mockgrep2 = "9480951582";
        String patternprintp2 = "9483918712";
        String eetm2p2 = "null";
        String essencep2 = "9538213512";
        String kartitp2 = "8105092894";
        String codingp2 = "null";
        String potpourrip2 = "9620080778";
        String cryptorigp2 = "9986956094";
        String googleitp2 = "null";
        String chuckgliderp2 = "null";
        String photographyp2 = "9902512464";
        String hirefirep2 = "null";
        String rubikscubep2 = "null";
        String bbroyp2 = "9880783959";
        String trdplp2 = "8147529660";
        String hogathonp2 = "null";
        String gamingConsolep2 = "8867580816";

        //Poster ID

        int techdcpi = R.drawable.techdc;
        int technogenpi = R.drawable.technogen;
        int circuitdebugpi = R.drawable.circuitdebug;
        int corcongpi = R.drawable.corpcong;
        int mockstockpi = R.drawable.mockstock;
        int treasurehuntpi = R.drawable.treasurehunt;
        int techjampi = R.drawable.techjam;
        int techtalkpi = R.drawable.techtalk;
        int gamingPCpi = R.drawable.gaming;
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
        eventDetailDataList.add(new EventDetailData(gamingPCpi, gamingPCname, gamingPCday, gamingPCtime, gamingPCloc, gamingPCdesc, gamingPCtm, gamingPCtr, gamingPCfp, gamingPCsp, gamingPCo1, gamingPCo2, gamingPCp1, gamingPCp2));
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
        eventDataList.add(new EventData(gamingPCname, (gamingPCday + " " + gamingPCtime), gamingPCpi));
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