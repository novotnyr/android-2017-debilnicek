Debilníček
==========

Jednoduchá aplikácia demonštrujúca vlastnosti Androidu.

*   `ListView` pre zoznam položiek
*   `FloatingActionButton` pre pridávanie nových položiek
*   `Dialog` pre získavanie jednoduchých informácií od používateľa
*   `ArrayAdapter` pre udržiavanie dát pre zoznam `ListView`
*   prispôsobenie vizuálu položiek v `ArrayAdapter`i. Položky, ktoré sú hotové, sú preškrtnuté
*   `SharedPreferences` ako spôsob perzistencie jednoduchých dát

Poznámky
--------

### Dáta

Perzistentné dáta sa ukladajú do Shared Preferences.

### Android Material Design

Projekt využíva Android Material Design Library. Do `build.gradle` bola pridaná závislosť.

    compile 'com.android.support:design:24.2.1'

Z tejto knižnice sa využíva *Floating Action Button*.

