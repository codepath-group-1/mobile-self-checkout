package com.codepath.shopmyself.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    CreditCard savedCard;
    CheckBox cbSaveCard;

    ArrayList<Item> itemArrayList;

    Button btnConfirmation;
    TextView tvTotalPrice;

    double total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getting cart information from intents
        itemArrayList = Parcels.unwrap(getIntent().getParcelableExtra("itemList"));
        total = getIntent().getExtras().getDouble("total");

        btnConfirmation = (Button) findViewById(R.id.btnConfirmation);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalAmount);

        //set total price in text view
        tvTotalPrice.setText(String.format("$%.2f", total));

        creditCardView = (CreditCardView) findViewById(R.id.card_name1);
        cardNameLocal = (EditText) findViewById(com.vinaygaba.creditcardview.R.id.card_name);
        cardNumberLocal = (EditText) findViewById(com.vinaygaba.creditcardview.R.id.card_number);
        typeLocal = (ImageView) findViewById(com.vinaygaba.creditcardview.R.id.card_logo);
        brandLogoLocal = (ImageView) findViewById(com.vinaygaba.creditcardview.R.id.brand_logo);
        chipLocal = (ImageView) findViewById(com.vinaygaba.creditcardview.R.id.chip);
        expiryDateLocal = (EditText) findViewById(com.vinaygaba.creditcardview.R.id.expiry_date);

        savedCard = CreditCard.getInstance();
        cbSaveCard = (CheckBox) findViewById(R.id.cbSaveCard);

        if(savedCard != null) {
            Log.d("sup:", "savedCard name: " + savedCard.getCardName());
            creditCardView.setCardName(savedCard.getCardName());
            creditCardView.setCardNumber(savedCard.getCardNumber());
            creditCardView.setExpiryDate(savedCard.getCardExpiry());
            creditCardView.setType(CardType.VISA);
            refreshCard(creditCardView);
        }


    }

    public void saveCreditCardPay (View v) {

        if(cbSaveCard.isChecked()) {

            String cardNumber = creditCardView.getCardNumber();
            String cardName = creditCardView.getCardName();
            String expiryDate = creditCardView.getExpiryDate();
            savedCard = new CreditCard(cardNumber, expiryDate, cardName);
            CreditCard.setInstance(savedCard);
            Log.d("Credit Card", "Credit Card info saved");
            Toast.makeText(this, "Credit Card info saved" , Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshCard(CreditCardView ccView) {
        cardNameLocal.setText(ccView.getCardName());
        cardNumberLocal.setText(ccView.getCardNumber());
        expiryDateLocal.setText(ccView.getExpiryDate());
    }

}
