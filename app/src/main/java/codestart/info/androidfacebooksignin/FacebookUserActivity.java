package codestart.info.androidfacebooksignin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.squareup.picasso.Picasso;

public class FacebookUserActivity extends AppCompatActivity {

    private ImageView mPhotoImageView;
    private TextView mNameTextView;
    private TextView mEmailTextView;
    private TextView mGenderTextView;
    private Button mLogOutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_user);

        //assign the views
        mPhotoImageView = (ImageView)findViewById(R.id.imageView);
        mNameTextView = (TextView)findViewById(R.id.nameTextView);
        mEmailTextView = (TextView)findViewById(R.id.emailTextView);
        mGenderTextView = (TextView)findViewById(R.id.genderTextView);
        mLogOutButton = (Button)findViewById(R.id.logout_button);

        try {
            //get intent which has our user data
            String name = getIntent().getStringExtra("NAME");
            String email = getIntent().getStringExtra("EMAIL");
            String gender = getIntent().getStringExtra("GENDER");
            String photoUrl = getIntent().getStringExtra("PHOTO_URL");
            Log.d("RONY", photoUrl);

            /**set the received data to the respective view**/
            mNameTextView.setText(name);
            mEmailTextView.setText(email);
            mGenderTextView.setText(gender);
            Picasso.with(this).load(photoUrl).placeholder(R.mipmap.ic_launcher).into(mPhotoImageView);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }
    private void logOut(){
        LoginManager.getInstance().logOut();
        finish(); //finish the activity
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logOut();
    }


}
