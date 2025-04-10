# InterTrain - Gervigreindar viðtalsþjálfi
## Lokaverkefni í Viðmótsforritun vorið 2025 - Viktor og Viktor


Við ákváðum að halda áfram með InterviewTrainer úr verkefni 4 og tengja það við gervigreindarlíkan.
Lokaútfærslan er þó nokkuð frábrugðin InterviewTrainer.

Gervigreindarlíkanið sem varð fyrir valinu er Gemini 2.0 Flash líkanið frá Google, helsta ástæða fyrir þessu vali er að hægt að er fá ókeypis aðgang að API-inu þeirra.
Þetta reyndist okkur nokkuð vel en það er þó einn galli, líkanið man ekki fyrri skilaboð.
Við leystum þann vanda á nokkuð einfaldan máta, við höldum einfaldlega utan um spjallsöguna og sendum hana í heild sinni í hvert skipti.
Þetta er klárlega ekki besta lausnin, auðvitað væri best að þurfa þess ekki þar sem þetta skapar nokkuð mikið overhead og vex ansi hratt.
En það er ekki raunhæft að vera með hærri væntingar frá ókeypis líkani.

Hér eru nokkar helstu breytingar sem við gerðum á virkni forritsins:
- Smíðuðum spjallsíðu í stað þess að vera með lista spurninga
- Tengdum við gervigreindarlíkan
- Breyttum útlitshönnun að mestu leiti


### Virkni

Til þess að tengjast Gemini forritaskilunum skrifuðum við tvo nýja klasa: **GeminiChatSession** og **InterviewBot**

**GeminiChatSession**

Sér um tengingu við Gemini API-ið og helstu aðgerðir (senda og taka við svörum) og heldur einnig utanum skilaboðasögu.
Eins og áður kom fram útfærðum við "minni" með því að senda einfaldlega alla skilaboðasöguna í hvert skipti sem notandi sendir skilaboð.

**InterviewBot**

Sá klasi sem viðmótið talar við og sér um að fá Gemini til að þykjast vera að taka atvinnuviðtal.
Er í raun milliliður fyrir framenda og GeminiChatSession til að auðvelda framenda að kalla fram viðtalsvirknina (og undirbúningsvirkni).

#### Keyrsluleiðbeiningar

Til að byggja og keyra forritið í Maven skal fylgja eftirfarandi leiðbeiningum.

1. Klóna repo-ið með ```git clone https://github.com/Nussari/InterTrain.git```
2. Opna terminal glugga í möppunni sem þú sóttir
3. Compile-a forritið með því að keyra ```mvn compile```
4. Keyra upp viðmótið með JavaFX: ```javafx:run```
