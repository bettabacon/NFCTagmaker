/**
 * Copyright (C) 2014 Mateusz Szafraniec
 * This file is part of NFCKey.
 *
 * NFCKey is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * NFCKey is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NFCKey; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Ten plik jest czÄ™Ĺ›ciÄ… NFCKey.
 *
 * NFCKey jest wolnym oprogramowaniem; moĹĽesz go rozprowadzaÄ‡ dalej
 * i/lub modyfikowaÄ‡ na warunkach Powszechnej Licencji Publicznej GNU,
 * wydanej przez FundacjÄ™ Wolnego Oprogramowania - wedĹ‚ug wersji 2 tej
 * Licencji lub (wedĹ‚ug twojego wyboru) ktĂłrejĹ› z pĂłĹşniejszych wersji.
 *
 * Niniejszy program rozpowszechniany jest z nadziejÄ…, iĹĽ bÄ™dzie on
 * uĹĽyteczny - jednak BEZ JAKIEJKOLWIEK GWARANCJI, nawet domyĹ›lnej
 * gwarancji PRZYDATNOĹšCI HANDLOWEJ albo PRZYDATNOĹšCI DO OKREĹšLONYCH
 * ZASTOSOWAĹ�. W celu uzyskania bliĹĽszych informacji siÄ™gnij do
 * Powszechnej Licencji Publicznej GNU.
 *
 * Z pewnoĹ›ciÄ… wraz z niniejszym programem otrzymaĹ‚eĹ› teĹĽ egzemplarz
 * Powszechnej Licencji Publicznej GNU (GNU General Public License);
 * jeĹ›li nie - napisz do Free Software Foundation, Inc., 59 Temple
 * Place, Fifth Floor, Boston, MA  02110-1301  USA
 */
package pl.net.szafraniec.NFCTagmaker;

import android.nfc.NdefMessage;

public class NFCTagmakerSettings {
	public static final String PREFS_NAME= "NFCTagmaker";
	public static NdefMessage nfc_payload;
	public static String uri;
	public static String phone = "1234567890";
	public static String name = "Nazwa";
	public static final String LOG_TAG = "NFCTagmaker";
	
}
