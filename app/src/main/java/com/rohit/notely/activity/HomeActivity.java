package com.rohit.notely.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohit.notely.R;
import com.rohit.notely.adapters.NotelyAdapter;
import com.rohit.notely.database.RealmHelper;
import com.rohit.notely.models.NoteData;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<NoteData> noteDataArrayList;
    private RecyclerView recyclerView;
    private boolean isDrawerOpen = false;
    private LinearLayout drawerlist, main_anim_layout;
    private FrameLayout menu_ll;
    private ImageView add_note,filter_indicator;
    private View navigationView;
    private RelativeLayout nav_heading_ll, nav_hearted_ll, nav_favourite_ll, nav_poem_ll,
            nav_story_ll, apply_ll;
    private ImageView nav_heading_image, nav_hearted_image, nav_favourite_image, nav_poem_image, nav_story_image;
    private TextView nav_heading_text, nav_hearted_text, nav_favourite_text, nav_poem_text, nav_story_text,
            no_notes_text;
    private NotelyAdapter recyclerAdapter;
    private boolean filterHeart = false, filterFav = false, filterStory = false, filterPoem = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initVIews();
        setNavigationView();

        slideDrawerViewOut();

        menu_ll.setOnClickListener(this);
        add_note.setOnClickListener(this);

    }

    private void initVIews() {
        drawerlist = (LinearLayout) findViewById(R.id.drawerlist);
        main_anim_layout = (LinearLayout) findViewById(R.id.main_anim_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        menu_ll = (FrameLayout) findViewById(R.id.menu_ll);
        add_note = (ImageView) findViewById(R.id.add_note);
        no_notes_text = (TextView) findViewById(R.id.no_notes_text);
        filter_indicator =(ImageView)findViewById(R.id.filter_indicator);
    }

    private void getAllNotes() {
        noteDataArrayList = RealmHelper.getAllNotes();
        if (noteDataArrayList != null && noteDataArrayList.size() > 0) {
            no_notes_text.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (recyclerAdapter == null) {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerAdapter = new NotelyAdapter(this, noteDataArrayList);
                recyclerView.setAdapter(recyclerAdapter);
            } else {
                recyclerAdapter.notifyChanges(noteDataArrayList);
            }
        } else {
            no_notes_text.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

    }

    private void setNavigationView() {
        navigationView = findViewById(R.id.navigation_layout);

        nav_heading_ll = navigationView.findViewById(R.id.nav_heading_layout);
        nav_heading_text = navigationView.findViewById(R.id.nav_heading_text);
        nav_heading_image = navigationView.findViewById(R.id.nav_heading_image);

        nav_hearted_ll = navigationView.findViewById(R.id.nav_hearted_layout);
        nav_hearted_text = navigationView.findViewById(R.id.nav_hearted_text);
        nav_hearted_image = navigationView.findViewById(R.id.nav_hearted_image);

        nav_favourite_ll = navigationView.findViewById(R.id.nav_favourite_layout);
        nav_favourite_text = navigationView.findViewById(R.id.nav_favourite_text);
        nav_favourite_image = navigationView.findViewById(R.id.nav_favourite_image);

        nav_poem_ll = navigationView.findViewById(R.id.nav_poem_layout);
        nav_poem_text = navigationView.findViewById(R.id.nav_poem_text);
        nav_poem_image = navigationView.findViewById(R.id.nav_poem_image);

        nav_story_ll = navigationView.findViewById(R.id.nav_story_layout);
        nav_story_text = navigationView.findViewById(R.id.nav_story_text);
        nav_story_image = navigationView.findViewById(R.id.nav_story_image);

        apply_ll = (RelativeLayout) findViewById(R.id.apply_layout);

        nav_heading_ll.setOnClickListener(this);
        nav_hearted_ll.setOnClickListener(this);
        nav_favourite_ll.setOnClickListener(this);
        nav_poem_ll.setOnClickListener(this);
        nav_story_ll.setOnClickListener(this);
        apply_ll.setOnClickListener(this);
    }


    private void closeNavigationBar() {
        if (isDrawerOpen) {
            slideDrawerViewOut();
            isDrawerOpen = false;
            menu_ll.setVisibility(View.VISIBLE);
            add_note.setVisibility(View.VISIBLE);
            slideMainActivityOnDrawerClose();
        }
    }

    private void slideDrawerViewOut() {
        int layout_width = (int) getResources().getDimension(R.dimen.layout_dimen180);
        ObjectAnimator anim = ObjectAnimator.ofFloat(drawerlist, "translationX", 0, layout_width);
        if (isDrawerOpen) {
            anim.setDuration(300);
        } else {
            anim.setDuration(0);
        }
        anim.start();
    }

    private void slideDrawerViewIn() {
        int layout_width = (int) getResources().getDimension(R.dimen.layout_dimen180);
        ObjectAnimator anim = ObjectAnimator.ofFloat(drawerlist, "translationX", layout_width, 0);
        anim.setDuration(300);
        anim.start();
    }

    private void slideMainActivityOnDrawerOpen() {
        int layout_width = (int) getResources().getDimension(R.dimen.layout_dimen180);
        ObjectAnimator anim = ObjectAnimator.ofFloat(main_anim_layout, "translationX", 0, -layout_width);
        anim.setDuration(300);
        anim.start();
    }

    private void slideMainActivityOnDrawerClose() {
        int layout_width = (int) getResources().getDimension(R.dimen.layout_dimen180);
        ObjectAnimator anim = ObjectAnimator.ofFloat(main_anim_layout, "translationX", -layout_width, 0);
        anim.setDuration(300);
        anim.start();
    }

    private void openNavigationbar() {
        if (!isDrawerOpen) {
            isDrawerOpen = true;
            menu_ll.setVisibility(View.GONE);
            add_note.setVisibility(View.GONE);
            slideMainActivityOnDrawerOpen();
            slideDrawerViewIn();
            setFilterViews();
        }
    }

    private void setFilterViews() {
        if (filterPoem) {
            changeToSelected(nav_poem_image, nav_poem_text);
        } else {
            changeToUnSelected(nav_poem_image, nav_poem_text);
        }
        if (filterStory) {
            changeToSelected(nav_story_image, nav_story_text);
        } else {
            changeToUnSelected(nav_story_image, nav_story_text);
        }
        if (filterHeart) {
            changeToSelected(nav_hearted_image, nav_hearted_text);
        } else {
            changeToUnSelected(nav_hearted_image, nav_hearted_text);
        }
        if (filterFav) {
            changeToSelected(nav_favourite_image, nav_favourite_text);
        } else {
            changeToUnSelected(nav_favourite_image, nav_favourite_text);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getAllNotes();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_note:
                Intent intent = new Intent(HomeActivity.this, AddNoteActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_ll:
                openNavigationbar();
                break;
            case R.id.nav_heading_layout:
                closeNavigationBar();
                break;
            case R.id.nav_hearted_layout:
                if (nav_hearted_text.getCurrentTextColor() == getResources().getColor(R.color.light_green)) {
                    changeToUnSelected(nav_hearted_image, nav_hearted_text);
                } else {
                    changeToSelected(nav_hearted_image, nav_hearted_text);

                }
                break;
            case R.id.nav_favourite_layout:
                if (nav_favourite_text.getCurrentTextColor() == getResources().getColor(R.color.light_green)) {
                    changeToUnSelected(nav_favourite_image, nav_favourite_text);
                } else {
                    changeToSelected(nav_favourite_image, nav_favourite_text);
                }
                break;
            case R.id.nav_poem_layout:
                if (nav_poem_text.getCurrentTextColor() == getResources().getColor(R.color.light_green)) {
                    changeToUnSelected(nav_poem_image, nav_poem_text);
                } else {
                    changeToSelected(nav_poem_image, nav_poem_text);
                }
                break;
            case R.id.nav_story_layout:
                if (nav_story_text.getCurrentTextColor() == getResources().getColor(R.color.light_green)) {
                    changeToUnSelected(nav_story_image, nav_story_text);
                } else {
                    changeToSelected(nav_story_image, nav_story_text);
                }
                break;
            case R.id.apply_layout:
                closeNavigationBar();
                filterData();
                break;
        }
    }

    private void filterData() {
        filter_indicator.setVisibility(View.VISIBLE);
        if (nav_story_text.getCurrentTextColor() == getResources().getColor(R.color.light_green)) {
            filterStory = true;
        } else {
            filterStory = false;
        }
        if (nav_poem_text.getCurrentTextColor() == getResources().getColor(R.color.light_green)) {
            filterPoem = true;
        } else {
            filterPoem = false;
        }
        if (nav_favourite_text.getCurrentTextColor() == getResources().getColor(R.color.light_green)) {
            filterFav = true;
        } else {
            filterFav = false;
        }
        if (nav_hearted_text.getCurrentTextColor() == getResources().getColor(R.color.light_green)) {
            filterHeart = true;
        } else {
            filterHeart = false;
        }
        ArrayList<NoteData> noteDatas;
        if (filterStory && filterPoem) {
            noteDatas = RealmHelper.getAllFilteredNotes("all", filterHeart, filterFav);
        } else if (filterStory) {
            noteDatas = RealmHelper.getAllFilteredNotes("Story", filterHeart, filterFav);
        } else if (filterPoem) {
            noteDatas = RealmHelper.getAllFilteredNotes("Poem", filterHeart, filterFav);
        } else {
            noteDatas = RealmHelper.getAllFilteredNotes("all", filterHeart, filterFav);
        }
        if (noteDatas != null && noteDatas.size() > 0) {
            noteDataArrayList = noteDatas;
            if (recyclerAdapter != null) {
                recyclerAdapter.notifyChanges(noteDataArrayList);
            } else {
                Toast.makeText(this, "Add your notes first!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No filtered data found!", Toast.LENGTH_SHORT).show();
        }

        if(!filterFav && !filterHeart && !filterStory && !filterPoem){
            filter_indicator.setVisibility(View.GONE);
        }

    }

    private void changeToSelected(ImageView imageView, TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.light_green));
        imageView.setColorFilter(getResources().getColor(R.color.light_green));
    }

    private void changeToUnSelected(ImageView imageView, TextView textView) {
        textView.setTextColor(getResources().getColor(R.color.white_pressed));
        imageView.setColorFilter(getResources().getColor(R.color.white_pressed));
    }
}
