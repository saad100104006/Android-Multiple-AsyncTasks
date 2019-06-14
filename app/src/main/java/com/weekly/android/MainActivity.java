package com.weekly.android;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button mBtnParallelExecution, mBtnSerialExecution;
    private ProgressBar mProgressBarOne, mProgressBarTwo, mProgressBarThree, mProgressBarFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mBtnSerialExecution = findViewById(R.id.btnSerialExecution);
        mProgressBarOne = findViewById(R.id.progressBar1);
        mProgressBarTwo = findViewById(R.id.progressBar2);
        mBtnParallelExecution = findViewById(R.id.btnParallelExecution);
        mProgressBarThree = findViewById(R.id.progressBar3);
        mProgressBarFour = findViewById(R.id.progressBar4);

        mBtnSerialExecution.setOnClickListener(this);
        mBtnParallelExecution.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSerialExecution:

                MyObject myObject = new MyObject(mProgressBarOne, 2);
                MyAsyncTask asyncTask = new MyAsyncTask();
                asyncTask.execute(myObject);

                myObject = new MyObject(mProgressBarTwo, 3);
                MyAsyncTask asyncTask1 = new MyAsyncTask();
                asyncTask1.execute(myObject);
                break;

            case R.id.btnParallelExecution:

                myObject = new MyObject(mProgressBarThree, 2);
                MyAsyncTask asyncTask2 = new MyAsyncTask();
                asyncTask2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myObject);
                myObject = new MyObject(mProgressBarFour, 3);

                MyAsyncTask asyncTask3 = new MyAsyncTask();
                asyncTask3.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myObject);
                break;
        }

    }

    private static class MyAsyncTask extends AsyncTask<MyObject, Integer, Void> {

        private WeakReference<MyObject> myObjectWeakReference;

        @Override
        protected Void doInBackground(MyObject... params) {
            this.myObjectWeakReference = new WeakReference<>(params[0]);
            for (int i = 0; i <= 10; i++) {
                publishProgress(i);

                try {
                    Thread.sleep(myObjectWeakReference.get().interval * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            myObjectWeakReference.get().progressBar.setProgress(values[0]);
        }
    }

    private class MyObject {
        private ProgressBar progressBar;
        private int interval;

        MyObject(ProgressBar progressBar, int interval) {
            this.progressBar = progressBar;
            this.interval = interval;
        }
    }
}
