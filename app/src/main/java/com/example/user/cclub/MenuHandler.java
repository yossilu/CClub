package com.example.user.cclub;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by talha on 13/01/2018.
 */

public class MenuHandler {
    static int[] pagesForAll = {R.id.readme_page, R.id.map_page};
    static int[] pagesForLoggedIn = {R.id.dashboard_page, R.id.logout_page, R.id.info_page};
    static int[] pagesForRest = {R.id.login_page, R.id.reset_page};
    final int NOT_EXIST_PAGE = 0;
    boolean isLoggedIn;
    Menu menu;
    AppCompatActivity currentPage;
    int currentPageID;
    ArrayList<MenuItem> menuItems;

    /*
        Constructor for the Menu Handler
        currentPage - a reference to the current running page to handle his menu and actions
        currentPageID - the R id of the current page.
     */
    public MenuHandler(AppCompatActivity currentPage, int currentPageID) {
        isLoggedIn = (FirebaseAuth.getInstance().getCurrentUser() != null);
        menu = (Menu) ((NavigationView) currentPage.findViewById(R.id.nav_view)).getMenu();
        this.currentPage = currentPage;
        this.currentPageID = currentPageID;
        menuItems = new ArrayList();
        for (int i = 0; i < menu.size(); i++) {
            menuItems.add(menu.getItem(i));
        }
        setVisibilityForItems();
    }

    /*
        handling the view to start the matching activity due to the item
     */
    public void onNavigationItemSelected(MenuItem item) {
        Intent intent;
        if (currentPageID == item.getItemId()) {
            Toast.makeText(currentPage, "Already at the same page!", Toast.LENGTH_LONG).show();
        } else {
            switch (item.getItemId()) {
                case R.id.login_page:
                    Toast.makeText(currentPage, "Going to Login", Toast.LENGTH_SHORT).show();
                    intent = new Intent(currentPage, LoginPage.class);
                    currentPage.startActivity(intent);
                    currentPage.finish();
                    break;
                case R.id.readme_page:
                    Toast.makeText(currentPage, "Going to Readme", Toast.LENGTH_SHORT).show();
                    intent = new Intent(currentPage, ReadmePage.class);
                    currentPage.startActivity(intent);
                    currentPage.finish();
                    break;
                case R.id.map_page:
                    Toast.makeText(currentPage, "Going to our location", Toast.LENGTH_SHORT).show();
                    intent = new Intent(currentPage, MapsActivity.class);
                    currentPage.startActivity(intent);
                    currentPage.finish();
                    break;
                case R.id.dashboard_page:
                    Toast.makeText(currentPage, "Going to change password area", Toast.LENGTH_SHORT).show();
                    intent = new Intent(currentPage, Dashboard.class);
                    currentPage.startActivity(intent);
                    currentPage.finish();
                    break;
                case R.id.reset_page:
                    Toast.makeText(currentPage, "Going to reset password area", Toast.LENGTH_SHORT).show();
                    intent = new Intent(currentPage, ForgotPassword.class);
                    currentPage.startActivity(intent);
                    currentPage.finish();
                    break;
                case R.id.info_page:
                    Toast.makeText(currentPage, "Going to personal info", Toast.LENGTH_SHORT).show();
                    intent = new Intent(currentPage, UserInfoPage.class);
                    currentPage.startActivity(intent);
                    currentPage.finish();
                    break;
                case R.id.logout_page:
                    Toast.makeText(currentPage, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    intent = new Intent(currentPage, LoginPage.class);
                    FirebaseAuth.getInstance().signOut();
                    currentPage.startActivity(intent);
                    currentPage.finish();
                    break;
            }
        }
    }

    /*
        set visibilities for items in the menu separated by logged in users, not logged in users and for ALL
     */
    public void setVisibilityForItems() {
        isLoggedIn = (FirebaseAuth.getInstance().getCurrentUser() != null);
        for (MenuItem item : menuItems) {
            if (isLoggedIn && isExist(pagesForLoggedIn, item.getItemId()))
                item.setVisible(true);
            else if (!isLoggedIn && isExist(pagesForRest, item.getItemId()))
                item.setVisible(true);
            else if (!isExist(pagesForAll, item.getItemId()))
                item.setVisible(false);
        }
    }

    /*
        checking if int value existing in a specific int array.
     */
    private boolean isExist(int[] arr, int key) {
        for (int i = 0; i < arr.length; i++) {
            if (key == arr[i])
                return true;
        }
        return false;
    }
}
