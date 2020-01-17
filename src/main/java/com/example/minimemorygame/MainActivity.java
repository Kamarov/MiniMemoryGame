package com.example.minimemorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
{

    /**
     * Czas, kiedy gra wystartowala.
     */
    public long m_long_GameStartTime = System.currentTimeMillis();
    /**
     * Najlepszy czas jaki mamy.
     */
    public long m_long_BestGameTime = Long.MAX_VALUE;

    /**
     * Do glownej petli aplikacji
     */
    Handler handler = null;

    /**
     * Karta, ktora najpierw kliknelismy
     */
    public MemoryCardButton m_MemoryCard_FirstClickedCard = null;
    /**
     * Karta, ktora pozniej kliknelismy
     */
    public MemoryCardButton m_MemoryCard_SecondClickedCard = null;

    /**
     * Wszystkie karty w naszej apce
     */
    protected List<MemoryCardButton> MyCards = new ArrayList<MemoryCardButton>(16);


    /**
     * Dostep do glownego okienka
     */
    public static MainActivity Single;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_long_GameStartTime = System.currentTimeMillis();

        //Dajemy do siebie referencje
        Single = this;

        //Tworzymy petle aplikacji
        InitMemoryCards();
        InitMegaUltraLoop();

        ResetGame(null);
    }


    /**
     * Inicjuje glowna  petle aplikacji...
     */
    private void InitMegaUltraLoop()
    {
        handler = new Handler();

        //Serio, Java? Nie dalo sie tego lepiej zrobic?
        Runnable r = new Runnable()
        {
            public void run()
            {
                OnUpdate();
                handler.postDelayed(this, 1);
            }
        };

        //Przed wyruszeniem w droge nalezy zebrac druzyne...
        handler.postDelayed(r, 500);
    }

    /**
     * Czy wygralismy wlasnie gre?
     * @return
     */
    public boolean IsGameWin()
    {
        //Przynajmniej foreach jest ladniejszy...
        for(MemoryCardButton c : MyCards)
            if(c.isM_bool_IsActive())
                return  false;

        return true;
    }

    /**
     * Czy numerki sie zgadzaja obu kart?
     * @return
     */
    public  boolean CanCollectCards()
    {
        if(m_MemoryCard_SecondClickedCard == null || m_MemoryCard_FirstClickedCard == null)
            return  false;

        return (m_MemoryCard_FirstClickedCard.getM_Int_ID() == m_MemoryCard_SecondClickedCard.getM_Int_ID());
    }

    /**
     * Zbiera aktualne wybrane karty
     */
    public void  Collect()
    {
        m_MemoryCard_FirstClickedCard.setM_bool_IsActive(false);
        m_MemoryCard_SecondClickedCard.setM_bool_IsActive(false);
        m_MemoryCard_FirstClickedCard = null;
        m_MemoryCard_SecondClickedCard = null;
    }


    /**
     * Inicuje wszystkie karty.
     */
    private void InitMemoryCards()
    {
        //Prawie jak javascript, huh....
        this.MyCards = Arrays.asList(
                new MemoryCardButton[]
                        {
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_1)),
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_2)),
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_3)),
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_4)),
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_5)),
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_6)),
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_7)),
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_8)),
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_9)),
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_10)),
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_11)),
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_12)),
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_13)),
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_14)),
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_15)),
                                new MemoryCardButton((Button) findViewById(R.id.Button_Card_16))
                        });
    }

    /**
     * Moja glowna petla aplikacji.
     */
    private void OnUpdate()
    {
        //Update czasu aplikacji
        if(!IsGameWin())
            TickGame();
    }

    /**
     * To, co podepnie nam logike aplikacji pod karty.
     * @param _card
     */
    public void OnClickUpdate(MemoryCardButton _card)
    {
        //No to taki przypadek pomijamy
        if(_card == m_MemoryCard_FirstClickedCard || _card == m_MemoryCard_SecondClickedCard)
            return;

        _card.ShowNumber(true);

        if(m_MemoryCard_FirstClickedCard == null)
            m_MemoryCard_FirstClickedCard = _card;
        else  if(m_MemoryCard_SecondClickedCard == null)
            m_MemoryCard_SecondClickedCard = _card;
        //Gdy juz sa jakies wczesniej dwie odkryte
        else
        {
            //No to tamte wygaszamy
            m_MemoryCard_FirstClickedCard.ShowNumber(false);
            m_MemoryCard_SecondClickedCard.ShowNumber(false);
            m_MemoryCard_FirstClickedCard = null;
            m_MemoryCard_SecondClickedCard = null;

            //A siebie wyswietlamy i zastepujemy referencje
            m_MemoryCard_FirstClickedCard = _card;
        }

        //Sprawdzamy, czy mozemy zebrac karty
        if(CanCollectCards())
            Collect();

        //Jak wygralismy gre, to mozemy nieco pokombinowac z zapisem czasu
        if(IsGameWin())
        {
            long gameTime = System.currentTimeMillis() - m_long_GameStartTime;
            if(gameTime < m_long_BestGameTime)
            {
                //Update najlepszego czasu
                m_long_BestGameTime = gameTime;
                TextView ptr = (TextView) findViewById(R.id.TextView_BestTime_Value);
                ptr.setText(GetTimeString(m_long_BestGameTime));

                //Teraz update aktualnego czasu
                ptr = (TextView) findViewById(R.id.TextView_Time);
                ptr.setText(GetTimeString(gameTime));
            }
        }

    }


    /**
     * To nam zresetuje gierke
     */
    public void ResetGame(View view)
    {
        TextView tvPtr = (TextView) findViewById(R.id.TextView_Time);
        m_long_GameStartTime = System.currentTimeMillis();
        TickGame();

        List<Integer> numbers = new ArrayList<Integer>();

        int len = (MyCards.size() / 2);

        //Dodajemy numerki
        for(int i=0; i<len; i++)
        {
            numbers.add(i+1);
            numbers.add(i+1);
        }

        //Pozbywamy sie kliknietych
        m_MemoryCard_FirstClickedCard = null;
        m_MemoryCard_SecondClickedCard = null;

        //Bedziemy losowac
        Random rand = new Random();

        //Inicjujemy karty
        for(MemoryCardButton c : MyCards)
        {
            Integer index = rand.nextInt(numbers.size());
            c.setM_Int_ID(numbers.get(index));

            //Taa, bo po co pisac removeAt, skoro mozna przeciazyc remove, huh?
            numbers.remove(index.intValue());
            c.ShowNumber(false);
            c.setM_bool_IsActive(true);
        }
    }

    /**
     * Bierze stringa czasu z longa
     * @param _value
     * @return
     */
    public String GetTimeString(long _value)
    {
        SimpleDateFormat timeFormater = new SimpleDateFormat("HH:mm:ss:SSS");
        Date tmpDate = new Date(_value);
        return timeFormater.format(tmpDate);
    }


    /**
     * Liczy nam czas w grze
     */
    public void TickGame()
    {
        long gameTime = System.currentTimeMillis() - m_long_GameStartTime;
        TextView tvPtr = (TextView) findViewById(R.id.TextView_Time);
        tvPtr.setText(GetTimeString(gameTime));
    }
}
