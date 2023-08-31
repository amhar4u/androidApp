package com.example.lms.Viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.Adapters.userMyAdapter;
import com.example.lms.R;
import com.example.lms.Classes.userItems;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class userViewHolder extends RecyclerView.ViewHolder {

    public TextView userempid;
    public TextView username;
    public TextView usercontact;
    public TextView useremail;
    public ImageView userprofile;
    Button deleteuser;
    FirebaseAuth auth;
    private  String password;
    private DatabaseReference usersRef;
    private List<userItems> items;
    private userMyAdapter adapter;

    public userViewHolder(@NonNull View itemView, List<userItems> items, userMyAdapter userMyAdapter) {
        super(itemView);
        userempid = itemView.findViewById(R.id.txtuserempid);
        username = itemView.findViewById(R.id.txtusername);
        usercontact = itemView.findViewById(R.id.txtusercontact);
        useremail = itemView.findViewById(R.id.txtuseremail);
        userprofile = itemView.findViewById(R.id.imguserprofile);
        deleteuser = itemView.findViewById(R.id.btndeleteuser);
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();
        this.items = items;
        this.adapter=userMyAdapter;

        deleteuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Call a method to handle the delete operation
                    deleteUser(position);
                }
            }
        });
    }
    private void deleteUser(int position) {
        // Get the user from your data source based on the position
        userItems user = items.get(position);

        // Get a reference to the Firebase database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Create a database query to find the user with a specific identifier (e.g., uid)
        Query userQuery = databaseRef.child("Users").orderByChild("uid").equalTo(user.getUid());

        // Remove the user from the database
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();

                    // Delete the user from Firebase Authentication
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), user.getPassword());
                    firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                        if (firebaseUser != null) {
                                            firebaseUser.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(itemView.getContext(), "Delete User Successfully", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                // Handle error deleting user from Firebase Authentication
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        // Handle error authenticating user
                                    }
                                }
                            });

                    // Remove the user from your data source
                    items.remove(position);

                    // Notify the adapter that an item has been removed at the specified position
                    adapter.notifyItemRemoved(position);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the cancellation or error case, if necessary
            }
        });
    }

}




