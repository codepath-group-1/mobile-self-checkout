package com.codepath.shopmyself.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.shopmyself.R;
import com.codepath.shopmyself.models.CreditCard;
import com.codepath.shopmyself.models.Item;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vinaygaba.creditcardview.CardType;
import com.vinaygaba.creditcardview.CreditCardView;

import org.parceler.Parcels;

import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    CreditCardView creditCardView;
    EditText cardNameLocal;
    EditText cardNumberLocal;
    ImageView typeLocal;
    ImageView brandLogoLocal;
    ImageView chipLocal;
    EditText expiryDateLocal;

    int cardNumberCount;
    int cardNameCount;
    int expiryDateCount;

    CreditCard savedCard;
    CheckBox cbSaveCard;

    ArrayList<Item> itemArrayList;

    Button btnConfirmation;
    TextView tvTotalPrice;

    double total;
    int count;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private DatabaseReference mDatabase;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mUserId = mFirebaseUser.getUid();

        //getting cart information from intents
        itemArrayList = Parcels.unwrap(getIntent().getParcelableExtra("itemList"));
        total = getIntent().getExtras().getDouble("total");
        count = getIntent().getExtras().getInt("count");

        btnConfirmation = (Button) findViewById(R.id.btnConfirmation);
        btnConfirmation.setEnabled(false);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalAmount);

        //set total price in text view
        tvTotalPrice.setText("(" + count + " Items) - " + String.format("$%.2f", total));

        creditCardView = (CreditCardView) findViewById(R.id.card_name1);
        cardNameLocal = (EditText) findViewById(com.vinaygaba.creditcardview.R.id.card_name);
        cardNumberLocal = (EditText) findViewById(com.vinaygaba.creditcardview.R.id.card_number);
        typeLocal = (ImageView) findViewById(com.vinaygaba.creditcardview.R.id.card_logo);
        brandLogoLocal = (ImageView) findViewById(com.vinaygaba.creditcardview.R.id.brand_logo);
        chipLocal = (ImageView) findViewById(com.vinaygaba.creditcardview.R.id.chip);
        expiryDateLocal = (EditText) findViewById(com.vinaygaba.creditcardview.R.id.expiry_date);

        cardNameLocal.setHint("ENTER CARD NAME");
        cardNumberLocal.setHint("ENTER CARD NUMBER");
        expiryDateLocal.setHint("ENTER EXP DATE");

        cardNumberCount = cardNumberLocal.getText().length();
        cardNameCount = cardNameLocal.getText().length();
        expiryDateCount = expiryDateLocal.getText().length();

        // Listen for Text Change
        setValidatorListeners();
        // Listen for Confirmation Click
        setConfirmationListener();

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

        if (buttonOkToEnable()) {
            //set confirmation button clickable
            btnConfirmation.setEnabled(true);
        }
    }

    public boolean buttonOkToEnable () {
        if(     creditCardView != null &&
                itemArrayList != null &&
                cardNameCount != 0 &&
                cardNumberCount != 0 &&
                expiryDateCount != 0) {
            return true;
        }
        return false;
    }

    public void launchActivity(View v) {
        Intent intent = new Intent(getApplicationContext(), ReceiptActivity.class);
        intent.putExtra("itemList", Parcels.wrap(itemArrayList));
        Bundle bundle = new Bundle();
        bundle.putDouble("total", total);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public void setConfirmationListener() {
        btnConfirmation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                saveCreditCardPay();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        launchActivity(view);
                    }
                }, 3000);


            }
        });
    }

    public void setValidatorListeners () {
        cardNumberLocal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cardNumberCount = charSequence.length();
                if (buttonOkToEnable()) {
                    //set confirmation button clickable
                    btnConfirmation.setEnabled(true);
                } else {
                    btnConfirmation.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        cardNameLocal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cardNameCount = charSequence.length();
                if (buttonOkToEnable()) {
                    //set confirmation button clickable
                    btnConfirmation.setEnabled(true);
                } else {
                    btnConfirmation.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        expiryDateLocal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                expiryDateCount = charSequence.length();
                if (buttonOkToEnable()) {
                    //set confirmation button clickable
                    btnConfirmation.setEnabled(true);
                } else {
                    btnConfirmation.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }


    public void saveCreditCardPay () {
        if(cbSaveCard.isChecked()) {
            String cardNumber = creditCardView.getCardNumber();
            String cardName = creditCardView.getCardName();
            String expiryDate = creditCardView.getExpiryDate();
            savedCard = new CreditCard(cardNumber, expiryDate, cardName);
            savedCard.save();
            Log.d("Credit Card", "Credit Card info saved");
            Toast.makeText(this, "Credit Card info saved" , Toast.LENGTH_SHORT).show();
            btnConfirmation.setText("Processing payment. Please wait");
        }
    }

    public void refreshCard(CreditCardView ccView) {
        cardNameLocal.setText(ccView.getCardName());
        cardNumberLocal.setText(ccView.getCardNumber());
        expiryDateLocal.setText(ccView.getExpiryDate());
    }
}
