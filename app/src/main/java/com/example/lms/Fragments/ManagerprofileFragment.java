package com.example.lms.Fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lms.Activities.Login;
import com.example.lms.R;
import com.example.lms.Classes.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ManagerprofileFragment extends Fragment {
    CircleImageView managerprofile,managersave,managerdelete,managerchangepass;
    EditText managername,manageraddress,managercontact;
    Button managerlogout;
    TextView manageremail,managerempid;
    public static final String SHARED_PREFS ="sharedPrefs";
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    ProgressDialog pd;

    private Uri imageUri;
    private String myUrl="";
    private StorageTask uploadTask;
    private StorageReference storageReference;
    private static final int GALLERY_IMAGE_CODE =100;
    private static final int CAMERA_IMAGE_CODE =200;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_managerprofile, container, false);

        managerempid=view.findViewById(R.id.txteditmanagerEmpId);
        manageremail=view.findViewById(R.id.txteditmanagerEmail);
        managerprofile =view.findViewById(R.id.profile_manager);
        managername=view.findViewById(R.id.txteditmanagername);
        manageraddress=view.findViewById(R.id.txteditmanageraddress);
        managercontact=view.findViewById(R.id.txteditmanagercontact);
        managersave=view.findViewById(R.id.btnmanagersave);
        managerchangepass=view.findViewById(R.id.btnmanagerchangepass);
        managerdelete=view.findViewById(R.id.btnmanagerdelete);
        managerlogout=view.findViewById(R.id.btnmanagerlogout);

        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.child("name").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String contact = snapshot.child("contact").getValue(String.class);
                    String link = snapshot.child("profilepic").getValue(String.class);
                    String uid = snapshot.child("empid").getValue(String.class);
                    String uemail = snapshot.child("email").getValue(String.class);

                    if (name != null) {
                        managername.setText(name);
                    }
                    if (contact != null) {
                        managercontact.setText(contact);
                    }
                    if (address != null) {
                        manageraddress.setText(address);
                    }
                    if (uid != null) {
                        managerempid.setText(uid);
                    }
                    if (uemail != null) {
                        manageremail.setText(uemail);
                    }

                    // Load profile image using Picasso
                    if (link != null && !link.isEmpty()) {
                        Picasso.get().load(link).into(managerprofile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle the cancellation or error case
                }
            });
        }


        managersave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = managername.getText().toString();
                String contct = managercontact.getText().toString();
                String address = manageraddress.getText().toString();



                if (TextUtils.isEmpty(name)){
                    managername.setError("Username is required");
                }else if (TextUtils.isEmpty(contct) && contct.length()==10) {
                    managercontact.setError("Contact is required");
                }
                else if (TextUtils.isEmpty(address)) {
                    manageraddress.setError("Address is required");
                }else {
                    uploadData(name,address,contct);
                }
            }
        });

        managerprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagePickDialog();
            }
        });

        managerlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", "");
                editor.apply();

                startActivity(new Intent(ManagerprofileFragment.this.getActivity(), Login.class));
                getActivity().finish();
                Toast.makeText(getActivity(), "Logout Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        managerchangepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePasswordDialog();
            }
        });

        managerdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show a confirmation dialog
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Confirm Deletion");
                alertDialogBuilder.setMessage("Are you sure you want to delete your account?");
                alertDialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            // Delete user from Realtime Database
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
                            userRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    // Delete user from Firebase Authentication
                                    user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            // Sign out and navigate to login screen
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(getActivity(), Login.class));
                                            getActivity().finish();
                                            Toast.makeText(getActivity(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), "Failed to delete account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.e("AccountDeletion", "Failed to delete account: " + e.getMessage());
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Failed to delete account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("AccountDeletion", "Failed to delete account: " + e.getMessage());
                                }
                            });
                        }
                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", null);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });


        return view;

    }

    private void imagePickDialog() {
        String[] option = {"Camara", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose image from");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(which == 0){
                    camaraPick();

                }
                if(which == 1){
                    galleryPick();
                }
            }
        });
        builder.create().show();
    }

    private void galleryPick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,GALLERY_IMAGE_CODE);
    }

    private void camaraPick() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp desc");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,CAMERA_IMAGE_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == GALLERY_IMAGE_CODE){
                imageUri = data.getData();
                managerprofile.setImageURI(imageUri);
            }
            if (requestCode == CAMERA_IMAGE_CODE){
                managerprofile.setImageURI(imageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    private void uploadData(String name, String address, String contct) {
        if (pd == null) {
            pd = new ProgressDialog(ManagerprofileFragment.this.getActivity());
            pd.setMessage("Please Wait..");
            pd.setIndeterminate(false);
            pd.setCancelable(false);
            pd.show();
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String existingName = dataSnapshot.child("name").getValue(String.class);
                    String existingAddress = dataSnapshot.child("address").getValue(String.class);
                    String existingContact = dataSnapshot.child("contact").getValue(String.class);
                    String existingProfilePic = dataSnapshot.child("profilepic").getValue(String.class);
                    String empid = dataSnapshot.child("empid").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String password = dataSnapshot.child("password").getValue(String.class);

                    // Update only the edited fields
                    if (!TextUtils.isEmpty(name)) {
                        existingName = name;
                    }
                    if (!TextUtils.isEmpty(address)) {
                        existingAddress = address;
                    }
                    if (!TextUtils.isEmpty(contct)) {
                        existingContact = contct;
                    }

                    // Create a new Users object with the updated fields
                    Users users = new Users();
                    users.setName(existingName);
                    users.setAddress(existingAddress);
                    users.setContact(existingContact);
                    users.setUid(auth.getCurrentUser().getUid());
                    users.setEmpid(empid);
                    users.setEmail(email);
                    users.setPassword(password);

                    // Check if a new profile picture is selected
                    if (imageUri != null) {
                        // Upload the new profile picture to Firebase Storage
                        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("Profile_images").child(auth.getCurrentUser().getUid());
                        storageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get the download URL of the uploaded image
                                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUri) {
                                        String newProfilePic = downloadUri.toString();
                                        users.setProfilepic(newProfilePic);

                                        // Update the data in the database
                                        userRef.setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                pd.dismiss();
                                                Toast.makeText(ManagerprofileFragment.this.getActivity(), "Update Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                pd.dismiss();
                                                Toast.makeText(ManagerprofileFragment.this.getActivity(), "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(ManagerprofileFragment.this.getActivity(), "Failed to upload profile picture: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(ManagerprofileFragment.this.getActivity(), "Failed to upload profile picture: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // No new profile picture selected, update the data without uploading the image
                        users.setProfilepic(existingProfilePic);

                        // Update the data in the database
                        userRef.setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                pd.dismiss();
                                Toast.makeText(ManagerprofileFragment.this.getActivity(), "Update Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(ManagerprofileFragment.this.getActivity(), "Failed to update: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismiss();
                Toast.makeText(ManagerprofileFragment.this.getActivity(), "Failed to update: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showChangePasswordDialog() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.change_password, null);

        EditText current_pass = view.findViewById(R.id.current_pass);
        EditText new_pass = view.findViewById(R.id.new_pass);
        EditText confirm_pass = view.findViewById(R.id.confirm_pass);
        AppCompatButton changePass_btn = view.findViewById(R.id.btnchangePass);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        changePass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = current_pass.getText().toString().trim();
                String newPassword = new_pass.getText().toString().trim();
                String confirmPassword = confirm_pass.getText().toString().trim();

                if (TextUtils.isEmpty(oldPassword)) {
                    Toast.makeText(getActivity(), "Enter your current password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPassword.length() < 6) {
                    Toast.makeText(getActivity(), "Password length must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(getActivity(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.dismiss();
                if (pd == null) {
                    pd = new ProgressDialog(getActivity());
                    pd.setMessage("Changing Password");
                    pd.setIndeterminate(false);
                    pd.setCancelable(false);
                    pd.show();
                }
                updatePassword(oldPassword, newPassword);
            }
        });
    }

    private void updatePassword(String oldPassword, String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);
            user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // Update the password field in the Realtime Database
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                            userRef.child("password").setValue(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Failed to update password in the database", Toast.LENGTH_SHORT).show();
                                    Log.e("ChangePassword", "Failed to update password in the database: " + e.getMessage());
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Failed to change password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("ChangePassword", "Failed to change password: " + e.getMessage());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("ChangePassword", "Authentication failed: " + e.getMessage());
                }
            });
        }
    }


}
