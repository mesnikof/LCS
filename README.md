****SNHU CS-360 Final Project****

This is a project to create a simple Local Coffee Shop app to run on Android.  The minimum
required Android version is 7.0 (Nougat).  This app has both end-user and admin functionality
built in.  Which is active depends on which user logs in.  The app also allows for end-users
to use their Google and/or Twitter accounts for authorization, and has some rudimentary mapping
tools available to allow the end-user to find the LCS location.



**Implementation Notes:**


**Built-In Username/Password Pairs:**
 -admin/admin     (Accesses administration activity page)<BR>
 -user/user       (Accesses standard user main activity page)<BR>
 -michael/michael (Access standard user main activity page)<BR>



**Working Modules:**

**Splash Screen**

**Login Activity Page**
 -Standard local login text fields and button.<BR>
 -Clear Button<BR>
 -Twitter Login Button<BR>
 -Google Login Button

**Admin Activity Page**
 -All fields and buttons<BR>
 -(Note: Not all database fields are currently in use)

**Main (User) Activity Page**
 -Logout Button<BR>
 -About-Us Button<BR>
 -Find-Us/Map Button<BR>
 -Contact-Us Button<BR>
 -Social Media/rate-Us Button<BR>
 -Items Scrolling Selector Field<BR>
 -(Note: Selection and ordering is not currently supported)

**Map Activity Fragment**
 -All features and buttons

**About-Us Pop-Up Fragment**
 -Email-Us Button<BR>
 -Call-Us Button<BR>
 -Return Button

**Contact-Us Pop-Up Fragment**
 -Email-Us connectivity (Icon, Label, and Address all spawn the email tool)<BR>
 -Call-Us connectivity (Icon, Label, and Number all spawn the dialer tool)

**Rate-Us / Social-Media Pop-Up Fragment**
 -Rate-Us connectivity (Icon Button spawns the connection to theGoogle Play Store)<BR>
 -Tweet connectivity



**To Do:**

Clean up all the "Toast" messages ued for tesst purposes.

Set up the "Create New User/Account" functionality on the Login Activity page.

Set up the Order Entry functionality for the user.

Set up the simulated order fulfilment buttons and functionality for the admin.

Set up the order tracking buttons and functionality for the user.




