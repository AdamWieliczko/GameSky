package pl.edu.uj.wieliczko.shopapplication

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import io.realm.Realm
import pl.edu.uj.wieliczko.shopapplication.models.User
import pl.edu.uj.wieliczko.shopapplication.realm.realmModelOperations.UserDatabaseOperations
import pl.edu.uj.wieliczko.shopapplication.servicehandlers.getUser
import pl.edu.uj.wieliczko.shopapplication.servicehandlers.postAndAddToRealmDB


class MainActivity : AppCompatActivity() {
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    private val REQ_ONE_TAP = 2022

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Realm.init(this)
        UserDatabaseOperations.synchronizeUser()

        setContentView(R.layout.activity_main)

        oneTapClient = Identity.getSignInClient(this)
        oneTapClient.signOut()
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(serverClientID)
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()

    }

    fun signInWithGoogle(view: View) {
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    Log.e("GoogleOneTap", "Started")
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("GoogleOneTap", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                Log.d("GoogleOneTap", e.localizedMessage)
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        var nickname = ""
        var password = ""

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    var idToken = credential.googleIdToken
                    if (idToken != null) {
                        if(idToken.length > 50) {
                            idToken = idToken.subSequence(0, 49).toString()
                        }
                        val user = User(
                            credential.id, credential.givenName.toString(),
                            credential.familyName.toString(), idToken
                        )

                        getUser(credential.id)
                        if(!UserDatabaseOperations.getRealmUsers().stream()
                                .anyMatch { i -> i.nickname == nickname }) {
                            postAndAddToRealmDB(user)
                        }


                        nickname = credential.id
                        password = idToken
                        Log.d("GoogleOneTap", "Got ID token.")
                    } else {
                        Log.d("GoogleOneTap", "No ID token!")
                    }
                } catch (e: ApiException) {
                    Log.d("GoogleOneTap", e.message.toString())
                }
            }
        }

        if(UserDatabaseOperations.getRealmUsers().stream()
                .anyMatch { i -> (i.nickname == nickname) && (i.password == password) } && nickname != "" && password != "") {
            val loginToShop = Intent(this, ActivityProducts::class.java).putExtra("UserName", nickname)
            startActivity(loginToShop)
        }
    }


    fun onLogin(view: View) {
        val nickname = findViewById<EditText>(R.id.insertNickname).text.toString()
        getUser(nickname)
        val password = findViewById<EditText>(R.id.insertPassword).text.toString()
        if (UserDatabaseOperations.getRealmUsers().stream()
                .anyMatch { i -> (i.nickname == nickname) && (i.password == password) }
        ) {
            val loginToShop = Intent(this, ActivityProducts::class.java).putExtra("UserName", nickname)
            startActivity(loginToShop)
        }
    }

    fun onRegister(view: View) {
        val registration = Intent(this, ActivityRegistration::class.java)
        startActivity(registration)
    }

    fun exit(view: View) {
        finish()
    }
}