package com.example.minimemorygame;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

public final class MemoryCardButton
{
    /**
     * Dostep do wszystkich przyciskow
     */
    public  static List<MemoryCardButton> AllMemoryCards = new ArrayList<MemoryCardButton>(16);

    /**
     * Przycisk do ktorego naleze
     */
    Button m_Button_Owner;

    /**
     * Bierze ID Buttonika
     * @return
     */
    public int getM_Int_ID()
    {
        return m_Int_ID;
    }

    /**
     * Ustawia ID buttonika
     * @param m_Int_ID
     */
    public void setM_Int_ID(int m_Int_ID)
    {
        this.m_Int_ID = m_Int_ID;
    }

    int m_Int_ID;


    public boolean isM_bool_IsActive()
    {
        return m_bool_IsActive;
    }

    public void setM_bool_IsActive(boolean m_bool_IsActive)
    {
        this.m_bool_IsActive = m_bool_IsActive;
        this.m_Button_Owner.setVisibility((m_bool_IsActive)? View.VISIBLE : View.INVISIBLE);
    }

    boolean m_bool_IsActive = true;

    /**
     * Pokazuje, badz ukrywa numer
     * @param _show
     */
    public void  ShowNumber(boolean _show)
    {
        m_Button_Owner.setText((_show)? Integer.toString(m_Int_ID) : "");
    }

    /**
     * Tworzy lepszy kontener nad buttonika
     */
    public MemoryCardButton(Button _button)
    {
        m_Button_Owner = _button;

        //Wyliczamy podstawowe parametry
        m_Int_ID = (AllMemoryCards.size()+2)/2;
        MemoryCardButton.AllMemoryCards.add(this);

        //Bo Java jest gupia i nie potrafi dobrze ogarniac delegatow...
        if(m_Button_Owner != null)
        {
            m_Button_Owner.setOnClickListener(new View.OnClickListener()
            {
                //Serio? Musze sie uciekac do takich zabiegow?
                MemoryCardButton ptr = AllMemoryCards.get(AllMemoryCards.size()-1);

                @Override
                public void onClick(View v)
                {
                    //Bo oczywiscie java nie wie co to dodawanie wskaznikow na funkcje...
                    MainActivity.Single.OnClickUpdate(ptr);
                }
            });
        }

        m_bool_IsActive = true;
    }
}
