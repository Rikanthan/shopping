package com.example.login_page.Login_front;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.login_page.Home.MainActivity;
import com.example.login_page.Views.Member;
import com.example.login_page.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import java.util.regex.Pattern;


public class sign_in extends AppCompatActivity {

    DatabaseReference reff;
    FirebaseAuth firebaseAuth;
    long maxid=0;
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

    private EditText fullname;
    private EditText email;
    private EditText mobile;
    private EditText location;
    private EditText pswd;
    private EditText conpswd;
    Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        fullname = findViewById(R.id.fullName);
        email = findViewById(R.id.userEmailId);
        mobile = findViewById(R.id.mobileNumber);
        location = findViewById(R.id.location);
        pswd = findViewById(R.id.password);
        conpswd = findViewById(R.id.confirmPassword);

        member=new Member();

        reff= FirebaseDatabase.getInstance().getReference().child("Member");
        firebaseAuth=FirebaseAuth.getInstance();
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                maxid=(snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




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

        String con=conpswd.getText().toString().trim();
        String ps=pswd.getText().toString().trim();
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

    public void signin(View v) {

        member.setName(fullname.getText().toString().trim());//fullname ;
        member.setEmail(email.getText().toString().trim());//email = findViewById(R.id.userEmailId);
        Long phn=Long.parseLong(mobile.getText().toString().trim());
        member.setMobile(phn);//mobile = findViewById(R.id.mobileNumber);
        member.setLocation(location.getText().toString().trim());//location = findViewById(R.id.location);
        member.setPassword(pswd.getText().toString().trim());//pswd = findViewById(R.id.password);
        //conpswd = findViewById(R.id.confirmPassword);


        if (!valideEmail() | !validePassword() | !validecon() |!validefullname() |!validelocation() |!validemobile()) {
            return;
        }
        String input = "Email: " + email.getText().toString();
        input += "\n";

        reff.child(String.valueOf(maxid+1)).setValue(member);
        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString().trim(),pswd.toString().trim());
        Toast.makeText(this, "Data input & user created sucessfully", Toast.LENGTH_SHORT).show();
        Intent i=new Intent(this, MainActivity.class);
        startActivity(i);
    }
}