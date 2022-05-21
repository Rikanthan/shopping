package com.example.bloodcamp.Login_front;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodcamp.Home.MainActivity;
import com.example.bloodcamp.Views.Donor;
import com.example.bloodcamp.Views.Member;
import com.example.bloodcamp.R;
import com.example.bloodcamp.Views.UserRole;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity {
    DatabaseReference reff;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=.*[0-9])" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{8,15}" +
                    "$");
    private static final Pattern EMAIL=Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
    private int mYear, mMonth, mDay;
    private EditText fullname;
    private EditText email;
    private EditText mobile;
    private EditText location;
    private EditText pswd;
    private EditText conpswd;
    private EditText nic;
    private TextView dob;
    private EditText city;
    private TextView userHeader;
    private TextView title;
    private boolean isUpdate;
    private static  final int REQUEST_LOCATION=1;
    private Button action;
    private ProgressBar progressBar;
    LocationManager locationManager;
    LinearLayout dateLayout;
    private String date = "";
    String latitude = "6.75",longitude = "79.97";
    Member member;
    String user ="";
    Donor donor;
    private String userType ="BloodCamp";
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();
        fullname = findViewById(R.id.fullName);
        email = findViewById(R.id.userEmailId);
        mobile = findViewById(R.id.mobileNumber);
        location = findViewById(R.id.location);
        pswd = findViewById(R.id.password);
        conpswd = findViewById(R.id.confirmPassword);
        nic = findViewById(R.id.NIC);
        action = findViewById(R.id.signUpBtn);
        dob = findViewById(R.id.DOB);
        city = findViewById(R.id.city);
        member = new Member();
        donor = new Donor();
        progressBar = findViewById(R.id.progress_bar);
        title = findViewById(R.id.title);
        userHeader = findViewById(R.id.userheader);
        radioGroup = findViewById(R.id.radio);
        dateLayout = findViewById(R.id.date_layout);
        reff = FirebaseDatabase.getInstance().getReference().child("Member");
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Check gps is enable or not
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //Write Function To enable gps
            OnGPS();
        }
        else
        {
            //GPS is already On then
            getLocation();
        }
        isUpdate = getIntent().getBooleanExtra("Action",false);
        if(isUpdate)
        {
            userHeader.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            title.setText("Edit User");
            action.setText("Update");
            userType = getIntent().getStringExtra("UserType");
            if(userType.contains("Donor"))
            {
                updateDonor();
            }
            else
            {
                updateMember();
            }
        }
    }
    private void addUser(Member member,String uid)
    {
        firestore.collection("Member")
                .document(uid).set(member);
    }
    private void addUserType(UserRole userRole)
    {

        firestore.collection("UserRole").
                document(userRole.getUid()).set(userRole);
    }
    private void addDonor(Donor donor, String uid)
    {
        firestore.collection("Donor")
                .document(uid).set(donor);
    }

    private boolean validefullname()
    {
        String full=fullname.getText().toString().trim();

        if(full.isEmpty())
        {
            fullname.setError("Fullname can't be empty");
            return false;
        }
        return  true;
    }
    private boolean validelocation()
    {
        String place=location.getText().toString().trim();
        if(place.isEmpty())
        {
            location.setError("location can't be empty");
            return false;
        }
        return  true;
    }
    private boolean validemobile()
    {
        String mob=mobile.getText().toString().trim();
        if(mob.isEmpty())
        {
            mobile.setError("mobile number can't be empty");
            return false;
        }
        return  true;
    }
    private boolean validecon()
    {
        String con = conpswd.getText().toString().trim();
        String ps = pswd.getText().toString().trim();
        if(con.isEmpty())
        {
            conpswd.setError("Field can't be empty");
            return false;
        }
        else if(!con.equals(ps))
        {
            conpswd.setError("Password didn't match");
            return  false;
        }
        return  true;
    }

    private boolean valideEmail() {
        String emailinput = email.getText().toString().trim();
        if (emailinput.isEmpty()) {
            email.setError("Email can't be empty");
            return false;
        } else if (!EMAIL.matcher(emailinput).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validePassword() {
        String passinput = pswd.getText().toString().trim();
        if (passinput.isEmpty()) {
            pswd.setError("password can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passinput).matches()) {
            pswd.setError("Password too weak");
            return false;
        } else {
            pswd.setError(null);
            return true;
        }
    }
    public void onUserClicked(View v)
    {
        boolean checked = ((RadioButton) v).isChecked();
        // Check which radio button was clicked
        switch(v.getId()) {
            case R.id.bloodcamp:
                if (checked)
                {
                    userType = "BloodCamp";
                    dateLayout.setVisibility(View.GONE);
                    nic.setVisibility(View.GONE);
                    city.setVisibility(View.GONE);
                }
                    break;
            case R.id.donor:
                if (checked)
                {
                    userType = "Donor";
                    dateLayout.setVisibility(View.VISIBLE);
                    nic.setVisibility(View.VISIBLE);
                    city.setVisibility(View.VISIBLE);
                }
                    break;
        }
    }

    private void updateDonor()
    {
        firestore
                .collection("Donor")
                .document(firebaseAuth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful())
                   {
                       Donor donor1 = task.getResult().toObject(Donor.class);
                       fullname.setText(donor1.getName());
                       email.setText(donor1.getEmail());
                       mobile.setText(donor1.getPhoneNumber());
                       location.setText(donor1.getCity());
                       pswd.setText(donor1.getPassword());
                       conpswd.setText(donor1.getPassword());
                       nic.setText(donor1.getNIC());
                       dob.setText(donor1.getDOB());
                       city.setText(donor1.getCity());
                   }
                });
    }
    private void updateMember()
    {
        if(userType.contains("Admin"))
        {
            user = "Admin";
        }
        else
        {
            user = "Member";
        }
        firestore
                .collection(user)
                .document(firebaseAuth.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Member donor1 = task.getResult().toObject(Member.class);
                        fullname.setText(donor1.getName());
                        email.setText(donor1.getEmail());
                        mobile.setText(String.valueOf(donor1.getMobile()));
                        location.setText(donor1.getLocation());
                        pswd.setText(donor1.getPassword());
                        conpswd.setText(donor1.getPassword());
                    }
                });
    }
    public void signin(View v) {

        System.out.println(userType);
        if(userType.contains("BloodCamp") || userType.contains("Admin"))
        {
            member.setName(fullname.getText().toString().trim());
            member.setEmail(email.getText().toString().trim());
            member.setPassword(pswd.getText().toString().trim());
            try {
                Long phn=Long.parseLong(mobile.getText().toString().trim());
                member.setMobile(phn);
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
            member.setLocation(location.getText().toString().trim());
            member.setUserType(userType);
        }
        else
        {
            donor.setName(fullname.getText().toString().trim());
            donor.setEmail(email.getText().toString().trim());
            donor.setPhoneNumber(mobile.getText().toString().trim());
            donor.setAddress(location.getText().toString().trim());
            donor.setDOB(dob.getText().toString().trim());
            donor.setNIC(nic.getText().toString().trim());
            donor.setCity(city.getText().toString().trim());
            donor.setLatitude(Double.parseDouble(latitude));
            donor.setLongitude(Double.parseDouble(longitude));
            donor.setPassword(pswd.getText().toString().trim());
        }
        if (!valideEmail() | !validePassword() | !validecon() |!validefullname() |!validelocation() |!validemobile()) {
            return;
        }
        if(isUpdate)
        {
            updateUser();
        }
        else
        {
            createUser();
        }
    }
    private void updateUser()
    {
        action.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth
                .getCurrentUser()
                .updateEmail(email.getText().toString().trim())
                .addOnCompleteListener(
                        task -> {
                            firebaseAuth
                                    .getCurrentUser()
                                    .updatePassword(pswd.getText().toString().trim())
                                    .addOnCompleteListener( task2 -> {
                                        if(task2.isSuccessful())
                                        {
                                            if(userType.contains("Donor"))
                                            {
                                                updateAndSaveDonor();
                                            }
                                            else
                                            {
                                                updateAndSaveUser(user);
                                            }
                                        }
                                    });
                        });
    }
    public void chooseDate(View v)
    {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    if(dayOfMonth < 10 && monthOfYear < 10)
                    {
                        date = year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth;
                    }
                    else if(dayOfMonth < 10)
                    {
                        date = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                    }
                    else if(monthOfYear < 10)
                    {
                        date = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }
                    else
                    {
                        date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    }
                    dob.setText(date);
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    private void updateAndSaveDonor()
    {
        firestore
                .collection("Donor")
                .document(firebaseAuth.getUid())
                .set(donor)
                .addOnSuccessListener(task ->{
                    Toast.makeText(this,"Donor Details updates successfully",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this,MainActivity.class);
                    startActivity(i);
                    progressBar.setVisibility(View.GONE);
                });
    }
    private void updateAndSaveUser(String userType)
    {
        member.setName(fullname.getText().toString().trim());
        firestore
                .collection(userType)
                .document(firebaseAuth.getUid())
                .set(member)
                .addOnSuccessListener(task ->{
                    Toast.makeText(this, userType+" Details updates successfully",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this,MainActivity.class);
                    startActivity(i);
                    progressBar.setVisibility(View.GONE);
                });
    }
    private void createUser()
    {
        UserRole userRole = new UserRole();
        firebaseAuth
                .createUserWithEmailAndPassword(
                        email.getText().toString().trim(),
                        pswd.getText().toString().trim())
                .addOnCompleteListener(
                        task -> {
                            if(task.isSuccessful())
                            {
                                String uid = task.getResult().getUser().getUid();
                                if(userType.contains("BloodCamp"))
                                {
                                    reff.child(uid).setValue(member);
                                    userRole.setName(member.getName());
                                    addUser(member,uid);
                                }
                                else if(userType.contains("Donor"))
                                {
                                    donor.setId(uid);
                                    reff.child(uid).setValue(donor);
                                    addDonor(donor,uid);
                                    userRole.setName(donor.getName());
                                }
                                userRole.setUserRole(userType);
                                userRole.setUid(uid);
                                addUserType(userRole);
                                Toast.makeText(SignIn.this, "Account has been successfully created", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(SignIn.this, MainActivity.class);
                                startActivity(i);
                            }
                            else if(!task.isSuccessful())
                            {
                                Toast.makeText(SignIn.this,"Email id already exists",Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }

    private void getLocation() {
        //Check Permissions again
        if (ActivityCompat.checkSelfPermission(SignIn.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(SignIn.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                System.out.println("Lattitude : "+ latitude);
            }
            else if (LocationNetwork !=null)
            {
                double lat = LocationNetwork.getLatitude();
                double longi = LocationNetwork.getLongitude();
                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                System.out.println("Lattitude : "+ latitude);
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();
                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                System.out.println("Lattitude : "+ latitude);
            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void OnGPS() {
        final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES",
                (dialog, which)
                        -> startActivity(
                                new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("NO", (dialog, which) -> dialog.cancel());
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}