package com.friday.tvlauncher;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends Activity {
    private static final int BG = Color.rgb(8,10,16), PANEL = Color.rgb(24,28,40), WHITE = Color.rgb(245,246,250), MUTED = Color.rgb(157,164,181), ACCENT = Color.rgb(205,177,255);
    private LinearLayout root, appGrid;
    private final Set<String> preferred = new HashSet<>();
    private final String[] preferredPackages = {"com.netflix.ninja", "com.google.android.youtube.tv", "com.amazon.amazonvideo.livingroom", "com.spotify.tv.android", "com.disneyplus.mea", "com.ezviz.tv", "com.ubnt.unifi.protect"};

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        Collections.addAll(preferred, preferredPackages);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        buildHome();
    }

    private GradientDrawable shape(int color, int radius, int stroke) {
        GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(radius);
        if (stroke != 0) d.setStroke(2, stroke); return d;
    }
    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(value); t.setTextColor(color); t.setTextSize(size);
        if (bold) t.setTypeface(Typeface.create("sans-serif", Typeface.BOLD)); return t;
    }
    private LinearLayout column() { LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); return l; }
    private LinearLayout.LayoutParams lp(int w, int h) { return new LinearLayout.LayoutParams(w,h); }

    private void buildHome() {
        ScrollView scroll = new ScrollView(this); scroll.setFillViewport(true); scroll.setBackgroundColor(BG);
        root = column(); root.setPadding(54,28,54,34); scroll.addView(root); setContentView(scroll);
        LinearLayout top = new LinearLayout(this); top.setGravity(Gravity.CENTER_VERTICAL); top.setOrientation(LinearLayout.HORIZONTAL);
        TextView brand = text("FRIDAY  /  TV", 16, WHITE, true); top.addView(brand, new LinearLayout.LayoutParams(0, -2, 1));
        TextView clock = text(new SimpleDateFormat("EEE, d MMM  •  h:mm a", Locale.getDefault()).format(new Date()), 14, MUTED, false); top.addView(clock); root.addView(top, lp(-1,48));
        TextView greeting = text("Good evening.", 36, WHITE, true); LinearLayout.LayoutParams gp=lp(-1,-2); gp.topMargin=18; root.addView(greeting,gp);
        TextView sub = text("Everything you love. One beautiful screen.", 16, MUTED, false); LinearLayout.LayoutParams sp=lp(-1,-2); sp.topMargin=4; root.addView(sub,sp);
        LinearLayout hero = column(); hero.setPadding(28,22,28,22); hero.setBackground(shape(0xff171c2b,24,0xff34384b));
        LinearLayout.LayoutParams hp=lp(-1,116); hp.topMargin=24; root.addView(hero,hp);
        TextView eyebrow=text("YOUR ENTERTAINMENT",11,ACCENT,true); hero.addView(eyebrow);
        TextView headline=text("A quieter kind of entertainment.",24,WHITE,true); LinearLayout.LayoutParams hlp=lp(-1,-2); hlp.topMargin=10; hero.addView(headline,hlp);
        TextView desc=text("Choose an app below to get started",14,MUTED,false); LinearLayout.LayoutParams dlp=lp(-1,-2); dlp.topMargin=5; hero.addView(desc,dlp);
        LinearLayout section = new LinearLayout(this); section.setGravity(Gravity.CENTER_VERTICAL); TextView label=text("Your apps",22,WHITE,true); section.addView(label,new LinearLayout.LayoutParams(0,-2,1));
        TextView all=text("INSTALLED",11,ACCENT,true); section.addView(all); LinearLayout.LayoutParams slp=lp(-1,-2); slp.topMargin=26; slp.bottomMargin=14; root.addView(section,slp);
        appGrid = new LinearLayout(this); appGrid.setOrientation(LinearLayout.VERTICAL); root.addView(appGrid,lp(-1,-2)); populateApps();
        LinearLayout footer=new LinearLayout(this); footer.setGravity(Gravity.CENTER_VERTICAL);
        TextView search=text("⌕   Search / voice",14,MUTED,true); search.setFocusable(true); search.setOnFocusChangeListener((v,f)->v.setBackground(shape(PANEL,18,f?ACCENT:0))); search.setPadding(18,12,18,12); search.setBackground(shape(PANEL,18,0)); search.setOnClickListener(v->voiceSearch()); footer.addView(search);
        TextView settings=text("  ⚙  Settings",14,MUTED,true); settings.setFocusable(true); settings.setOnFocusChangeListener((v,f)->v.setBackground(shape(PANEL,18,f?ACCENT:0))); settings.setPadding(18,12,18,12); settings.setBackground(shape(PANEL,18,0)); settings.setOnClickListener(v->openSettings()); LinearLayout.LayoutParams setp=lp(-2,-2); setp.leftMargin=12; footer.addView(settings,setp);
        LinearLayout.LayoutParams fp=lp(-1,-2); fp.topMargin=24; root.addView(footer,fp);
    }

    private void populateApps() {
        PackageManager pm=getPackageManager(); Intent query=new Intent(Intent.ACTION_MAIN); query.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> apps=pm.queryIntentActivities(query,0);
        if(apps.isEmpty()){ query.removeCategory(Intent.CATEGORY_LEANBACK_LAUNCHER); query.addCategory(Intent.CATEGORY_LAUNCHER); apps=pm.queryIntentActivities(query,0); }
        final List<ResolveInfo> list=new ArrayList<>(); Set<String> seen=new HashSet<>();
        for(ResolveInfo ri:apps){ String p=ri.activityInfo.packageName; if(p.equals(getPackageName())||!seen.add(p))continue; list.add(ri); }
        Collections.sort(list,(a,b)->{ boolean ap=preferred.contains(a.activityInfo.packageName), bp=preferred.contains(b.activityInfo.packageName); if(ap!=bp)return ap?-1:1; return String.valueOf(a.loadLabel(pm)).compareToIgnoreCase(String.valueOf(b.loadLabel(pm))); });
        int perRow=4; LinearLayout row=null;
        for(int i=0;i<list.size();i++){
            if(i%perRow==0){row=new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL); LinearLayout.LayoutParams rp=lp(-1,-2); rp.bottomMargin=12; appGrid.addView(row,rp);}
            ResolveInfo ri=list.get(i); String pkg=ri.activityInfo.packageName;
            LinearLayout tile=column(); tile.setGravity(Gravity.CENTER); tile.setPadding(10,14,10,12); tile.setBackground(shape(PANEL,20,0)); tile.setFocusable(true); tile.setClickable(true); tile.setOnFocusChangeListener((v,focused)->{ v.setBackground(shape(focused?0xff30364a:PANEL,20,focused?ACCENT:0)); v.setScaleX(focused?1.035f:1f); v.setScaleY(focused?1.035f:1f); }); tile.setOnClickListener(v->launch(pkg));
            try { android.graphics.drawable.Drawable icon=ri.loadIcon(pm); ImageView image=new ImageView(this); image.setImageDrawable(icon); image.setScaleType(ImageView.ScaleType.FIT_CENTER); tile.addView(image,lp(54,54)); } catch(Exception ignored){}
            TextView name=text(String.valueOf(ri.loadLabel(pm)),13,WHITE,true); name.setGravity(Gravity.CENTER); name.setMaxLines(1); LinearLayout.LayoutParams np=lp(-1,-2); np.topMargin=9; tile.addView(name,np);
            LinearLayout.LayoutParams tp=new LinearLayout.LayoutParams(0,106,1); if(i%perRow!=0)tp.leftMargin=12; row.addView(tile,tp);
        }
        if(list.isEmpty()){TextView empty=text("No launchable apps found.",14,MUTED,false); appGrid.addView(empty);}
    }
    private void launch(String pkg){try{Intent i=getPackageManager().getLaunchIntentForPackage(pkg); if(i==null){i=new Intent(Intent.ACTION_MAIN);i.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);i.setPackage(pkg);} if(i!=null)startActivity(i);else Toast.makeText(this,"App launch unavailable",Toast.LENGTH_SHORT).show();}catch(Exception e){Toast.makeText(this,"Could not open app",Toast.LENGTH_SHORT).show();}}
    private void voiceSearch(){try{Intent i=new Intent(RecognizerIntent.ACTION_VOICE_SEARCH_HANDS_FREE); i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(i);}catch(ActivityNotFoundException e){try{startActivity(new Intent(Intent.ACTION_ASSIST));}catch(Exception ignored){Toast.makeText(this,"Voice search is not available on this device",Toast.LENGTH_SHORT).show();}}}
    private void openSettings(){try{startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));}catch(Exception e){Toast.makeText(this,"Settings unavailable",Toast.LENGTH_SHORT).show();}}
}
