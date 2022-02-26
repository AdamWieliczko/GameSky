package pl.edu.uj.wieliczko.shopapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import pl.edu.uj.wieliczko.shopapplication.models.User
import pl.edu.uj.wieliczko.shopapplication.realm.realmModelOperations.UserDatabaseOperations
import pl.edu.uj.wieliczko.shopapplication.servicehandlers.getUser
import pl.edu.uj.wieliczko.shopapplication.servicehandlers.postUser

class ActivityRegistration : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
    }

    private fun createNormalFieldsList(): MutableList<String> {
        val normalFields: MutableList<String> = ArrayList()
        normalFields.add(findViewById<EditText>(R.id.insertEmail).text.toString())
        normalFields.add(findViewById<EditText>(R.id.insertName).text.toString())
        normalFields.add(findViewById<EditText>(R.id.insertSurname).text.toString())
        normalFields.add(findViewById<EditText>(R.id.insertPassword).text.toString())
        normalFields.add(findViewById<EditText>(R.id.repeatPassword).text.toString())
        return normalFields
    }

    private fun createEmailFieldsList(): MutableList<String> {
        val emailFields: MutableList<String> = ArrayList()

        emailFields.add(findViewById<EditText>(R.id.insertEmail).text.toString())
        return emailFields
    }

    private fun createPasswordFieldsList(): MutableList<String> {
        val passwordFields: MutableList<String> = ArrayList()

        passwordFields.add(findViewById<EditText>(R.id.insertPassword).text.toString())
        passwordFields.add(findViewById<EditText>(R.id.repeatPassword).text.toString())
        return passwordFields
    }

    private fun validated(): Boolean {
        val normalFields = createNormalFieldsList()
        val emailFields = createEmailFieldsList()
        val passwordFields = createPasswordFieldsList()

        val nickname = findViewById<EditText>(R.id.insertNickname).text.toString()
        getUser(nickname)
        normalFields.add(nickname)

        normalFields.forEach {
            if(it.isEmpty()) {
                return false
            }
        }
        passwordFields.forEach { pass ->
            passwordFields.forEach() { passTwo ->
                if(!pass.equals(passTwo)) {
                    return false
                }
            }
        }
        emailFields.forEach {
            if(!it.substringAfter('@').contains('.')) {
                return false
            }
        }

        if(UserDatabaseOperations.getRealmUsers().stream().anyMatch{i -> i.nickname == nickname }) {
            return false
        }

        return true
    }

    fun goBack(view: View) {
        this.finish()
    }

    private fun createNewUserAndUpdateInRealmDB() {
        val nickname = findViewById<EditText>(R.id.insertNickname).text.toString()
        val user = User(
            nickname,
            findViewById<EditText>(R.id.insertName).text.toString(),
            findViewById<EditText>(R.id.insertSurname).text.toString(),
            findViewById<EditText>(R.id.insertPassword).text.toString()
        )

        postUser(user)
        getUser(nickname)
    }

    fun createAccount(view: View) {
        if(validated()) {
            createNewUserAndUpdateInRealmDB()
            UserDatabaseOperations.synchronizeUser()
            this.finish()
        }
    }
    fun loginAfterCreatingAccount(view: View) {
        if(validated()) {
            val nickname = findViewById<EditText>(R.id.insertNickname).text.toString()
            createNewUserAndUpdateInRealmDB()
            this.finish()
            val loginToShop = Intent(this, ActivityProducts::class.java).putExtra("UserName", nickname)
            startActivity(loginToShop)
        }
    }
}