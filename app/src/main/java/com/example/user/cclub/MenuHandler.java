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
    final int NOT_EXIST_PAGE = 0;
    boolean isLoggedIn;
    Menu menu;
    AppCompatActivity currentPage;
    int currentPageID;
    ArrayList<MenuItem> menuItems;
    public MenuHandler(AppCompatActivity currentPage, int currentPageID){
        isLoggedIn = (FirebaseAuth.getInstance().getCurrentUser() != null);
        menu = (Menu)((NavigationView)currentPage.findViewById(R.id.nav_view)).getMenu();
        this.currentPage = currentPage;
        this.currentPageID = currentPageID;
        menuItems = new ArrayList();
        for (int i = 0 ; i < menu.size() ; i ++){
            menuItems.add(menu.getItem(i));
        }
        setVisibilityForItems();
    }
    public void onNavigationItemSelected(MenuItem item){
        Intent intent;
        if (currentPageID == item.getItemId()){
            Toast.makeText(currentPage, "Already at the same page!", Toast.LENGTH_LONG).show();
        }
        else {
            switch(item.getItemId()){
                    case R.id.login_page:
                        Toast.makeText(currentPage,"Going to Login",Toast.LENGTH_SHORT).show();
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

    public void setVisibilityForItems() {
        isLoggedIn = (FirebaseAuth.getInstance().getCurrentUser() != null);
        for (int i = 0; i < menu.size(); i++){
            if (!isLoggedIn && (menu.getItem(i).getItemId() == R.id.login_page || menu.getItem(i).getItemId() == R.id.readme_page || menu.getItem(i).getItemId() == R.id.map_page)) {
                menu.getItem(i).setVisible(true);
            }
            else if (!isLoggedIn)
                menu.getItem(i).setVisible(false);
            else if (isLoggedIn && menu.getItem(i).getItemId() == R.id.login_page)
                menu.getItem(i).setVisible(false);
            else
                menu.getItem(i).setVisible(true);
        }
    }
}
