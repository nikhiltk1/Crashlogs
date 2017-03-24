package suventure.nikhil.com.crashlogs;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    Button buttonSave;
    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread.setDefaultUncaughtExceptionHandler(handleAppCrash);
        buttonSave=(Button)findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s.length();//It will crash.
            }
        });
    }

    private Thread.UncaughtExceptionHandler handleAppCrash =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {
                    ex.printStackTrace();
                    //send email here

                    StringBuilder text = new StringBuilder();
                    try {


                        File dir = new File(Environment.getExternalStorageDirectory()+"/nikhil");
                        try{
                            if(!dir.exists()){
                            dir.mkdir();
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        File textFile = new File(Environment.getExternalStorageDirectory()+"/nikhil/crash.txt");

                        try {
                            textFile.createNewFile();


                        PrintWriter pw = new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath()+"/nikhil/crash.txt", true));
                        ex.printStackTrace(pw);
                        pw.flush();

                        pw.close();




                            BufferedReader br = new BufferedReader(new FileReader(textFile));
                            String line;

                            while ((line = br.readLine()) != null) {
                                text.append(line);
                                text.append('\n');
                            }
                            br.close();

                            textFile.delete();
                            dir.delete();

                        }catch (IOException e){e.printStackTrace();}

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }



                    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{  "nikhil.tk@suventure.in"});
                    emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Crash Report");
                    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text.toString());


                    emailIntent.setType("message/rfc822");

                    try {
                        startActivity(Intent.createChooser(emailIntent,
                                "Send email using..."));
                    } catch (android.content.ActivityNotFoundException e) {
                        Toast.makeText(MainActivity.this,
                                "No email clients installed.",
                                Toast.LENGTH_SHORT).show();
                    }

                }

            };









}
