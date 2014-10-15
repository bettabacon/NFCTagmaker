/**
 * Copyright (C) 2014 Mateusz Szafraniec
 * This file is part of Burze nad Polską.
 *
 * Burze nad Polską is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Burze nad Polską is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Burze nad Polską; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Ten plik jest częścią Burze nad Polską.
 *
 * Burze nad Polską jest wolnym oprogramowaniem; możesz go rozprowadzać dalej
 * i/lub modyfikować na warunkach Powszechnej Licencji Publicznej GNU,
 * wydanej przez Fundację Wolnego Oprogramowania - według wersji 2 tej
 * Licencji lub (według twojego wyboru) którejś z późniejszych wersji.
 *
 * Niniejszy program rozpowszechniany jest z nadzieją, iż będzie on
 * użyteczny - jednak BEZ JAKIEJKOLWIEK GWARANCJI, nawet domyślnej
 * gwarancji PRZYDATNOŚCI HANDLOWEJ albo PRZYDATNOŚCI DO OKREŚLONYCH
 * ZASTOSOWAŃ. W celu uzyskania bliższych informacji sięgnij do
 * Powszechnej Licencji Publicznej GNU.
 *
 * Z pewnością wraz z niniejszym programem otrzymałeś też egzemplarz
 * Powszechnej Licencji Publicznej GNU (GNU General Public License);
 * jeśli nie - napisz do Free Software Foundation, Inc., 59 Temple
 * Place, Fifth Floor, Boston, MA  02110-1301  USA
 */

package pl.net.szafraniec.msfunctions.lite;

import pl.net.szafraniec.NFCTagmaker.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutDialog extends Dialog {
    private static int mIcon;
    private static String mInfo;
    private static String mLegal;
    private static Context mContext = null;

    public AboutDialog(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * This is the standard Android on create method that gets called when the
     * activity initialized.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.about);
        TextView tv = (TextView) findViewById(R.id.legal_text);
        if (Tools.notNullOrEmpty(mLegal)) {
            tv.setText(Html.fromHtml(mLegal));
        }
        else {
            tv.setText(Html.fromHtml(Tools.readRawTextFile(R.raw.legal,
                    mContext)));
        }
        tv = (TextView) findViewById(R.id.info_text);
        if (Tools.notNullOrEmpty(mInfo)) {
            tv.setText(Html.fromHtml(mInfo));
        }
        else {
            tv.setText(Html.fromHtml(Tools
                    .readRawTextFile(R.raw.info, mContext)
                    + Tools.getAppVersion(mContext)));
        }
        tv.setLinkTextColor(Color.WHITE);
        final ImageView iv = (ImageView) findViewById(R.id.aboutIcon);
        iv.setImageResource(mIcon);
        Linkify.addLinks(tv, Linkify.ALL);
        this.setTitle(mContext.getString(R.string.action_about));

    }

    public void setIcon(int icon) {
        mIcon = icon;
    }

    public void setInfo(String info) {
        mInfo = info;
    }

    public void setLegal(String legal) {
        mLegal = legal;
    }

}
