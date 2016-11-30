package com.codepath.shopmyself.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.codepath.shopmyself.R;
import com.codepath.shopmyself.models.CreditCard;
import com.vinaygaba.creditcardview.CardType;
import com.vinaygaba.creditcardview.CreditCardView;

public class PaymentDetailsActivity extends AppCompatActivity {

    CreditCardView creditCardView;
    CreditCard savedCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        creditCardView = (CreditCardView)findViewById(R.id.card_name1);
        savedCard = CreditCard.getInstance();

        if(savedCard != null) {
            creditCardView.setCardName(savedCard.getCardName());
            creditCardView.setCardNumber(savedCard.getCardNumber());
            creditCardView.setExpiryDate(savedCard.getCardExpiry());
            creditCardView.setType(CardType.VISA);
        }
    }

    public void saveCreditCard (View v) {

        String cardNumber = creditCardView.getCardNumber();
        String cardName = creditCardView.getCardName();
        String expiryDate = creditCardView.getExpiryDate();
        savedCard = new CreditCard(cardNumber, expiryDate, cardName);
        CreditCard.setInstance(savedCard);
        Log.d("Credit Card", "Credit Card info saved");

        // TODO: save the card1 with userdetails
    }


}

