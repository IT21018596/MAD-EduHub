package com.example.eduhub;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NoteDetailsActivity extends AppCompatActivity {
    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn;
    Button chooseImgBtn, removeImgBtn;
    ImageView noteImageView;
    TextView pageTitleTextView, deleteNoteTextViewBtn, charLimitTextView;
    String title, content, docId, imageUri, savedImageUri, savedImageName, imageName;
    boolean isEditMode = false;

    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference savedStorageReference;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        titleEditText = findViewById(R.id.notes_title_text);
        contentEditText = findViewById(R.id.notes_content_text);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteTextViewBtn = findViewById(R.id.delete_note_text_view_btn);
        charLimitTextView = findViewById(R.id.char_limit_text_view);
        chooseImgBtn = findViewById(R.id.choose_image_btn);
        removeImgBtn = findViewById(R.id.remove_image_btn);
        noteImageView = findViewById(R.id.note_image_view);

        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");
        savedImageUri = getIntent().getStringExtra("imageUri");
        savedImageName = getIntent().getStringExtra("imageName");

        if (docId != null && !docId.isEmpty()) {
            isEditMode = true;
        }

        if(savedImageUri != null){
            noteImageView.setVisibility(View.VISIBLE);
            savedStorageReference = FirebaseStorage.getInstance().getReference().child("noteImages/"+savedImageName);
            Glide.with(getApplicationContext()).load(savedImageUri).into(noteImageView);

        }

        titleEditText.setText(title);
        contentEditText.setText(content);

        if (isEditMode) {
            pageTitleTextView.setText("Edit Note");
            deleteNoteTextViewBtn.setVisibility(View.VISIBLE);
        }

        // String convert = String.valueOf(contentEditText.length());
        String trimmed = contentEditText.getText().toString().trim();
        int words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;

        charLimitTextView.setText("Word count: " + words);

        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2 >= 30)
                    Toast.makeText(NoteDetailsActivity.this, "Max character length of note title is " + String.valueOf(30), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = contentEditText.length();
                String convert = String.valueOf(length);
                String trimmed = contentEditText.getText().toString().trim();
                int words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
                charLimitTextView.setText("Word count: " + words);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        chooseImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        removeImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(savedImageUri==null){
                    filePath = null;
                    imageName = null;
                    imageUri = null;
                    noteImageView.setVisibility(View.GONE);
                    chooseImgBtn.setVisibility(View.VISIBLE);
                    removeImgBtn.setVisibility(View.GONE);
                }else{
                    savedStorageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            savedImageUri = null;
                            savedImageName = null;
                            filePath = null;
                            imageName = null;
                            imageUri = null;

                            String noteTitle = titleEditText.getText().toString();
                            String noteContent = contentEditText.getText().toString();

                            if (noteTitle == null || noteTitle.isEmpty()) {
                                titleEditText.setError("Note title is required");
                                return;
                            }

                            Note note = new Note();
                            note.setTitle(noteTitle);
                            note.setContent(noteContent);
                            note.setTimestamp(Timestamp.now());
                            note.setImageUri(imageUri);
                            note.setImageName(imageName);

                            String successMessage = "Note added successfully";
                            String unsuccessMessage = "Failed while adding note";

                            if (isEditMode) {
                                successMessage = "Note updated successfully";
                                unsuccessMessage = "Failed while updating note";
                            }

                            DocumentReference documentReference;
                            if (isEditMode) {
                                //update note
                                documentReference = Utility.getCollectionReferenceForNotes().document(docId);
                            } else {
                                //create new note
                                documentReference = Utility.getCollectionReferenceForNotes().document();
                            }

                            String finalSuccessMessage = successMessage;
                            String finalUnsuccessMessage = unsuccessMessage;

                            documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Utility.showToast(NoteDetailsActivity.this, finalSuccessMessage);
                                    } else {
                                        Utility.showToast(NoteDetailsActivity.this, finalUnsuccessMessage);
                                    }
                                }
                            });

                            noteImageView.setVisibility(View.GONE);
                            removeImgBtn.setVisibility(View.GONE);
                            chooseImgBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(NoteDetailsActivity.this, "Image removed", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(NoteDetailsActivity.this, "Can not remove image", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        deleteNoteTextViewBtn.setOnClickListener((v) -> {
            deleteNoteFromFirebase();
        });
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the Uri of data
            filePath = data.getData();
            noteImageView.setImageURI(filePath);
            noteImageView.setVisibility(View.VISIBLE);
            removeImgBtn.setVisibility(View.VISIBLE);
            chooseImgBtn.setVisibility(View.GONE);
        }
    }

    // Select Image method
    private void SelectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    // UploadImage method
    private void uploadImage() {
        if (filePath != null){
            uploadImageToFirebase(filePath);
        }else{
            imageUri = null;
            saveNote();
        }
    }

    private void uploadImageToFirebase(Uri filePath) {
        imageName = +System.currentTimeMillis()+"."+getFileExtension(filePath);
        StorageReference file = storageReference.child("noteImages/"+imageName);
        file.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUri = uri.toString();
                        Toast.makeText(NoteDetailsActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        saveNote();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NoteDetailsActivity.this, "Failed to upload the image", Toast.LENGTH_SHORT).show();
                imageUri = null;
                saveNote();
            }
        });
    }

    private String getFileExtension(Uri filePath) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(contentResolver.getType(filePath));
    }

    void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        if (noteTitle == null || noteTitle.isEmpty()) {
            titleEditText.setError("Note title is required");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());
        note.setImageUri(imageUri);
        note.setImageName(imageName);

        DocumentReference documentReference;
        if (isEditMode) {
            //update note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        } else {
            //create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }
        saveNoteToFirebase(note);

    }

    void saveNoteToFirebase(Note note) {
        String successMessage = "Note added successfully";
        String unsuccessMessage = "Failed while adding note";

        if (isEditMode) {
            successMessage = "Note updated successfully";
            unsuccessMessage = "Failed while updating note";
        }

        DocumentReference documentReference;
        if (isEditMode) {
            //update note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        } else {
            //create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }

        String finalSuccessMessage = successMessage;
        String finalUnsuccessMessage = unsuccessMessage;

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.showToast(NoteDetailsActivity.this, finalSuccessMessage);
                    startActivity(new Intent(NoteDetailsActivity.this, NoteListActivity.class));
                    finish();
                } else {
                    Utility.showToast(NoteDetailsActivity.this, finalUnsuccessMessage);
                }
            }
        });
    }

    void deleteNoteFromFirebase() {
        if(savedImageUri!=null){
            savedStorageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    noteImageView.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NoteDetailsActivity.this, "Can not remove image", Toast.LENGTH_SHORT).show();
                }
            });
        }


        DocumentReference documentReference;

        documentReference = Utility.getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.showToast(NoteDetailsActivity.this, "Note deleted successfully");
                    finish();
                } else {
                    Utility.showToast(NoteDetailsActivity.this, "Failed while deleting note");
                }
            }
        });
    }
}