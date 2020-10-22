# Transformers

Icons taken from a website called Flat Icon 

AutoBot: https://www.flaticon.com/free-icon/transformer_813423?term=transformers&page=1&position=22
Decepticon: https://www.flaticon.com/free-icon/transformer_813433?term=transformers&page=1&position=35
Battle Icon: https://www.flaticon.com/free-icon/swords_1191182?term=swords&page=1&position=28
Plus Icon: https://www.flaticon.com/free-icon/add_860785?term=plus&page=1&position=17

How to Run Project:
1. Open Android Studio
2. Open Project
3. Connect an Android device or launch an Android emulator
4. Click the Play button at the top of the screen between your device name and Bug Icon
5. App should deploy to device 

Play the Game:
1. Open App
2. First screen should display a plus icon(green) and a battle icon (red with swords)
3. Click the plus and add your first Transformer by entering its stats and picking a team 
4. App should navigate back to first screen and display stats after the create button is pressed
5. User can edit these stats or delete the Transformer from this screen 
6. Clicking edit with navigate back to the Create fragment but display an Update Button instead of a create button
7. Once you have created your Transformers for both teams click the battle button and let the war begin
8. User will be told the result and the dead Transformers will be removed from the screen 

Assumptions made:
1. some api endpoints apprear to not work or return empty data 
2. delete function are not allowed for the token that is suppiled by allspark (get 401 error)
3. assume the token expires once every hour so requires a refresh 
4. store data in a local db and continue to use that while I have issues with the api
