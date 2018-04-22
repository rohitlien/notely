package com.rohit.notely.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.rohit.notely.R;
import com.rohit.notely.database.RealmHelper;
import com.rohit.notely.models.NoteData;
import com.rohit.notely.utils.NotelyTools;

import java.util.List;

public class AddNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView undo_button, save_button, edit_button, story_txt, poem_txt, noteDate;
    private EditText header_et, note_et;
    private Switch noteSwitch;
    private ImageView close_button;
    private LinearLayout note_switch_ll;
    private NoteData noteData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        initViews();
        getIntentData();

    }

    private void initViews() {

        undo_button = (TextView) findViewById(R.id.undo_button);
        save_button = (TextView) findViewById(R.id.save_button);
        edit_button = (TextView) findViewById(R.id.edit_button);
        story_txt = (TextView) findViewById(R.id.story_txt);
        poem_txt = (TextView) findViewById(R.id.poem_txt);
        noteDate = (TextView) findViewById(R.id.noteDate);
        note_switch_ll = (LinearLayout) findViewById(R.id.note_switch_ll);

        close_button = (ImageView) findViewById(R.id.close_button);

        header_et = (EditText) findViewById(R.id.header_et);
        note_et = (EditText) findViewById(R.id.note_et);
        noteSwitch = (Switch) findViewById(R.id.note_switch);

        undo_button.setOnClickListener(this);
        save_button.setOnClickListener(this);
        close_button.setOnClickListener(this);
        edit_button.setOnClickListener(this);

        noteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    poem_txt.setVisibility(View.VISIBLE);
                    story_txt.setVisibility(View.GONE);
                } else {
                    poem_txt.setVisibility(View.GONE);
                    story_txt.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getIntentData() {
        if (getIntent() != null) {
            String noteId = getIntent().getStringExtra("noteId");
            if (noteId != null && !noteId.isEmpty()) {
                noteData = RealmHelper.getNoteById(noteId);
                if (noteData == null) {
                    Toast.makeText(this, "This note has been deleted !", Toast.LENGTH_SHORT).show();
                } else {
                    setNoteData(noteData);
                }
            }
        }
    }

    private void setNoteData(NoteData noteData) {
        header_et.setText(noteData.getTitle());
        note_et.setText(noteData.getContent());
        if (noteData.getType().equalsIgnoreCase("Poem")) {
            noteSwitch.setChecked(true);
            poem_txt.setVisibility(View.VISIBLE);
            story_txt.setVisibility(View.GONE);
        } else {
            noteSwitch.setChecked(false);
            poem_txt.setVisibility(View.GONE);
            story_txt.setVisibility(View.VISIBLE);
        }
        NotelyTools notelyTools = new NotelyTools();
        noteDate.setText(notelyTools.getDate(noteData.getTimeStamp()));
        noteDate.setVisibility(View.VISIBLE);
        edit_button.setVisibility(View.VISIBLE);
        undo_button.setVisibility(View.GONE);
        save_button.setVisibility(View.GONE);
        note_switch_ll.setVisibility(View.GONE);
        header_et.setBackgroundColor(getResources().getColor(R.color.appbar_color));
        note_et.setEnabled(false);
        header_et.setEnabled(false);
        note_et.setTextColor(getResources().getColor(R.color.black));
        header_et.setTextColor(getResources().getColor(R.color.black));

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.undo_button) {
            undoData();
        } else if (id == R.id.save_button) {
            saveData();
        } else if (id == R.id.close_button) {
            finish();
        } else if (id == R.id.edit_button) {
            undo_button.setVisibility(View.VISIBLE);
            save_button.setVisibility(View.VISIBLE);
            edit_button.setVisibility(View.GONE);
            header_et.setEnabled(true);
            note_et.setEnabled(true);
            note_switch_ll.setVisibility(View.VISIBLE);
            noteDate.setVisibility(View.GONE);
            header_et.setBackgroundColor(getResources().getColor(R.color.background_white));
        }
    }

    private void undoData() {
        String contentText = note_et.getText().toString();
        String newContentText = "";
        if (contentText != null && contentText.length() > 0) {
            String strarray[] = contentText.split(" ");
            if (strarray.length > 0) {
                for (int i = 0; i < (strarray.length - 1); i++) {
                    if (i != 0) {
                        newContentText = newContentText + " ";
                    }
                    newContentText = newContentText + strarray[i];
                }
                note_et.setText("" + newContentText);
            }
        } else {
            Toast.makeText(this, "Nothing to undo !", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveData() {
        header_et.setError(null);
        note_et.setError(null);
        if (header_et.getText().toString() != null && header_et.getText().toString().trim().length() > 0) {
            if (note_et.getText().toString() != null && note_et.getText().toString().trim().length() > 0) {
                String timeStamp = "" + System.currentTimeMillis();
                String type = "";
                String content = note_et.getText().toString().trim();
                String header = header_et.getText().toString().trim();
                if (noteSwitch.isChecked()) {
                    type = "Poem";
                } else {
                    type = "Story";
                }
                if (noteData == null) {
                    noteData = new NoteData();
                    noteData.setId(timeStamp);
                    noteData.setContent(content);
                    noteData.setTitle(header);
                    noteData.setHearted(false);
                    noteData.setTimeStamp(timeStamp);
                    noteData.setDelete(false);
                    noteData.setFavourite(false);
                    noteData.setType(type);
                    RealmHelper.addNoteData(noteData);

                    Toast.makeText(this, "Note added !", Toast.LENGTH_SHORT).show();
                } else {
                    RealmHelper.updateNote(noteData.getId(), type, content, header, timeStamp);
                }
                finish();

            } else {
                note_et.setError("Please enter content !");
            }
        } else {
            header_et.setError("Please enter header !");
        }
    }
}
