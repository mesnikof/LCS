****SNHU CS-360 Final Project****

This is a project to create a simple Local Coffee Shop app to run on Android.  The minimum
required Android version is 7.0 (Nougat).  This app has both end-user and admin functionality
built in.  Which is active depends on which user logs in.  The app also allows for end-users
to use their Google and/or Twitter accounts for authorization, and has some rudimentary mapping
tools available to allow the end-user to find the LCS location.



**Implementation Notes:**


**Built-In Username/Password Pairs:**

-admin/admin     (Accesses administration activity page)
-user/user       (Accesses standard user main activity page)
-michael/michael (Access standard user main activity page)



**Working Modules:**

**Splash Screen**

**Login Activity Page**
-Standard local login text fields and button.
-Clear Button
-Twitter Login Button
-Google Login Button

**Admin Activity Page**
    All fields and buttons
    (Note: Not all database fields are currently in use)

**Main (User) Activity Page**
    Logout Button
    About-Us Button
    Find-Us/Map Button
    Contact-Us Button
    Social Media/rate-Us Button
    Items Scrolling Selector Field
    (Note: Selection and ordering is not currently supported)

**Map Activity Fragment**
    All features and buttons

**About-Us Pop-Up Fragment**
    Email-Us Button
    Call-Us Button
    Return Button

**Contact-Us Pop-Up Fragment**
    Email-Us connectivity (Icon, Label, and Address all spawn the email tool)
    Call-Us connectivity (Icon, Label, and Number all spawn the dialer tool)



**To Do:**

Clean up all the "Toast" messages ued for tesst purposes.

Set up the "Create New User/Accountnt" functionality on the Login Activity page.

Set up the Order Entry functionality for the user.

Set up the simulated order fulfilment buttons and functionality for the admin.

Set up the order tracking buttons and functionality for the user.




