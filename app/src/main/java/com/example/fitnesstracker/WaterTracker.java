package com.example.fitnesstracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.HashMap;

public class WaterTracker extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    TextView glasstv;
    Button track_waterbtn;
    ImageButton backbtn;

    ImageButton incre_glass, decre_glass;

    int total_glass = 0;

    CircularProgressBar circularProgressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_tracker);

        glasstv = findViewById(R.id.glasstv);
        track_waterbtn = findViewById(R.id.track_waterbtn);

        firebaseAuth = FirebaseAuth.getInstance();

        incre_glass = findViewById(R.id.incre_water);
        decre_glass = findViewById(R.id.decre_water);
        backbtn = findViewById(R.id.backbtn);



        circularProgressBar = findViewById(R.id.progress_circular);

        incre_glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total_glass++;
                glasstv.setText(total_glass+"");
                circularProgressBar.setProgressWithAnimation(total_glass,1000L);

            }
        });

        decre_glass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(total_glass>0){
                    total_glass--;
                    glasstv.setText(total_glass+"");
                    circularProgressBar.setProgressWithAnimation(total_glass,1000L);

                }


            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WaterTracker.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        track_waterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(total_glass==0){
                    saveToDB();
                }
                else {
                    updateDB();

                }

            }
        });

        loadDetails();
    }

    private void loadDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("Uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            String total_glass_str = ""+ds.child("Water").child("Water Consumed").getValue();

                            if(total_glass_str.equals("null")){
                                Toast.makeText(WaterTracker.this,"Track Water Consumed",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                total_glass = Integer.parseInt(total_glass_str);
                                glasstv.setText(total_glass_str+"");
                                circularProgressBar.setProgressWithAnimation(total_glass,1000L);

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void updateDB() {

        String total_glass = glasstv.getText().toString().trim();

        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("Water Consumed",""+total_glass);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Water").updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(WaterTracker.this,"Water Updated",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(WaterTracker.this,"Error in Tracking\nPlease try later",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveToDB() {

        String total_glass = glasstv.getText().toString().trim();

        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("Water Consumed",""+total_glass);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("Water").setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(WaterTracker.this,"Water Tracked",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(WaterTracker.this,"Error in Tracking\nPlease try later",Toast.LENGTH_SHORT).show();
                    }
                });


    }
}