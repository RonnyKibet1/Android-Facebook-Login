package codestart.info.androidfacebooksignin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private LoginButton mLoginButton;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //assign call back manager
        callbackManager = CallbackManager.Factory.create();

        //find the login button
        mLoginButton = (LoginButton)findViewById(R.id.login_button);
        mLoginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        logInCallBackRegister();

    }

    private void logInCallBackRegister(){



        // Callback registration
        mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //use profile tracker to get user public info, except email and gender which you will have to use graph request
                //getFbInfoWithProfileTracker();

                //we use this to get user email as well as the rest of the info also included using the profile tracker
                getUserProfileWithGraphRequest(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(MainActivity.this, "login canceled by user", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(MainActivity.this, "Error " + exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**get public data**/
    private void getFbInfoWithProfileTracker() {
        ProfileTracker mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                // Fetch user details from New Profile
                //if new profile is null, use the old profile
                String name;
                String photo;
                if(newProfile != null){
                     name = newProfile.getName();
                     photo = newProfile.getProfilePictureUri(150, 150).toString();
                }else{
                    name = oldProfile.getName();
                    photo = oldProfile.getProfilePictureUri(150, 150).toString();
                }

                Toast.makeText(MainActivity.this, "" + newProfile.getName(), Toast.LENGTH_SHORT).show();

            }
        };
    }

    /**get public and private data like fetch email by requesting user permissions**/
    private void getUserProfileWithGraphRequest(AccessToken accessToken){
        //Toast.makeText(MainActivity.this,"Please Wait...",Toast.LENGTH_SHORT).show();
        GraphRequest request = GraphRequest.newMeRequest(accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String email_id = object.getString("email");
                            String gender = object.getString("gender");
                            String profile_name = object.getString("name");
                            long fb_id = object.getLong("id");
                            //get photo URL and convert to string, make sure you use https as facebook photo with android can cause problems
                            URL img_value = new URL("https://graph.facebook.com/"+fb_id+"/picture?type=large");
                            String photoUrl = img_value.toString();

                            goToViewUserProfileActivity(email_id, profile_name, fb_id, gender, photoUrl);

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                             e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, gender");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void goToViewUserProfileActivity(String email, String name, long id, String gender, String photoUrl){
        Intent goToFbUserActivity = new Intent(this, FacebookUserActivity.class);
        goToFbUserActivity.putExtra("EMAIL", email);
        goToFbUserActivity.putExtra("NAME", name);
        goToFbUserActivity.putExtra("ID", id);
        goToFbUserActivity.putExtra("GENDER", gender);
        goToFbUserActivity.putExtra("PHOTO_URL", photoUrl);
        startActivity(goToFbUserActivity);

    }
}
