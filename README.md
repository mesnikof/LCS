<H1>****SNHU CS-360 Final Project****</H1>

This is a project to create a simple Local Coffee Shop app to run on Android.  The minimum
required Android version is 7.0 (Nougat).  This app has both end-user and admin functionality
built in.  Which is active depends on which user logs in.  The app also allows for end-users
to use their Google and/or Twitter accounts for authorization, and has some rudimentary mapping
tools available to allow the end-user to find the LCS location.



<H2>**Implementation Notes:**</H2>


**Built-In Username/Password Pairs:**<BR>
 -admin/admin     (Accesses administration activity page)<BR>
 -user/user       (Accesses standard user main activity page)<BR>
 -michael/michael (Access standard user main activity page)<BR>



<H3>**Working Modules:**</H3>

**Splash Screen**

**Login Activity Page**<BR>
 -Standard local login text fields and button.<BR>
 -Clear Button<BR>
 -Create New User/Account Button<BR>
 -Twitter Login Button<BR>
 -Google Login Button

**Admin Activity Page**<BR>
 -All fields and buttons<BR>
 -(Note: Not all database fields are currently in use)

**Main (User) Activity Page**<BR>
 -Logout Button<BR>
 -About-Us Button<BR>
 -Find-Us/Map Button<BR>
 -Contact-Us Button<BR>
 -Social Media/rate-Us Button<BR>
 -Items Scrolling Selector Field<BR>
 -(Note: Selection and ordering is not currently supported)

**Map Activity Fragment**<BR>
 -All features and buttons

**About-Us Pop-Up Fragment**<BR>
 -Email-Us Button<BR>
 -Call-Us Button<BR>
 -Return Button

**Contact-Us Pop-Up Fragment**<BR>
 -Email-Us connectivity (Icon, Label, and Address all spawn the email tool)<BR>
 -Call-Us connectivity (Icon, Label, and Number all spawn the dialer tool)

**Rate-Us / Social-Media Pop-Up Fragment**<BR>
 -Rate-Us connectivity (Icon Button spawns the connection to theGoogle Play Store)<BR>
 -Tweet connectivity<BR>
 --Note: To Do: There is an issue with the Twitter API kit that is currently preventing this app
 from internally sending tweets/shares.  Currently this is requiring app users to re-login to
 Twitter to allow these tweets to be sent.  This is being researched.



<H3>**To Do:**</H3>

Clean up all the "Toast" messages used for test purposes. (not required)

Set up the simulated order fulfilment buttons and functionality for the admin.

Set up the order tracking buttons and functionality for the user.

Add the ability to recover lost username and/or password. (not required)

Add the ability to add images to new items. (not required)

Make it possible to edit items already in the shooping cart, rather than emptying
the cart and starting over. (not required)

Add better input data integrity checking (CC number length, data validity, etc.). (not required)

Add more internal integrity checking (method value return checking, TRY/CATCH blocks, etc.) (not required)


