package com.friday.tvlauncher;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
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

/** Minimal premium AK PACK TV home. Native Android views keep memory use low. */
public class MainActivity extends Activity {
    private static final int BG = Color.rgb(247, 246, 243);
    private static final int RED = Color.rgb(210, 25, 43);
    private static final int INK = Color.rgb(27, 28, 31);
    private static final int MUTED = Color.rgb(117, 117, 121);
    private static final int CARD = Color.WHITE;
    private final Handler handler = new Handler();
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm", Locale.getDefault());
    private final SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
    private final SimpleDateFormat fullDateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
    private TextView clock;
    private TextView day;
    private TextView fullDate;
    private final String[] priorityPackages = {
        "com.google.android.youtube.tv",
        "com.netflix.ninja",
        "com.amazon.amazonvideo.livingroom",
        "com.spotify.tv.android",
        "com.disneyplus.mea",
        "com.ubnt.unifi.protect"
    };
    private final Runnable tick = new Runnable() {
        @Override public void run() {
            Date now = new Date();
            if (clock != null) clock.setText(timeFormat.format(now));
            if (day != null) day.setText(dayFormat.format(now).toUpperCase(Locale.getDefault()));
            if (fullDate != null) fullDate.setText(fullDateFormat.format(now));
            handler.postDelayed(this, 15000L);
        }
    };

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        buildScreen();
    }

    @Override protected void onResume() {
        super.onResume();
        handler.removeCallbacks(tick);
        handler.post(tick);
    }

    @Override protected void onPause() {
        handler.removeCallbacks(tick);
        super.onPause();
    }

    private int dp(float n) {
        return (int) (n * getResources().getDisplayMetrics().density + 0.5f);
    }

    private GradientDrawable rounded(int color, int radius, int stroke) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        if (stroke != 0) d.setStroke(dp(1), stroke);
        return d;
    }

    private TextView label(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setIncludeFontPadding(false);
        if (bold) t.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        return t;
    }

    private LinearLayout column() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }

    private LinearLayout.LayoutParams lp(int w, int h) {
        return new LinearLayout.LayoutParams(w, h);
    }

    private void buildScreen() {
        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(BG);

        View redOrb = new View(this);
        GradientDrawable orb = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[] { 0x20D2192B, 0x00D2192B }
        );
        orb.setShape(GradientDrawable.OVAL);
        redOrb.setBackground(orb);
        FrameLayout.LayoutParams orbParams = new FrameLayout.LayoutParams(dp(470), dp(470), Gravity.RIGHT | Gravity.TOP);
        orbParams.rightMargin = dp(170);
        orbParams.topMargin = dp(-250);
        root.addView(redOrb, orbParams);

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.HORIZONTAL);
        page.setPadding(dp(54), dp(34), dp(42), dp(32));
        root.addView(page, new FrameLayout.LayoutParams(-1, -1));

        LinearLayout main = column();
        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(0, -1, 1f);
        mainParams.rightMargin = dp(54);
        page.addView(main, mainParams);

        LinearLayout brandRow = new LinearLayout(this);
        brandRow.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout mark = new LinearLayout(this);
        mark.setGravity(Gravity.CENTER);
        mark.setBackground(rounded(RED, 13, 0));
        TextView markText = label("AK", 23, Color.WHITE, true);
        mark.addView(markText);
        brandRow.addView(mark, lp(dp(58), dp(48)));

        LinearLayout brandWords = column();
        brandWords.setPadding(dp(13), 0, 0, 0);
        TextView brandName = label("AK PACK", 20, INK, true);
        brandName.setLetterSpacing(0.06f);
        brandWords.addView(brandName);
        TextView brandSub = label("F I T N E S S", 9, RED, true);
        brandWords.addView(brandSub);
        brandRow.addView(brandWords, new LinearLayout.LayoutParams(0, -2, 1f));

        TextView server = label("AK PACK SERVER", 11, MUTED, true);
        server.setLetterSpacing(0.18f);
        server.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        brandRow.addView(server);
        main.addView(brandRow, lp(-1, dp(58)));

        LinearLayout hero = column();
        hero.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams heroParams = new LinearLayout.LayoutParams(-1, 0, 1f);
        main.addView(hero, heroParams);

        TextView eyebrow = label("YOUR DAY. YOUR SPACE.", 12, RED, true);
        eyebrow.setLetterSpacing(0.22f);
        hero.addView(eyebrow);

        clock = label(timeFormat.format(new Date()), 100, INK, false);
        clock.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        LinearLayout.LayoutParams clockParams = lp(-1, -2);
        clockParams.topMargin = dp(17);
        hero.addView(clock, clockParams);

        day = label(dayFormat.format(new Date()).toUpperCase(Locale.getDefault()), 19, INK, true);
        day.setLetterSpacing(0.16f);
        LinearLayout.LayoutParams dayParams = lp(-1, -2);
        dayParams.topMargin = dp(9);
        hero.addView(day, dayParams);

        fullDate = label(fullDateFormat.format(new Date()), 16, MUTED, false);
        LinearLayout.LayoutParams dateParams = lp(-1, -2);
        dateParams.topMargin = dp(8);
        hero.addView(fullDate, dateParams);

        LinearLayout info = new LinearLayout(this);
        info.setGravity(Gravity.CENTER_VERTICAL);
        info.setPadding(dp(18), 0, dp(22), 0);
        info.setBackground(rounded(Color.WHITE, 17, 0xFFEAE7E3));
        LinearLayout.LayoutParams infoParams = lp(-1, dp(62));
        infoParams.topMargin = dp(20);
        hero.addView(info, infoParams);

        View dot = new View(this);
        dot.setBackground(rounded(RED, 20, 0));
        info.addView(dot, lp(dp(8), dp(8)));
        TextView infoText = label("ONE PACK. ONE STANDARD. NO SHORTCUTS.", 11, INK, true);
        infoText.setLetterSpacing(0.08f);
        LinearLayout.LayoutParams infoTextParams = lp(-2, -2);
        infoTextParams.leftMargin = dp(12);
        info.addView(infoText, infoTextParams);

        TextView location = label("KODUNGALLUR  ·  KERALA", 10, MUTED, true);
        location.setLetterSpacing(0.12f);
        LinearLayout.LayoutParams locationParams = lp(-1, dp(28));
        locationParams.topMargin = dp(14);
        hero.addView(location, locationParams);

        LinearLayout side = column();
        side.setGravity(Gravity.CENTER_HORIZONTAL);
        side.setPadding(dp(15), dp(18), dp(15), dp(18));
        side.setBackground(rounded(Color.WHITE, 25, 0xFFEAE7E3));
        LinearLayout.LayoutParams sideParams = new LinearLayout.LayoutParams(dp(218), -1);
        page.addView(side, sideParams);

        TextView sideTitle = label("QUICK ACCESS", 11, MUTED, true);
        sideTitle.setLetterSpacing(0.16f);
        side.addView(sideTitle, lp(-1, dp(28)));

        LinearLayout apps = column();
        apps.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams appsParams = new LinearLayout.LayoutParams(-1, 0, 1f);
        appsParams.topMargin = dp(8);
        side.addView(apps, appsParams);
        addPriorityApps(apps);

        Button allApps = new Button(this);
        allApps.setText("ALL APPS  →");
        allApps.setAllCaps(false);
        allApps.setTextSize(13);
        allApps.setTextColor(Color.WHITE);
        allApps.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        allApps.setLetterSpacing(0.04f);
        allApps.setBackground(rounded(RED, 16, 0));
        allApps.setFocusable(true);
        allApps.setOnFocusChangeListener((v, focused) -> {
            v.setBackground(rounded(focused ? 0xFFAA1021 : RED, 16, focused ? 0xFF780A16 : 0));
            v.setScaleX(focused ? 1.035f : 1f);
            v.setScaleY(focused ? 1.035f : 1f);
        });
        allApps.setOnClickListener(v -> showAllApps());
        LinearLayout.LayoutParams allParams = lp(-1, dp(52));
        allParams.topMargin = dp(12);
        side.addView(allApps, allParams);

        setContentView(root);
        allApps.setNextFocusDownId(allApps.getId());
    }

    private List<ResolveInfo> getApps() {
        PackageManager pm = getPackageManager();
        Intent query = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(query, 0);
        if (apps.isEmpty()) {
            query.removeCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
            query.addCategory(Intent.CATEGORY_LAUNCHER);
            apps = pm.queryIntentActivities(query, 0);
        }
        List<ResolveInfo> result = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (ResolveInfo ri : apps) {
            String pkg = ri.activityInfo.packageName;
            if (!pkg.equals(getPackageName()) && seen.add(pkg)) result.add(ri);
        }
        Collections.sort(result, (a, b) ->
            String.valueOf(a.loadLabel(pm)).compareToIgnoreCase(String.valueOf(b.loadLabel(pm))));
        return result;
    }

    private void addPriorityApps(LinearLayout parent) {
        List<ResolveInfo> all = getApps();
        PackageManager pm = getPackageManager();
        int added = 0;
        for (String pkg : priorityPackages) {
            for (ResolveInfo ri : all) {
                if (pkg.equals(ri.activityInfo.packageName)) {
                    addAppTile(parent, ri, pm, false);
                    added++;
                    break;
                }
            }
            if (added == 4) break;
        }
        if (added == 0) {
            for (int i = 0; i < Math.min(4, all.size()); i++) addAppTile(parent, all.get(i), pm, false);
        }
    }

    private void addAppTile(LinearLayout parent, ResolveInfo ri, PackageManager pm, boolean compact) {
        String pkg = ri.activityInfo.packageName;
        LinearLayout tile = new LinearLayout(this);
        tile.setGravity(Gravity.CENTER_VERTICAL);
        tile.setPadding(dp(11), 0, dp(8), 0);
        tile.setBackground(rounded(0xFFF8F7F5, 14, 0xFFEDEAE6));
        tile.setFocusable(true);
        tile.setClickable(true);
        tile.setOnFocusChangeListener((v, focused) -> {
            v.setBackground(rounded(focused ? 0xFFFFE8EB : 0xFFF8F7F5, 14, focused ? RED : 0xFFEDEAE6));
            v.setScaleX(focused ? 1.035f : 1f);
            v.setScaleY(focused ? 1.035f : 1f);
        });
        tile.setOnClickListener(v -> launch(pkg));
        ImageView icon = new ImageView(this);
        try { icon.setImageDrawable(ri.loadIcon(pm)); } catch (Exception ignored) { }
        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        tile.addView(icon, lp(dp(34), dp(34)));
        TextView name = label(String.valueOf(ri.loadLabel(pm)), 12, INK, true);
        name.setSingleLine(true);
        name.setEllipsize(android.text.TextUtils.TruncateAt.END);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(0, -2, 1f);
        nameParams.leftMargin = dp(10);
        tile.addView(name, nameParams);
        TextView arrow = label("›", 23, RED, false);
        tile.addView(arrow);

        LinearLayout.LayoutParams params = lp(-1, dp(compact ? 50 : 48));
        params.topMargin = dp(9);
        parent.addView(tile, params);
    }

    private void showAllApps() {
        List<ResolveInfo> apps = getApps();
        PackageManager pm = getPackageManager();
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(BG);
        LinearLayout list = column();
        list.setPadding(dp(20), dp(16), dp(20), dp(20));
        scroll.addView(list);
        for (ResolveInfo ri : apps) addAppTile(list, ri, pm, true);
        TextView title = label("ALL APPS", 18, INK, true);
        title.setPadding(dp(20), dp(18), dp(20), dp(8));
        new AlertDialog.Builder(this)
            .setCustomTitle(title)
            .setView(scroll)
            .setNegativeButton("CLOSE", (dialog, which) -> dialog.dismiss())
            .show();
    }

    private void launch(String pkg) {
        try {
            Intent intent = getPackageManager().getLeanbackLaunchIntentForPackage(pkg);
            if (intent == null) intent = getPackageManager().getLaunchIntentForPackage(pkg);
            if (intent != null) startActivity(intent);
            else Toast.makeText(this, "App launch unavailable", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Could not open app", Toast.LENGTH_SHORT).show();
        }
    }

    @Override public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
            showAllApps();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
