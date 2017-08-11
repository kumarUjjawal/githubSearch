package usemalloc.com.githubsearch;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText searchBoxEditText;
    TextView urlDisplayTextView;
    TextView searchResultsTextView;
    TextView errorMessageTextView;
    ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBoxEditText = (EditText) findViewById(R.id.search_id);
        urlDisplayTextView = (TextView) findViewById(R.id.url_text_id);
        searchResultsTextView = (TextView) findViewById(R.id.search_result_id);
        errorMessageTextView = (TextView) findViewById(R.id.error_message_display);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
    }

    private void showJsonDataView() {
        errorMessageTextView.setVisibility(View.INVISIBLE);
        searchResultsTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        errorMessageTextView.setVisibility(View.INVISIBLE);
        searchResultsTextView.setVisibility(View.VISIBLE);
    }

    private void makeGitHubSearchQuery() {
        String githubQuery = searchBoxEditText.getText().toString();
        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
        urlDisplayTextView.setText(githubSearchUrl.toString());
        String gitHubSearchResults = null;
        new GithubQueryTask().execute(githubSearchUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedMenuItem = item.getItemId();
        if (selectedMenuItem == R.id.action_search) {
            makeGitHubSearchQuery();
        }
        return super.onOptionsItemSelected(item);
    }

    public class GithubQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String githubSearchResults = null;
            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return githubSearchResults;
        }

        @Override
        protected void onPostExecute(String githubSearchResult) {
            loadingIndicator.setVisibility(View.INVISIBLE);
            if (githubSearchResult != null && !githubSearchResult.equals("")) {
                searchResultsTextView.setText(githubSearchResult);
                showJsonDataView();
                searchResultsTextView.setText(githubSearchResult);
            } else {
                showErrorMessage();
            }
        }

        @Override
        protected void onPreExecute() {
            loadingIndicator.setVisibility(View.VISIBLE);
        }
    }



}


