package com.amitdaskne.amitbrow;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    private EditText searchInput;
    private GridView quickAccessGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchInput = findViewById(R.id.searchInput);
        quickAccessGrid = findViewById(R.id.quickAccessGrid);
        setupQuickAccess();
    }
    private void setupQuickAccess() {
        ArrayList<String> sites = new ArrayList<>();
        sites.add("Google");
        sites.add("YouTube");
        sites.add("GitHub");
        sites.add("Reddit");
        sites.add("Wikipedia");
        sites.add("Amazon");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sites);
        quickAccessGrid.setAdapter(adapter);
    }
    public void openBrowser(View view) {
        String url = searchInput.getText().toString().trim();
        if (url.isEmpty()) url = "https://www.google.com";
        if (!url.startsWith("http://") && !url.startsWith("https://")) url = "https://" + url;
        Intent intent = new Intent(this, BrowserActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }
}
