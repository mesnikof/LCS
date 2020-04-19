****SNHU CS-360 Final Project****

This is a project to create a simple Local Coffee Shop app to run on Android.  The minimum
required Android version is 7.0 (Nougat).  This app has both end-user and admin functionality
built in.  Which is active depends on which user logs in.  The app also allows for end-users
to use their Google and/or Twitter accounts for authorization, and has some rudimentary mapping
tools available to allow the end-user to find the LCS location.



**Implementation Notes:**


**Built-In Username/Password Pairs:**

 -admin/admin     (Accesses administration activity page)\n
 -user/user       (Accesses standard user main activity page)\n
 -michael/michael (Access standard user main activity page)\n



**Working Modules:**

**Splash Screen**

**Login Activity Page**
 -Standard local login text fields and button.\n
 -Clear Button\n
 -Twitter Login Button\n
 -Google Login Button

**Admin Activity Page**
 -All fields and buttons\n
 -(Note: Not all database fields are currently in use)

**Main (User) Activity Page**
 -Logout Button\n
 -About-Us Button\n
 -Find-Us/Map Button\n
 -Contact-Us Button\n
 -Social Media/rate-Us Button\n
 -Items Scrolling Selector Field\n
 -(Note: Selection and ordering is not currently supported)

**Map Activity Fragment**
 -All features and buttons

**About-Us Pop-Up Fragment**
 -Email-Us Button\n
 -Call-Us Button\n
 -Return Button

**Contact-Us Pop-Up Fragment**
 -Email-Us connectivity (Icon, Label, and Address all spawn the email tool)\n
 -Call-Us connectivity (Icon, Label, and Number all spawn the dialer tool)

**Rate-Us / Social-Media Pop-Up Fragment**
 -Rate-Us connectivity (Icon Button spawns the connection to theGoogle Play Store)\n
 -Tweet connectivity\n



**To Do:**

Clean up all the "Toast" messages ued for tesst purposes.

Set up the "Create New User/Accountnt" functionality on the Login Activity page.

Set up the Order Entry functionality for the user.

Set up the simulated order fulfilment buttons and functionality for the admin.

Set up the order tracking buttons and functionality for the user.




