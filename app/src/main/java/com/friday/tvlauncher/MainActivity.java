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
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
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

/** Lightweight, remote-first Friday home screen. Uses platform widgets only. */
public class MainActivity extends Activity {
    private static final int BG = Color.rgb(7, 10, 17);
    private static final int PANEL = Color.argb(174, 27, 34, 49);
    private static final int PANEL_FOCUS = Color.rgb(49, 58, 78);
    private static final int WHITE = Color.rgb(247, 248, 252);
    private static final int MUTED = Color.rgb(164, 174, 194);
    private static final int ACCENT = Color.rgb(196, 158, 255);
    private final Handler handler = new Handler();
    private final SimpleDateFormat clockFormat = new SimpleDateFormat("h:mm", Locale.getDefault());
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM", Locale.getDefault());
    private TextView clockText;
    private TextView dateText;
    private LinearLayout dock;
    private final Set<String> preferred = new HashSet<>();
    private final String[] preferredPackages = {
        "com.google.android.youtube.tv", "com.netflix.ninja",
        "com.amazon.amazonvideo.livingroom", "com.spotify.tv.android",
        "com.disneyplus.mea", "com.ezviz.tv", "com.ubnt.unifi.protect"
    };
    private final Runnable clockTick = new Runnable() {
        @Override public void run() {
            Date now = new Date();
            if (clockText != null) clockText.setText(clockFormat.format(now));
            if (dateText != null) dateText.setText(dateFormat.format(now));
            handler.postDelayed(this, 30_000L);
        }
    };

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        Collections.addAll(preferred, preferredPackages);
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        buildHome();
    }

    @Override protected void onResume() {
        super.onResume();
        handler.removeCallbacks(clockTick);
        handler.post(clockTick);
    }

    @Override protected void onPause() {
        handler.removeCallbacks(clockTick);
        super.onPause();
    }

    private int dp(float value) { return (int) (value * getResources().getDisplayMetrics().density + 0.5f); }
    private GradientDrawable shape(int color, int radiusDp, int strokeColor) {
        GradientDrawable d = new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            new int[] { color, blend(color, 0xFF080B13, 0.12f) }
        );
        d.setCornerRadius(dp(radiusDp));
        if (strokeColor != 0) d.setStroke(dp(1), strokeColor);
        return d;
    }
    private int blend(int from, int to, float amount) {
        return Color.rgb(
            (int)(Color.red(from) * (1 - amount) + Color.red(to) * amount),
            (int)(Color.green(from) * (1 - amount) + Color.green(to) * amount),
            (int)(Color.blue(from) * (1 - amount) + Color.blue(to) * amount)
        );
    }
    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value); t.setTextColor(color); t.setTextSize(size);
        t.setIncludeFontPadding(false);
        if (bold) t.setTypeface(Typeface.create("sans-serif", Typeface.BOLD));
        return t;
    }
    private LinearLayout column() {
        LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); return l;
    }
    private LinearLayout.LayoutParams lp(int w, int h) { return new LinearLayout.LayoutParams(w, h); }

    private void buildHome() {
        FrameLayout canvas = new FrameLayout(this);
        GradientDrawable backdrop = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[] { 0xFF172238, 0xFF171725, BG }
        );
        canvas.setBackground(backdrop);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(dp(34), dp(24), dp(24), dp(24));
        canvas.addView(layout, new FrameLayout.LayoutParams(-1, -1));

        LinearLayout main = column();
        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(0, -1, 1f);
        mainParams.rightMargin = dp(22);
        layout.addView(main, mainParams);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        TextView brand = text("FRIDAY", 18, WHITE, true);
        header.addView(brand, new LinearLayout.LayoutParams(0, -2, 1f));
        TextView edition = text("TV EXPERIENCE", 10, ACCENT, true);
        edition.setLetterSpacing(0.16f);
        header.addView(edition);
        main.addView(header, lp(-1, dp(34)));

        LinearLayout hero = column();
        hero.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout.LayoutParams heroParams = new LinearLayout.LayoutParams(-1, 0, 1f);
        heroParams.topMargin = dp(12);
        main.addView(hero, heroParams);
        TextView eyebrow = text("YOUR SPACE. YOUR ENTERTAINMENT.", 11, ACCENT, true);
        eyebrow.setLetterSpacing(0.12f);
        hero.addView(eyebrow);
        TextView title = text("Good evening.", 39, WHITE, false);
        title.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        LinearLayout.LayoutParams titleParams = lp(-1, -2); titleParams.topMargin = dp(12);
        hero.addView(title, titleParams);
        TextView subtitle = text("Everything you enjoy, in one place.", 16, MUTED, false);
        LinearLayout.LayoutParams subParams = lp(-1, -2); subParams.topMargin = dp(9);
        hero.addView(subtitle, subParams);

        LinearLayout dockHeader = new LinearLayout(this);
        dockHeader.setGravity(Gravity.CENTER_VERTICAL);
        TextView appsLabel = text("YOUR APPS", 12, WHITE, true);
        appsLabel.setLetterSpacing(0.14f);
        dockHeader.addView(appsLabel, new LinearLayout.LayoutParams(0, -2, 1f));
        TextView hint = text("SELECT TO OPEN", 9, MUTED, true);
        hint.setLetterSpacing(0.10f);
        dockHeader.addView(hint);
        main.addView(dockHeader, lp(-1, dp(25)));

        HorizontalScrollView horizontal = new HorizontalScrollView(this);
        horizontal.setHorizontalScrollBarEnabled(false);
        horizontal.setClipToPadding(false);
        LinearLayout.LayoutParams scrollParams = lp(-1, dp(126));
        scrollParams.topMargin = dp(8);
        main.addView(horizontal, scrollParams);
        dock = new LinearLayout(this);
        dock.setOrientation(LinearLayout.HORIZONTAL);
        dock.setGravity(Gravity.CENTER_VERTICAL);
        horizontal.addView(dock, new HorizontalScrollView.LayoutParams(-2, -1));
        populateApps();

        LinearLayout control = buildControlCenter();
        LinearLayout.LayoutParams controlParams = new LinearLayout.LayoutParams(dp(242), -1);
        layout.addView(control, controlParams);
        setContentView(canvas);
    }

    private LinearLayout buildControlCenter() {
        LinearLayout panel = column();
        panel.setPadding(dp(18), dp(18), dp(18), dp(16));
        panel.setBackground(shape(PANEL, 28, 0x35FFFFFF));
        panel.setFocusable(false);

        TextView heading = text("CONTROL CENTER", 11, WHITE, true);
        heading.setLetterSpacing(0.12f);
        panel.addView(heading);
        clockText = text(clockFormat.format(new Date()), 42, WHITE, false);
        clockText.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        LinearLayout.LayoutParams cp = lp(-1, -2); cp.topMargin = dp(22);
        panel.addView(clockText, cp);
        dateText = text(dateFormat.format(new Date()), 12, MUTED, false);
        LinearLayout.LayoutParams dateParams = lp(-1, -2); dateParams.topMargin = dp(5);
        panel.addView(dateText, dateParams);

        View divider = new View(this); divider.setBackgroundColor(0x35FFFFFF);
        LinearLayout.LayoutParams dividerParams = lp(-1, dp(1)); dividerParams.topMargin = dp(22); dividerParams.bottomMargin = dp(15);
        panel.addView(divider, dividerParams);
        TextView quick = text("QUICK SETTINGS", 10, MUTED, true);
        quick.setLetterSpacing(0.12f);
        panel.addView(quick);

        addAction(panel, "◉   Wi-Fi & Network", Settings.ACTION_WIFI_SETTINGS);
        addAction(panel, "⌁   Bluetooth", Settings.ACTION_BLUETOOTH_SETTINGS);
        addAction(panel, "▣   Display & Picture", Settings.ACTION_DISPLAY_SETTINGS);
        addAction(panel, "⚙   All Settings", Settings.ACTION_SETTINGS);

        View spacer = new View(this);
        panel.addView(spacer, new LinearLayout.LayoutParams(1, 0, 1f));
        TextView footer = text("FRIDAY  •  HOME", 9, MUTED, true);
        footer.setLetterSpacing(0.12f);
        panel.addView(footer);
        return panel;
    }

    private void addAction(LinearLayout parent, String label, String action) {
        TextView button = text(label, 13, WHITE, true);
        button.setGravity(Gravity.CENTER_VERTICAL);
        button.setPadding(dp(12), 0, dp(8), 0);
        button.setFocusable(true);
        button.setClickable(true);
        button.setBackground(shape(0x252D374A, 15, 0x20FFFFFF));
        button.setOnFocusChangeListener((v, focused) -> {
            v.setBackground(shape(focused ? PANEL_FOCUS : 0x252D374A, 15, focused ? ACCENT : 0x20FFFFFF));
            v.setScaleX(focused ? 1.025f : 1f);
            v.setScaleY(focused ? 1.025f : 1f);
        });
        button.setOnClickListener(v -> openSettingsAction(action));
        LinearLayout.LayoutParams params = lp(-1, dp(48));
        params.topMargin = dp(9);
        parent.addView(button, params);
    }

    private void openSettingsAction(String action) {
        try {
            startActivity(new Intent(action).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (ActivityNotFoundException | SecurityException e) {
            try { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
            catch (Exception ignored) { Toast.makeText(this, "Settings unavailable", Toast.LENGTH_SHORT).show(); }
        }
    }

    private void populateApps() {
        PackageManager pm = getPackageManager();
        Intent query = new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        List<ResolveInfo> apps = pm.queryIntentActivities(query, 0);
        if (apps.isEmpty()) {
            query.removeCategory(Intent.CATEGORY_LEANBACK_LAUNCHER).addCategory(Intent.CATEGORY_LAUNCHER);
            apps = pm.queryIntentActivities(query, 0);
        }
        final List<ResolveInfo> list = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (ResolveInfo ri : apps) {
            String pkg = ri.activityInfo.packageName;
            if (pkg.equals(getPackageName()) || !seen.add(pkg)) continue;
            list.add(ri);
        }
        Collections.sort(list, (a, b) -> {
            boolean ap = preferred.contains(a.activityInfo.packageName);
            boolean bp = preferred.contains(b.activityInfo.packageName);
            if (ap != bp) return ap ? -1 : 1;
            return String.valueOf(a.loadLabel(pm)).compareToIgnoreCase(String.valueOf(b.loadLabel(pm)));
        });

        for (int i = 0; i < list.size(); i++) {
            ResolveInfo ri = list.get(i);
            String pkg = ri.activityInfo.packageName;
            LinearLayout tile = new LinearLayout(this);
            tile.setOrientation(LinearLayout.VERTICAL);
            tile.setGravity(Gravity.CENTER);
            tile.setPadding(dp(8), dp(10), dp(8), dp(8));
            tile.setBackground(shape(0x9A1B2231, 19, 0x22FFFFFF));
            tile.setFocusable(true); tile.setClickable(true);
            tile.setOnFocusChangeListener((v, focused) -> {
                v.setBackground(shape(focused ? 0xE33B455B : 0x9A1B2231, 19, focused ? ACCENT : 0x22FFFFFF));
                v.setScaleX(focused ? 1.07f : 1f); v.setScaleY(focused ? 1.07f : 1f);
                v.setElevation(focused ? dp(8) : 0);
            });
            tile.setOnClickListener(v -> launch(pkg));
            try {
                ImageView icon = new ImageView(this);
                icon.setImageDrawable(ri.loadIcon(pm));
                icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
                tile.addView(icon, lp(dp(52), dp(52)));
            } catch (Exception ignored) { }
            TextView name = text(String.valueOf(ri.loadLabel(pm)), 11, WHITE, true);
            name.setGravity(Gravity.CENTER);
            name.setMaxLines(1);
            LinearLayout.LayoutParams nameParams = lp(-1, -2); nameParams.topMargin = dp(7);
            tile.addView(name, nameParams);
            LinearLayout.LayoutParams tileParams = new LinearLayout.LayoutParams(dp(116), dp(104));
            if (i > 0) tileParams.leftMargin = dp(10);
            dock.addView(tile, tileParams);
        }
        if (list.isEmpty()) {
            TextView empty = text("No launchable apps found", 13, MUTED, false);
            dock.addView(empty);
        } else if (dock.getChildCount() > 0) {
            dock.post(() -> dock.getChildAt(0).requestFocus());
        }
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
            View first = dock != null && dock.getChildCount() > 0 ? dock.getChildAt(0) : null;
            if (first != null) first.requestFocus();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
