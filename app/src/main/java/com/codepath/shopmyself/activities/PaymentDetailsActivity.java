package com.codepath.shopmyself.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.shopmyself.R;
import com.codepath.shopmyself.models.CreditCard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vinaygaba.creditcardview.CardType;
import com.vinaygaba.creditcardview.CreditCardView;

public class PaymentDetailsActivity extends AppCompatActivity {

    CreditCardView creditCardView;
    EditText cardNameLocal;
    EditText cardNumberLocal;
    ImageView typeLocal;
    ImageView brandLogoLocal;
    ImageView chipLocal;
    EditText expiryDateLocal;

    CreditCard savedCard;
    CheckBox cbSaveCard;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private DatabaseReference mDatabase;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mUserId = mFirebaseUser.getUid();

        creditCardView = (CreditCardView) findViewById(R.id.card_name1);
        cardNameLocal = (EditText) findViewById(com.vinaygaba.creditcardview.R.id.card_name);
        cardNumberLocal = (EditText) findViewById(com.vinaygaba.creditcardview.R.id.card_number);
        typeLocal = (ImageView) findViewById(com.vinaygaba.creditcardview.R.id.card_logo);
        brandLogoLocal = (ImageView) findViewById(com.vinaygaba.creditcardview.R.id.brand_logo);
        chipLocal = (ImageView) findViewById(com.vinaygaba.creditcardview.R.id.chip);
        expiryDateLocal = (EditText) findViewById(com.vinaygaba.creditcardview.R.id.expiry_date);

        mDatabase.child("users").child(mUserId).child(CreditCard.key)
                 .addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         if (dataSnapshot.hasChildren()) {
                             savedCard = new CreditCard(dataSnapshot);

                             Log.d("sup:", "savedCard name: " + savedCard.getCardName());
                             creditCardView.setCardName(savedCard.getCardName());
                             creditCardView.setCardNumber(savedCard.getCardNumber());
                             creditCardView.setExpiryDate(savedCard.getCardExpiry());
                             creditCardView.setType(CardType.VISA);
                             cbSaveCard.setChecked(true);

                             refreshCard(creditCardView);
                         }
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {
                    }
                 });

        cbSaveCard = (CheckBox) findViewById(R.id.cbSaveCard);
    }

    public void saveCreditCardPay (View v) {

        if(cbSaveCard.isChecked()) {

            String cardNumber = creditCardView.getCardNumber();
            String cardName = creditCardView.getCardName();
            String expiryDate = creditCardView.getExpiryDate();
            savedCard = new CreditCard(cardNumber, expiryDate, cardName);
            savedCard.save();
            Log.d("Credit Card", "Credit Card info saved");
            Toast.makeText(this, "Credit Card info saved" , Toast.LENGTH_SHORT).show();
        }

        launchCheckout();
    }

    public void refreshCard(CreditCardView ccView) {
        cardNameLocal.setText(ccView.getCardName());
        cardNumberLocal.setText(ccView.getCardNumber());
        expiryDateLocal.setText(ccView.getExpiryDate());
    }

    public  void launchCheckout() {
        // Launch Checkout here
    }
}

