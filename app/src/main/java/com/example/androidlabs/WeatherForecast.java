package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class WeatherForecast extends AppCompatActivity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        progressBar = (ProgressBar) findViewById(R.id.ProgressBar);
//        progressBar.setVisibility(View.VISIBLE);

        ForecastQuery forecast = new ForecastQuery();
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
        forecast.execute(urlString);
    }

    protected class ForecastQuery extends AsyncTask<String, Integer, String> {
        String UV, min_temperature, max_temperature, current_temperature;
        String iconName;
        Bitmap icon;

        @Override
        //Type 1
        protected String doInBackground(String... strings) {
            String ret = null;
            String queryURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
            String openUV = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";
            try {       // Connect to the server:
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //Set up the XML parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inStream, "UTF-8");

                //Iterate over the XML tags:
                int EVENT_TYPE;         //While not the end of the document:
                while ((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT) {
                    switch (EVENT_TYPE) {
                        case START_TAG:         //This is a start tag < ... >
                            String tagName = xpp.getName(); // What kind of tag?
                            if (tagName.equals("temperature")) {
                                current_temperature = xpp.getAttributeValue(null, "value");
                                publishProgress(20);
                                min_temperature = xpp.getAttributeValue(null, "min");
                                publishProgress(50);
                                max_temperature = xpp.getAttributeValue(null, "max");
                                publishProgress(75);
                            } else if (tagName.equalsIgnoreCase("weather")) {
                                iconName = xpp.getAttributeValue(null, "icon");
                            }
                            break;
                        case END_TAG:           //This is an end tag: </ ... >
                            break;
                        case TEXT:              //This is text between tags < ... > Hello world </ ... >
                            break;
                    }
                    String message = "";
                    try {
                        boolean fileExists = fileExistance(iconName + ".png");
                        if (fileExists) {
                            FileInputStream fis = null;
                            try {
                                fis = new FileInputStream(getBaseContext().getFileStreamPath(iconName + ".png"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            icon = BitmapFactory.decodeStream(fis);
                            message = "Image already exists";
                        } else {
                            String OpenWeatherMap = "http://openweathermap.org/img/w/" + iconName + ".png";
                            URL iconUrl = new URL(OpenWeatherMap);
                            icon = getImage(iconUrl);
                            FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                            icon.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                            outputStream.flush();
                            outputStream.close();
                            message = "Adding new image";
                        }
                        Log.i("INFO", "file name=" + iconName + ".png" + message);
                        publishProgress(100);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    xpp.next(); // move the pointer to next XML element
                }
            } catch (MalformedURLException mfe) {
                ret = "Malformed URL exception";
            } catch (IOException ioe) {
                ret = "IO Exception. Is the Wifi connected?";
            } catch (XmlPullParserException pe) {
                ret = "XML Pull exception. The XML is not properly formed";
            }
            //What is returned here will be passed as a parameter to onPostExecute:

            try {
                URL url2 = new URL(openUV);
                HttpURLConnection urlConnection = (HttpURLConnection) url2.openConnection();
                InputStream inStream = urlConnection.getInputStream();
                String response = convertStreamToString(inStream);
                JSONObject jObject = new JSONObject(response);
                Double UV1 = jObject.getDouble("value");
                UV = Double.toString(UV1);
            } catch (MalformedURLException mfe) {
                ret = "Malformed URL exception";
            } catch (IOException ioe) {
                ret = "IO Exception. Is the Wifi connected?";
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return ret;
        }

        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            //update GUI Stuff:
            TextView current_temperature0 = findViewById(R.id.current_temperature);
            TextView min_temperature0 = findViewById(R.id.min_temperature);
            TextView max_temperature0 = findViewById(R.id.max_temperature);
            TextView UV_rating = findViewById(R.id.UV_rating);
            current_temperature0.setText("Current temperature : " + current_temperature + "°C ");
            min_temperature0.setText("Min temperature : " + min_temperature + "°C ");
            max_temperature0.setText("Max temperature : " + max_temperature + "°C ");
            UV_rating.setText("UV rating : " + UV);

            ImageView view = (ImageView) findViewById(R.id.ImageView01);
            view.setImageBitmap(icon);

            progressBar.setVisibility(View.INVISIBLE);
        }

        @Override                       //Type 2
        protected void onProgressUpdate(Integer... values) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
            super.onProgressUpdate(values);
            //Update GUI stuff only:
        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        protected Bitmap getImage(URL url11) {

            HttpURLConnection connection1 = null;
            try {
                connection1 = (HttpURLConnection) url11.openConnection();
                connection1.connect();
                int response = connection1.getResponseCode();
                if (response == 200) {
                    return BitmapFactory.decodeStream(connection1.getInputStream());
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (connection1 != null) {
                    connection1.disconnect();
                }
            }
        }

        private String convertStreamToString(InputStream in) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return stringBuilder.toString();
        }
    }
}
